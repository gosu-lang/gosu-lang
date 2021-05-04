/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders;

import java.util.*;



import gw.gosudoc.com.sun.javadoc.MethodDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MethodWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Builds documentation for a method.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 * @since 1.5
 */
@Deprecated
public class MethodBuilder extends AbstractMemberBuilder {

    /**
     * The index of the current field that is being documented at this point
     * in time.
     */
    private int currentMethodIndex;

    /**
     * The class whose methods are being documented.
     */
    private final gw.gosudoc.com.sun.javadoc.ClassDoc classDoc;

    /**
     * The visible methods for the given class.
     */
    private final VisibleMemberMap visibleMemberMap;

    /**
     * The writer to output the method documentation.
     */
    private final MethodWriter writer;

    /**
     * The methods being documented.
     */
    private List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> methods;


    /**
     * Construct a new MethodBuilder.
     *
     * @param context       the build context.
     * @param classDoc the class whoses members are being documented.
     * @param writer the doclet specific writer.
     */
    private MethodBuilder(Context context,
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
            MethodWriter writer) {
        super(context);
        this.classDoc = classDoc;
        this.writer = writer;
        visibleMemberMap = new VisibleMemberMap(
                classDoc,
                VisibleMemberMap.METHODS,
                configuration);
        methods =
                new ArrayList<>(visibleMemberMap.getLeafClassMembers(configuration));
        if (configuration.getMemberComparator() != null) {
            Collections.sort(methods, configuration.getMemberComparator());
        }
    }

    /**
     * Construct a new MethodBuilder.
     *
     * @param context       the build context.
     * @param classDoc the class whoses members are being documented.
     * @param writer the doclet specific writer.
     *
     * @return an instance of a MethodBuilder.
     */
    public static MethodBuilder getInstance( Context context,
                                             gw.gosudoc.com.sun.javadoc.ClassDoc classDoc, MethodWriter writer) {
        return new MethodBuilder(context, classDoc, writer);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "MethodDetails";
    }

    /**
     * Returns a list of methods that will be documented for the given class.
     * This information can be used for doclet specific documentation
     * generation.
     *
     * @param classDoc the {@link gw.gosudoc.com.sun.javadoc.ClassDoc} we want to check.
     * @return a list of methods that will be documented.
     */
    public List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> members( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        return visibleMemberMap.getMembersFor(classDoc);
    }

    /**
     * Returns the visible member map for the methods of this class.
     *
     * @return the visible member map for the methods of this class.
     */
    public VisibleMemberMap getVisibleMemberMap() {
        return visibleMemberMap;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasMembersToDocument() {
        return methods.size() > 0;
    }

    /**
     * Build the method documentation.
     *
     * @param node the XML element that specifies which components to document
     * @param memberDetailsTree the content tree to which the documentation will be added
     */
    public void buildMethodDoc(XMLNode node, Content memberDetailsTree) {
        if (writer == null) {
            return;
        }
        int size = methods.size();
        if (size > 0) {
            Content methodDetailsTree = writer.getMethodDetailsTreeHeader(
                    classDoc, memberDetailsTree);
            for (currentMethodIndex = 0; currentMethodIndex < size;
                    currentMethodIndex++) {
                Content methodDocTree = writer.getMethodDocTreeHeader(
                        (gw.gosudoc.com.sun.javadoc.MethodDoc) methods.get(currentMethodIndex),
                        methodDetailsTree);
                buildChildren(node, methodDocTree);
                methodDetailsTree.addContent(writer.getMethodDoc(
                        methodDocTree, (currentMethodIndex == size - 1)));
            }
            memberDetailsTree.addContent(
                    writer.getMethodDetails(methodDetailsTree));
        }
    }

    /**
     * Build the signature.
     *
     * @param node the XML element that specifies which components to document
     * @param methodDocTree the content tree to which the documentation will be added
     */
    public void buildSignature(XMLNode node, Content methodDocTree) {
        methodDocTree.addContent(
                writer.getSignature((gw.gosudoc.com.sun.javadoc.MethodDoc) methods.get(currentMethodIndex)));
    }

    /**
     * Build the deprecation information.
     *
     * @param node the XML element that specifies which components to document
     * @param methodDocTree the content tree to which the documentation will be added
     */
    public void buildDeprecationInfo(XMLNode node, Content methodDocTree) {
        writer.addDeprecated(
                (gw.gosudoc.com.sun.javadoc.MethodDoc) methods.get(currentMethodIndex), methodDocTree);
    }

    /**
     * Build the comments for the method.  Do nothing if
     * {@link Configuration#nocomment} is set to true.
     *
     * @param node the XML element that specifies which components to document
     * @param methodDocTree the content tree to which the documentation will be added
     */
    public void buildMethodComments(XMLNode node, Content methodDocTree) {
        if (!configuration.nocomment) {
            gw.gosudoc.com.sun.javadoc.MethodDoc method = (gw.gosudoc.com.sun.javadoc.MethodDoc) methods.get(currentMethodIndex);

            if (method.inlineTags().length == 0) {
                DocFinder.Output docs = DocFinder.search(configuration,
                        new DocFinder.Input(method));
                method = docs.inlineTags != null && docs.inlineTags.length > 0 ?
                    (gw.gosudoc.com.sun.javadoc.MethodDoc) docs.holder : method;
            }
            //NOTE:  When we fix the bug where ClassDoc.interfaceTypes() does
            //       not pass all implemented interfaces, holder will be the
            //       interface type.  For now, it is really the erasure.
            writer.addComments(method.containingClass(), method, methodDocTree);
        }
    }

    /**
     * Build the tag information.
     *
     * @param node the XML element that specifies which components to document
     * @param methodDocTree the content tree to which the documentation will be added
     */
    public void buildTagInfo(XMLNode node, Content methodDocTree) {
        writer.addTags((MethodDoc) methods.get(currentMethodIndex),
                methodDocTree);
    }

    /**
     * Return the method writer for this builder.
     *
     * @return the method writer for this builder.
     */
    public MethodWriter getWriter() {
        return writer;
    }
}
