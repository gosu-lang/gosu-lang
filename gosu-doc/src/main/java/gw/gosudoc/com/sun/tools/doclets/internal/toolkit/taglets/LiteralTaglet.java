/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import java.util.Map;

import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;


/**
 * An inline Taglet used to denote literal text.
 * The enclosed text is interpreted as not containing HTML markup or
 * nested javadoc tags.
 * For example, the text:
 * <blockquote>  {@code {@literal a<B>c}}  </blockquote>
 * displays as:
 * <blockquote>  {@literal a<B>c}  </blockquote>
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Scott Seligman
 * @since 1.5
 */

@Deprecated
public class LiteralTaglet extends BaseInlineTaglet {

    private static final String NAME = "literal";

    public static void register(Map<String, Taglet> map) {
        map.remove(NAME);
        map.put(NAME, new LiteralTaglet());
    }

    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput(Tag tag, TagletWriter writer) {
        return writer.literalTagOutput(tag);
    }
}
