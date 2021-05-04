/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.javadoc;

/**
 * Represents a java package.  Provides access to information
 * about the package, the package's comment and tags, and the
 * classes in the package.
 * <p>
 * Each method whose return type is an array will return an empty
 * array (never null) when there are no objects in the result.
 *
 * @since 1.2
 * @author Kaiyang Liu (original)
 * @author Robert Field (rewrite)
 *
 * @deprecated
 *   The declarations in this package have been superseded by those
 *   in the package {@code jdk.javadoc.doclet}.
 *   For more information, see the <i>Migration Guide</i> in the documentation for that package.
 */
@Deprecated
public interface PackageDoc extends Doc
{

    /**
     * Get all classes and interfaces in the package, filtered to the specified
     * <a href="{@docRoot}/com/sun/javadoc/package-summary.html#included">access
     * modifier option</a>.
     *
     * @return       filtered classes and interfaces in this package
     * @param filter Specifying true filters according to the specified access
     *               modifier option.
     *               Specifying false includes all classes and interfaces
     *               regardless of access modifier option.
     * @since 1.4
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] allClasses( boolean filter);

    /**
     * Get all
     * <a href="{@docRoot}/com/sun/javadoc/package-summary.html#included">included</a>
     * classes and interfaces in the package.  Same as allClasses(true).
     *
     * @return all included classes and interfaces in this package.
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] allClasses();

    /**
     * Get included
     * <a href="{@docRoot}/com/sun/javadoc/package-summary.html#class">ordinary</a>
     * classes (that is, exclude exceptions, errors, enums, interfaces, and
     * annotation types)
     * in this package.
     *
     * @return included ordinary classes in this package.
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] ordinaryClasses();

    /**
     * Get included Exception classes in this package.
     *
     * @return included Exceptions in this package.
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] exceptions();

    /**
     * Get included Error classes in this package.
     *
     * @return included Errors in this package.
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] errors();

    /**
     * Get included enum types in this package.
     *
     * @return included enum types in this package.
     * @since 1.5
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] enums();

    /**
     * Get included interfaces in this package, omitting annotation types.
     *
     * @return included interfaces in this package.
     */
    gw.gosudoc.com.sun.javadoc.ClassDoc[] interfaces();

    /**
     * Get included annotation types in this package.
     *
     * @return included annotation types in this package.
     * @since 1.5
     */
    AnnotationTypeDoc[] annotationTypes();

    /**
     * Get the annotations of this package.
     * Return an empty array if there are none.
     *
     * @return the annotations of this package.
     * @since 1.5
     */
    AnnotationDesc[] annotations();

    /**
     * Lookup a class or interface within this package.
     *
     * @param className A String containing the name of the class to look up.
     * @return ClassDoc of found class or interface,
     * or null if not found.
     */
    ClassDoc findClass( String className);
}
