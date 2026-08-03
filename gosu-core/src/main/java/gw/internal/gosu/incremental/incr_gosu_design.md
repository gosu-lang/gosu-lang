# Incremental Gosu Compilation Design

## 1. Two-layer architecture

Incremental compilation is split across two cooperating layers that live in two
different repositories:

| Layer | Repo | Role                                                                                                                                                                                                              |
|---|----------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Gradle plugin** (driver) | `gradle-gosu-plugin` | Uses Gradle's `InputChanges` to compute *which* types changed/were removed, then **forks the gosuc CLI once**, passing those sets as arguments. Drives no loop of its own.                                        |
| **gosuc** (executor) | `gosu-lang`   | Given the change sets, computes the recompile set, deletes stale outputs, compiles, records dependency edges from the produced bytecode, and persists the dependency graph. **This is the repo documented here.** |

The plugin side is out of scope for this branch; it interacts with gosuc **only**
through the command-line contract in §3.

### API / implementation split within gosu-lang

The compiler is further split so that the public API module (`gosu-core-api`)
carries no implementation:

```
gosu-core-api (API)                         gosu-core (impl)
├─ gw.lang.IIncrementalCompilationManager   ├─ gw.internal.gosu.incremental
│    (the contract, §4)                      │    .IncrementalCompilationManager   (impl of the contract)
├─ gw.lang.GosuShop                          │    .DependenciesClassVisitor        (bytecode dep extractor)
│    .createIncrementalCompilationManager()  │
└─ gw.lang.gosuc.simple.GosuCompiler         └─ obtained at runtime via
     (the driver that calls the manager)          CommonServices.getGosuIndustrialPark()
```

`GosuShop.createIncrementalCompilationManager(depFile, sourceRoots, localJavaTypes,
allSourceFiles, verbose)` delegates to `CommonServices.getGosuIndustrialPark()`
(the `IGosuShop` service factory), which constructs the `gosu-core`
implementation. `GosuCompiler` (in `gosu-core-api`) therefore depends only on the
`IIncrementalCompilationManager` interface, never on the impl class.

---

## 2. End-to-end flow

```
Gradle plugin (separate repo)
  │  computes changed/removed/local-java type FQCNs from InputChanges
  ▼
forks:  gosuc -incremental
              -dependency-file build/tmp/gosuc-deps-{task}.json
              -changed-types  <fqcn:fqcn:…>
              -removed-types  <fqcn:fqcn:…>
              -local-java-types <fqcn:fqcn:…>
              -sourcepath … -classpath … -d <destDir> …  <all source files>
  ▼
GosuCompiler.compile(options, driver)          ── §5
  ├─ create IncrementalCompilationManager       (loads dep file, builds fqcn→source map)
  ├─ recompileSet = calculateRecompilationSet(changed, removed)   ── §7  (transitive BFS)
  ├─ delete .class + source-copy for (removed ∪ recompileSet)     ── §8  (up front, non-transactional)
  ├─ compile the mapped source files            (or ALL, if the set is empty ⇒ initial build)
  │    └─ per compiled class: trackDependencies(bytes, gosuClass) ── §6
  └─ if no errors: updateDependencyFile(recompileSet, effectivelyRemoved)  ── §9 (atomic write)
```

---

## 3. Command-line contract (`CommandLineOptions`)

The branch adds the following JCommander `@Parameter` options to gosuc. This is the
entire surface the Gradle plugin drives:

| Flag | Field | Meaning |
|---|---|---|
| `-incremental` | `boolean _incremental` | Enables the incremental path. Absent ⇒ compile all sources (unchanged legacy behavior). |
| `-dependency-file <path>` | `String _dependencyFile` | Path to the dependency file. **Default `.gosuc-deps.json`**; the plugin passes `build/tmp/gosuc-deps-{taskName}.json`. |
| `-changed-types <fqcns>` | `String _changedTypes` | Path-separator-delimited FQCNs (Java **and** Gosu) whose source changed. Exposed as `Set<String> getChangedTypes()`. |
| `-removed-types <fqcns>` | `String _removedTypes` | Path-separator-delimited FQCNs whose source was deleted. Exposed as `Set<String> getRemovedTypes()`. |
| `-local-java-types <fqcns>` | `String _localJavaTypes` | Path-separator-delimited FQCNs of **same-module Java types** (the plugin populates this by scanning `build/classes/java/main`). Exposed as `List<String> getLocalJavaTypes()`. |
| `-verbose` | `boolean _verbose` | Diagnostic logging throughout the incremental path. |

