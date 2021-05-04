/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

/**
    Contains the base classes that make up a doclet.  Doclets that reuse
    the functionality provided by the toolkit should have the following
    characteristics:
    <ul>
        <li>
            The main driver class should extend
            {@link gw.gosudoc.com.sun.tools.doclets.internal.toolkit.AbstractDoclet}.
        </li>
        <li>
            The doclet configuration class should extend
            {@link gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration}.
        </li>
        <li>
            The doclet should have a writer factory that implements
            {@link gw.gosudoc.com.sun.tools.doclets.internal.toolkit.WriterFactory}.
            This class constructs writers that write doclet specific output.
        </li>
        <li>
            The doclet should have a taglet writer that extends
            {@link gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter}.
             This writer determines how to output each given tag.
        </li>
    </ul>

    <p><b>This is NOT part of any supported API.
    If you write code that depends on this, you do so at your own risk.
    This code and its internal interfaces are subject to change or
    deletion without notice.</b>
*/

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit;
