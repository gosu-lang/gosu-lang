/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.links;

import gw.gosudoc.com.sun.javadoc.WildcardType;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

/**
 * A factory that constructs links from given link information.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.5
 */
@Deprecated
public abstract class LinkFactory {

    /**
     * Return an empty instance of a content object.
     *
     * @return an empty instance of a content object.
     */
    protected abstract Content newContent();

    /**
     * Constructs a link from the given link information.
     *
     * @param linkInfo the information about the link.
     * @return the output of the link.
     */
    public Content getLink(LinkInfo linkInfo) {
        if (linkInfo.type != null) {
            gw.gosudoc.com.sun.javadoc.Type type = linkInfo.type;
            Content link = newContent();
            if (type.isPrimitive()) {
                //Just a primitive.
                link.addContent(type.typeName());
            } else if (type.asAnnotatedType() != null && type.dimension().length() == 0) {
                link.addContent(getTypeAnnotationLinks(linkInfo));
                linkInfo.type = type.asAnnotatedType().underlyingType();
                link.addContent(getLink(linkInfo));
                return link;
            } else if (type.asWildcardType() != null) {
                //Wildcard type.
                linkInfo.isTypeBound = true;
                link.addContent("?");
                WildcardType wildcardType = type.asWildcardType();
                gw.gosudoc.com.sun.javadoc.Type[] extendsBounds = wildcardType.extendsBounds();
                for (int i = 0; i < extendsBounds.length; i++) {
                    link.addContent(i > 0 ? ", " : " extends ");
                    setBoundsLinkInfo(linkInfo, extendsBounds[i]);
                    link.addContent(getLink(linkInfo));
                }
                gw.gosudoc.com.sun.javadoc.Type[] superBounds = wildcardType.superBounds();
                for (int i = 0; i < superBounds.length; i++) {
                    link.addContent(i > 0 ? ", " : " super ");
                    setBoundsLinkInfo(linkInfo, superBounds[i]);
                    link.addContent(getLink(linkInfo));
                }
            } else if (type.asTypeVariable()!= null) {
                link.addContent(getTypeAnnotationLinks(linkInfo));
                linkInfo.isTypeBound = true;
                //A type variable.
                gw.gosudoc.com.sun.javadoc.Doc owner = type.asTypeVariable().owner();
                if ((! linkInfo.excludeTypeParameterLinks) &&
                        owner instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
                    linkInfo.classDoc = (gw.gosudoc.com.sun.javadoc.ClassDoc) owner;
                    Content label = newContent();
                    label.addContent(type.typeName());
                    linkInfo.label = label;
                    link.addContent(getClassLink(linkInfo));
                } else {
                    //No need to link method type parameters.
                    link.addContent(type.typeName());
                }

                gw.gosudoc.com.sun.javadoc.Type[] bounds = type.asTypeVariable().bounds();
                if (! linkInfo.excludeTypeBounds) {
                    linkInfo.excludeTypeBounds = true;
                    for (int i = 0; i < bounds.length; i++) {
                        link.addContent(i > 0 ? " & " : " extends ");
                        setBoundsLinkInfo(linkInfo, bounds[i]);
                        link.addContent(getLink(linkInfo));
                    }
                }
            } else if (type.asClassDoc() != null) {
                //A class type.
                if (linkInfo.isTypeBound &&
                        linkInfo.excludeTypeBoundsLinks) {
                    //Since we are excluding type parameter links, we should not
                    //be linking to the type bound.
                    link.addContent(type.typeName());
                    link.addContent(getTypeParameterLinks(linkInfo));
                    return link;
                } else {
                    linkInfo.classDoc = type.asClassDoc();
                    link = newContent();
                    link.addContent(getClassLink(linkInfo));
                    if (linkInfo.includeTypeAsSepLink) {
                        link.addContent(getTypeParameterLinks(linkInfo, false));
                    }
                }
            }

            if (linkInfo.isVarArg) {
                if (type.dimension().length() > 2) {
                    //Javadoc returns var args as array.
                    //Strip out the first [] from the var arg.
                    link.addContent(type.dimension().substring(2));
                }
                link.addContent("...");
            } else {
                while (type != null && type.dimension().length() > 0) {
                    if (type.asAnnotatedType() != null) {
                        linkInfo.type = type;
                        link.addContent(" ");
                        link.addContent(getTypeAnnotationLinks(linkInfo));
                        link.addContent("[]");
                        type = type.asAnnotatedType().underlyingType().getElementType();
                    } else {
                        link.addContent("[]");
                        type = type.getElementType();
                    }
                }
                linkInfo.type = type;
                Content newLink = newContent();
                newLink.addContent(getTypeAnnotationLinks(linkInfo));
                newLink.addContent(link);
                link = newLink;
            }
            return link;
        } else if (linkInfo.classDoc != null) {
            //Just a class link
            Content link = newContent();
            link.addContent(getClassLink(linkInfo));
            if (linkInfo.includeTypeAsSepLink) {
                link.addContent(getTypeParameterLinks(linkInfo, false));
            }
            return link;
        } else {
            return null;
        }
    }

