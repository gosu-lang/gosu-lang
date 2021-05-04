/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;




import gw.gosudoc.com.sun.javadoc.FieldDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.*;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.EnumConstantWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Writes enum constant documentation in HTML format.
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
public class EnumConstantWriterImpl extends AbstractMemberWriter
    implements EnumConstantWriter, MemberSummaryWriter
{

    public EnumConstantWriterImpl(SubWriterHolderWriter writer,
        gw.gosudoc.com.sun.javadoc.ClassDoc classdoc) {
        super(writer, classdoc);
    }

    public EnumConstantWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree) {
        memberSummaryTree.addContent( HtmlConstants.START_OF_ENUM_CONSTANT_SUMMARY);
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
    public Content getEnumConstantsDetailsTreeHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                                      Content memberDetailsTree) {
        memberDetailsTree.addContent(HtmlConstants.START_OF_ENUM_CONSTANT_DETAILS);
        Content enumConstantsDetailsTree = writer.getMemberTreeHeader();
        enumConstantsDetailsTree.addContent(writer.getMarkerAnchor(
                SectionName.ENUM_CONSTANT_DETAIL));
        Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                writer.enumConstantsDetailsLabel);
        enumConstantsDetailsTree.addContent(heading);
        return enumConstantsDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getEnumConstantsTreeHeader( gw.gosudoc.com.sun.javadoc.FieldDoc enumConstant,
                                               Content enumConstantsDetailsTree) {
        enumConstantsDetailsTree.addContent(
                writer.getMarkerAnchor(enumConstant.name()));
        Content enumConstantsTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(enumConstant.name());
        enumConstantsTree.addContent(heading);
        return enumConstantsTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature( gw.gosudoc.com.sun.javadoc.FieldDoc enumConstant) {
        Content pre = new HtmlTree( HtmlTag.PRE);
        writer.addAnnotationInfo(enumConstant, pre);
        addModifiers(enumConstant, pre);
        Content enumConstantLink = writer.getLink(new LinkInfoImpl(
                configuration, LinkInfoImpl.Kind.MEMBER, enumConstant.type()));
        pre.addContent(enumConstantLink);
        pre.addContent(" ");
        if (configuration.linksource) {
            Content enumConstantName = new StringContent(enumConstant.name());
            writer.addSrcLink(enumConstant, enumConstantName, pre);
        } else {
            addName(enumConstant.name(), pre);
        }
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated( gw.gosudoc.com.sun.javadoc.FieldDoc enumConstant, Content enumConstantsTree) {
        addDeprecatedInfo(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments( gw.gosudoc.com.sun.javadoc.FieldDoc enumConstant, Content enumConstantsTree) {
        addComment(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addTags( gw.gosudoc.com.sun.javadoc.FieldDoc enumConstant, Content enumConstantsTree) {
        writer.addTagsInfo(enumConstant, enumConstantsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getEnumConstantsDetails(Content enumConstantsDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(enumConstantsDetailsTree));
            return htmlTree;
        }
        return getMemberTree(enumConstantsDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getEnumConstants(Content enumConstantsTree,
            boolean isLastContent) {
        return getMemberTree(enumConstantsTree, isLastContent);
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        writer.close();
    }

    public int getMemberKind() {
        return VisibleMemberMap.ENUM_CONSTANTS;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Enum_Constant_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration.getText("doclet.Member_Table_Summary",
                configuration.getText("doclet.Enum_Constant_Summary"),
                configuration.getText("doclet.enum_constants"));
    }

    /**
     * {@inheritDoc}
     */
    public Content getCaption() {
        return configuration.getResource("doclet.Enum_Constants");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        String[] header = new String[] {
            configuration.getText("doclet.0_and_1",
                    configuration.getText("doclet.Enum_Constant"),
                    configuration.getText("doclet.Description"))
        };
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor(
                SectionName.ENUM_CONSTANT_SUMMARY));
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
    @Override
    public void setSummaryColumnStyle(HtmlTree tdTree) {
        tdTree.addStyle(HtmlStyle.colOne);
    }

    /**
     * {@inheritDoc}
     */
    protected void addInheritedSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd,
                                            gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content linksTree) {
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content tdSummaryType) {
        //Not applicable.
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER,
                (gw.gosudoc.com.sun.javadoc.MemberDoc) member, ((FieldDoc)member).qualifiedName());
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean link) {
        if (link) {
            if (cd == null) {
                return writer.getHyperLink(SectionName.ENUM_CONSTANT_SUMMARY,
                        writer.getResource("doclet.navEnum"));
            } else {
                return writer.getHyperLink(
                        SectionName.ENUM_CONSTANTS_INHERITANCE,
                        configuration.getClassName(cd), writer.getResource("doclet.navEnum"));
            }
        } else {
            return writer.getResource("doclet.navEnum");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavDetailLink(boolean link, Content liNav) {
        if (link) {
            liNav.addContent(writer.getHyperLink(
                    SectionName.ENUM_CONSTANT_DETAIL,
                    writer.getResource("doclet.navEnum")));
        } else {
            liNav.addContent(writer.getResource("doclet.navEnum"));
        }
    }
}
