/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.RawHtml;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

/**
 * This taglet acts as a wrapper to enable
 * {@link gw.gosudoc.com.sun.tools.doclets.Taglet} type taglets to work
 * with the current version of Javadoc.
 * Note: this taglet only works with legacy taglets (those compatible with
 * Javadoc 1.4.x) that writes strings.
 * This taglet is able to wrap most most legacy taglets because
 * the standard doclet is the only known doclet to use legacy taglets.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.5
 * @author Jamie Ho
 */

@Deprecated
public class LegacyTaglet implements Taglet {

    private gw.gosudoc.com.sun.tools.doclets.Taglet legacyTaglet;

    public LegacyTaglet( gw.gosudoc.com.sun.tools.doclets.Taglet t) {
        legacyTaglet = t;
    }

    /**
     * {@inheritDoc}
     */
    public boolean inField() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inField();
    }

    /**
     * {@inheritDoc}
     */
    public boolean inConstructor() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inConstructor();
    }

    /**
     * {@inheritDoc}
     */
    public boolean inMethod() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inMethod();
    }

    /**
     * {@inheritDoc}
     */
    public boolean inOverview() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inOverview();
    }

    /**
     * {@inheritDoc}
     */
    public boolean inPackage() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inPackage();
    }

    /**
     * {@inheritDoc}
     */
    public boolean inType() {
        return legacyTaglet.isInlineTag() || legacyTaglet.inType();
    }

    /**
     * Return true if this <code>Taglet</code>
     * is an inline tag.
     * @return true if this <code>Taglet</code>
     * is an inline tag and false otherwise.
     */
    public boolean isInlineTag() {
        return legacyTaglet.isInlineTag();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return legacyTaglet.getName();
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Tag tag, TagletWriter writer)
            throws IllegalArgumentException {
        Content output = writer.getOutputInstance();
        output.addContent(new RawHtml(legacyTaglet.toString(tag)));
        return output;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Doc holder, TagletWriter writer)
            throws IllegalArgumentException {
        Content output = writer.getOutputInstance();
        Tag[] tags = holder.tags(getName());
        if (tags.length > 0) {
            String tagString = legacyTaglet.toString(tags);
            if (tagString != null) {
                output.addContent(new RawHtml(tagString));
            }
        }
        return output;
    }
}
