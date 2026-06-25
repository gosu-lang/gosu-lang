package gw.lang;

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
     * are computed lazily in {@link #calculateRecompilationSet(Set, Set)} by walking
     * the resulting graph.
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