    private void setBoundsLinkInfo(LinkInfo linkInfo, gw.gosudoc.com.sun.javadoc.Type bound) {
        linkInfo.classDoc = null;
        linkInfo.label = null;
        linkInfo.type = bound;
    }

    /**
     * Return the link to the given class.
     *
     * @param linkInfo the information about the link to construct.
     *
     * @return the link for the given class.
     */
    protected abstract Content getClassLink(LinkInfo linkInfo);

    /**
     * Return the link to the given type parameter.
     *
     * @param linkInfo     the information about the link to construct.
     * @param typeParam the type parameter to link to.
     */
    protected abstract Content getTypeParameterLink(LinkInfo linkInfo,
        gw.gosudoc.com.sun.javadoc.Type typeParam);

    protected abstract Content getTypeAnnotationLink(LinkInfo linkInfo,
            gw.gosudoc.com.sun.javadoc.AnnotationDesc annotation);

    /**
     * Return the links to the type parameters.
     *
     * @param linkInfo     the information about the link to construct.
     * @return the links to the type parameters.
     */
    public Content getTypeParameterLinks(LinkInfo linkInfo) {
        return getTypeParameterLinks(linkInfo, true);
    }

    /**
     * Return the links to the type parameters.
     *
     * @param linkInfo     the information about the link to construct.
     * @param isClassLabel true if this is a class label.  False if it is
     *                     the type parameters portion of the link.
     * @return the links to the type parameters.
     */
    public Content getTypeParameterLinks(LinkInfo linkInfo, boolean isClassLabel) {
        Content links = newContent();
        gw.gosudoc.com.sun.javadoc.Type[] vars;
        if (linkInfo.executableMemberDoc != null) {
            vars = linkInfo.executableMemberDoc.typeParameters();
        } else if (linkInfo.type != null &&
                linkInfo.type.asParameterizedType() != null){
            vars =  linkInfo.type.asParameterizedType().typeArguments();
        } else if (linkInfo.classDoc != null){
            vars = linkInfo.classDoc.typeParameters();
        } else {
            //Nothing to document.
            return links;
        }
        if (((linkInfo.includeTypeInClassLinkLabel && isClassLabel) ||
             (linkInfo.includeTypeAsSepLink && ! isClassLabel)
              )
            && vars.length > 0) {
            links.addContent("<");
            for (int i = 0; i < vars.length; i++) {
                if (i > 0) {
                    links.addContent(",");
                }
                links.addContent(getTypeParameterLink(linkInfo, vars[i]));
            }
            links.addContent(">");
        }
        return links;
    }

    public Content getTypeAnnotationLinks(LinkInfo linkInfo) {
        Content links = newContent();
        if (linkInfo.type.asAnnotatedType() == null)
            return links;
        gw.gosudoc.com.sun.javadoc.AnnotationDesc[] annotations = linkInfo.type.asAnnotatedType().annotations();
        for (int i = 0; i < annotations.length; i++) {
            if (i > 0) {
                links.addContent(" ");
            }
            links.addContent(getTypeAnnotationLink(linkInfo, annotations[i]));
        }

        links.addContent(" ");
        return links;
    }
}
