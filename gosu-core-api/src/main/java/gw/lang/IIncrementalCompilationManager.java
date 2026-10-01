package gw.lang;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Set;

public interface IIncrementalCompilationManager {

    /**
     * Returns whether a usable dependency graph was loaded at construction: the dependency file
     * existed, parsed, carried the version this manager reads, and every entry had both of its
     * fields.
     *
     * <p>{@code false} means the driver has nothing to walk and must compile every source, then
     * persist the graph from scratch. That covers an absent file (first build, or a driver that
     * deleted it to force a full rebuild), an unreadable file, a file written by a gosuc with a
     * different dependency-file version, and a file whose entries lack a field, which no gosuc
     * writes. Treating any but the first as an empty graph instead would compile only the changed
     * types and silently leave their consumers stale.
     */
    boolean hasValidExistingDepFile();

    /**
     * Record the single-hop dependency edges produced when {@code gosuClass} is
     * compiled to the given bytecode, and compute the class's ABI hash from the same bytes.
     *
     * <p>Runs the two-phase walk over {@code bytes} via DependenciesClassVisitor
     * (constant-pool scan in the constructor + structural {@code ClassVisitor} callbacks
     * via {@code accept}, which record the edges and assemble the canonical ABI text in one
     * pass), then a narrow AST pass via trackTypeliteralsFromAST for references that don't
     * make it into bytecode.
     *
     * <p>Only <em>direct</em> producer-consumer edges are recorded; transitive cascades
     * are computed lazily by the incremental compile driver, which walks the resulting
     * graph via {@link #getOrCreateConsumersFor(String)}, gated by {@link #hasNewABI(String)}.
     * Both take the class's bytecode-shape FQCN, {@link #getClassFileName(IType)}, as the key.
     *
     * @param bytes     compiled bytecode for {@code gosuClass}
     * @param gosuClass the type whose dependencies are being recorded; used as the
     *                  consumer side of every edge produced by this call
     */
    void trackDependencies(byte[] bytes, IGosuClass gosuClass);

    /**
     * Reconcile the in-memory dependency graph and ABI hashes via updateDependencies and
     * persist the result to disk as one entry per type, holding that type's ABI hash and its
     * consumers. Types and consumer lists are sorted before serialization for deterministic
     * JSON output.
     */
    void updateDependencyFile(Set<String> typeFqcnsToCompile, Set<String> removedTypes);


    /**
     * Returns the source file path for {@code fqcn} if it names a known local Gosu
     * type, or {@code null} otherwise.
     *
     * <p>For inner-class and block FQCNs (those containing {@code $}), looks up
     * the outermost enclosing type -- inner classes don't have their own source
     * files. For example, {@code "example.Outer$Inner"} and
     * {@code "example.Outer$AnonymouS__0$block_0_"} both resolve to the path of
     * {@code Outer.gs}.
     *
     * <p>Callers should treat {@code null} as "not a local Gosu type" -- it may
     * be a Java type (see shouldTrackType), a JRE class, a JAR-packaged
     * dependency, or simply unknown.
     */
    String getGosuFilePathFromFqcn(String fqcn);

    /**
     * Return the consumers recorded for {@code fqcn} in the previously-persisted dependency graph --
     * every type that must be recompiled if {@code fqcn} changes -- <em>inserting</em> and returning an
     * empty set if {@code fqcn} has no recorded entry (e.g. a net-new type).
     *
     * <p>Reflects the graph as loaded at construction; edges recorded during the current build via
     * {@link #trackDependencies(byte[], IGosuClass)} are not visible here until
     * {@link #updateDependencyFile(Set, Set)} reconciles them.
     *
     * <p>Used by the incremental driver to walk the reverse-dependency graph while interleaving
     * compilation.
     */
    Set<String> getOrCreateConsumersFor( String fqcn);

    /**
     * Builds the bytecode-style FQCN of {@code type} -- i.e. the form found in
     * {@code .class} filenames, with {@code $} as the separator between an enclosing
     * type and a nested one. For top-level types this is just the type's name.
     *
     * <p>Defined as a structural recurrence on the enclosing-type chain, over the type's
     * <i>erasure</i>:
     * <ul>
     *   <li>a parameterized type is first replaced by its generic type, so that no type
     *       arguments reach the result;</li>
     *   <li>if {@code type} is top-level (no enclosing type), the result is
     *       {@code type.getName()};</li>
     *   <li>otherwise, the result is {@code getClassFileName(enclosing) + "$" +
     *       type.getRelativeName()}.</li>
     * </ul>
     *
     * <p>Examples:
     * <ul>
     *   <li>top-level: {@code example.Outer} -&gt; {@code "example.Outer"}</li>
     *   <li>member class: {@code example.Outer.Inner} -&gt; {@code "example.Outer$Inner"}</li>
     *   <li>parameterized: {@code example.Outer.Inner<String>} -&gt;
     *       {@code "example.Outer$Inner"}</li>
     *   <li>nested block: {@code Outer.AnonymouS__0.block_0_} -&gt;
     *       {@code "example.Outer$AnonymouS__0$block_0_"}</li>
     * </ul>
     * <p>
     * Used as the FQCN shape stored in the dep graph so dep-file keys match
     * {@code .class} artifacts.
     */
    String getClassFileName( IType type );

    /**
     * Returns whether the ABI of {@code fqcn}, as recorded by {@link #trackDependencies(byte[], IGosuClass)}
     * during this build, differs from the ABI persisted for it by the previous build.
     *
     * <p>{@code true} whenever there is nothing to compare: the type was not hashed in this build
     * (not compiled by gosuc: a local Java type, a removed type, a class its recompiled source
     * no longer declares), or the previous build stored no hash for it (a new type). Every such
     * case fails safe toward cascading.
     *
     * <p>The driver enqueues a recompiled type's consumers only when this returns {@code true},
     * which is what stops a cascade at a type whose recompile left its consumer-visible surface
     * unchanged.
     *
     * <p>With verbose logging on, prints one line per call: {@code ABI CHANGED} or
     * {@code ABI STABLE}, the reason (no fresh hash, no previous hash, hash changed, hash
     * same), the FQCN, and the previous and fresh hashes.
     */
    boolean hasNewABI( String fqcn );

    /**
     * Compute the set of Gosu types that need to be recompiled given a set of changed
     * and removed types.
     * <p>
     * Walks the reverse-dependency graph ({@code typeDependencies}) breadth-first starting
     * from the union of changed and removed types, collecting every Gosu consumer reachable
     * along the way. Java types in {@code localJavaTypes} are walked through to find their
     * Gosu consumers but excluded from the result (gosuc cannot recompile Java sources).
     * Removed types are excluded from the result themselves (their source files are gone),
     * though their downstream consumers are not.
     * <p>
     * This is the full, ungated closure: it knows nothing about ABI hashes and is not on the
     * driver's path.
     *
     * @param changedTypes types whose source was modified; the changed types themselves
     *                     (if Gosu) plus all transitive Gosu consumers are returned
     * @param removedTypes types whose source was deleted; the removed types themselves
     *                     are NOT returned, but their transitive Gosu consumers are
     * @return the FQCNs of Gosu types that need recompilation
     */
    Set<String> calculateRecompilationSet(Set<String> changedTypes, Set<String> removedTypes);
}
