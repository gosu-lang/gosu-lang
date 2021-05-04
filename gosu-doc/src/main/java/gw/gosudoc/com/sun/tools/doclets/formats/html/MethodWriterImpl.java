/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;




import gw.gosudoc.com.sun.javadoc.Type;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.*;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.MethodWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.ImplementedMethods;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Writes method documentation in HTML format.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Robert Field
 * @author Atul M Dambalkar
 * @author Jamie Ho (rewrite)
 * @author Bhavesh Patel (Modified)
 */
@Deprecated
public class MethodWriterImpl extends AbstractExecutableMemberWriter
        implements MethodWriter, MemberSummaryWriter
{

    /**
     * Construct a new MethodWriterImpl.
     *
     * @param writer the writer for the class that the methods belong to.
     * @param classDoc the class being documented.
     */
    public MethodWriterImpl(SubWriterHolderWriter writer, gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        super(writer, classDoc);
    }

    /**
     * Construct a new MethodWriterImpl.
     *
     * @param writer The writer for the class that the methods belong to.
     */
    public MethodWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMemberSummaryHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                           Content memberSummaryTree) {
        memberSummaryTree.addContent( HtmlConstants.START_OF_METHOD_SUMMARY);
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
    public Content getMethodDetailsTreeHeader( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc,
                                               Content memberDetailsTree) {
        memberDetailsTree.addContent(HtmlConstants.START_OF_METHOD_DETAILS);
        Content methodDetailsTree = writer.getMemberTreeHeader();
        methodDetailsTree.addContent(writer.getMarkerAnchor(
                SectionName.METHOD_DETAIL));
        Content heading = HtmlTree.HEADING(HtmlConstants.DETAILS_HEADING,
                writer.methodDetailsLabel);
        methodDetailsTree.addContent(heading);
        return methodDetailsTree;
    }

    /**
     * {@inheritDoc}
     */
    public Content getMethodDocTreeHeader( gw.gosudoc.com.sun.javadoc.MethodDoc method,
                                           Content methodDetailsTree) {
        String erasureAnchor;
        if ((erasureAnchor = getErasureAnchor(method)) != null) {
            methodDetailsTree.addContent(writer.getMarkerAnchor((erasureAnchor)));
        }
        methodDetailsTree.addContent(
                writer.getMarkerAnchor(writer.getAnchor(method)));
        Content methodDocTree = writer.getMemberTreeHeader();
        Content heading = new HtmlTree(HtmlConstants.MEMBER_HEADING);
        heading.addContent(method.name());
        methodDocTree.addContent(heading);
        return methodDocTree;
    }

    /**
     * Get the signature for the given method.
     *
     * @param method the method being documented.
     * @return a content object for the signature
     */
    public Content getSignature( gw.gosudoc.com.sun.javadoc.MethodDoc method) {
        Content pre = new HtmlTree( HtmlTag.PRE);
        writer.addAnnotationInfo(method, pre);
        int annotationLength = pre.charCount();
        addModifiers(method, pre);
        addTypeParameters(method, pre);
        addReturnType(method, pre);
        if (configuration.linksource) {
            Content methodName = new StringContent(method.name());
            writer.addSrcLink(method, methodName, pre);
        } else {
            addName(method.name(), pre);
        }
        int indent = pre.charCount() - annotationLength;
        addParameters(method, pre, indent);
        addExceptions(method, pre, indent);
        return pre;
    }

    /**
     * {@inheritDoc}
     */
    public void addDeprecated( gw.gosudoc.com.sun.javadoc.MethodDoc method, Content methodDocTree) {
        addDeprecatedInfo(method, methodDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public void addComments( gw.gosudoc.com.sun.javadoc.Type holder, gw.gosudoc.com.sun.javadoc.MethodDoc method, Content methodDocTree) {
        gw.gosudoc.com.sun.javadoc.ClassDoc holderClassDoc = holder.asClassDoc();
        if (method.inlineTags().length > 0) {
            if (holder.asClassDoc().equals(classdoc) ||
                    (! (holderClassDoc.isPublic() ||
                    utils.isLinkable(holderClassDoc, configuration)))) {
                writer.addInlineComment(method, methodDocTree);
            } else {
                Content link =
                        writer.getDocLink(LinkInfoImpl.Kind.METHOD_DOC_COPY,
                        holder.asClassDoc(), method,
                        holder.asClassDoc().isIncluded() ?
                            holder.typeName() : holder.qualifiedTypeName(),
                            false);
                Content codelLink = HtmlTree.CODE(link);
                Content descfrmLabel = HtmlTree.SPAN( HtmlStyle.descfrmTypeLabel, holder.asClassDoc().isClass()?
                    writer.descfrmClassLabel : writer.descfrmInterfaceLabel);
                descfrmLabel.addContent(writer.getSpace());
                descfrmLabel.addContent(codelLink);
                methodDocTree.addContent(HtmlTree.DIV(HtmlStyle.block, descfrmLabel));
                writer.addInlineComment(method, methodDocTree);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addTags( gw.gosudoc.com.sun.javadoc.MethodDoc method, Content methodDocTree) {
        writer.addTagsInfo(method, methodDocTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMethodDetails(Content methodDetailsTree) {
        if (configuration.allowTag(HtmlTag.SECTION)) {
            HtmlTree htmlTree = HtmlTree.SECTION(getMemberTree(methodDetailsTree));
            return htmlTree;
        }
        return getMemberTree(methodDetailsTree);
    }

    /**
     * {@inheritDoc}
     */
    public Content getMethodDoc(Content methodDocTree,
            boolean isLastContent) {
        return getMemberTree(methodDocTree, isLastContent);
    }

    /**
     * Close the writer.
     */
    public void close() throws IOException {
        writer.close();
    }

    public int getMemberKind() {
        return VisibleMemberMap.METHODS;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryLabel(Content memberTree) {
        Content label = HtmlTree.HEADING(HtmlConstants.SUMMARY_HEADING,
                writer.getResource("doclet.Method_Summary"));
        memberTree.addContent(label);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableSummary() {
        return configuration.getText("doclet.Member_Table_Summary",
                configuration.getText("doclet.Method_Summary"),
                configuration.getText("doclet.methods"));
    }

    /**
     * {@inheritDoc}
     */
    public Content getCaption() {
        return configuration.getResource("doclet.Methods");
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSummaryTableHeader( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        String[] header = new String[] {
            writer.getModifierTypeHeader(),
            configuration.getText("doclet.0_and_1",
                    configuration.getText("doclet.Method"),
                    configuration.getText("doclet.Description"))
        };
        return header;
    }

    /**
     * {@inheritDoc}
     */
    public void addSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content memberTree) {
        memberTree.addContent(writer.getMarkerAnchor(
                SectionName.METHOD_SUMMARY));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryAnchor( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content inheritedTree) {
        inheritedTree.addContent(writer.getMarkerAnchor(
                SectionName.METHODS_INHERITANCE, configuration.getClassName(cd)));
    }

    /**
     * {@inheritDoc}
     */
    public void addInheritedSummaryLabel( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Content inheritedTree) {
        Content classLink = writer.getPreQualifiedClassLink(
                LinkInfoImpl.Kind.MEMBER, cd, false);
        Content label = new StringContent(cd.isClass() ?
            configuration.getText("doclet.Methods_Inherited_From_Class") :
            configuration.getText("doclet.Methods_Inherited_From_Interface"));
        Content labelHeading = HtmlTree.HEADING(HtmlConstants.INHERITED_SUMMARY_HEADING,
                label);
        labelHeading.addContent(writer.getSpace());
        labelHeading.addContent(classLink);
        inheritedTree.addContent(labelHeading);
    }

    /**
     * {@inheritDoc}
     */
    protected void addSummaryType( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content tdSummaryType) {
        gw.gosudoc.com.sun.javadoc.MethodDoc meth = (gw.gosudoc.com.sun.javadoc.MethodDoc)member;
        addModifierAndType(meth, meth.returnType(), tdSummaryType);
    }

    /**
     * {@inheritDoc}
     */
    protected static void addOverridden( HtmlDocletWriter writer,
                                         gw.gosudoc.com.sun.javadoc.Type overriddenType, gw.gosudoc.com.sun.javadoc.MethodDoc method, Content dl) {
        if (writer.configuration.nocomment) {
            return;
        }
        gw.gosudoc.com.sun.javadoc.ClassDoc holderClassDoc = overriddenType.asClassDoc();
        if (! (holderClassDoc.isPublic() ||
            writer.configuration.utils.isLinkable(holderClassDoc, writer.configuration))) {
            //This is an implementation detail that should not be documented.
            return;
        }
        if (overriddenType.asClassDoc().isIncluded() && ! method.isIncluded()) {
            //The class is included but the method is not.  That means that it
            //is not visible so don't document this.
            return;
        }
        Content label = writer.overridesLabel;
        LinkInfoImpl.Kind context = LinkInfoImpl.Kind.METHOD_OVERRIDES;

        if (method != null) {
            if (overriddenType.asClassDoc().isAbstract() && method.isAbstract()){
                //Abstract method is implemented from abstract class,
                //not overridden
                label = writer.specifiedByLabel;
                context = LinkInfoImpl.Kind.METHOD_SPECIFIED_BY;
            }
            Content dt = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, label));
            dl.addContent(dt);
            Content overriddenTypeLink =
                    writer.getLink(new LinkInfoImpl(writer.configuration, context, overriddenType));
            Content codeOverridenTypeLink = HtmlTree.CODE(overriddenTypeLink);
            String name = method.name();
            Content methlink = writer.getLink(
                    new LinkInfoImpl(writer.configuration, LinkInfoImpl.Kind.MEMBER,
                    overriddenType.asClassDoc())
                    .where(writer.getName(writer.getAnchor(method))).label(name));
            Content codeMethLink = HtmlTree.CODE(methlink);
            Content dd = HtmlTree.DD(codeMethLink);
            dd.addContent(writer.getSpace());
            dd.addContent(writer.getResource("doclet.in_class"));
            dd.addContent(writer.getSpace());
            dd.addContent(codeOverridenTypeLink);
            dl.addContent(dd);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected static void addImplementsInfo( HtmlDocletWriter writer,
                                             gw.gosudoc.com.sun.javadoc.MethodDoc method, Content dl) {
        if(writer.configuration.nocomment){
            return;
        }
        ImplementedMethods implementedMethodsFinder =
                new ImplementedMethods(method, writer.configuration);
        gw.gosudoc.com.sun.javadoc.MethodDoc[] implementedMethods = implementedMethodsFinder.build();
        for ( gw.gosudoc.com.sun.javadoc.MethodDoc implementedMeth : implementedMethods) {
            gw.gosudoc.com.sun.javadoc.Type intfac = implementedMethodsFinder.getMethodHolder(implementedMeth);
            Content intfaclink = writer.getLink(new LinkInfoImpl(
                    writer.configuration, LinkInfoImpl.Kind.METHOD_SPECIFIED_BY, intfac));
            Content codeIntfacLink = HtmlTree.CODE(intfaclink);
            Content dt = HtmlTree.DT(HtmlTree.SPAN(HtmlStyle.overrideSpecifyLabel, writer.specifiedByLabel));
            dl.addContent(dt);
            Content methlink = writer.getDocLink(
                    LinkInfoImpl.Kind.MEMBER, implementedMeth,
                    implementedMeth.name(), false);
            Content codeMethLink = HtmlTree.CODE(methlink);
            Content dd = HtmlTree.DD(codeMethLink);
            dd.addContent(writer.getSpace());
            dd.addContent(writer.getResource("doclet.in_interface"));
            dd.addContent(writer.getSpace());
            dd.addContent(codeIntfacLink);
            dl.addContent(dd);
        }
    }

    /**
     * Add the return type.
     *
     * @param method the method being documented.
     * @param htmltree the content tree to which the return type will be added
     */
    protected void addReturnType( gw.gosudoc.com.sun.javadoc.MethodDoc method, Content htmltree) {
        Type type = method.returnType();
        if (type != null) {
            Content linkContent = writer.getLink(
                    new LinkInfoImpl(configuration, LinkInfoImpl.Kind.RETURN_TYPE, type));
            htmltree.addContent(linkContent);
            htmltree.addContent(writer.getSpace());
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Content getNavSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean link) {
        if (link) {
            if (cd == null) {
                return writer.getHyperLink(
                        SectionName.METHOD_SUMMARY,
                        writer.getResource("doclet.navMethod"));
            } else {
                return writer.getHyperLink(
                        SectionName.METHODS_INHERITANCE,
                        configuration.getClassName(cd), writer.getResource("doclet.navMethod"));
            }
        } else {
            return writer.getResource("doclet.navMethod");
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavDetailLink(boolean link, Content liNav) {
        if (link) {
            liNav.addContent(writer.getHyperLink(
                    SectionName.METHOD_DETAIL, writer.getResource("doclet.navMethod")));
        } else {
            liNav.addContent(writer.getResource("doclet.navMethod"));
        }
    }
}
