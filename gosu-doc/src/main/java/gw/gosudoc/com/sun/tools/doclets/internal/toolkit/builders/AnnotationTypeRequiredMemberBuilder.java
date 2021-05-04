/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders;

import java.util.*;



import gw.gosudoc.com.sun.javadoc.MemberDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Builds documentation for required annotation type members.
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
public class AnnotationTypeRequiredMemberBuilder extends AbstractMemberBuilder {

    /**
     * The annotation type whose members are being documented.
     */
    protected gw.gosudoc.com.sun.javadoc.ClassDoc classDoc;

    /**
     * The visible members for the given class.
     */
    protected VisibleMemberMap visibleMemberMap;

    /**
     * The writer to output the member documentation.
     */
    protected AnnotationTypeRequiredMemberWriter writer;

    /**
     * The list of members being documented.
     */
    protected List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> members;

    /**
     * The index of the current member that is being documented at this point
     * in time.
     */
    protected int currentMemberIndex;

    /**
     * Construct a new AnnotationTypeRequiredMemberBuilder.
     *
     * @param context  the build context.
     * @param classDoc the class whose members are being documented.
     * @param writer the doclet specific writer.
     */
    protected AnnotationTypeRequiredMemberBuilder(Context context,
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
            AnnotationTypeRequiredMemberWriter writer,
            int memberType) {
        super(context);
        this.classDoc = classDoc;
        this.writer = writer;
        this.visibleMemberMap = new VisibleMemberMap(classDoc, memberType,
            configuration);
        this.members = new ArrayList<>(this.visibleMemberMap.getMembersFor(classDoc));
        if (configuration.getMemberComparator() != null) {
            Collections.sort(this.members, configuration.getMemberComparator());
        }
    }


    /**
     * Construct a new AnnotationTypeMemberBuilder.
     *
     * @param context  the build context.
     * @param classDoc the class whose members are being documented.
     * @param writer the doclet specific writer.
     */
    public static AnnotationTypeRequiredMemberBuilder getInstance(
            Context context, gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
            AnnotationTypeRequiredMemberWriter writer) {
        return new AnnotationTypeRequiredMemberBuilder(context, classDoc,
                    writer,
                    VisibleMemberMap.ANNOTATION_TYPE_MEMBER_REQUIRED);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "AnnotationTypeRequiredMemberDetails";
    }

    /**
     * Returns a list of members that will be documented for the given class.
     * This information can be used for doclet specific documentation
     * generation.
     *
     * @param classDoc the {@link gw.gosudoc.com.sun.javadoc.ClassDoc} we want to check.
     * @return a list of members that will be documented.
     */
    public List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> members( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        return visibleMemberMap.getMembersFor(classDoc);
    }

    /**
     * Returns the visible member map for the members of this class.
     *
     * @return the visible member map for the members of this class.
     */
    public VisibleMemberMap getVisibleMemberMap() {
        return visibleMemberMap;
    }

    /**
     * summaryOrder.size()
     */
    public boolean hasMembersToDocument() {
        return members.size() > 0;
    }

    /**
     * Build the annotation type required member documentation.
     *
     * @param node the XML element that specifies which components to document
     * @param memberDetailsTree the content tree to which the documentation will be added
     */
    public void buildAnnotationTypeRequiredMember(XMLNode node, Content memberDetailsTree) {
        buildAnnotationTypeMember(node, memberDetailsTree);
    }

    /**
     * Build the member documentation.
     *
     * @param node the XML element that specifies which components to document
     * @param memberDetailsTree the content tree to which the documentation will be added
     */
    public void buildAnnotationTypeMember(XMLNode node, Content memberDetailsTree) {
        if (writer == null) {
            return;
        }
        int size = members.size();
        if (size > 0) {
            writer.addAnnotationDetailsMarker(memberDetailsTree);
            for (currentMemberIndex = 0; currentMemberIndex < size;
                    currentMemberIndex++) {
                Content detailsTree = writer.getMemberTreeHeader();
                writer.addAnnotationDetailsTreeHeader(classDoc, detailsTree);
                Content annotationDocTree = writer.getAnnotationDocTreeHeader(
                        (gw.gosudoc.com.sun.javadoc.MemberDoc) members.get(currentMemberIndex), detailsTree);
                buildChildren(node, annotationDocTree);
                detailsTree.addContent(writer.getAnnotationDoc(
                        annotationDocTree, (currentMemberIndex == size - 1)));
                memberDetailsTree.addContent(writer.getAnnotationDetails(detailsTree));
            }
        }
    }

    /**
     * Build the signature.
     *
     * @param node the XML element that specifies which components to document
     * @param annotationDocTree the content tree to which the documentation will be added
     */
    public void buildSignature(XMLNode node, Content annotationDocTree) {
        annotationDocTree.addContent(
                writer.getSignature((gw.gosudoc.com.sun.javadoc.MemberDoc) members.get(currentMemberIndex)));
    }

    /**
     * Build the deprecation information.
     *
     * @param node the XML element that specifies which components to document
     * @param annotationDocTree the content tree to which the documentation will be added
     */
    public void buildDeprecationInfo(XMLNode node, Content annotationDocTree) {
        writer.addDeprecated((gw.gosudoc.com.sun.javadoc.MemberDoc) members.get(currentMemberIndex),
                annotationDocTree);
    }

    /**
     * Build the comments for the member.  Do nothing if
     * {@link Configuration#nocomment} is set to true.
     *
     * @param node the XML element that specifies which components to document
     * @param annotationDocTree the content tree to which the documentation will be added
     */
    public void buildMemberComments(XMLNode node, Content annotationDocTree) {
        if(! configuration.nocomment){
            writer.addComments((gw.gosudoc.com.sun.javadoc.MemberDoc) members.get(currentMemberIndex),
                    annotationDocTree);
        }
    }

    /**
     * Build the tag information.
     *
     * @param node the XML element that specifies which components to document
     * @param annotationDocTree the content tree to which the documentation will be added
     */
    public void buildTagInfo(XMLNode node, Content annotationDocTree) {
        writer.addTags((MemberDoc) members.get(currentMemberIndex),
                annotationDocTree);
    }

    /**
     * Return the annotation type required member writer for this builder.
     *
     * @return the annotation type required member constant writer for this
     * builder.
     */
    public AnnotationTypeRequiredMemberWriter getWriter() {
        return writer;
    }
}
