/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import java.util.Map;
import gw.gosudoc.com.sun.javadoc.Tag;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

/**
 * An inline Taglet used to denote literal code fragments.
 * The enclosed text is interpreted as not containing HTML markup or
 * nested javadoc tags, and is rendered in a font suitable for code.
 *
 * <p> The tag {@code {@code ...}} is equivalent to
 * {@code <code>{@literal ...}</code>}.
 * For example, the text:
 * <blockquote>  The type {@code {@code List<P>}}  </blockquote>
 * displays as:
 * <blockquote>  The type {@code List<P>}  </blockquote>
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
public class CodeTaglet extends BaseInlineTaglet {

    private static final String NAME = "code";

    public static void register(Map<String, Taglet> map) {
        map.remove(NAME);
        map.put(NAME, new CodeTaglet());
    }

    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput(Tag tag, TagletWriter writer) {
        return writer.codeTagOutput(tag);
    }
}
