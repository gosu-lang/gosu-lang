/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html.markup;

import java.io.*;
import java.util.*;

import gw.gosudoc.com.sun.javadoc.ClassDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.ConfigurationImpl;
import gw.gosudoc.com.sun.tools.doclets.formats.html.SectionName;

import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocFile;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocLink;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPath;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPaths;


/**
 * Class for the Html Format Code Generation specific to JavaDoc.
 * This Class contains methods related to the Html Code Generation which
 * are used by the Sub-Classes in the package com.sun.tools.doclets.standard
 * and com.sun.tools.doclets.oneone.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.2
 * @author Atul M Dambalkar
 * @author Robert Field
 */
@Deprecated
public abstract class HtmlDocWriter extends HtmlWriter {

    public static final String CONTENT_TYPE = "text/html";

    DocPath pathToRoot;

    /**
     * Constructor. Initializes the destination file name through the super
     * class HtmlWriter.
     *
     * @param filename String file name.
     */
    public HtmlDocWriter( Configuration configuration, DocPath filename)
            throws IOException {
        super(configuration, filename);
        this.pathToRoot = filename.parent().invert();
        configuration.message.notice("doclet.Generating_0",
            DocFile.createFileForOutput(configuration, filename).getPath());
    }

    /**
     * Accessor for configuration.
     */
    public abstract Configuration configuration();

    public Content getHyperLink( DocPath link, String label) {
        return getHyperLink(link, new StringContent(label), false, "", "", "");
    }

    /**
     * Get Html Hyper Link Content.
     *
     * @param where      Position of the link in the file. Character '#' is not
     *                   needed.
     * @param label      Tag for the link.
     * @return a content tree for the hyper link
     */
    public Content getHyperLink(String where,
                               Content label) {
        return getHyperLink(getDocLink(where), label, "", "");
    }

    /**
     * Get Html Hyper Link Content.
     *
     * @param sectionName      The section name to which the link will be created.
     * @param label            Tag for the link.
     * @return a content tree for the hyper link
     */
    public Content getHyperLink(SectionName sectionName,
                               Content label) {
        return getHyperLink(getDocLink(sectionName), label, "", "");
    }

    /**
     * Get Html Hyper Link Content.
     *
     * @param sectionName      The section name combined with where to which the link
     *                         will be created.
     * @param where            The fragment combined with sectionName to which the link
     *                         will be created.
     * @param label            Tag for the link.
     * @return a content tree for the hyper link
     */
    public Content getHyperLink(SectionName sectionName, String where,
                               Content label) {
        return getHyperLink(getDocLink(sectionName, where), label, "", "");
    }

    /**
     * Get the link.
     *
     * @param where      Position of the link in the file.
     * @return a DocLink object for the hyper link
     */
    public DocLink getDocLink(String where) {
        return DocLink.fragment(getName(where));
    }

    /**
     * Get the link.
     *
     * @param sectionName      The section name to which the link will be created.
     * @return a DocLink object for the hyper link
     */
    public DocLink getDocLink(SectionName sectionName) {
        return DocLink.fragment(sectionName.getName());
    }

    /**
     * Get the link.
     *
     * @param sectionName      The section name combined with where to which the link
     *                         will be created.
     * @param where            The fragment combined with sectionName to which the link
     *                         will be created.
     * @return a DocLink object for the hyper link
     */
    public DocLink getDocLink(SectionName sectionName, String where) {
        return DocLink.fragment(sectionName.getName() + getName(where));
    }

