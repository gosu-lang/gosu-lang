/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;




import gw.gosudoc.com.sun.javadoc.AnnotatedType;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlStyle;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlTree;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletConstants;

/**
 * Print method and constructor info.
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
public abstract class AbstractExecutableMemberWriter extends AbstractMemberWriter {

    public AbstractExecutableMemberWriter(SubWriterHolderWriter writer,
            gw.gosudoc.com.sun.javadoc.ClassDoc classdoc) {
        super(writer, classdoc);
    }

    public AbstractExecutableMemberWriter(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * Add the type parameters for the executable member.
     *
     * @param member the member to write type parameters for.
     * @param htmltree the content tree to which the parameters will be added.
     */
    protected void addTypeParameters( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member, Content htmltree) {
        Content typeParameters = getTypeParameters(member);
        if (!typeParameters.isEmpty()) {
            htmltree.addContent(typeParameters);
            htmltree.addContent(writer.getSpace());
        }
    }

    /**
     * Get the type parameters for the executable member.
     *
     * @param member the member for which to get the type parameters.
     * @return the type parameters.
     */
    protected Content getTypeParameters( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member) {
        LinkInfoImpl linkInfo = new LinkInfoImpl(configuration,
            LinkInfoImpl.Kind.MEMBER_TYPE_PARAMS, member);
        return writer.getTypeParameterLinks(linkInfo);
    }

    /**
     * {@inheritDoc}
     */
    protected Content getDeprecatedLink( gw.gosudoc.com.sun.javadoc.ProgramElementDoc member) {
        gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc emd = (gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)member;
        return writer.getDocLink(LinkInfoImpl.Kind.MEMBER, (gw.gosudoc.com.sun.javadoc.MemberDoc) emd,
                emd.qualifiedName() + emd.flatSignature());
    }

    /**
     * Add the summary link for the member.
     *
     * @param context the id of the context where the link will be printed
     * @param cd the classDoc that we should link to
     * @param member the member being linked to
     * @param tdSummary the content tree to which the link will be added
     */
    protected void addSummaryLink( LinkInfoImpl.Kind context, gw.gosudoc.com.sun.javadoc.ClassDoc cd, gw.gosudoc.com.sun.javadoc.ProgramElementDoc member,
                                   Content tdSummary) {
        gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc emd = (gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc)member;
        String name = emd.name();
        Content memberLink = HtmlTree.SPAN( HtmlStyle.memberNameLink,
                writer.getDocLink(context, cd, (gw.gosudoc.com.sun.javadoc.MemberDoc) emd,
                name, false));
        Content code = HtmlTree.CODE(memberLink);
        addParameters(emd, false, code, name.length() - 1);
        tdSummary.addContent(code);
    }

    /**
     * Add the inherited summary link for the member.
     *
     * @param cd the classDoc that we should link to
     * @param member the member being linked to
     * @param linksTree the content tree to which the link will be added
     */
    protected void addInheritedSummaryLink( gw.gosudoc.com.sun.javadoc.ClassDoc cd,
                                            gw.gosudoc.com.sun.javadoc.ProgramElementDoc member, Content linksTree) {
        linksTree.addContent(
                writer.getDocLink(LinkInfoImpl.Kind.MEMBER, cd, (gw.gosudoc.com.sun.javadoc.MemberDoc) member,
                member.name(), false));
    }

    /**
     * Add the parameter for the executable member.
     *
     * @param member the member to write parameter for.
     * @param param the parameter that needs to be written.
     * @param isVarArg true if this is a link to var arg.
     * @param tree the content tree to which the parameter information will be added.
     */
    protected void addParam( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member, gw.gosudoc.com.sun.javadoc.Parameter param,
                             boolean isVarArg, Content tree) {
        if (param.type() != null) {
            Content link = writer.getLink(new LinkInfoImpl(
                    configuration, LinkInfoImpl.Kind.EXECUTABLE_MEMBER_PARAM,
                    param.type()).varargs(isVarArg));
            tree.addContent(link);
        }
        if(param.name().length() > 0) {
            tree.addContent(writer.getSpace());
            tree.addContent(param.name());
        }
    }

    /**
     * Add the receiver annotations information.
     *
     * @param member the member to write receiver annotations for.
     * @param rcvrType the receiver type.
     * @param descList list of annotation description.
     * @param tree the content tree to which the information will be added.
     */
    protected void addReceiverAnnotations( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member, gw.gosudoc.com.sun.javadoc.Type rcvrType,
                                           gw.gosudoc.com.sun.javadoc.AnnotationDesc[] descList, Content tree) {
        writer.addReceiverAnnotationInfo(member, descList, tree);
        tree.addContent(writer.getSpace());
        tree.addContent(rcvrType.typeName());
        LinkInfoImpl linkInfo = new LinkInfoImpl(configuration,
                LinkInfoImpl.Kind.CLASS_SIGNATURE, rcvrType);
        tree.addContent(writer.getTypeParameterLinks(linkInfo));
        tree.addContent(writer.getSpace());
        tree.addContent("this");
    }


    /**
     * Add all the parameters for the executable member.
     *
     * @param member the member to write parameters for.
     * @param htmltree the content tree to which the parameters information will be added.
     */
    protected void addParameters( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member, Content htmltree, int indentSize) {
        addParameters(member, true, htmltree, indentSize);
    }

    /**
     * Add all the parameters for the executable member.
     *
     * @param member the member to write parameters for.
     * @param includeAnnotations true if annotation information needs to be added.
     * @param htmltree the content tree to which the parameters information will be added.
     */
    protected void addParameters( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member,
                                  boolean includeAnnotations, Content htmltree, int indentSize) {
        htmltree.addContent("(");
        String sep = "";
        gw.gosudoc.com.sun.javadoc.Parameter[] params = member.parameters();
        String indent = makeSpace(indentSize + 1);
        gw.gosudoc.com.sun.javadoc.Type rcvrType = member.receiverType();
        if (includeAnnotations && rcvrType instanceof AnnotatedType ) {
            gw.gosudoc.com.sun.javadoc.AnnotationDesc[] descList = rcvrType.asAnnotatedType().annotations();
            if (descList.length > 0) {
                addReceiverAnnotations(member, rcvrType, descList, htmltree);
                sep = "," + DocletConstants.NL + indent;
            }
        }
        int paramstart;
        for (paramstart = 0; paramstart < params.length; paramstart++) {
            htmltree.addContent(sep);
            gw.gosudoc.com.sun.javadoc.Parameter param = params[paramstart];
            if (!param.name().startsWith("this$")) {
                if (includeAnnotations) {
                    boolean foundAnnotations =
                            writer.addAnnotationInfo(indent.length(),
                            member, param, htmltree);
                    if (foundAnnotations) {
                        htmltree.addContent(DocletConstants.NL);
                        htmltree.addContent(indent);
                    }
                }
                addParam(member, param,
                    (paramstart == params.length - 1) && member.isVarArgs(), htmltree);
                break;
            }
        }

        for (int i = paramstart + 1; i < params.length; i++) {
            htmltree.addContent(",");
            htmltree.addContent(DocletConstants.NL);
            htmltree.addContent(indent);
            if (includeAnnotations) {
                boolean foundAnnotations =
                        writer.addAnnotationInfo(indent.length(), member, params[i],
                        htmltree);
                if (foundAnnotations) {
                    htmltree.addContent(DocletConstants.NL);
                    htmltree.addContent(indent);
                }
            }
            addParam(member, params[i], (i == params.length - 1) && member.isVarArgs(),
                    htmltree);
        }
        htmltree.addContent(")");
    }

    /**
     * Add exceptions for the executable member.
     *
     * @param member the member to write exceptions for.
     * @param htmltree the content tree to which the exceptions information will be added.
     */
    protected void addExceptions( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc member, Content htmltree, int indentSize) {
        gw.gosudoc.com.sun.javadoc.Type[] exceptions = member.thrownExceptionTypes();
        if (exceptions.length > 0) {
            LinkInfoImpl memberTypeParam = new LinkInfoImpl(configuration,
                    LinkInfoImpl.Kind.MEMBER, member);
            String indent = makeSpace(indentSize + 1 - 7);
            htmltree.addContent(DocletConstants.NL);
            htmltree.addContent(indent);
            htmltree.addContent("throws ");
            indent = makeSpace(indentSize + 1);
            Content link = writer.getLink(new LinkInfoImpl(configuration,
                    LinkInfoImpl.Kind.MEMBER, exceptions[0]));
            htmltree.addContent(link);
            for(int i = 1; i < exceptions.length; i++) {
                htmltree.addContent(",");
                htmltree.addContent(DocletConstants.NL);
                htmltree.addContent(indent);
                Content exceptionLink = writer.getLink(new LinkInfoImpl(
                        configuration, LinkInfoImpl.Kind.MEMBER, exceptions[i]));
                htmltree.addContent(exceptionLink);
            }
        }
    }

    protected gw.gosudoc.com.sun.javadoc.ClassDoc implementsMethodInIntfac( gw.gosudoc.com.sun.javadoc.MethodDoc method,
                                                                            gw.gosudoc.com.sun.javadoc.ClassDoc[] intfacs) {
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc intf : intfacs) {
            gw.gosudoc.com.sun.javadoc.MethodDoc[] methods = intf.methods();
            if (methods.length > 0) {
                for ( gw.gosudoc.com.sun.javadoc.MethodDoc md : methods) {
                    if (md.name().equals(method.name()) &&
                        md.signature().equals(method.signature())) {
                        return intf;
                    }
                }
            }
        }
        return null;
    }

    /**
     * For backward compatibility, include an anchor using the erasures of the
     * parameters.  NOTE:  We won't need this method anymore after we fix
     * see tags so that they use the type instead of the erasure.
     *
     * @param emd the ExecutableMemberDoc to anchor to.
     * @return the 1.4.x style anchor for the ExecutableMemberDoc.
     */
    protected String getErasureAnchor( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc emd) {
        StringBuilder buf = new StringBuilder(emd.name() + "(");
        gw.gosudoc.com.sun.javadoc.Parameter[] params = emd.parameters();
        boolean foundTypeVariable = false;
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                buf.append(",");
            }
            gw.gosudoc.com.sun.javadoc.Type t = params[i].type();
            foundTypeVariable = foundTypeVariable || t.asTypeVariable() != null;
            buf.append(t.isPrimitive() ?
                t.typeName() : t.asClassDoc().qualifiedName());
            buf.append(t.dimension());
        }
        buf.append(")");
        return foundTypeVariable ? writer.getName(buf.toString()) : null;
    }
}
