package gw.lang;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Set;

public interface IIncrementalCompilationManager {

    /**
     * Record the single-hop dependency edges produced when {@code gosuClass} is
     * compiled to the given bytecode.
     *
     * <p>Runs the two-phase walk over {@code bytes} via DependenciesClassVisitor
     * (constant-pool scan in the constructor + structural {@code ClassVisitor} callbacks
     * via {@code accept}), then a narrow AST pass via trackTypeliteralsFromAST
     * for references that don't make it into bytecode.
     *
     * <p>Only <em>direct</em> producer-consumer edges are recorded; transitive cascades
     * are computed lazily by the incremental compile driver, which walks the resulting
     * graph via {@link #getOrCreateConsumersFor(String)}.
     *
     * @param bytes     compiled bytecode for {@code gosuClass}
     * @param gosuClass the type whose dependencies are being recorded; used as the
     *                  consumer side of every edge produced by this call
     */
    void trackDependencies(byte[] bytes, IGosuClass gosuClass);

    /**
     * Reconcile the in-memory dependency graph via updateDependencies and
     * persist the result to disk. Keys and consumer lists are sorted before
     * serialization for deterministic JSON output.
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
     * Compute the set of Gosu types that need to be recompiled given a set of changed
     * and removed types.
     * <p>
     * Walks the reverse-dependency graph ({@code typeDependencies}) breadth-first starting
     * from the union of changed and removed types, collecting every Gosu consumer reachable
     * along the way. Java types in {@code localJavaTypes} are walked through to find their
     * Gosu consumers but excluded from the result (gosuc cannot recompile Java sources).
     * Removed types are excluded from the result themselves (their source files are gone),
     * though their downstream consumers are not.
     *
     * @param changedTypes types whose source was modified; the changed types themselves
     *                     (if Gosu) plus all transitive Gosu consumers are returned
     * @param removedTypes types whose source was deleted; the removed types themselves
     *                     are NOT returned, but their transitive Gosu consumers are
     * @return the FQCNs of Gosu types that need recompilation
     */
    Set<String> calculateRecompilationSet(Set<String> changedTypes, Set<String> removedTypes);
}