Delimiter is `File.pathSeparator` throughout; empty/blank strings parse to empty
collections.

---

## 4. The manager contract (`IIncrementalCompilationManager`)

A new interface in `gosu-core-api` (`gw.lang`) with exactly four methods:

```java
void         trackDependencies(byte[] bytes, IGosuClass gosuClass);
void         updateDependencyFile(Set<String> typeFqcnsToCompile, Set<String> removedTypes);
String       getGosuFilePathFromFqcn(String fqcn);
Set<String>  calculateRecompilationSet(Set<String> changedTypes, Set<String> removedTypes);
```

- **`trackDependencies`** — records the *direct* producer→consumer edges observed
  when `gosuClass` was compiled to `bytes` (§6). Transitive cascades are **not**
  computed here.
- **`calculateRecompilationSet`** — walks the reverse-dependency graph to return
  the full set of Gosu types needing recompilation (§7).
- **`getGosuFilePathFromFqcn`** — maps an FQCN to its `.gs*` source path, resolving
  inner/block FQCNs up to their outermost enclosing source (§10).
- **`updateDependencyFile`** — reconciles and persists the graph (§9).

---

## 5. The driver: `GosuCompiler.compile`

`GosuCompiler.compile(CommandLineOptions, ICompilerDriver)` branches on
`options.isIncremental()`:

**Non-incremental** — `compileFilteredSources(getSourceFiles(options), …)` compiles
everything (this is the pre-existing behavior, now factored into a helper).

**Incremental** — the new path:

1. **Build source roots** by tokenizing `-sourcepath` on `File.pathSeparator`.
   These roots let the manager map source paths ↔ FQCNs.
2. **Construct the manager** via `GosuShop.createIncrementalCompilationManager(...)`,
   passing the dep-file path, source roots, `-local-java-types`, the full source
   list, and the verbose flag. Construction **loads** the existing dep file into the
   in-memory `typeDependencies` graph and builds the `fqcn → source path` index.
3. **Compute the recompile set**:
   `typeFqcnsToCompile = calculateRecompilationSet(changedTypes, removedTypes)` (§7).
4. **Delete stale outputs** for `removedTypes ∪ typeFqcnsToCompile` **before**
   compiling (`deleteClassAndSourceFiles`, §8). This is explicitly
   **non-transactional** — a code TODO notes that a compile failure after deletion
   has no rollback.
5. **Map FQCNs → source files** via `getGosuFilePathFromFqcn`:
   - a `$`-FQCN that resolves to no source is a **stale inner-class producer** and is
     silently skipped;
   - a top-level FQCN that resolves to no source **throws** `IllegalStateException`
     (a code TODO flags that a full rebuild might be the better recovery, but the
     current choice is to fail loud for debugging).
   Inner classes collapse onto the same source file (deduped via a `Set`).
6. **Choose the compile set**:
   - if the mapped set is **empty**, treat this as the **initial build** and compile
     **all** source files (this repopulates the graph from scratch);
   - otherwise compile exactly the mapped source files.
7. **Compile** via `compileFilteredSources`, which splits Gosu vs `.java` files and
   returns whether an error/warning **threshold** was exceeded.
8. **Persist**, but only `if (!driver.hasErrors())`:
   - compute `effectivelyRemoved = removedTypes ∪ { $-FQCN ∈ recompileSet whose
     .class no longer exists on disk }` — this catches inner classes that were
     dropped when their outer source changed or was deleted;
   - call `updateDependencyFile(typeFqcnsToCompile, effectivelyRemoved)` (§9).

Gating the dep-file write on `!driver.hasErrors()` means a **failed compile leaves
the previous dep file untouched** — a broken run cannot corrupt the graph.

### Where edges are recorded — `populateGosuClassFile`

