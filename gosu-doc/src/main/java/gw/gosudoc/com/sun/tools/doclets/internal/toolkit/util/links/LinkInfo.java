/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.links;

import gw.gosudoc.com.sun.javadoc.Type;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

/**
 * Encapsulates information about a link.
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
public abstract class LinkInfo {

    /**
     * The ClassDoc we want to link to.  Null if we are not linking
     * to a ClassDoc.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc classDoc;

    /**
     * The executable member doc we want to link to.  Null if we are not linking
     * to an executable member.
     */
    public gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc executableMemberDoc;

    /**
     * The Type we want to link to.  Null if we are not linking to a type.
     */
    public Type type;

    /**
     * True if this is a link to a VarArg.
     */
    public boolean isVarArg = false;

    /**
     * Set this to true to indicate that you are linking to a type parameter.
     */
    public boolean isTypeBound = false;

    /**
     * Whether the document element is in a Java 5 declaration
     * location or not.
     */
    public boolean isJava5DeclarationLocation = true;

    /**
     * The label for the link.
     */
    public Content label;

    /**
     * True if the link should be strong.
     */
    public boolean isStrong = false;

    /**
     * True if we should include the type in the link label.  False otherwise.
     */
    public boolean includeTypeInClassLinkLabel = true;

    /**
     * True if we should include the type as separate link.  False otherwise.
     */
    public boolean includeTypeAsSepLink = false;

    /**
     * True if we should exclude the type bounds for the type parameter.
     */
    public boolean excludeTypeBounds = false;

    /**
     * True if we should print the type parameters, but not link them.
     */
    public boolean excludeTypeParameterLinks = false;

    /**
     * True if we should print the type bounds, but not link them.
     */
    public boolean excludeTypeBoundsLinks = false;

    /**
     * By default, the link can be to the page it's already on.  However,
     * there are cases where we don't want this (e.g. heading of class page).
     */
    public boolean linkToSelf = true;

    /**
     * Return an empty instance of a content object.
     *
     * @return an empty instance of a content object.
     */
    protected abstract Content newContent();

    /**
     * Return true if this link is linkable and false if we can't link to the
     * desired place.
     *
     * @return true if this link is linkable and false if we can't link to the
     * desired place.
     */
    public abstract boolean isLinkable();

    /**
     * Return the label for this class link.
     *
     * @param configuration the current configuration of the doclet.
     * @return the label for this class link.
     */
    public Content getClassLinkLabel(Configuration configuration) {
        if (label != null && !label.isEmpty()) {
            return label;
        } else if (isLinkable()) {
            Content label = newContent();
            label.addContent(classDoc.name());
            return label;
        } else {
            Content label = newContent();
            label.addContent(configuration.getClassName(classDoc));
            return label;
        }
    }
}
