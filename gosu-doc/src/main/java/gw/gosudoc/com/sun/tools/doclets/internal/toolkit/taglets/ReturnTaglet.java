/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * A taglet that represents the @return tag.
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
public class ReturnTaglet extends BaseExecutableMemberTaglet
        implements InheritableTaglet {

    public ReturnTaglet() {
        name = "return";
    }

    /**
     * {@inheritDoc}
     */
    public void inherit( DocFinder.Input input, DocFinder.Output output) {
       gw.gosudoc.com.sun.javadoc.Tag[] tags = input.element.tags("return");
        if (tags.length > 0) {
            output.holder = input.element;
            output.holderTag = tags[0];
            output.inlineTags = input.isFirstSentence ?
                tags[0].firstSentenceTags() : tags[0].inlineTags();
        }
    }

    /**
     * Return true if this <code>Taglet</code>
     * is used in constructor documentation.
     * @return true if this <code>Taglet</code>
     * is used in constructor documentation and false
     * otherwise.
     */
    public boolean inConstructor() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( gw.gosudoc.com.sun.javadoc.Doc holder, TagletWriter writer) {
        gw.gosudoc.com.sun.javadoc.Type returnType = ((gw.gosudoc.com.sun.javadoc.MethodDoc) holder).returnType();
        gw.gosudoc.com.sun.javadoc.Tag[] tags = holder.tags(name);

        //Make sure we are not using @return tag on method with void return type.
        if (returnType.isPrimitive() && returnType.typeName().equals("void")) {
            if (tags.length > 0) {
                writer.getMsgRetriever().warning(holder.position(),
                    "doclet.Return_tag_on_void_method");
            }
            return null;
        }
        //Inherit @return tag if necessary.
        if (tags.length == 0) {
            DocFinder.Output inheritedDoc =
                DocFinder.search(writer.configuration(), new DocFinder.Input((gw.gosudoc.com.sun.javadoc.MethodDoc) holder, this));
            tags = inheritedDoc.holderTag == null ? tags : new Tag[] {inheritedDoc.holderTag};
        }
        return tags.length > 0 ? writer.returnTagOutput(tags[0]) : null;
    }
}