Immediately after a class's bytecode is written to disk, the driver calls
`_incrementalManager.trackDependencies(bytes, gosuClass)` (only when incremental is
active). It then recurses over `gosuClass.getInnerClasses()`, so **every compiled
unit — top-level, member, anonymous, and block class — is tracked individually**
with its own bytecode.

---

## 6. Dependency extraction (bytecode-driven, with an AST supplement)

`trackDependencies(bytes, gosuClass)` runs two passes:

```java
ClassReader reader = new ClassReader(bytes);
DependenciesClassVisitor visitor = new DependenciesClassVisitor(reader, this);
reader.accept(visitor, ClassReader.SKIP_FRAMES);   // bytecode pass
trackTypeliteralsFromAST(gosuClass);               // AST supplement
```

### 6.1 `DependenciesClassVisitor` — the bytecode pass

An ASM `ClassVisitor` (API level `ASM5`, shaded as `gw.internal.ext.org.objectweb.asm`)
that treats the **class being visited as the consumer** and records an edge
`producer → consumer` for every referenced type it can recompile. It is itself
**two-phase**:

1. **Constant-pool scan** (in the constructor) — a fast O(n) sweep over the constant
   pool that records every `CONSTANT_Class` entry (tag `7`). This catches references
   buried in method bodies (instantiations, casts, local-variable types) without an
   instruction-level walk.
2. **Structural visit** (`reader.accept(…, SKIP_FRAMES)`) — the standard
   `ClassVisitor` callbacks add signature-level references the constant pool misses:

   | Callback | Types recorded |
   |---|---|
   | `visit` | superclass, interfaces, class generic `Signature` |
   | `visitField` | field descriptor + generic signature; field & type annotations |
   | `visitMethod` | return type, parameter types, declared exceptions, generic signature |
   | `MethodVisitor.visitLocalVariable` | local-variable descriptor + signature |
   | `MethodVisitor.visit*Annotation` | method / parameter / type annotations |
   | `MethodVisitor.visitInvokeDynamicInsn` | bootstrap method descriptor, owner, and `Type`/`Handle` bootstrap args (lambda/`invokedynamic` desugaring) |
   | `visitAnnotation` (class) | annotation type descriptor |
   | `AnnotationVisitor.visit` | annotation **values** that are `Type` (class literals inside annotation args) |

   Generic signatures are parsed with `SignatureReader` + a `SignatureVisitor` so
   that type arguments (e.g. `List<MyType>`) are captured, not just the erased
   descriptor.

Every candidate flows through `maybeAddDependentType`, which unwraps array types to
their element type, keeps only `OBJECT`-sort types, and calls
`shouldTrackType(producerFqcn)` (§10) before `recordTypeDependency(producer,
consumer)`. The constructor also calls `getOrCreateConsumerSet(consumerFqcn)` so
**every compiled type is registered as a graph key**, even if it has no consumers.

> **Single bucket — no accessible/private split.** All edges land in one consumer
> set. The visitor defines an `isAccessible(access)` helper but **never calls it** —
> it is dead code, a placeholder for the asymmetric-cascade optimization that this
> branch does not implement. Consequently the recompile BFS (§7) over-approximates
> (see §12).

### 6.2 `trackTypeliteralsFromAST` — the compile-time-only supplement

Some Gosu references never survive into bytecode (notably **type-literal
expressions** — `MyType` used as a value/feature literal). A narrow AST pass covers
them: it visits the class statement (`getClassStatementWithoutCompile()`), and for
each `ITypeLiteralExpression` records edges via `trackTypeLiteralDependency`, which
recurses through arrays, parameterized type arguments, and compound-type components
(deduping through a shared `trackedTypes` set that also guards cyclic generic
signatures like `class C<T extends C<T>>`).

Each visited node is gated by `element.getGosuClass() == gsClass`, so elements that
lexically belong to a nested class/block are **skipped** here and instead tracked by
that nested unit's own `trackDependencies` call (§5). This keeps each compiled
unit's edge set scoped to itself.

### 6.3 Annotation handling & coverage

Annotations are recorded as **ordinary dependency edges** by the bytecode pass
(§6.1): `visitAnnotation` at class / field / method / parameter / type-annotation
level records the annotation type, and `AnnotationVisitor.visit` records annotation
**values** that are class literals (e.g. `@Schema(type = MyType)`). There is no
annotation-specific subsystem — and none is needed. The four annotation-related
mechanisms Gradle's Java incremental compiler carries are, on the Gosu side, either
already covered or structurally inapplicable:

