/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.*;
import java.util.*;




import gw.gosudoc.com.sun.javadoc.Doc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlConstants;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlStyle;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlTag;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.HtmlTree;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPath;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPaths;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;

/**
 * Generate the file with list of all the classes in this run. This page will be
 * used in the left-hand bottom frame, when "All Classes" link is clicked in
 * the left-hand top frame. The name of the generated file is
 * "allclasses-frame.html".
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Atul M Dambalkar
 * @author Doug Kramer
 * @author Bhavesh Patel (Modified)
 */
@Deprecated
public class AllClassesFrameWriter extends HtmlDocletWriter {

    /**
     * Index of all the classes.
     */
    protected IndexBuilder indexbuilder;

    /**
     * BR tag to be used within a document tree.
     */
    final HtmlTree BR = new HtmlTree( HtmlTag.BR);

    /**
     * Construct AllClassesFrameWriter object. Also initializes the indexbuilder
     * variable in this class.
     * @param configuration  The current configuration
     * @param filename       Path to the file which is getting generated.
     * @param indexbuilder   Unicode based Index from {@link IndexBuilder}
     * @throws IOException
     * @throws DocletAbortException
     */
    public AllClassesFrameWriter( ConfigurationImpl configuration,
                                  DocPath filename, IndexBuilder indexbuilder)
                              throws IOException {
        super(configuration, filename);
        this.indexbuilder = indexbuilder;
    }

    /**
     * Create AllClassesFrameWriter object. Then use it to generate the
     * "allclasses-frame.html" file. Generate the file in the current or the
     * destination directory.
     *
     * @param indexbuilder IndexBuilder object for all classes index.
     * @throws DocletAbortException
     */
    public static void generate(ConfigurationImpl configuration,
                                IndexBuilder indexbuilder) {
        AllClassesFrameWriter allclassgen;
        DocPath filename = DocPaths.ALLCLASSES_FRAME;
        try {
            allclassgen = new AllClassesFrameWriter(configuration,
                                                    filename, indexbuilder);
            allclassgen.buildAllClassesFile(true);
            allclassgen.close();
            filename = DocPaths.ALLCLASSES_NOFRAME;
            allclassgen = new AllClassesFrameWriter(configuration,
                                                    filename, indexbuilder);
            allclassgen.buildAllClassesFile(false);
            allclassgen.close();
        } catch (IOException exc) {
            configuration.standardmessage.
                     error("doclet.exception_encountered",
                           exc.toString(), filename);
            throw new DocletAbortException(exc);
        }
    }

    /**
     * Print all the classes in the file.
     * @param wantFrames True if we want frames.
     */
    protected void buildAllClassesFile(boolean wantFrames) throws IOException {
        String label = configuration.getText("doclet.All_Classes");
        Content body = getBody(false, getWindowTitle(label));
        Content heading = HtmlTree.HEADING( HtmlConstants.TITLE_HEADING,
                HtmlStyle.bar, allclassesLabel);
        body.addContent(heading);
        Content ul = new HtmlTree(HtmlTag.UL);
        // Generate the class links and add it to the tdFont tree.
        addAllClasses(ul, wantFrames);
        HtmlTree htmlTree = (configuration.allowTag(HtmlTag.MAIN))
                ? HtmlTree.MAIN(HtmlStyle.indexContainer, ul)
                : HtmlTree.DIV(HtmlStyle.indexContainer, ul);
        body.addContent(htmlTree);
        printHtmlDocument(null, false, body);
    }

    /**
     * Use the sorted index of all the classes and add all the classes to the
     * content list.
     *
     * @param content HtmlTree content to which all classes information will be added
     * @param wantFrames True if we want frames.
     */
    protected void addAllClasses(Content content, boolean wantFrames) {
        for (int i = 0; i < indexbuilder.elements().length; i++) {
            Character unicode = (Character)((indexbuilder.elements())[i]);
            addContents(indexbuilder.getMemberList(unicode), wantFrames, content);
        }
    }

    /**
     * Given a list of classes, generate links for each class or interface.
     * If the class kind is interface, print it in the italics font. Also all
     * links should target the right-hand frame. If clicked on any class name
     * in this page, appropriate class page should get opened in the right-hand
     * frame.
     *
     * @param classlist Sorted list of classes.
     * @param wantFrames True if we want frames.
     * @param content HtmlTree content to which the links will be added
     */
    protected void addContents( List<gw.gosudoc.com.sun.javadoc.Doc> classlist, boolean wantFrames,
                                Content content) {
        for ( Doc doc : classlist) {
            gw.gosudoc.com.sun.javadoc.ClassDoc cd = (gw.gosudoc.com.sun.javadoc.ClassDoc) doc;
            if (!utils.isCoreClass(cd)) {
                continue;
            }
            Content label = italicsClassName(cd, false);
            Content linkContent;
            if (wantFrames) {
                linkContent = getLink(new LinkInfoImpl(configuration,
                                                       LinkInfoImpl.Kind.ALL_CLASSES_FRAME, cd).label(label).target("classFrame"));
            } else {
                linkContent = getLink(new LinkInfoImpl(configuration, LinkInfoImpl.Kind.DEFAULT, cd).label(label));
            }
            Content li = HtmlTree.LI(linkContent);
            content.addContent(li);
        }
    }
}
