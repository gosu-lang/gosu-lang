/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;
import java.util.*;




import gw.gosudoc.com.sun.javadoc.ConstructorDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.*;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.ConstructorWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Writes constructor documentation.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 * @author Atul M Dambalkar
 * @author Bhavesh Patel (Modified)
 */
@Deprecated
public class ConstructorWriterImpl extends AbstractExecutableMemberWriter
    implements ConstructorWriter, MemberSummaryWriter
{

    private boolean foundNonPubConstructor = false;

    /**
     * Construct a new ConstructorWriterImpl.
     *
     * @param writer The writer for the class that the constructors belong to.
     * @param classDoc the class being documented.
     */
    public ConstructorWriterImpl(SubWriterHolderWriter writer,
            gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        super(writer, classDoc);
        VisibleMemberMap visibleMemberMap = new VisibleMemberMap(classDoc,
            VisibleMemberMap.CONSTRUCTORS, configuration);
        List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> constructors = new ArrayList<>(visibleMemberMap.getMembersFor(classDoc));
        for ( gw.gosudoc.com.sun.javadoc.ProgramElementDoc constructor : constructors) {
            if (constructor.isProtected() || constructor.isPrivate()) {
                setFoundNonPubConstructor(true);
            }
        }
    }

    /**
     * Construct a new ConstructorWriterImpl.
     *
     * @param writer The writer for the class that the constructors belong to.
     */
    public ConstructorWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree) {
        memberSummaryTree.addContent( HtmlConstants.START_OF_CONSTRUCTOR_SUMMARY);
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
    public Content getConstructorDetailsTreeHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                                    Content memberDetailsTree) {
        memberDetailsTree.addContent(HtmlConstants.START_OF_CONSTRUCTOR_DETAILS);
        Content constructorDetailsTree = writer.getMemberTreeHeader();
        constructorDetailsTree.addContent(writer.getMarkerAnchor(
                SectionName.CONSTRUCTOR_DETAIL));
        Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                writer.constructorDetailsLabel);
        constructorDetailsTree.addContent(heading);
        return constructorDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getConstructorDocTreeHeader( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor,
                                                Content constructorDetailsTree) {
        String erasureAnchor;
        if ((erasureAnchor = getErasureAnchor(constructor)) != null) {
            constructorDetailsTree.addContent(writer.getMarkerAnchor((erasureAnchor)));
        }
        constructorDetailsTree.addContent(
                writer.getMarkerAnchor(writer.getAnchor(constructor)));
        Content constructorDocTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(constructor.name());
        constructorDocTree.addContent(heading);
        return constructorDocTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getSignature( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor) {
        Content pre = new HtmlTree( HtmlTag.PRE);
        writer.addAnnotationInfo(constructor, pre);
        int annotationLength = pre.charCount();
        addModifiers(constructor, pre);
        if (configuration.linksource) {
            Content constructorName = new StringContent(constructor.name());
            writer.addSrcLink(constructor, constructorName, pre);
        } else {
            addName(constructor.name(), pre);
        }
        int indent = pre.charCount() - annotationLength;
        addParameters(constructor, pre, indent);
        addExceptions(constructor, pre, indent);
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSummaryColumnStyle(HtmlTree tdTree) {
        if (foundNonPubConstructor)
            tdTree.addStyle( HtmlStyle.colLast);
        else
            tdTree.addStyle(HtmlStyle.colOne);
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor, Content constructorDocTree) {
        addDeprecatedInfo(constructor, constructorDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments( gw.gosudoc.com.sun.javadoc.ConstructorDoc constructor, Content constructorDocTree) {
        addComment(constructor, constructorDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addTags( ConstructorDoc constructor, Content constructorDocTree) {
        writer.addTagsInfo(constructor, constructorDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getConstructorDetails(Content constructorDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(constructorDetailsTree));
            return htmlTree;
        }
        return getMemberTree(constructorDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getConstructorDoc(Content constructorDocTree,
            boolean isLastContent) {
        return getMemberTree(constructorDocTree, isLastContent);
    }

    /**
     * Close the writer.
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * Let the writer know whether a non public constructor was found.
     *
     * @param foundNonPubConstructor true if we found a non public constructor.
     */
    public void setFoundNonPubConstructor(boolean foundNonPubConstructor) {
        this.foundNonPubConstructor = foundNonPubConstructor;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Constructor_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration.getText("doclet.Member_Table_Summary",
                configuration.getText("doclet.Constructor_Summary"),
                configuration.getText("doclet.constructors"));
    }

    /**
     * {@inheritDoc}
     */
    public Content getCaption() {
        return configuration.getResource("doclet.Constructors");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        String[] header;
        if (foundNonPubConstructor) {
            header = new String[] {
                configuration.getText("doclet.Modifier"),
                configuration.getText("doclet.0_and_1",
                        configuration.getText("doclet.Constructor"),
                        configuration.getText("doclet.Description"))
            };
        }
        else {
            header = new String[] {
                configuration.getText("doclet.0_and_1",
                        configuration.getText("doclet.Constructor"),
                        configuration.getText("doclet.Description"))
            };
        }
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor(
                SectionName.CONSTRUCTOR_SUMMARY));
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

    public int getMemberKind() {
        return VisibleMemberMap.CONSTRUCTORS;
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean link) {
        if (link) {
            return writer.getHyperLink(SectionName.CONSTRUCTOR_SUMMARY,
                    writer.getResource("doclet.navConstructor"));
        } else {
            return writer.getResource("doclet.navConstructor");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavDetailLink(boolean link, Content liNav) {
        if (link) {
            liNav.addContent(writer.getHyperLink(
                    SectionName.CONSTRUCTOR_DETAIL,
                    writer.getResource("doclet.navConstructor")));
        } else {
            liNav.addContent(writer.getResource("doclet.navConstructor"));
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content tdSummaryType) {
        if (foundNonPubConstructor) {
            Content code = new HtmlTree(HtmlTag.CODE);
            if (member.isProtected()) {
                code.addContent("protected ");
            } else if (member.isPrivate()) {
                code.addContent("private ");
            } else if (member.isPublic()) {
                code.addContent(writer.getSpace());
            } else {
                code.addContent(
                        configuration.getText("doclet.Package_private"));
            }
            tdSummaryType.addContent(code);
        }
    }
}
