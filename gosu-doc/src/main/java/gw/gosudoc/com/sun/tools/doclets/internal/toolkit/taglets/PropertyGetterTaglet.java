/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.taglets;

/**
 * A taglet that adds the initial line of documentation to the JavaFX
 * property getters.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Deprecated
public class PropertyGetterTaglet extends BasePropertyTaglet {

    /**
     * Construct a new PropertyGetterTaglet.
     */
    public PropertyGetterTaglet () {
        name = "propertyGetter";
    }

    @Override
    String getText(TagletWriter tagletWriter) {
        return tagletWriter.configuration().getText("doclet.PropertyGetter");
    }
}
