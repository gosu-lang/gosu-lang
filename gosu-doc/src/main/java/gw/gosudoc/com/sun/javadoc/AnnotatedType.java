/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.javadoc;


/**
 * Represents an annotated type.
 * For example:
 * <pre>
 *      {@code @NonNull String}
 *      {@code @Positive int}
 * </pre>
 *
 * @author Mahmood Ali
 * @since 1.8
 * @deprecated
 *   The declarations in this package have been superseded by those
 *   in the package {@code jdk.javadoc.doclet}.
 *   For more information, see the <i>Migration Guide</i> in the documentation for that package.
 */
@Deprecated
public interface AnnotatedType extends gw.gosudoc.com.sun.javadoc.Type
{

    /**
     * Returns the annotations associated with this type.
     * @return the annotations associated with this type
     */
    AnnotationDesc[] annotations();

    /**
     * Returns the underlying type.
     * @return the underlying type
     */
    Type underlyingType();
}
