# Incremental Gosu Compilation Design

## Goal and rationale

Given the Gosu types whose source changed or was removed, recompile exactly what a full build
would compile differently, and nothing more than a per-class approximation of it. Without
pruning, the safe answer is the transitive closure of consumers: a comment edit to a widely
used type recompiles its whole downstream cone. gosuc prunes that cone with a per-class **ABI
hash**: a changed source is recompiled, since its `.class` is stale whatever happened, and its
consumers follow only if the hash of what a separately compiled consumer can observe about it
moved (§6.4, §7). A consumer compiled against the old ABI is valid against an identical new
one; that is the premise of separate compilation. This makes pruning within a project finer
than Gradle's Java incremental compiler, which recompiles a changed class's dependents without
checking whether its ABI moved and applies ABI checks only across subproject boundaries (§11).

**Safety premise.** Skipping a consumer is sound iff the hash covers everything the consumer
compiles against. The bytecode covers the plain JVM surface; Gosu bakes more into consumers
than the producer's class file carries (default values, block parameter names, structure and
`Dynamic` types, Gosu access, enhanced types, variance), so the hash adds a Gosu surface read
from type info (§6.4). Where that cannot be made precise cheaply, the safe default is not to
hash: local Java types carry `NO_ABI_HASH` and classes naming a structure
`CANNOT_COMPUTE_ABI_HASH`, and both cascade whenever recompiled (§6.4, §6.5). What no hash can express, a reference that leaves
no trace in the consumer's class file, is covered by an edge (§6.1, §6.2). Remaining gaps are
listed in §12; the differential harness that compares incremental against full builds is the
net for any that are unknown.

**Hashing rather than private and public consumer lists.** An earlier design split each
producer's consumers into those that use it only in method bodies and those that use it in
their own surface, stopping the cascade at the first kind, as Gradle's Java compiler does for
private dependents. Hashing gives that for free and more: a consumer using `X` only in bodies
recompiles to an unchanged hash of its own, so the walk stops there, with no second list to
maintain.

**Non-goals.** No change to the Gradle plugin: the whole compile, hash and walk loop lives in
gosuc (§1, §3). No finer than per-class granularity (§12, limitation 1). No re-analysis of the
dependency graph from the output directory (§12, limitation 3).

## 1. Two-layer architecture

Incremental compilation is split across two cooperating layers that live in two
different repositories:

| Layer | Repo | Role |
|---|---|---|
| **Gradle plugin** (driver) | `gradle-gosu-plugin` | Uses Gradle's `InputChanges` to compute *which* types changed/were removed, then **forks the gosuc CLI once**, passing those sets as arguments. Drives no loop of its own. |
| **gosuc** (executor) | `gosu-lang` | Given the change sets, computes the recompile set, deletes stale outputs, compiles, records dependency edges and an ABI hash from the produced bytecode, and persists the dependency graph. **This is the repo documented here.** |

The plugin side is out of scope for this document; it interacts with gosuc **only**
through the command-line contract in §3.

### API / implementation split within gosu-lang

The compiler is split so that the public API module (`gosu-core-api`) carries no
implementation:

```
gosu-core-api (API)                         gosu-core (impl)
├─ gw.lang.IIncrementalCompilationManager   ├─ gw.internal.gosu.incremental
│    (the contract, §4)                      │    .IncrementalCompilationManager   (impl of the contract)
├─ gw.lang.GosuShop                          │    .DependenciesClassVisitor        (dep edges + ABI hash, §6)
│    .createIncrementalCompilationManager()  └─ obtained at runtime via
└─ gw.lang.gosuc.simple.GosuCompiler              CommonServices.getGosuIndustrialPark()
     (the driver that calls the manager)
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
  ├─ create IncrementalCompilationManager       (loads dep file: graph + ABI hashes; builds fqcn→source map)
  ├─ no usable dep file? ⇒ compile ALL source files, then write the graph  (§5)
  ▼
compileGosuIncrementally(options, driver)      ── §5
  ├─ delete .class + $*.class + source copy for (changed ∪ removed)  ── §8 (up front, non-transactional)
  ├─ seed a worklist with (changed ∪ removed), then walk it          ── §7
  │    ├─ compile the visited type's source on demand (once per source file)
  │    │    └─ per compiled class: trackDependencies(bytes, gosuClass) ── §6  (edges + ABI hash, one walk)
  │    ├─ enqueue the consumers of each type whose ABI hash moved, and of every type gosuc has
  │    │  no fresh hash for (local Java, removed, no longer declared by its source, or a class
  │    │  whose surface names a structure, which is not hashed)
  │    └─ otherwise, if this turn compiled the source, enqueue every nested class the compile
  │       produced, so each is gated on its own hash
  └─ if no errors and no threshold abort: updateDependencyFile(compiled, effectivelyRemoved)  ── §9 (atomic write)
```

---

## 3. Command-line contract (`CommandLineOptions`)

The following JCommander `@Parameter` options drive the incremental path. This is the
entire surface the Gradle plugin uses:

| Flag | Field | Meaning |
|---|---|---|
| `-incremental` | `boolean _incremental` | Enables the incremental path. Absent ⇒ compile all sources (legacy behavior). |
| `-dependency-file <path>` | `String _dependencyFile` | Path to the dependency file. **Default `.gosuc-deps.json`**; the plugin passes `build/tmp/gosuc-deps-{taskName}.json`. |
| `-changed-types <fqcns>` | `String _changedTypes` | Path-separator-delimited FQCNs (Java **and** Gosu) whose source changed. Exposed as `Set<String> getChangedTypes()`. |
| `-removed-types <fqcns>` | `String _removedTypes` | Path-separator-delimited FQCNs whose source was deleted. Exposed as `Set<String> getRemovedTypes()`. **May overlap `-changed-types` as supplied** — a source deleted and re-added inside one change window is reported as both — and the driver makes the two disjoint before use (§5). |
| `-local-java-types <fqcns>` | `String _localJavaTypes` | Path-separator-delimited FQCNs of **same-module Java types** (the plugin populates this by scanning `build/classes/java/main`). Exposed as `Set<String> getLocalJavaTypes()`. |
| `-verbose` | `boolean _verbose` | Diagnostic logging along the incremental path: which path was taken (a full rebuild, or an incremental round and the source files it recompiled), dependency-file loading and saving, and, for every type the walk decides on, whether its ABI counted as changed and why (§7). Stale-output deletion (§8) reports failures unconditionally and logs nothing otherwise. |

Delimiter is `File.pathSeparator` throughout; empty/blank strings parse to empty
collections.

---

## 4. The manager contract (`IIncrementalCompilationManager`)

An interface in `gosu-core-api` (`gw.lang`):

```java
boolean      hasValidExistingDepFile();
void         trackDependencies(byte[] bytes, IGosuClass gosuClass);
boolean      hasNewABI(String fqcn);
void         updateDependencyFile(Set<String> typeFqcnsToCompile, Set<String> removedTypes);
String       getGosuFilePathFromFqcn(String fqcn);
Set<String>  getOrCreateConsumersFor(String fqcn);
String       getClassFileName(IType type);
Set<String>  calculateRecompilationSet(Set<String> changedTypes, Set<String> removedTypes);
```

- **`hasValidExistingDepFile`** — whether construction loaded a usable graph: the dep file
  existed, parsed, carried the current format version, and every entry had both of its
  fields (§9.3). `false` is the driver's full-rebuild signal (§5).
- **`trackDependencies`** — records the *direct* producer→consumer edges observed when
  `gosuClass` was compiled to `bytes` and computes the class's ABI hash, in one walk over
  the same bytes (§6). Transitive cascades are **not** computed here. The class is keyed by
  its bytecode-shape FQCN, `getClassFileName(gosuClass)`, which is also the key for
  `hasNewABI` and `getOrCreateConsumersFor`.
- **`hasNewABI`** — whether the hash recorded for `fqcn` this build differs from the one
  persisted by the previous build. `true` whenever there is nothing to compare: not hashed
  this build (not compiled), or no previous hash (a new type). Every such case fails safe
  toward cascading.
