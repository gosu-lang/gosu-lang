/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders;

import java.util.*;



import gw.gosudoc.com.sun.javadoc.MethodDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.PropertyWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Builds documentation for a property.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 * @since 1.7
 */
@Deprecated
public class PropertyBuilder extends AbstractMemberBuilder {

    /**
     * The class whose properties are being documented.
     */
    private final gw.gosudoc.com.sun.javadoc.ClassDoc classDoc;

    /**
     * The visible properties for the given class.
     */
    private final VisibleMemberMap visibleMemberMap;

    /**
     * The writer to output the property documentation.
     */
    private final PropertyWriter writer;

    /**
     * The list of properties being documented.
     */
    private final List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> properties;

    /**
     * The index of the current property that is being documented at this point
     * in time.
     */
    private int currentPropertyIndex;

    /**
     * Construct a new PropertyBuilder.
     *
     * @param context  the build context.
     * @param classDoc the class whoses members are being documented.
     * @param writer the doclet specific writer.
     */
    private PropertyBuilder(Context context,
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
            PropertyWriter writer) {
        super(context);
        this.classDoc = classDoc;
        this.writer = writer;
        visibleMemberMap =
                new VisibleMemberMap(
                classDoc,
                VisibleMemberMap.PROPERTIES,
                configuration);
        properties =
                new ArrayList<>(visibleMemberMap.getMembersFor(classDoc));
        if (configuration.getMemberComparator() != null) {
            Collections.sort(properties, configuration.getMemberComparator());
        }
    }

    /**
     * Construct a new PropertyBuilder.
     *
     * @param context  the build context.
     * @param classDoc the class whoses members are being documented.
     * @param writer the doclet specific writer.
     */
    public static PropertyBuilder getInstance(Context context,
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
            PropertyWriter writer) {
        return new PropertyBuilder(context, classDoc, writer);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "PropertyDetails";
    }

    /**
     * Returns a list of properties that will be documented for the given class.
     * This information can be used for doclet specific documentation
     * generation.
     *
     * @param classDoc the {@link gw.gosudoc.com.sun.javadoc.ClassDoc} we want to check.
     * @return a list of properties that will be documented.
     */
    public List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> members( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        return visibleMemberMap.getMembersFor(classDoc);
    }

    /**
     * Returns the visible member map for the properties of this class.
     *
     * @return the visible member map for the properties of this class.
     */
    public VisibleMemberMap getVisibleMemberMap() {
        return visibleMemberMap;
    }

    /**
     * summaryOrder.size()
     */
    public boolean hasMembersToDocument() {
        return properties.size() > 0;
    }

    /**
     * Build the property documentation.
     *
     * @param node the XML element that specifies which components to document
     * @param memberDetailsTree the content tree to which the documentation will be added
     */
    public void buildPropertyDoc(XMLNode node, Content memberDetailsTree) {
        if (writer == null) {
            return;
        }
        int size = properties.size();
        if (size > 0) {
            Content propertyDetailsTree = writer.getPropertyDetailsTreeHeader(
                    classDoc, memberDetailsTree);
            for (currentPropertyIndex = 0; currentPropertyIndex < size;
                    currentPropertyIndex++) {
                Content propertyDocTree = writer.getPropertyDocTreeHeader(
                        (gw.gosudoc.com.sun.javadoc.MethodDoc) properties.get(currentPropertyIndex),
                        propertyDetailsTree);
                buildChildren(node, propertyDocTree);
                propertyDetailsTree.addContent(writer.getPropertyDoc(
                        propertyDocTree, (currentPropertyIndex == size - 1)));
            }
            memberDetailsTree.addContent(
                    writer.getPropertyDetails(propertyDetailsTree));
        }
    }

    /**
     * Build the signature.
     *
     * @param node the XML element that specifies which components to document
     * @param propertyDocTree the content tree to which the documentation will be added
     */
    public void buildSignature(XMLNode node, Content propertyDocTree) {
        propertyDocTree.addContent(
                writer.getSignature((gw.gosudoc.com.sun.javadoc.MethodDoc) properties.get(currentPropertyIndex)));
    }

    /**
     * Build the deprecation information.
     *
     * @param node the XML element that specifies which components to document
     * @param propertyDocTree the content tree to which the documentation will be added
     */
    public void buildDeprecationInfo(XMLNode node, Content propertyDocTree) {
        writer.addDeprecated(
                (gw.gosudoc.com.sun.javadoc.MethodDoc) properties.get(currentPropertyIndex), propertyDocTree);
    }

    /**
     * Build the comments for the property.  Do nothing if
     * {@link Configuration#nocomment} is set to true.
     *
     * @param node the XML element that specifies which components to document
     * @param propertyDocTree the content tree to which the documentation will be added
     */
    public void buildPropertyComments(XMLNode node, Content propertyDocTree) {
        if (!configuration.nocomment) {
            writer.addComments((gw.gosudoc.com.sun.javadoc.MethodDoc) properties.get(currentPropertyIndex), propertyDocTree);
        }
    }

    /**
     * Build the tag information.
     *
     * @param node the XML element that specifies which components to document
     * @param propertyDocTree the content tree to which the documentation will be added
     */
    public void buildTagInfo(XMLNode node, Content propertyDocTree) {
        writer.addTags((MethodDoc) properties.get(currentPropertyIndex), propertyDocTree);
    }

    /**
     * Return the property writer for this builder.
     *
     * @return the property writer for this builder.
     */
    public PropertyWriter getWriter() {
        return writer;
    }
}
