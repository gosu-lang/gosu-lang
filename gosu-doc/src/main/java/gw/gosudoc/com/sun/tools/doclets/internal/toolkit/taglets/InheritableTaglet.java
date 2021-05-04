/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFinder;

/**
 * A taglet should implement this interface if it supports the inheritDoc
 * tag or is automatically inherited if it is missing.
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
public interface InheritableTaglet extends Taglet {

    /**
     * Given an {@link DocFinder.Output}
     * object, set its values with the appropriate information to inherit
     * documentation.
     *
     * @param input  the input for documentation search.
     * @param output the output for documentation search.
     */
    void inherit(DocFinder.Input input, DocFinder.Output output);
}
