/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * A taglet that represents the @see tag.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.4
 */
@Deprecated
public class SeeTaglet extends BaseTaglet implements InheritableTaglet {

    public SeeTaglet() {
        name = "see";
    }

    /**
     * {@inheritDoc}
     */
    public void inherit( DocFinder.Input input, DocFinder.Output output) {
        Tag[] tags = input.element.seeTags();
        if (tags.length > 0) {
            output.holder = input.element;
            output.holderTag = tags[0];
            output.inlineTags = input.isFirstSentence ?
                tags[0].firstSentenceTags() : tags[0].inlineTags();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Doc holder, TagletWriter writer) {
        gw.gosudoc.com.sun.javadoc.SeeTag[] tags = holder.seeTags();
        if (tags.length == 0 && holder instanceof gw.gosudoc.com.sun.javadoc.MethodDoc ) {
            DocFinder.Output inheritedDoc =
                DocFinder.search(writer.configuration(), new DocFinder.Input((gw.gosudoc.com.sun.javadoc.MethodDoc) holder, this));
            if (inheritedDoc.holder != null) {
                tags = inheritedDoc.holder.seeTags();
            }
        }
        return writer.seeTagOutput(holder, tags);
    }
}