| Java mechanism | This branch |
|---|---|
| Annotations as dependency edges | **Covered** — recorded authoritatively from bytecode (constant pool + `visitAnnotation`), the same model Java uses. |
| `dependencyToAll` escape hatch for `@Retention(SOURCE)` annotations | **N/A.** Java needs it because it extracts deps from bytecode *after* SOURCE annotations are stripped. Gosu has no equivalent blind spot: the annotation type's reference reaches the graph via the consumer's constant pool / signatures and the AST supplement (§6.2), independent of retention. |
| `module-info` / `package-info` full-rebuild triggers | **N/A** — Gosu has neither construct. |
| Annotation-processor subsystem (`generatedTypesByOrigin`, isolating/aggregating) | **N/A** — gosuc runs no JSR-269 processors. AP-generated code is produced by `compileJava` upstream and reaches gosuc as `.class` files listed in `-local-java-types`, tracked like any other same-module Java type (§10). |

Concrete cases pinned by gosuc-level e2e tests in `IncrementalCompilationEndToEndIT`:

- **class literal inside an annotation argument** (`@Schema(type = MyType)`) —
  `testClassLiteralInsideAnnotationArgValueRecompilesConsumer`;
- **compile-time constant inside an annotation argument** (`@MyAnno(A.FOO + 12)`),
  edge recorded before constant folding —
  `testConstantInAnnotationArgValueDoesNotMaskDependency`;
- **array of a Gosu type** (`MyType[]`) —
  `testGosuFieldOfArrayOfGosuTypeRecompilesOnComponentChange`;
- **parameterized types**, Java- and Gosu-flavored (`List<MyType>`, `Container<MyType>`) —
  `testGosuFieldOfParameterizedJavaTypeRecompilesOnTypeParamChange`,
  `testGosuFieldOfParameterizedGosuTypeRecompilesOnTypeParamChange`;
- **cascade precision** — an unrelated consumer is *not* pulled in when an annotation
  type changes — `testTopLevelAnnotationChangeDoesNotOverRecompileUnrelatedSources`;
- **graph hygiene** — JRE types and JAR-sourced (non-source-root) Gosu types stay out
  of the graph — `testJavaJreTypeNotRecordedInDepGraph`,
  `testGosuTypeNotFromSrcRootsNotRecordedInDepGraph`.

---

## 7. Recompile-set computation (`calculateRecompilationSet`)

The graph `typeDependencies : Map<String, Set<String>>` is keyed **producer →
consumers**: `typeDependencies[X]` is every type that must recompile if `X` changes.
It reflects the *previously compiled* `.class` files; the incoming
`changedTypes`/`removedTypes` are *source-level* changes not yet reflected in the
`.class` artifacts. The BFS bridges the two:

```
visited  = {}; worklist = {}; toRecompile = {}
seed worklist with (changedTypes ∪ removedTypes)          // putIfAbsent empty sets to avoid NPE
while worklist not empty:
    X = worklist.remove()
    if X ∉ localJavaTypes and X ∉ removedTypes:           // walk-through-not-compile / gone
        toRecompile.add(X)
    for consumer in typeDependencies[X]:                  // ALL consumers, unconditionally
        if consumer ∉ visited: enqueue(consumer)
return toRecompile
```

Key properties:

- **Transitive & full.** Every consumer of a visited type is enqueued
  unconditionally, so the cascade is the complete transitive closure. Because there
  is no accessible/private distinction, it is **over-approximate but never
  under-approximate** (§12).
- **Local Java types are walked through but not compiled.** A changed same-module
  Java type (in `-local-java-types`) is used to find its Gosu consumers but is
  excluded from `toRecompile` — gosuc cannot recompile Java sources; `compileJava`
  already did.
- **Removed types cascade but aren't compiled.** Their source is gone, so they are
  excluded from `toRecompile`, but their downstream consumers still cascade.