- **`getOrCreateConsumersFor`** — returns the consumers recorded for one producer,
  **inserting** an empty set when the FQCN has no entry. This is the single step the
  driver's worklist walks the graph through (§7); the insertion is contractual, since it
  registers the FQCN as a graph key and is therefore observable in the dep file (§9.3).
- **`getGosuFilePathFromFqcn`** — maps an FQCN to its `.gs*` source path, resolving
  inner/block FQCNs up to their outermost enclosing source (§10).
- **`getClassFileName`** — builds the bytecode-shape FQCN of a type (`$` between an
  enclosing type and a nested one), over the type's erasure. This is the key shape the dep
  graph stores, so keys match `.class` artifacts (§10).
- **`updateDependencyFile`** — reconciles and persists the graph and the hashes (§9).
- **`calculateRecompilationSet`** — a standalone transitive BFS over the same graph,
  returning the full, ungated recompile set in one call. It knows nothing about ABI hashes
  and is **not** on the driver's path (§7 describes what is); it is retained as a test-only
  oracle for the graph.

---

## 5. The driver: `GosuCompiler.compile`

`GosuCompiler.compile(CommandLineOptions, ICompilerDriver)` branches on
`options.isIncremental()`:

**Non-incremental** — `compileFilteredSources(getSourceFiles(options), …)` compiles
everything.

**Incremental** — the path below:

1. **Build source roots** by tokenizing `-sourcepath` on `File.pathSeparator`. These roots
   let the manager map source paths ↔ FQCNs.
2. **Construct the manager** via `GosuShop.createIncrementalCompilationManager(...)`,
   passing the dep-file path, source roots, `-local-java-types`, the full source list, and
   the verbose flag. Construction **loads** the existing dep file into the in-memory
   `typeDependencies` graph — one `ProducerInfo` record per type, holding its ABI hash and
   its consumers, the shape the file itself uses (§9.3) — and builds the
   `fqcn → source path` index.
