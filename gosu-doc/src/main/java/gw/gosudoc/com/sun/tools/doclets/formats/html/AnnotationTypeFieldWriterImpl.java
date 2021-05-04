/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;



import gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.*;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.AnnotationTypeFieldWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;

/**
 * Writes annotation type field documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Bhavesh Patel
 */
@Deprecated
public class AnnotationTypeFieldWriterImpl extends AbstractMemberWriter
    implements AnnotationTypeFieldWriter, MemberSummaryWriter
{

    /**
     * Construct a new AnnotationTypeFieldWriterImpl.
     *
     * @param writer         the writer that will write the output.
     * @param annotationType the AnnotationType that holds this member.
     */
    public AnnotationTypeFieldWriterImpl(SubWriterHolderWriter writer,
            AnnotationTypeDoc annotationType) {
        super(writer, annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree) {
        memberSummaryTree.addContent(
                HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_SUMMARY);
        Content memberTree = writer.getMemberTreeHeader();
        writer.addSummaryHeader(this, classDoc, memberTree);
        return memberTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberTreeHeader() {
        return writer.getMemberTreeHeader();
    }

    /**
     * {@inheritDoc}
     */
    public void addMemberTree(Content memberSummaryTree, Content memberTree) {
        writer.addMemberTree(memberSummaryTree, memberTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotationFieldDetailsMarker(Content memberDetails) {
        memberDetails.addContent(HtmlConstants.START_OF_ANNOTATION_TYPE_FIELD_DETAILS);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotationDetailsTreeHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                                Content memberDetailsTree) {
        if (!writer.printedAnnotationFieldHeading) {
            memberDetailsTree.addContent(writer.getMarkerAnchor(
                    SectionName.ANNOTATION_TYPE_FIELD_DETAIL));
            Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                    writer.fieldDetailsLabel);
            memberDetailsTree.addContent(heading);
            writer.printedAnnotationFieldHeading = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDocTreeHeader( gw.gosudoc.com.sun.javadoc.MemberDoc member,
                                               Content annotationDetailsTree) {
        annotationDetailsTree.addContent(
                writer.getMarkerAnchor(member.name()));
        Content annotationDocTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(member.name());
        annotationDocTree.addContent(heading);
        return annotationDocTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature( gw.gosudoc.com.sun.javadoc.MemberDoc member) {
        Content pre = new HtmlTree( HtmlTag.PRE);
        writer.addAnnotationInfo(member, pre);
        addModifiers(member, pre);
        Content link =
                writer.getLink(new LinkInfoImpl(configuration,
                        LinkInfoImpl.Kind.MEMBER, getType(member)));
        pre.addContent(link);
        pre.addContent(writer.getSpace());
        if (configuration.linksource) {
            Content memberName = new StringContent(member.name());
            writer.addSrcLink(member, memberName, pre);
        } else {
            addName(member.name(), pre);
        }
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated( gw.gosudoc.com.sun.javadoc.MemberDoc member, Content annotationDocTree) {
        addDeprecatedInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments( gw.gosudoc.com.sun.javadoc.MemberDoc member, Content annotationDocTree) {
        addComment(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addTags( gw.gosudoc.com.sun.javadoc.MemberDoc member, Content annotationDocTree) {
        writer.addTagsInfo(member, annotationDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDetails(Content annotationDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(annotationDetailsTree));
            return htmlTree;
        }
        return getMemberTree(annotationDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getAnnotationDoc(Content annotationDocTree,
            boolean isLastContent) {
        return getMemberTree(annotationDocTree, isLastContent);
    }

    /**
     * Close the writer.
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Field_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration.getText("doclet.Member_Table_Summary",
                configuration.getText("doclet.Field_Summary"),
                configuration.getText("doclet.fields"));
    }

    /**
     * {@inheritDoc}
     */
    public Content getCaption() {
        return configuration.getResource("doclet.Fields");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        String[] header = new String[] {
            writer.getModifierTypeHeader(),
            configuration.getText("doclet.0_and_1",
                    configuration.getText("doclet.Fields"),
                    configuration.getText("doclet.Description"))
        };
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor(
                SectionName.ANNOTATION_TYPE_FIELD_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryLabel( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content inheritedTree) {
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryLink( LinkInfoImpl.Kind context, gw.gosudoc.com.sun.javadoc.ClassDoc cd, gw.gosudoc.com.sun.javadoc.ProgramElementDoc member,
                                   Content tdSummary) {
        Content memberLink = HtmlTree.SPAN( HtmlStyle.memberNameLink,
                writer.getDocLink(context, (gw.gosudoc.com.sun.javadoc.MemberDoc) member, member.name(), false));
        Content code = HtmlTree.CODE(memberLink);
        tdSummary.addContent(code);
    }

    /**
     * {@inheritDoc}
     */
    protected void addInheritedSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd,
                                            gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content linksTree) {
        //Not applicable.
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content tdSummaryType) {
        gw.gosudoc.com.sun.javadoc.MemberDoc m = (gw.gosudoc.com.sun.javadoc.MemberDoc)member;
        addModifierAndType(m, getType(m), tdSummaryType);
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER,
                (gw.gosudoc.com.sun.javadoc.MemberDoc) member, ((gw.gosudoc.com.sun.javadoc.MemberDoc)member).qualifiedName());
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean link) {
        if (link) {
            return writer.getHyperLink(
                    SectionName.ANNOTATION_TYPE_FIELD_SUMMARY,
                    writer.getResource("doclet.navField"));
        } else {
            return writer.getResource("doclet.navField");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavDetailLink(boolean link, Content liNav) {
        if (link) {
            liNav.addContent(writer.getHyperLink(
                    SectionName.ANNOTATION_TYPE_FIELD_DETAIL,
                    writer.getResource("doclet.navField")));
        } else {
            liNav.addContent(writer.getResource("doclet.navField"));
        }
    }

    private gw.gosudoc.com.sun.javadoc.Type getType( gw.gosudoc.com.sun.javadoc.MemberDoc member) {
        if (member instanceof gw.gosudoc.com.sun.javadoc.FieldDoc ) {
            return ((gw.gosudoc.com.sun.javadoc.FieldDoc) member).type();
        } else {
            return ((gw.gosudoc.com.sun.javadoc.MethodDoc) member).returnType();
        }
    }
}