- **Invariant that keeps the lookup null-safe.** `typeDependencies[X]` is iterated
  without a null guard. This is safe because every compiled type is registered as a
  key (§6.1), so every consumer FQCN reachable in the graph is also a key; seeds are
  additionally `putIfAbsent`-seeded.

---

## 8. Stale-output deletion (`deleteClassAndSourceFiles`)

Before compiling, the driver deletes, for each FQCN in `removedTypes ∪
typeFqcnsToCompile`:

- the **`.class` file** — `destDir/<fqcn-with-'/'>.class`;
- the **source copy** — gosuc packages `.gs*` sources alongside `.class` files in the
  output dir, so any stale source copy is removed too. Because the original
  extension isn't recoverable from an FQCN, deletion is attempted for **all** known
  Gosu extensions (`.gs .gsx .gsp .gst .gr .grs`); `File.delete()` is a no-op on
  absent files.

There is **no per-FQCN `$*.class` glob**. Nested compiled units are cleaned because
the BFS already pulls every nested FQCN into `typeFqcnsToCompile` (bidirectional
bytecode edges from each nested class's `InnerClasses` attribute), so each nested
`.class` is deleted directly by being in the input set.

> **Non-transactional.** Deletion happens *before* the compiler runs and there is no
> stash/restore. If the compile then fails, the deleted outputs are gone with no
> rollback. This is a known gap (code TODO); a future stash-and-restore step would
> close it.

---

## 9. Dependency-file persistence

### 9.1 Reconciliation (`updateDependencies`)

`updateDependencyFile(typeFqcnsToCompile, removedTypes)` first reconciles the
in-memory graph:

1. **Drop removed producers**: `typeDependencies.remove(R)` for each removed type.
2. **Strip stale consumers**: from *every* producer's consumer set, remove all of
   `typeFqcnsToCompile` and `removedTypes` — a recompiled/removed type can no longer
   be assumed to still consume its old producers (its source changed).
3. **Merge refreshed edges**: for each producer in `currentUsedBy` (this session's
   freshly recorded edges), union its recomputed consumers into
   `typeDependencies` (`computeIfAbsent`), bringing the old producer up to date.
4. Clear `currentUsedBy`.

This drop-then-overlay pattern is what keeps dropped edges from lingering: a
producer whose only consumer stopped referencing it ends up with that consumer
stripped in step 2 and not re-added in step 3.

### 9.2 Serialization (atomic, deterministic)

The graph is then written **atomically**: to `<depFile>.tmp` via a Gson streaming
`JsonWriter` (`setHtmlSafe(false)`, two-space indent), then `Files.move(…,
ATOMIC_MOVE, REPLACE_EXISTING)`. A crash mid-write cannot truncate the live file
(which `loadDependencyFile` would otherwise silently read as "no prior state",
erasing the whole graph). Output is **deterministic** — producers via `TreeMap`,
each consumer list sorted — so the file is stable for the Gradle build cache.

### 9.3 On-disk format (version `0.1`)

```json
{
  "version": "0.1",
  "consumers": {
    "<producerFqcn>": [ "<consumerFqcn>", ... ]
  }
}
```

FQCNs use bytecode shape, with `$` between an enclosing type and a nested one
(`example.Outer$Inner`). `loadDependencyFile` returns an **empty map** (⇒ next build
repopulates from a full compile) on a version mismatch, a missing file, or an
`IOException | IllegalStateException` while parsing.

---

## 10. Type filtering & FQCN↔source resolution

**`shouldTrackType(fqcn)`** gates every edge. A type qualifies iff it is either:

- a **local Gosu type** — `getGosuFilePathFromFqcn(fqcn) != null`, i.e. its source
  lives under a configured source root; or
- a **same-module Java type** — `fqcn ∈ localJavaTypes`.

Everything else (JRE classes, types from external JARs) is dropped: gosuc cannot
trigger their recompilation, so an edge to them would never be actionable.

**`getGosuFilePathFromFqcn(fqcn)`** consults the `gosuFqcnToSourcePath` index (built
at construction from the full source list, keyed on outermost FQCN). On a miss it
strips trailing `$…` segments and retries, so `example.Outer$Inner` and
`example.Outer$Anon__0$block_0_` both resolve to `Outer.gs`. Returns `null` for
anything not a known local Gosu source.

Source roots and candidate paths are canonicalized (`toAbsolutePath().normalize()`)
so lookups don't depend on how the caller spelled a path. `buildGosuFqcnToSourcePath`
enforces a 1:1 FQCN↔source invariant, throwing if two sources map to the same FQCN
or a source can't be rooted.

---

## 11. Comparison with the Gradle Java incremental compiler

Gradle's `language-java` incremental compiler is the reference this branch mirrors
where it helps and deliberately diverges where Gosu's architecture allows something
simpler. Side by side:

| Capability | Gradle Java incr. compiler | This Gosu branch |
|---|---|---|
| Per-class dep extraction | ASM bytecode scan (`ClassDependenciesVisitor`) | ASM bytecode scan (`DependenciesClassVisitor`) + narrow AST supplement for type literals (§6) |
| Transitive cascade | Yes (`ClassSetAnalysis.findTransitiveDependents`) | Yes (`calculateRecompilationSet` BFS, §7) |
| Accessible vs. private edge buckets | Yes — asymmetric cascade (private deps don't propagate) | **No** — single bucket, full transitive cascade (§12) |
| Annotations as dependency edges | Yes | Yes — constant pool + `visitAnnotation` (§6.3) |
| `dependencyToAll` (SOURCE-retention, `module-info`) | Yes | **N/A** — structurally unnecessary / no such constructs (§6.3) |
| Inlineable-constant ABI tracking | Yes (hash of `name\|value`) | **No** — consumers conservatively recompile on any producer change |
| Annotation-processor subsystem | Full (isolating / aggregating, `generatedTypesByOrigin`) | **N/A** — gosuc runs no APs; `compileJava` handles them upstream (§6.3) |
| Post-compile graph source | Re-derived from output `.class` files every build (self-healing) | Accumulated in memory, persisted as JSON; never re-derived (§12) |
| Persistence format | Kryo binary `PreviousCompilationData` | JSON `gosuc-deps-*.json`, `version` `"0.1"` (§9) |
| Output safety on failure | `CompileTransaction` stash / restore | Delete-before-compile, non-transactional (§8) |
| Within-project ABI-stable pruning | No | No — future work |

Two divergences are worth calling out. First, Gosu **needs no** SOURCE-retention /
`module-info` / annotation-processor machinery — those are bytecode-extraction
artifacts or Java-language constructs that don't apply to Gosu's source/AST-based
tracking. Second, Gosu **lacks** two optimizations Gradle has — accessible/private
edge buckets and inlineable-constant tracking — both correctness-neutral, costing
only extra recompilation (§12).

---

## 12. Correctness properties & known limitations

**Sound (never under-recompiles).** The BFS enqueues *all* consumers transitively,
so any type whose `.class` could be stale is recompiled. The old graph may be
over-approximate (an edge dropped this build is still acted on until the graph is
rewritten) but never under-approximate.

**Why source-order doesn't matter.** In-project Gosu types resolve from `.gs`
**source** via the TypeSystem, never from a sibling `.class`. So recompiling `X`
always sees the *new* source-derived type info of any changed `Y`, regardless of
compile order — which is why batch-compiling the whole recompile set in one session
is safe.

Documented limitations on this branch:

1. **Over-recompilation** — single consumer bucket, no accessible/private asymmetry.
   A private/method-body-only reference still cascades to a consumer's own
   consumers. Correctness-neutral, wasteful at scale.
2. **Non-transactional deletion** (§8) — delete-before-compile with no rollback.
3. **In-memory accumulation, no fresh re-analysis** — the graph is loaded once and
   mutated; it is never re-derived from the output `.class` files. A dep-extraction
   bug can therefore persist in the dep file across builds. Mitigated (not closed)
   by: gating the write on `!hasErrors()` (§5) and the `effectivelyRemoved`
   inner-class sweep (§5).
4. **Empty recompile set ⇒ compile-all** — in incremental mode an empty mapped set
   is treated as an initial build and recompiles everything. 
5. **No ABI-level pruning, no `dependencyToAll`/SOURCE-retention/`module-info`
   machinery** — the first is future work; the latter are
   structurally not applicable to Gosu's source/AST-based extraction.
