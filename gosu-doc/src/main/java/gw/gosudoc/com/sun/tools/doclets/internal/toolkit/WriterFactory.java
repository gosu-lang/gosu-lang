/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit;


import gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.ClassTree;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * The interface for a factory creates writers.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.4
 */

@Deprecated
public interface WriterFactory {

    /**
     * Return the writer for the constant summary.
     *
     * @return the writer for the constant summary.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract ConstantsSummaryWriter getConstantsSummaryWriter()
        throws Exception;

    /**
     * Return the writer for the package summary.
     *
     * @param packageDoc the package being documented.
     * @param prevPkg the previous package that was documented.
     * @param nextPkg the next package being documented.
     * @return the writer for the package summary.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract PackageSummaryWriter getPackageSummaryWriter( gw.gosudoc.com.sun.javadoc.PackageDoc
        packageDoc, gw.gosudoc.com.sun.javadoc.PackageDoc prevPkg, gw.gosudoc.com.sun.javadoc.PackageDoc nextPkg)
    throws Exception;

    /**
     * Return the writer for a class.
     *
     * @param classDoc the class being documented.
     * @param prevClass the previous class that was documented.
     * @param nextClass the next class being documented.
     * @param classTree the class tree.
     * @return the writer for the class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract ClassWriter getClassWriter( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                                gw.gosudoc.com.sun.javadoc.ClassDoc prevClass, gw.gosudoc.com.sun.javadoc.ClassDoc nextClass, ClassTree classTree)
            throws Exception;

    /**
     * Return the writer for an annotation type.
     *
     * @param annotationType the type being documented.
     * @param prevType the previous type that was documented.
     * @param nextType the next type being documented.
     * @return the writer for the annotation type.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract AnnotationTypeWriter getAnnotationTypeWriter(
      AnnotationTypeDoc annotationType, gw.gosudoc.com.sun.javadoc.Type prevType, gw.gosudoc.com.sun.javadoc.Type nextType)
            throws Exception;

    /**
     * Return the method writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @return the method writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract MethodWriter getMethodWriter(ClassWriter classWriter)
            throws Exception;

    /**
     * Return the annotation type field writer for a given annotation type.
     *
     * @param annotationTypeWriter the writer for the annotation type
     *        being documented.
     * @return the member writer for the given annotation type.  Return null if
     *         this writer is not supported by the doclet.
     */
    public abstract AnnotationTypeFieldWriter
            getAnnotationTypeFieldWriter(
        AnnotationTypeWriter annotationTypeWriter) throws Exception;

    /**
     * Return the annotation type optional member writer for a given annotation
     * type.
     *
     * @param annotationTypeWriter the writer for the annotation type
     *        being documented.
     * @return the member writer for the given annotation type.  Return null if
     *         this writer is not supported by the doclet.
     */
    public abstract AnnotationTypeOptionalMemberWriter
            getAnnotationTypeOptionalMemberWriter(
        AnnotationTypeWriter annotationTypeWriter) throws Exception;

    /**
     * Return the annotation type required member writer for a given annotation type.
     *
     * @param annotationTypeWriter the writer for the annotation type
     *        being documented.
     * @return the member writer for the given annotation type.  Return null if
     *         this writer is not supported by the doclet.
     */
    public abstract AnnotationTypeRequiredMemberWriter
            getAnnotationTypeRequiredMemberWriter(
        AnnotationTypeWriter annotationTypeWriter) throws Exception;

    /**
     * Return the enum constant writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @return the enum constant writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract EnumConstantWriter getEnumConstantWriter(
        ClassWriter classWriter) throws Exception;

    /**
     * Return the field writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @return the field writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract FieldWriter getFieldWriter(ClassWriter classWriter)
            throws Exception;

    /**
     * Return the property writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @return the property writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract PropertyWriter getPropertyWriter(ClassWriter classWriter)
            throws Exception;

    /**
     * Return the constructor writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @return the method writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     */
    public abstract ConstructorWriter getConstructorWriter(
        ClassWriter classWriter)
    throws Exception;

    /**
     * Return the specified member summary writer for a given class.
     *
     * @param classWriter the writer for the class being documented.
     * @param memberType  the {@link VisibleMemberMap} member type indicating
     *                    the type of member summary that should be returned.
     * @return the summary writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     *
     * @see VisibleMemberMap
     * @throws IllegalArgumentException if memberType is unknown.
     */
    public abstract MemberSummaryWriter getMemberSummaryWriter(
        ClassWriter classWriter, int memberType)
    throws Exception;

    /**
     * Return the specified member summary writer for a given annotation type.
     *
     * @param annotationTypeWriter the writer for the annotation type being
     *                             documented.
     * @param memberType  the {@link VisibleMemberMap} member type indicating
     *                    the type of member summary that should be returned.
     * @return the summary writer for the give class.  Return null if this
     * writer is not supported by the doclet.
     *
     * @see VisibleMemberMap
     * @throws IllegalArgumentException if memberType is unknown.
     */
    public abstract MemberSummaryWriter getMemberSummaryWriter(
        AnnotationTypeWriter annotationTypeWriter, int memberType)
    throws Exception;

    /**
     * Return the writer for the serialized form.
     *
     * @return the writer for the serialized form.
     */
    public SerializedFormWriter getSerializedFormWriter() throws Exception;
}
