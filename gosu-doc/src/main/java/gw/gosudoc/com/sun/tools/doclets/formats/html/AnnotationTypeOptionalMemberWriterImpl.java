/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;



import gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlConstants;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlTree;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.StringContent;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;

/**
 * Writes annotation type optional member documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 */
@Deprecated
public class AnnotationTypeOptionalMemberWriterImpl extends
        AnnotationTypeRequiredMemberWriterImpl
    implements AnnotationTypeOptionalMemberWriter, MemberSummaryWriter
{

    /**
     * Construct a new AnnotationTypeOptionalMemberWriterImpl.
     *
     * @param writer         the writer that will write the output.
     * @param annotationType the AnnotationType that holds this member.
     */
    public AnnotationTypeOptionalMemberWriterImpl(SubWriterHolderWriter writer,
        AnnotationTypeDoc annotationType) {
        super(writer, annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree) {
        memberSummaryTree.addContent(
                HtmlConstants.START_OF_ANNOTATION_TYPE_OPTIONAL_MEMBER_SUMMARY);
        Content memberTree = writer.getMemberTreeHeader();
        writer.addSummaryHeader(this, classDoc, memberTree);
        return memberTree;
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
    public void addDefaultValueInfo( gw.gosudoc.com.sun.javadoc.MemberDoc member, Content annotationDocTree) {
        if (((gw.gosudoc.com.sun.javadoc.AnnotationTypeElementDoc) member).defaultValue() != null) {
            Content dt = HtmlTree.DT(writer.getResource("doclet.Default"));
            Content dl = HtmlTree.DL(dt);
            Content dd = HtmlTree.DD(new StringContent(
                    ((gw.gosudoc.com.sun.javadoc.AnnotationTypeElementDoc) member).defaultValue().toString()));
            dl.addContent(dd);
            annotationDocTree.addContent(dl);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Annotation_Type_Optional_Member_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration.getText("doclet.Member_Table_Summary",
                configuration.getText("doclet.Annotation_Type_Optional_Member_Summary"),
                configuration.getText("doclet.annotation_type_optional_members"));
    }

    /**
     * {@inheritDoc}
     */
    public Content getCaption() {
        return configuration.getResource("doclet.Annotation_Type_Optional_Members");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        String[] header = new String[] {
            writer.getModifierTypeHeader(),
            configuration.getText("doclet.0_and_1",
                    configuration.getText("doclet.Annotation_Type_Optional_Member"),
                    configuration.getText("doclet.Description"))
        };
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor(
                SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean link) {
        if (link) {
            return writer.getHyperLink(
                    SectionName.ANNOTATION_TYPE_OPTIONAL_ELEMENT_SUMMARY,
                    writer.getResource("doclet.navAnnotationTypeOptionalMember"));
        } else {
            return writer.getResource("doclet.navAnnotationTypeOptionalMember");
        }
    }
}