    /**
     * Convert the name to a valid HTML name.
     *
     * @param name the name that needs to be converted to valid HTML name.
     * @return a valid HTML name string.
     */
    public String getName(String name) {
        StringBuilder sb = new StringBuilder();
        char ch;
        /* The HTML 4 spec at http://www.w3.org/TR/html4/types.html#h-6.2 mentions
         * that the name/id should begin with a letter followed by other valid characters.
         * The HTML 5 spec (draft) is more permissive on names/ids where the only restriction
         * is that it should be at least one character long and should not contain spaces.
         * The spec draft is @ http://www.w3.org/html/wg/drafts/html/master/dom.html#the-id-attribute.
         *
         * For HTML 4, we need to check for non-characters at the beginning of the name and
         * substitute it accordingly, "_" and "$" can appear at the beginning of a member name.
         * The method substitutes "$" with "Z:Z:D" and will prefix "_" with "Z:Z".
         */
        for (int i = 0; i < name.length(); i++) {
            ch = name.charAt(i);
            switch (ch) {
                case '(':
                case ')':
                case '<':
                case '>':
                case ',':
                    sb.append('-');
                    break;
                case ' ':
                case '[':
                    break;
                case ']':
                    sb.append(":A");
                    break;
                // Any appearance of $ needs to be substituted with ":D" and not with hyphen
                // since a field name "P$$ and a method P(), both valid member names, can end
                // up as "P--". A member name beginning with $ needs to be substituted with
                // "Z:Z:D".
                case '$':
                    if (i == 0)
                        sb.append("Z:Z");
                    sb.append(":D");
                    break;
                // A member name beginning with _ needs to be prefixed with "Z:Z" since valid anchor
                // names can only begin with a letter.
                case '_':
                    if (i == 0)
                        sb.append("Z:Z");
                    sb.append(ch);
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Get Html hyperlink.
     *
     * @param link       path of the file.
     * @param label      Tag for the link.
     * @return a content tree for the hyper link
     */
    public Content getHyperLink(DocPath link, Content label) {
        return getHyperLink(link, label, "", "");
    }

    public Content getHyperLink(DocLink link, Content label) {
        return getHyperLink(link, label, "", "");
    }

    public Content getHyperLink(DocPath link,
                               Content label, boolean strong,
                               String stylename, String title, String target) {
        return getHyperLink(new DocLink(link), label, strong,
                stylename, title, target);
    }

    public Content getHyperLink(DocLink link,
                               Content label, boolean strong,
                               String stylename, String title, String target) {
        Content body = label;
        if (strong) {
            body = HtmlTree.SPAN(HtmlStyle.typeNameLink, body);
        }
        if (stylename != null && stylename.length() != 0) {
            HtmlTree t = new HtmlTree(HtmlTag.FONT, body);
            t.addAttr(HtmlAttr.CLASS, stylename);
            body = t;
        }
        HtmlTree l = HtmlTree.A(link.toString(), body);
        if (title != null && title.length() != 0) {
            l.addAttr(HtmlAttr.TITLE, title);
        }
        if (target != null && target.length() != 0) {
            l.addAttr(HtmlAttr.TARGET, target);
        }
        return l;
    }

    /**
     * Get Html Hyper Link.
     *
     * @param link       String name of the file.
     * @param label      Tag for the link.
     * @param title      String that describes the link's content for accessibility.
     * @param target     Target frame.
     * @return a content tree for the hyper link.
     */
    public Content getHyperLink(DocPath link,
            Content label, String title, String target) {
        return getHyperLink(new DocLink(link), label, title, target);
    }

    public Content getHyperLink(DocLink link,
            Content label, String title, String target) {
        HtmlTree anchor = HtmlTree.A(link.toString(), label);
        if (title != null && title.length() != 0) {
            anchor.addAttr(HtmlAttr.TITLE, title);
        }
        if (target != null && target.length() != 0) {
            anchor.addAttr(HtmlAttr.TARGET, target);
        }
        return anchor;
    }

    /**
     * Get the name of the package, this class is in.
     *
     * @param cd    ClassDoc.
     */
    public String getPkgName( ClassDoc cd) {
        String pkgName = cd.containingPackage().name();
        if (pkgName.length() > 0) {
            pkgName += ".";
            return pkgName;
        }
        return "";
    }

    public boolean getMemberDetailsListPrinted() {
        return memberDetailsListPrinted;
    }

    /**
     * Print the frames version of the Html file header.
     * Called only when generating an HTML frames file.
     *
     * @param title Title of this HTML document
     * @param configuration the configuration object
     * @param body the body content tree to be added to the HTML document
     */
    public void printFramesDocument(String title, ConfigurationImpl configuration,
            HtmlTree body) throws IOException {
        Content htmlDocType = configuration.isOutputHtml5()
                ? DocType.HTML5
                : DocType.TRANSITIONAL;
        Content htmlComment = new Comment(configuration.getText("doclet.New_Page"));
        Content head = new HtmlTree(HtmlTag.HEAD);
        head.addContent(getGeneratedBy(!configuration.notimestamp));
        Content windowTitle = HtmlTree.TITLE(new StringContent(title));
        head.addContent(windowTitle);
        Content meta = HtmlTree.META("Content-Type", CONTENT_TYPE,
                (configuration.charset.length() > 0) ?
                        configuration.charset : HtmlConstants.HTML_DEFAULT_CHARSET);
        head.addContent(meta);
        head.addContent(getStyleSheetProperties(configuration));
        head.addContent(getFramesJavaScript());
        Content htmlTree = HtmlTree.HTML(configuration.getLocale().getLanguage(),
                head, body);
        Content htmlDocument = new HtmlDocument(htmlDocType,
                htmlComment, htmlTree);
        write(htmlDocument);
    }

    /**
     * Returns a link to the stylesheet file.
     *
     * @return an HtmlTree for the lINK tag which provides the stylesheet location
     */
    public HtmlTree getStyleSheetProperties(ConfigurationImpl configuration) {
        String stylesheetfile = configuration.stylesheetfile;
        DocPath stylesheet;
        if (stylesheetfile.isEmpty()) {
            stylesheet = DocPaths.STYLESHEET;
        } else {
            DocFile file = DocFile.createFileForInput(configuration, stylesheetfile);
            stylesheet = DocPath.create(file.getName());
        }
        HtmlTree link = HtmlTree.LINK("stylesheet", "text/css",
                pathToRoot.resolve(stylesheet).getPath(),
                "Style");
        return link;
    }

    protected Comment getGeneratedBy(boolean timestamp) {
        String text = "Generated by javadoc"; // marker string, deliberately not localized
        if (timestamp) {
            Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
            Date today = calendar.getTime();
            text += " ("+ configuration.getDocletSpecificBuildDate() + ") on " + today;
        }
        return new Comment(text);
    }
}
