/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.javadoc.Doc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;

/**
 * A taglet that represents the @deprecated tag.
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
public class DeprecatedTaglet extends BaseTaglet{

    public DeprecatedTaglet() {
        name = "deprecated";
    }

    /**
     * {@inheritDoc}
     */
    public Content getTagletOutput( Doc holder, TagletWriter writer) {
        return writer.deprecatedTagOutput(holder);
    }
}