3. **Branch on `hasValidExistingDepFile()`.** If the manager loaded **no usable graph** —
   the dep file is absent, unreadable, or carries another format version (§9.3) — this is a
   full rebuild: compile every source file, then write the graph from scratch (still gated on
   step 7's conditions) and return. Everything below runs only when a graph was loaded, and
   lives in `compileGosuIncrementally`.

   The dep file is the **only** signal that means "compile everything", so **a driver
   wanting a full rebuild must delete it** — empty `-changed-types`/`-removed-types` will
   not do, being indistinguishable from an incremental round whose cascade came out empty,
   which must compile nothing. The Gradle plugin deletes it in `GosuCompile` whenever
   `InputChanges.isIncremental()` is false. Treating an unreadable file as an empty graph
   instead would compile the changed types alone and leave their consumers stale.
4. **Make the change sets disjoint.** A type can be reported as both changed and removed —
   a source deleted and re-added inside one change window looks exactly like that — so
   `removedTypes.removeAll(changedTypes)` runs first: if the source is present now, "changed"
   wins. Without it a re-added type would have its source copy deleted at step 5, be skipped
   by the walk's removed-type test at step 6, and never be regenerated.
5. **Delete stale outputs** for `changedTypes` and for `removedTypes` **before** compiling
   (`deleteClassAndSourceFiles`, §8). This is **non-transactional**: a compile failure after
   deletion has no rollback.
6. **Walk the reverse-dependency graph, compiling on demand and gating the cascade on ABI
   hashes** (§7). The worklist is seeded with `changedTypes ∪ removedTypes`. Each visited
   FQCN is handled by kind:
   - a **local Java type** is walked through, never compiled: its consumers are enqueued
     unconditionally, since gosuc has no fresh hash for it and Gradle only reports it when
     its ABI changed;
   - a **removed type** is walked through the same way. Its nested classes are among its
     consumers — each names its enclosing class in its own `InnerClasses` entry — so they
     are reached through the graph and, having no source, take the stale-producer path
     below;
   - any other FQCN joins `typeFqcnsToCompile` and is mapped to a source via
     `getGosuFilePathFromFqcn`. A `$`-FQCN that resolves to no source is a **stale
     nested-class producer**: nothing regenerates it. A top-level FQCN that resolves to no
     source **throws** `IllegalStateException`: the graph carries an entry no source
     explains, and the failure is kept loud for debugging. Otherwise the source is compiled,
     unless an earlier FQCN already compiled it — inner classes collapse onto the same
     source file, which is compiled once;
   - **its consumers are then enqueued iff `hasNewABI`**, which is true when its hash moved
     and whenever there is no fresh hash to compare: a walked-through type, a class its
     recompiled source no longer declares (the sweep in step 7 purges it), a class whose
     surface names a structure, which is not hashed (§6.4). **Otherwise, if
     this turn compiled the source, every nested class the compile produced is enqueued
     instead**, so each takes its own turn and is gated on its own hash: a nested class whose
     own hash moved cascades to its own consumers regardless of what its enclosing class's
     hash did. When the enclosing class's hash did move, its nested classes are among the
     consumers just enqueued, since each names its enclosing class in its own `InnerClasses`
     entry. A recompiled class whose hash is unchanged therefore enqueues nothing beyond its
     own nested classes, which is what stops the cascade.

   The walk stops early if a compile trips the error/warning **threshold**. A walk that
   reaches no compilable type compiles nothing — that is an empty cascade, not a full
   rebuild (step 3).
7. **Persist**, but only `if (!driver.hasErrors() && !thresholdExceeded)`:
   - compute `effectivelyRemoved = removedTypes ∪ { $-FQCN ∈ typeFqcnsToCompile whose
     .class no longer exists on disk }` — this catches nested classes that were dropped
     when their outer source changed or was deleted. This is why such an FQCN joins
     `typeFqcnsToCompile` even when nothing compiles it: the sweep iterates that set, and a
     stale nested-class producer left out of it would never be purged from the graph;
   - call `updateDependencyFile(typeFqcnsToCompile, effectivelyRemoved)` (§9).

Gating the dep-file write on both conditions means a **failed or truncated compile leaves
the previous dep file untouched** — a broken run cannot corrupt the graph, and a threshold
abort cannot persist the partial edge set it managed to record.

### Where edges and hashes are recorded — `populateGosuClassFile`

Immediately after a class's bytecode is written to disk, the driver calls
`_incrementalManager.trackDependencies(bytes, gosuClass)` (only when incremental is active)
and remembers the class's nested classes for the walk above. It then recurses over
`gosuClass.getInnerClasses()`, so **every compiled unit — top-level, member, anonymous, and
block class — is tracked and hashed individually** with its own bytecode.

---

## 6. Dependency extraction and ABI hashing: one walk over the bytecode

`trackDependencies(bytes, gosuClass)` walks the class file once, recording dependency edges
and assembling the class's canonical ABI text in the same visitor, then runs the AST
supplement and stores the hash:

```java
ClassReader reader = new ClassReader(bytes);
DependenciesClassVisitor visitor = new DependenciesClassVisitor(gosuClass, reader, this, false);   // its flag for printing the text stays off
reader.accept(visitor, ClassReader.SKIP_FRAMES);   // edges (§6.1) + canonical ABI text (§6.4)
currTypeDependencies.get(visitor.getConsumerFqcn()).abiHash = visitor.getAbiHash();
recordEdgesFromAST(gosuClass);               // AST supplement (§6.2)
```

The walk runs with `SKIP_FRAMES` only: edge extraction needs method bodies and
local-variable tables, and the ABI side records nothing from any code-level callback.

### 6.1 `DependenciesClassVisitor` — the edges

An ASM `ClassVisitor` (API level `ASM5`, shaded as `gw.internal.ext.org.objectweb.asm`)
that treats the **class being visited as the consumer** and records an edge
`producer → consumer` for every referenced type it can recompile. The same callbacks also
collect the class's bytecode ABI (§6.4), so one walk serves both. Edge extraction is itself
**two-phase**:

1. **Constant-pool scan** (in the constructor) — a fast O(n) sweep over the constant pool
   that records every `CONSTANT_Class` entry (tag `7`). This catches references buried in
   method bodies (instantiations, casts, local-variable types) without an instruction-level
   walk.
2. **Structural visit** (`reader.accept(…, SKIP_FRAMES)`) — the standard `ClassVisitor`
   callbacks add signature-level references the constant pool misses:

   | Callback | Types recorded |
   |---|---|
   | `visit` | superclass, interfaces, class generic `Signature` |
   | `visitField` | field descriptor + generic signature; field annotations |
   | `visitMethod` | return type, parameter types, declared exceptions, generic signature |
   | `MethodVisitor.visitLocalVariable` | local-variable descriptor + signature |
   | `MethodVisitor.visitMethodInsn` / `visitFieldInsn` | parameter and return types of every invoked method, type of every accessed field — present only in the member's descriptor, never as a constant-pool class entry |
   | `MethodVisitor.visit*Annotation` | method / parameter / type annotations |
   | `MethodVisitor.visitInvokeDynamicInsn` | bootstrap method descriptor, owner, and `Type`/`Handle` bootstrap args (lambda/`invokedynamic` desugaring) |
   | `MethodVisitor.visitAnnotationDefault` | class literals, enum types and nested annotation types inside an annotation member's default value |
   | `visitOuterClass` | the enclosing method's parameter and return types of a local or anonymous class |
   | `visitAnnotation` (class) | annotation type descriptor |
   | `visitTypeAnnotation` (class, field) | type annotation descriptors on the class declaration and on fields |
   | `AnnotationVisitor.visit` / nested `visitAnnotation` / `visitEnum` | annotation **values** that are `Type` (class literals inside annotation args), nested annotation types, and the enum type of an enum value |

   Generic signatures are parsed with `SignatureReader` + a `SignatureVisitor` so that type
   arguments (e.g. `List<MyType>`) are captured, not just the erased descriptor.

One edge comes from type info rather than bytecode: an edge from every **local Java ancestor** of the class, not
only its direct supertype. Under `Mid extends JMid extends JBase` (`JMid`, `JBase` same-module Java), a change to
`JBase` is reported alone, since `JMid`'s own ABI fingerprint does not move, and `Mid`'s class file names `JMid`
only. The edge brings `Mid` into the walk, and its hash, which covers its inherited surface (§6.4), carries the change
on to `Mid`'s own subclasses and consumers. Gosu ancestors need no such edge: each is recompiled whenever its own
supertypes' surface moves, and its hash moves with it. Pinned by
`testLocalJavaSupertypeChangeReachesGosuSubclassesThroughALocalJavaClass`.

Every candidate flows through `maybeAddDependentType`, which unwraps array types to their
element type, keeps only `OBJECT`-sort types, and calls `shouldTrackType(producerFqcn)`
(§10) before `recordTypeDependency(producer, consumer)`. The constructor also calls
`getOrCreateCurrentConsumerSet(consumerFqcn)` so **every compiled type is registered as a
graph key**, even if it has no consumers.

> **Single bucket — no accessible/private split.** All edges land in one consumer set.
> Whether an edge propagates further is decided per build by the consumer's own ABI hash
> (§6.4, §7), not by how the consumer referenced the producer, so the graph needs no second
> bucket.

### 6.2 `recordEdgesFromAST` — the compile-time-only supplement

Some Gosu references never survive into bytecode (notably **type-literal expressions** —
`MyType` used as a value/feature literal). A narrow AST pass covers them: it visits the
class statement (`getClassStatementWithoutCompile()`), and for each
`ITypeLiteralExpression` records edges via `trackTypeLiteralDependency`, which recurses
through arrays, parameterized type arguments, and compound-type components (deduping
through a shared `trackedTypes` set that also guards cyclic generic signatures like
`class C<T extends C<T>>`).

The same pass covers two more references that leave no trace in the consumer's class file:

- **Compile-time constants.** gosuc folds a constant into string concatenations, int
  `switch` cases, annotation arguments and default arguments, so the consumer's bytecode
  carries the value and no reference to the type declaring it. A constant named through its
  declaring type is covered by that type literal, but not one inherited and named unqualified
  or through a subtype (`Mid.FOO`), nor one imported as a feature (`uses Base#FOO`). For every
  identifier naming a Gosu static final constant, and every member access naming a static
  compile-time constant, Gosu or Java, the pass records an edge from the type that declares it
  (`constantDeclaringType`). The member access matters most for a Java constant named through a
  Java subclass in an annotation argument or a `switch` case, where gosuc folds it (it reads a
  Java constant at runtime in a string concatenation): when `JBase` changes under
  `JMid extends JBase`, Gradle reports `JBase` alone and the consumer names only `JMid`. Pinned by
  `testJavaConstantFoldedIntoAnAnnotationThroughAJavaSubclassRecompilesTheConsumer` and
  `testJavaConstantFoldedIntoASwitchCaseThroughAJavaSubclassRecompilesTheConsumer`.
- **What a value passed where a structure is expected offers.** A structure is defined by its
  members and gosuc writes it as `Object`, so whether `Caller`'s `Fixture.pick( new Rec() )`
  still compiles, with `pick( x : Named )` and a `Rec` that merely matches `Named`, depends on
  what `Rec` offers. When `Named` asks for a `sized() : Sized` and `Rec` meets it with a
  `sized() : RecSized`, `Caller` compiles only while `RecSized` fits `Sized`. A change to
  `RecSized` recompiles `Rec`, whose member line still says `sized() : RecSized`, so `Rec`'s
  hash stands still and the walk stops there. With local Java types the chain does not even
  exist: the graph records no edges between Java types, so when `JRecSized` changes, Gradle
  reports it alone and nothing leads from it to `Caller`, whatever the hashes say. For every
  method call, `new` expression or property write whose parameters, or property, name a
  structure, and for every `as` coercion, implicit or explicit, to a structure, the pass records
  an edge to the class from the type of every value passed and from every in-project type it
  reaches through its non-private members' declared types, its supertypes and its type
  arguments (`trackStructuralArgument`). The same is followed for every type argument written
  for a type variable bounded by a structure, since gosuc checks it against the bound wherever
  the parameterized type is written: with `class Holder<T extends Named>`, both
  `new Holder<Rec>()` and `var h : Holder<Rec>` compile only while `Rec` fits `Named`, with no
  call or coercion involved (`trackTypeArgumentsBoundedByStructures`, pinned by
  `testChangeToAMemberTypeOfATypeArgumentBoundedByAStructure…`). The source compiled, so the value fits; which members
  the structure asks for is not worked out, and every member is followed, which only adds
  edges. Every argument is followed, not only those passed for a structure, since pairing them
  with their parameters would have to follow named arguments and defaults. The walk stops at a
  structure, which is not hashed (§6.4), and at a type outside the project. The structure side,
  a change to `Named` itself or to a structure it names, needs no edge: `Named` and every class
  naming it report a change whenever recompiled (§6.4). Pinned by
  `testChangeToAMemberTypeOfAnArgumentReachesCallersPassingItForAStructure`,
  `testChangeToAParameterTypeOfAnArgumentsMemberReachesCallersPassingItForAStructure`,
  `testChangeToAMemberTypeOfATypeArgumentReachesCallersPassingItForAStructure`,
  `testChangeToAMemberTypeOfAValueAssignedToAStructureReachesTheAssigningClass` and
  `testChangeToALocalJavaMemberTypeOfAJavaArgumentReachesCallersPassingItForAStructure`, and
  for the other forms a value reaches a structure in (a block invocation, an inherited property
  written unqualified, an enhancement method, a list literal, a value another class returned, a
  value typed with a type variable, which stands for its bound, and a value passed for a method
  type variable bounded by a structure, where the parser inserts no coercion and only the
  invoked method's declared types reveal the structure) by the
  `testChangeToAMemberTypeOfAValue…ForAStructureReachesCaller` tests; the cost of following every member is shown by
  `testArgumentPassedForAStructureRecompilesCallerOnChangesToTypesItsOtherMembersReach`, and
  §6.5 weighs it against following only what the structure asks for.

Each visited node is gated by `element.getGosuClass() == gsClass`, so elements that
lexically belong to a nested class/block are **skipped** here and instead tracked by that
nested unit's own `trackDependencies` call (§5). This keeps each compiled unit's edge set
scoped to itself.

### 6.3 Annotation handling & coverage

Annotations are recorded as **ordinary dependency edges** by the bytecode pass (§6.1):
`visitAnnotation` at class / field / method / parameter / type-annotation level records the
annotation type, and `AnnotationVisitor.visit` records annotation **values** that are class
literals (e.g. `@Schema(type = MyType)`). There is no annotation-specific subsystem. The
four annotation-related mechanisms Gradle's Java incremental compiler carries are, on the
Gosu side, either already covered or structurally inapplicable:

| Java mechanism | gosuc |
|---|---|
| Annotations as dependency edges | **Covered** — recorded from bytecode (constant pool + `visitAnnotation`), the same model Java uses. |
| `dependencyToAll` escape hatch for `@Retention(SOURCE)` annotations | **N/A.** Java needs it because it extracts deps from bytecode *after* SOURCE annotations are stripped. The annotation type's reference reaches the graph via the consumer's constant pool / signatures and the AST supplement (§6.2), independent of retention. |
| `module-info` / `package-info` full-rebuild triggers | **N/A** — Gosu has neither construct. |
| Annotation-processor subsystem | **N/A** — gosuc runs no JSR-269 processors. AP-generated code is produced by `compileJava` upstream and reaches gosuc as `.class` files listed in `-local-java-types`, tracked like any other same-module Java type (§10). |

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
- **types named only in a member descriptor** (the return type of an invoked method, the type of
  an accessed field) — `testTypeNamedOnlyInAnInvokedMethodDescriptorIsADependency`,
  `testTypeNamedOnlyInAnAccessedFieldDescriptorIsADependency`;
- **an enum constant baked from an annotation default**, whose type the user's source never
  names — `testEnumAnnotationValueBakedFromADefaultIsADependency`;
- **cascade precision** — an unrelated consumer is *not* pulled in when an annotation type
  changes — `testTopLevelAnnotationChangeDoesNotOverRecompileUnrelatedSources`;
- **graph hygiene** — JRE types and JAR-sourced (non-source-root) Gosu types stay out of the
  graph — `testJavaJreTypeNotRecordedInDepGraph`,
  `testGosuTypeNotFromSrcRootsNotRecordedInDepGraph`.

### 6.4 The per-class ABI hash

Every compiled class, except one whose surface names a structure (below), gets a SHA-1 hex digest of a canonical text of everything a separately
compiled consumer can observe about it at compile time (change detection, not security, so
the shorter digest is enough). Two class files with the same ABI hash identically; the
driver uses that to stop a cascade at a class whose recompile left its consumer-visible
surface unchanged (§7). With `-verbose`, the walk prints each type's verdict, `ABI CHANGED`
or `ABI STABLE`, with the reason and both digests; the canonical text itself is not printed.

**Bytecode ABI.** The §6.1 walk collects, alongside the edges: the class file version, the
class access flags, name, generic `Signature` and superclass; the **member classes** it
declares (its `InnerClasses` entries, excluding block classes, anonymous classes, and
private member classes — see below); its interfaces, in declaration order; its annotations
and type annotations; and every **consumer-visible** field (access, name, descriptor, signature,
`ConstantValue`, annotations, type annotations) and method (access, name, descriptor,
signature, declared exceptions, annotations, parameter annotations, type annotations, an
annotation member's `AnnotationDefault`, and `MethodParameters` names). gosuc writes
`AnnotationDefault` but neither type annotations nor `MethodParameters`; those two are hashed
for completeness. Each list is sorted before hashing, so emission order is irrelevant, with
two exceptions, both of them ordered values rather than sets. The elements of an array-valued
annotation argument keep their class-file order, since that order, and their number, is part
of the argument's value. The interfaces keep their declaration order, because Gosu resolves an
inherited member by merging each interface in that order and keeping the first match, so
permuting an `extends` or `implements` clause can change which interface's member a consumer
binds to, and with it the bytecode that consumer compiles. Annotation values, nested
annotations and arrays are rendered into the annotation's text, so a changed annotation
argument moves the hash, a reordered array argument included. An annotation's named members
are sorted like everything else; gosuc
writes them in the annotation type's declaration order and looks each up by name, so the
order a usage site lists them in never reaches the class file.

**Member classes.** A consumer resolves `Outer.Inner` through `Outer`, so the enclosing
class's text names its member classes, and deleting or renaming one moves the enclosing
class's hash, which is how the stale `Outer$Inner` is reached and its consumers rebuilt
(§7). Block and anonymous classes are left out: gosuc lists them in `InnerClasses` too, and
a method-body edit that adds a lambda must not change the enclosing class's hash. A private
member class is left out as well, since nothing outside its own source file can name it;
the trade-off is a stale graph key when one is deleted (§12).

**Visibility follows Gosu, not the JVM.** gosuc never emits `ACC_PRIVATE` for ordinary
members: a Gosu-private member (explicit, or a `var` with no modifier) is written as
package-private so nested classes can reach it, and a member declared `internal` is written
as package-private with a `@gw.lang.ir.Internal` annotation. A member is therefore hashed
iff it is public or protected, or package-private and annotated `@Internal`. The test masks
the public and protected bits rather than comparing the access value to zero, because gosuc
ORs the static, final, abstract, enum, transient and deprecated bits onto the same value: a
private `static var` arrives as `ACC_STATIC`, not `0`, and is no less private for it. A
package-private member without the annotation is Gosu-private and no other source file can
name it; a same-package Java class compiled *after* the Gosu output could, but such a class
lives in another compile task, whose staleness Gradle decides from its own compile-classpath
fingerprint rather than from this graph.

**Gosu compile-time surface.** Much of what a consumer compiles against never reaches the
producer's class file, so it is appended from the producer's type info, as sorted lines:

| Surface | Why it is consumer-visible | Source |
|---|---|---|
| Values of non-private `static final` compile-time constants (primitive, String, enum), with the declared type | gosuc initializes fields in `<clinit>` and emits no `ConstantValue` attribute, yet consumers fold these constants into string concatenations, `switch` cases and annotation arguments | `ICompileTimeConstantValue` on the declared properties |
| Parameter names of non-private methods and constructors, keyed by parameter types so overloads stay distinct | named-argument call sites bind against them | `IOptionalParamCapable.getParameterNames()` |
| Default parameter values of non-private methods and constructors: the expression's text and its **evaluated value** | a default must be a compile-time constant, and every call site that omits the argument bakes its value (`DefaultArgLiteral`); the text alone does not move when it names a constant, private or declared elsewhere, whose value changed | `IOptionalParamCapable.getDefaultValueExpressions()`, rendered through `toString` and `evaluate()` |
| The Gosu type of every non-private property, return value and parameter, with each member's Gosu access, static-ness and, for a property, readability and writability | gosuc writes a structure and `dynamic.Dynamic` as `Object`, with no generic signature unless the type is parameterized; it writes a block type as an `IFunctionN` that carries neither the block's parameter names, which named arguments to the block bind against, nor its default values, which a call omitting the argument bakes; and it writes a protected enhancement member as public | `DependenciesClassVisitor.typeText`, which renders block types, type arguments, arrays and compound types recursively |
| For every in-project direct supertype, a digest of the surface it passes on: the non-private properties and methods it declares (constructors excepted) and, through their own digests, what it inherits in turn | a consumer binds to an inherited member through the subclass: gosuc names the subclass as the owner of the call, so the consumer records no edge from the supertype, and the subclass's own class file does not change when the supertype's surface does. Without it a renamed `Base.call_me()` would recompile `Mid extends Base` with an unchanged hash and stop the walk there, leaving `Leaf extends Mid` calling a method that is gone (pinned by `testHashBaseClassPropagatesInSubClasses`). The digest is read from type info rather than taken from the supertype's ABI hash: type info resolves in-project Gosu types from source, so it describes the new supertype whether or not the walk has compiled it yet, and it covers local Java supertypes, which have no hash. Each digest is computed once per build and shared by every subclass | `DependenciesClassVisitor.inheritedSurfaceDigest` |
| What the class itself is: its Gosu modifiers, the type an enhancement enhances, and the declared variance and bound of each type variable | gosuc writes a protected member class as public; an enhancement's receiver is written raw; a class signature cannot express variance, which decides what a type's parameterizations are assignable to | `IGosuClass.getModifiers()`, `IGosuEnhancement.getEnhancedType()`, `ITypeVariableDefinition.getVariance()` |

A parameter without a default renders as `none`, whether the defaults array carries a null
entry for it or is empty; an explicit `= null` default is a `NullExpression` and renders as
`'null' = null`. The two must differ: a caller may omit the argument only in the second case.
An evaluated value renders with its class, so the int `1` and the string `"1"` differ, an array
element by element, and null unquoted, so no quoted value can spell it.

**Values are quoted.** A value carrying arbitrary text — a constant's value, an annotation
argument, a default parameter expression — is written single-quoted with every quote inside it
doubled, which leaves the delimiting quotes as the only unescaped ones. Concatenated raw, a
value could spell the separator between two elements and make two different surfaces hash
alike: `@A(x = "a", y = "b,y c")` and `@A(x = "a,y b", y = "c")` render the same bare text. The
`none` above is a sentinel rather than a value and stays unquoted, so a default expression whose
own text is `none` remains distinct from no default at all.

**Every compiled class is hashed**, anonymous and block classes included, unless its surface
names a structure (below): each is a compiled unit with its own record. A private member class is hashed like any other: its consumers all sit in
its own source, which the walk has compiled whole by the time it reads the hash, so whether the hash moves decides
nothing. Only local Java types, which gosuc never compiles, carry `NO_ABI_HASH`, and the classes
below carry `CANNOT_COMPUTE_ABI_HASH`; `hasNewABI` reads either as "changed". A class whose hash
cannot be computed is not tolerated: the exception aborts the compile, and no dep file is written.
Reconciliation likewise throws if a type credited as compiled has `NO_ABI_HASH` (§9.1). Hashing
therefore never silently degrades; a defect in it surfaces as a failed build rather than as a
permanently cascading type.

**A class whose surface names a structure is not hashed.** A structure, and every class whose
non-private members, constructors, type-variable bounds or enhanced type name a structure, or
that inherits such a member from an in-project supertype, gets `CANNOT_COMPUTE_ABI_HASH`
(`DependenciesClassVisitor.namesStructure`), which `hasNewABI` reads as "changed" whenever the
class is recompiled. gosuc writes a structure as `Object`, and whether a value fits it depends on
the structure's members and on theirs in turn, which no class file or member line captures: a
change to `Sized` recompiles `Named { function sized() : Sized }`, whose member lines stay as they
were, and a change to `Named` recompiles `Fixture.pick( x : Named )`, whose class file stays as it
was, while `Caller`, passing a `Rec` to `pick`, may no longer compile. Reporting a change carries
it through every class naming a structure to their consumers, at the cost of recompiling those
consumers on any recompile of such a class, a comment edit included. `CANNOT_COMPUTE_ABI_HASH` is
stored like a hash (§9.1), so a class that stops naming a structure is compared with it rather than
with a hash from before it named one; it is a value of its own, rather than `NO_ABI_HASH`, so that a
compiled class left with no hash at all is still caught as an internal inconsistency. Since a
structure is never hashed, whether a type is a structure needs no line of its own: turning an
interface into a structure moves its hash to `CANNOT_COMPUTE_ABI_HASH`
(`testStructureVersusInterfaceIsAbi`). Pinned by
`testStructureChangeReachesCallerOfMemberTakingIt`, `…OfMethodWhoseTypeVariableItBounds`,
`…OfInheritedMemberTakingIt`, `…OfConstructorTakingIt`,
`testStructureChangeReachesCallerWritingAPropertyOfIt`,
`testChangeToAStructureAStructureNamesReachesCallers` and, in `AbiHashIT`,
`testStructureReturnTypeIsAbi`, `testStructurePropertyTypeIsAbi`,
`testClassNamingAStructureAnywhereOnItsSurfaceIsNotHashed` (each way a surface can name a
structure) and `testStructureNamedOnlyInABodyOrAPrivateMemberIsHashed` (the ways it does not).
§6.5 weighs this against
hashing such classes precisely.

The bytecode half and the Gosu surface are pinned by `AbiHashIT`, which compiles small
Gosu fixtures with gosuc and reads their hashes from the dep file: stable across body edits,
comments, member order, private members, blocks and anonymous classes, and the order and
spelling (named or positional) of an annotation's arguments; moved by public members,
descriptors, generic signatures, constant values, access flags, supertypes, annotations and
their values, the order and number of an annotation array's elements, the declaration order
of an annotation type's members (its positional users bind by it), parameter names, default
values, `internal` members, annotation arguments or constant values whose own text contains the
separators the canonical text uses, and the order an interface declares its supertypes in.

---

### 6.5 Structures: the current design and its alternatives

A structure is satisfied structurally: no class declares that it fits one, gosuc writes it as
`Object`, and whether a value fits is checked where the value is used, from the structure's
members and from what the value's type offers, each followed into the types they name. Neither
side reaches a class file or a member line, so both are handled outside the ordinary edges and
hashes. The current design favours simplicity over precision on both sides.

**The structure side: no hash** (§6.4). A structure, and every class whose surface names one,
gets `CANNOT_COMPUTE_ABI_HASH` and cascades whenever it is recompiled. A change to `Named`, or to a `Sized`
that `Named`'s members name, then reaches `Caller`, passing a `Rec` to
`Fixture.pick( x : Named )`, through `Named → Fixture → Caller`, without any edge from `Named` to
`Caller`. The cost falls on the classes naming a structure, not on structures themselves: a
structure has no bodies, so nearly every edit to one changes its surface anyway, while any
recompile of `Fixture`, a body-only edit included, recompiles all of `Fixture`'s consumers
(pinned by `testUnhashedClassNamingAStructureStopsCascadingAtAHashedConsumer`). The cascade stops
at the first hashed consumer. Alternatives:

- *Hash classes naming structures, carrying the structures' surfaces.* `Fixture` keeps its own
  hash plus a line `structure Named <digest>` per structure it names, the digest covering
  `Named` and every structure it reaches, rendered together so that structures referring to
  each other hash the same in any order. Structures themselves stay unhashed. `Fixture`'s hash
  then moves exactly when what `Named` asks for does, so body edits to it stop cascading, and
  the chain `Named → Fixture → Caller` still carries every change to `Named`. This is the
  preferred refinement if measurements show classes naming structures to be common.
- *Hash structures and classes alike on their own surfaces, and record `Named → Caller` at
  every use site* from the structures in an invoked member's parameter types, as the bytecode
  walk does from a descriptor for an interface. `Fixture`'s hash then says nothing about `Named`,
  so when `Named` changes the walk stops at `Fixture` and only the direct edge reaches `Caller`:
  correctness rests on the AST pass recognising every form that can pass a value for a structure
  (block invocations and feature literals among them), where the first alternative falls back on
  the declaring class. Without hashing the direct edge is never needed: the walk reaches
  `Caller` through `Named → Fixture → Caller`, since the class declaring a structure-typed member
  always names the structure and is always an in-project Gosu class.

**The argument side: a closure** (§6.2, `trackStructuralArgument`). Where a value of a
nominal type `Rec` is passed or assigned for a structure, the class gets an edge from `Rec` and
from every in-project type `Rec` names, directly or indirectly, through its non-private members,
supertypes and type arguments. These edges are needed whatever the hashes say: with local Java
types the graph has no edges between Java types, so nothing else leads from a changed
`JRecSized` to `Caller`. Following everything, rather than what the structure asks for, has two
costs, both confined to such use sites: a large `Rec` can give `Caller` edges from much of the
project, recompiling it on almost any ABI change (pinned by
`testArgumentPassedForAStructureRecompilesCallerOnChangesToTypesItsOtherMembersReach`), and the
walk loads the type info of every type it reaches, from source for Gosu types, once per class
using `Rec` that way. Alternatives:

- *Follow only what the structure asks for.* For each member the structure requires, take
  `Rec`'s members of the same name and arity (`IMethodInfo.getDisplayName()`, since
  `getName()` carries the parameter types) and record edges from their declared types,
  recursing where the required member's return type is a structure and fitting `Rec`'s type
  arguments against the same structures. The edges are then bounded by the structure rather
  than by `Rec`. What decides whether a matching member exists at all is `Rec`'s own surface,
  hashed and reached through `Rec → Caller`, inherited members included through the
  inherited-surface digest.
- *Cache what a walk from a type reaches* for the build in the manager, as the inherited-surface
  digests are, so each further use site reuses it. This addresses the compile-time cost only
  and combines with either walk.
- *Record what gosuc's structural check reads* by hooking its assignability check while
  compiling the class. The edges would be exact, but the hook reaches into the compiler's
  internals, and a change in how the check caches or short-circuits would drop edges silently.
- A depth limit on the walk is not an option: a fit can read arbitrarily deep (`Rec` →
  `RecSized` → `RecInner` …).

Before choosing either refinement, measure: how many classes name a structure, how often their
recompiles cascade, how many use sites pass a nominal value for a structure, and how many edges
the closure adds there. If those numbers are small, the current design is the right trade-off.

## 7. Graph traversal, compile-on-demand, and the ABI gate

The graph `typeDependencies : Map<String, ProducerInfo>` is keyed **producer →
{abiHash, consumers}**: `typeDependencies[X].consumers` is every type that must recompile
if `X` changes. It reflects the *previously compiled* `.class` files; the incoming
`changedTypes`/`removedTypes` are *source-level* changes not yet reflected in the `.class`
artifacts. The driver's walk bridges the two, compiling as it goes:

```
visited             = changedTypes ∪ removedTypes
worklist            = changedTypes, then removedTypes
typeFqcnsToCompile  = {}                       // FQCNs credited as recompiled (or found gone)
sourceFilesCompiled = {}                       // source files already handed to the compiler
enqueue(T)          = if T ∉ visited: visited.add(T); worklist.add(T)
while worklist not empty and not thresholdExceeded:
    X = worklist.remove()
    compiledHere = false
    if X ∉ localJavaTypes and X ∉ removedTypes:            // walked through otherwise: never compiled by gosuc
        typeFqcnsToCompile.add(X)
        F = getGosuFilePathFromFqcn(X)
        if F == null:  throw unless X contains '$'         // stale nested-class producer
        else if F ∉ sourceFilesCompiled:
            sourceFilesCompiled.add(F); compile(F); compiledHere = true
    if hasNewABI(X):                                       // also true whenever X has no fresh hash
        for consumer in getOrCreateConsumersFor(X): enqueue(consumer)   // X's nested classes are among them
    else if compiledHere:
        for N in nested classes written by compile(F): enqueue(N)      // each is gated on its own hash
```

Key properties:

- **Transitive, gated on ABI.** A compiled class's consumers are enqueued only when its
  ABI hash moved (§6.4). A body-only, comment-only or private-only edit therefore
  recompiles the edited source and nothing else; a public-surface edit recompiles the
  direct consumers, and the cascade continues past a consumer only if *its* recompile
  moved *its* hash. Every case with no hash to compare — local Java types, removed types,
  classes their source no longer declares — cascades unconditionally, so the walk is
  **never under-approximate** (§12).
- **Every nested class a compile produced takes its own turn**, gated on its own hash, so an
  inner class whose ABI moved cascades to its own consumers, keyed under `Outer$Inner`,
  whether or not its enclosing class's hash moved, and a change three levels down reaches
  only the consumers bound to that level: each enclosing class's hash covers its member
  classes' names, not their members (pinned by
  `testThreeLevelNestedMemberClassChangeRecompilesConsumerWithExpectedDepFile`). It reaches
  the worklist by one of two routes. When the enclosing class's hash moved, it is among that
  class's consumers — every nested class names each of its enclosing classes in its own
  `InnerClasses` attribute — and is enqueued with them. When the hash is unchanged, the walk
  enqueues the nested classes the compile produced directly, since nothing else would.
- **Classes no compile produced need no enumeration.** Every nested class consumes its
  enclosing class (its own `InnerClasses` entry names it), and an enclosing class's hash
  covers the names of its non-private member classes (§6.4). A removed outer therefore
  reaches its nested classes through `enqueue`, and a deleted member class moves the outer's
  hash and is enqueued as the outer's consumer; nothing compiles it, so it has no fresh hash,
  `hasNewABI` cascades to its consumers, and the sweep purges it.
- **Compilation is keyed on the source file, not the FQCN.** A source declaring several
  compiled units is compiled once however many of its FQCNs the walk reaches. Compile order
  does not affect the result: in-project Gosu types resolve from source, never from a
  sibling `.class` (§12).
- **Local Java types are walked through but not compiled.** A changed same-module Java
  type (in `-local-java-types`) is used to find its Gosu consumers but is excluded from
  `typeFqcnsToCompile` — gosuc cannot recompile Java sources; `compileJava` already did.
- **Removed types cascade but aren't compiled.** Their source is gone, so they are excluded
  from `typeFqcnsToCompile`, but their downstream consumers still cascade. This rests on
  `removedTypes` being accurate: a type still present on disk but reported removed would be
  skipped by the `X ∈ removedTypes` test and never rebuilt, which is why the driver
  subtracts `changedTypes` from `removedTypes` first (§5).
- **A threshold abort stops the walk**, and the dep file is then left untouched (§5 step 7).
- **The consumer lookup is null-safe by construction.** `getOrCreateConsumersFor` inserts an
  empty set for an FQCN with no entry, so the loop needs no null guard. That insertion is
  observable: it registers the FQCN as a graph key and is persisted (§9.3, §12).

`calculateRecompilationSet` computes the full, ungated closure in a single call without
compiling anything. It is not on this path; it is retained as a test-only oracle for the
graph.

---

## 8. Stale-output deletion (`deleteClassAndSourceFiles`)

Before compiling, the driver deletes, for each FQCN in `changedTypes` and each FQCN in
`removedTypes`:

- the **`.class` file** — `destDir/<fqcn-with-'/'>.class`;
- its **nested outputs** — every `<fqcn>$*.class` in the same package directory, which
  covers inner, anonymous and block classes;
- the **source copy** — gosuc packages `.gs*` sources alongside `.class` files in the
  output dir, so any stale source copy is removed too. Because the original extension isn't
  recoverable from an FQCN, deletion is attempted for **all** known Gosu extensions
  (`.gs .gsx .gsp .gst .gr .grs`); those not present are skipped.

**Only changed and removed types are cleared, not the whole cascade.** A cascade consumer's
source did not change, so recompiling it regenerates exactly the same set of class files
and overwrites them in place — it cannot orphan a nested output. Only an edited or deleted
source can drop a nested class, which is what makes the `$*.class` glob over those two
sets sufficient.

> **Non-transactional.** Deletion happens *before* the compiler runs and there is no
> stash/restore. If the compile then fails, the deleted outputs are gone with no rollback;
> the next build takes the full-rebuild path (§5 step 3) and regenerates them.

---

## 9. Dependency-file persistence

### 9.1 Reconciliation (`updateDependencies`)

`updateDependencyFile(typeFqcnsToCompile, removedTypes)` first reconciles the in-memory
graph:

1. **Drop removed types**: `typeDependencies.remove(R)` for each removed type — its record
   goes, hash included.
2. **Strip stale consumers**: from *every* type's record, remove all of
   `typeFqcnsToCompile` and `removedTypes` — a recompiled/removed type can no longer be
   assumed to still consume its old producers (its source changed).
3. **Merge this build's records**: for each producer recorded this build (`currTypeDependencies`),
   union its recomputed consumers into the persisted record (`computeIfAbsent`), bringing
   the old producer up to date, and, if the record carries a hash — only classes compiled
   this build do — replace the persisted hash with it: it describes the `.class` file now on
   disk. A producer that was merely referenced by a compiled class says `NO_ABI_HASH` and
   leaves the persisted hash alone. `CANNOT_COMPUTE_ABI_HASH`, for a class whose surface
   names a structure (§6.4), is written like any fresh hash: keeping the old hash would let a
   class that names a structure and then returns to its earlier surface compare equal to it,
   and leave consumers compiled against the version in between. A producer credited as compiled
   (`∈ typeFqcnsToCompile`) that nonetheless has `NO_ABI_HASH` is an internal inconsistency and
   **throws** `IllegalStateException`: every compiled class gets a hash or
   `CANNOT_COMPUTE_ABI_HASH` (§6.4), so this cannot happen unless something upstream broke.
4. Clear `currTypeDependencies`.

This drop-then-overlay pattern is what keeps dropped edges from lingering: a producer whose
only consumer stopped referencing it ends up with that consumer stripped in step 2 and not
re-added in step 3.

### 9.2 Serialization (atomic, deterministic)

The graph is then written **atomically**: to `<depFile>.tmp` via a Gson streaming
`JsonWriter` (`setHtmlSafe(false)`, two-space indent), then `Files.move(…, ATOMIC_MOVE,
REPLACE_EXISTING)`. A crash mid-write cannot truncate the live file (which
`loadDependencyFile` would otherwise read as "no usable file", forcing a full rebuild).
Output is **deterministic** — types in sorted order, each consumer list sorted, `abi_hash`
before `consumers` in every entry — so the file is stable for the Gradle build cache, and
a graph regenerated from scratch is byte-identical to one carried forward.

### 9.3 On-disk format (version `0.3`)

```json
{
  "version": "0.3",
  "dep_graph": {
    "<fqcn>": {
      "abi_hash": "<40 hex chars, SHA-1>  |  CANNOT_COMPUTE_ABI_HASH  |  NO_ABI_HASH",
      "consumers": [ "<consumerFqcn>", ... ]
    }
  }
}
```

One entry per type, keyed by FQCN in bytecode shape, with `$` between an enclosing type and
a nested one (`example.Outer$Inner`). `abi_hash` is the type's ABI hash (§6.4), the literal
`CANNOT_COMPUTE_ABI_HASH` for a class whose surface names a structure, which gosuc does not hash
(§6.4), or the literal `NO_ABI_HASH` for a local Java type, which gosuc never compiles. `consumers` lists
every type that must recompile when this one changes. Version `0.3` widened the hashed
surface (§6.4) and added the edges from local Java ancestors (§6.1); a `0.2` file lacks
both, so it is rejected like any other version. Keeping hash and consumers in one
record means the two can never name different sets of types, and makes "not hashed" visible
rather than an absent key. The file is **usable** (`hasValidExistingDepFile()`) only if it
parses, its `version` is `0.3`, it carries a `dep_graph`, and every entry carries both
fields; otherwise nothing is loaded and the driver compiles everything (§5 step 3), which
also regenerates the file. gosuc is the only writer of this file, through the atomic move
above, and a Gradle build-cache restore returns it byte for byte, so an entry lacking a
field is not a file this gosuc wrote: it is rejected outright rather than read as "no hash"
or "no consumers".

---

## 10. Type filtering & FQCN↔source resolution

**`shouldTrackType(fqcn)`** gates every edge. A type qualifies iff it is either:

- a **local Gosu type** — `getGosuFilePathFromFqcn(fqcn) != null`, i.e. its source lives
  under a configured source root; or
- a **same-module Java type** — `fqcn ∈ localJavaTypes`.

Everything else (JRE classes, types from external JARs) is dropped: gosuc cannot trigger
their recompilation, so an edge to them would never be actionable.

**`getGosuFilePathFromFqcn(fqcn)`** consults the `gosuFqcnToSourcePath` index (built at
construction from the full source list, keyed on outermost FQCN). On a miss it strips
trailing `$…` segments and retries, so `example.Outer$Inner` and
`example.Outer$Anon__0$block_0_` both resolve to `Outer.gs`. Returns `null` for anything
not a known local Gosu source.

**Producer keys are erased FQCNs.** `getClassFileName` replaces a parameterized type with
its generic type before walking the enclosing-type chain, so no key ever carries type
arguments. A key is only ever matched against `.class` artifacts and against the FQCNs in
`-changed-types`, and a parameterized name matches neither — but the two failure modes
differ, and the `$`-stripping above is what makes one of them silent:

- a parameterized **nested** type, `example.Outer$Inner<String>`, has a `$`, so the retry
  loop strips it and resolves `example.Outer`. `shouldTrackType` returns *true* and the
  edge is recorded under a key that can never fire.
- a parameterized **top-level** type, `example.Box<example.Leaf>`, has no `$` to strip. The
  lookup fails, `shouldTrackType` returns false, and the edge is dropped with no trace —
  losing dependencies for every type literal that resolves to a generic type.

Erasing at the top of `getClassFileName` covers both, and because the erasure precedes the
recursion it applies to a parameterized *enclosing* type as well. It is also the only step
needed: a parameterized type is itself an `IJavaType`/`IGosuClass`, so it reaches
`getClassFileName` on its own and no separate descent into its generic type is required to
record the link.

Source roots and candidate paths are canonicalized (`toAbsolutePath().normalize()`) so
lookups don't depend on how the caller spelled a path. `buildGosuFqcnToSourcePath` enforces
a 1:1 FQCN↔source invariant, throwing if two sources map to the same FQCN or a source can't
be rooted.

---

## 11. Comparison with the Gradle Java incremental compiler

Gradle's `language-java` incremental compiler is the reference this design mirrors where it
helps and deliberately diverges where Gosu's architecture allows something simpler. Side by
side:

| Capability | Gradle Java incr. compiler | gosuc |
|---|---|---|
| Per-class dep extraction | ASM bytecode scan (`ClassDependenciesVisitor`) | ASM bytecode scan (`DependenciesClassVisitor`) + narrow AST supplement for type literals (§6) |
| Transitive cascade | Yes (`ClassSetAnalysis.findTransitiveDependents`) | Yes — worklist walk interleaved with compilation, gated per class on the ABI hash (§7) |
| Accessible vs. private edge buckets | Yes — asymmetric cascade (private deps don't propagate) | **No** — single bucket; the consumer's own ABI hash decides whether the cascade continues past it (§6.4, §7) |
| Annotations as dependency edges | Yes | Yes — constant pool + `visitAnnotation` (§6.3) |
| `dependencyToAll` (SOURCE-retention, `module-info`) | Yes | **N/A** — structurally unnecessary / no such constructs (§6.3) |
| Inlineable-constant ABI tracking | Yes (hash of `name\|value`) | Yes — compile-time constant values are part of the ABI hash, read from type info since gosuc emits no `ConstantValue` (§6.4) |
| Annotation-processor subsystem | Full (isolating / aggregating, `generatedTypesByOrigin`) | **N/A** — gosuc runs no APs; `compileJava` handles them upstream (§6.3) |
| Post-compile graph source | Re-derived from output `.class` files every build (self-healing) | Accumulated in memory, persisted as JSON; never re-derived (§12) |
| Persistence format | Kryo binary `PreviousCompilationData` | JSON `gosuc-deps-*.json`, `version` `"0.3"` (§9) |
| Output safety on failure | `CompileTransaction` stash / restore | Delete-before-compile, non-transactional (§8) |
| Analysis failure | Falls back to a full rebuild with a logged cause | Fails the build: a class that cannot be hashed aborts the compile (§6.4) |
| Within-project ABI-stable pruning | No — a comment edit to `A` recompiles `A`'s direct dependents | **Yes** — per-class ABI hash compared after each recompile (§6.4, §7) |

Two divergences are worth calling out. First, Gosu **needs no** SOURCE-retention /
`module-info` / annotation-processor machinery — those are bytecode-extraction artifacts or
Java-language constructs that don't apply to Gosu's source/AST-based tracking. Second, Gosu
replaces Gradle's accessible/private edge buckets with an ABI check on the freshly compiled
consumer, which is the more precise of the two: a consumer that references a producer only
from method bodies comes out with an unchanged hash after recompiling against the new
producer, so its own consumers are not enqueued — the same place Java's private-dependent
rule stops — and the check extends within a module to the ABI-stability pruning Gradle only
applies across subprojects. Gosu also hashes surface that has no bytecode representation
(default parameter values, parameter names, constant values), which Java's bytecode-only
extraction would miss.

---

## 12. Correctness properties & known limitations

**Sound (never under-recompiles), given the ABI premise.** The walk enqueues the consumers
of every recompiled class whose ABI hash moved, and of every type it has no fresh hash for,
so any type whose `.class` could be stale is recompiled — provided the hash captures
everything the Gosu compiler consumes from a producer at compile time. For JVM surface
(signatures, generics, annotations) that is the bytecode ABI; for the Gosu-specific surface
gosuc bakes into callers without writing it to the producer's class file — constant values,
default parameter values, parameter names — the hash reads the producer's type info (§6.4).
The old graph may be over-approximate (an edge dropped this build is still acted on until
the graph is rewritten) but never under-approximate.

**Why source-order doesn't matter.** In-project Gosu types resolve from `.gs` **source** via
the TypeSystem, never from a sibling `.class`. So recompiling `X` always sees the *new*
source-derived type info of any changed `Y`, regardless of compile order — which is what
makes it safe to compile during the traversal, before the full cascade is even known, and
what lets stale-output deletion be confined to the changed and removed sets (§8).

Known limitations:

1. **Per-class ABI granularity.** Any ABI change to `X` recompiles every consumer of `X`,
   including consumers that use none of the changed members. Correctness-neutral;
   per-member precision is possible later if profiling justifies it. A class whose surface
   names a structure goes further: it is not hashed, so any recompile of it, a comment edit
   included, recompiles all its consumers (§6.4).
2. **Non-transactional deletion** (§8) — delete-before-compile with no rollback.
3. **In-memory accumulation, no fresh re-analysis** — the graph is loaded once and mutated;
   it is never re-derived from the output `.class` files. A dep-extraction bug can
   therefore persist in the dep file across builds. Mitigated (not closed) by gating the
   write on `!hasErrors() && !thresholdExceeded` (§5) and the `effectivelyRemoved`
   inner-class sweep (§5).
4. **The full-rebuild signal is out-of-band** — it is the dep file's absence or
   unreadability, not anything on the command line (§5). A driver that asks for a full
   rebuild while leaving a valid file in place gets an incremental round with an empty
   cascade: nothing compiled, exit 0, stale outputs. Nothing here can detect that; the
   obligation sits entirely with the driver.
5. **No `dependencyToAll`/SOURCE-retention/`module-info` machinery** — structurally not
   applicable to Gosu's source/AST-based extraction.
6. **The graph accumulates empty entries for unconsumed local Java types.** Walking a
   changed same-module Java type calls `getOrCreateConsumersFor`, which registers it as a
   key; if no Gosu type consumes it, the key persists as an entry with no consumers and
   nothing ever prunes it. The ceiling is the module's Java class count. It is a dep-file
   size concern only — an empty consumer set costs the walk nothing. One visible
   consequence: a graph regenerated from scratch (dep file deleted, §5 step 3) omits these
   keys, because the full-rebuild path never runs the walk.
7. **A deleted private member class leaves a stale graph key.** Private member classes are
   not part of the enclosing class's hash (§6.4), so deleting one does not move that hash,
   its consumers — all in the same source, already recompiled — are left alone, and its
   `Outer$Inner` entry stays in the dep file, still listed as a consumer of every type the
   deleted class used, until a cascade through `Outer` or through one of those types reaches
   it and the sweep purges it. The second route has a cost: an ABI change to a type that only
   the deleted class used resolves the stale key to the enclosing source and recompiles it
   once for nothing. Outputs are correct throughout; the class file itself is deleted with
   the enclosing class's stale outputs (§8). Accepted as is: the case is rare and the cost is
   one extra compile, after which the key is gone. Pinned by
   `testDeletingAPrivateMemberClassPurgesItFromTheDependencyFile`, which documents the extra
   recompile and that the cascade it triggers purges the key.
8. **The ABI hash covers the Gosu-specific surface known to be baked into callers** (§6.4).
   A survey of the language as `gosu-test` exercises it found the default values, block
   types, structures, `dynamic.Dynamic`, Gosu access, enhanced types and variance now on the
   surface, each pinned in `AbiHashIT`, and the constant and structure edges of §6.2, pinned in
   `IncrementalCompilationEndToEndIT`. A block type's parameter types are pinned by
   `testBlockTypeParameterTypesAreAbi` (`int` and `Integer` are written alike). Edges that
   rest on the AST pass because their target never reaches the consumer's class file are
   pinned too: a legacy `IAnnotation` usage
   (`testLegacyIAnnotationConstructorChangeRecompilesTheAnnotatedClass`), a SOURCE-retention
   annotation (`testSourceRetentionJavaAnnotationChangeRecompilesTheAnnotatedClass`) and a
   template's `<%@ extends %>` base (`testTemplateBaseClassChangeRecompilesTheTemplate`). What
   remains is believed to change only whether a stale consumer would still compile, never its
   bytecode:
   - the variance Gosu infers for a type variable declared with none is read from all the
     declared members, private ones included, and checked only against a subclass's
     supertype clause. A probe adding a private setter to a `Box<T>` read only through a
     public getter left a consumer's `Box<Object> = new Box<String>( … )` compiling in a full
     build as well, and no case where the inference changes what a consumer compiles has been
     found;
   - a type or enhancement that is new this build can change how an unchanged consumer
     resolves a name (a same-package class shadowing a wildcard import, an enhancement
     colliding with one the consumer uses, a new enum constant making an unqualified one
     ambiguous), and no edge points from a type that did not exist. The first is reproduced
     and pinned by `testNewSamePackageTypeShadowingAWildcardImportIsNotDetected`: the
     incremental build succeeds where a full build fails. A probe adding an enhancement with
     the same method name as one a consumer uses produced the same class files as a full
     build. One way to close the shadowing case: when a type is new this build, also enqueue
     the consumers of every existing type with the same simple name; an enhancement changing
     resolution would need an index of method names.

   Any further gap can only be closed by adding to the hashed surface or the edges; the
   differential harness (`--clean-repo-build`) is the net that would expose it as a consumer
   `.class` mismatch.
9. **Dependencies on private members are out of scope.** A feature literal may name a member
   of another class whatever its visibility (`Util#secret()`), and compiles to a lookup by name.
   Private members are not ABI (§6.4), so renaming or retyping one moves no hash and the
   literal's owner is not recompiled: the build succeeds where a full build would fail, and the
   stale literal fails only when it runs. This is accepted; a consumer in the same source is
   unaffected, since the source is compiled whole. Pinned by
   `testRenamingAPrivateMethodBoundByAFeatureLiteralDoesNotRecompileTheLiteralsOwner`.
10. **Classes whose surface names a structure are not hashed** (§6.4, §6.5). Any recompile of
    one, a body-only edit included, recompiles all its consumers; the cascade stops at the
    first hashed consumer. Correctness-neutral; §6.5 describes how to hash them precisely.
11. **A value passed for a structure brings edges from everything its type reaches** (§6.2,
    §6.5). The use site is recompiled on an ABI change to any in-project type the value's type
    names, directly or indirectly, whether or not the structure asks for it, and the walk loads
    the type info of all of them. Correctness-neutral; §6.5 describes how to follow only what the
    structure asks for.
