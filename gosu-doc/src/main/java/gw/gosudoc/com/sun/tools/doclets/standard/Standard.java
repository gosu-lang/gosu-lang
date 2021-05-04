/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.standard;


import gw.gosudoc.com.sun.javadoc.LanguageVersion;
import gw.gosudoc.com.sun.tools.doclets.formats.html.HtmlDoclet;

/**
 * This doclet generates HTML-formatted documentation for the specified packages and types.
 * @deprecated The doclet has been superseded by its replacement,
 * {@link jdk.javadoc.doclet.StandardDoclet}.
 */
@Deprecated(forRemoval=true, since="9")
public class Standard {

    public static int optionLength(String option) {
        return HtmlDoclet.optionLength(option);
    }

    public static boolean start( gw.gosudoc.com.sun.javadoc.RootDoc root) {
        return HtmlDoclet.start(root);
    }

    public static boolean validOptions(String[][] options,
                                   gw.gosudoc.com.sun.javadoc.DocErrorReporter reporter) {
        return HtmlDoclet.validOptions(options, reporter);
    }

    public static LanguageVersion languageVersion() {
        return HtmlDoclet.languageVersion();
    }

}
