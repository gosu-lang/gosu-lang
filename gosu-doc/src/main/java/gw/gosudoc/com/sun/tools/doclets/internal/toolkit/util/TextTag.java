/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;
import gw.gosudoc.com.sun.javadoc.Tag;


/**
 * A tag that holds nothing but plain text.  This is useful for passing
 * text to methods that only accept inline tags as a parameter.
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
public class TextTag implements gw.gosudoc.com.sun.javadoc.Tag
{
    protected final String text;
    protected final String name = "Text";
    protected final gw.gosudoc.com.sun.javadoc.Doc holder;

    /**
     *  Constructor
     */
    public TextTag( gw.gosudoc.com.sun.javadoc.Doc holder, String text) {
        super();
        this.holder = holder;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    public String name() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public gw.gosudoc.com.sun.javadoc.Doc holder() {
        return holder;
    }

    /**
     * {@inheritDoc}
     */
    public String kind() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String text() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return name + ":" + text;
    }

    /**
     * {@inheritDoc}
     */
    public gw.gosudoc.com.sun.javadoc.Tag[] inlineTags() {
        return new gw.gosudoc.com.sun.javadoc.Tag[] {this};
    }

    /**
     * {@inheritDoc}
     */
    public gw.gosudoc.com.sun.javadoc.Tag[] firstSentenceTags() {
        return new Tag[] {this};
    }

    /**
     * {@inheritDoc}
     */
    public gw.gosudoc.com.sun.javadoc.SourcePosition position() {
        return holder.position();
    }
}
