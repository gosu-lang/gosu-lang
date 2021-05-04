/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.formats.html;

import java.io.IOException;
import java.util.Collection;




import gw.gosudoc.com.sun.javadoc.PackageDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.markup.*;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPath;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocPaths;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;

/**
 * Generate the package index for the left-hand frame in the generated output.
 * A click on the package name in this frame will update the page in the bottom
 * left hand frame with the listing of contents of the clicked package.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Atul M Dambalkar
 */
@Deprecated
public class PackageIndexFrameWriter extends AbstractPackageIndexWriter {

    /**
     * Construct the PackageIndexFrameWriter object.
     *
     * @param filename Name of the package index file to be generated.
     */
    public PackageIndexFrameWriter(ConfigurationImpl configuration,
                                   DocPath filename) throws IOException {
        super(configuration, filename);
    }

    /**
     * Generate the package index file named "overview-frame.html".
     * @throws DocletAbortException
     */
    public static void generate(ConfigurationImpl configuration) {
        PackageIndexFrameWriter packgen;
        DocPath filename = DocPaths.OVERVIEW_FRAME;
        try {
            packgen = new PackageIndexFrameWriter(configuration, filename);
            packgen.buildPackageIndexFile("doclet.Window_Overview", false);
            packgen.close();
        } catch (IOException exc) {
            configuration.standardmessage.error(
                        "doclet.exception_encountered",
                        exc.toString(), filename);
            throw new DocletAbortException(exc);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void addPackagesList( Collection<gw.gosudoc.com.sun.javadoc.PackageDoc> packages, String text,
                                    String tableSummary, Content body) {
        Content heading = HtmlTree.HEADING( HtmlConstants.PACKAGE_HEADING, true,
                packagesLabel);
        HtmlTree htmlTree = (configuration.allowTag( HtmlTag.MAIN))
                ? HtmlTree.MAIN( HtmlStyle.indexContainer, heading)
                : HtmlTree.DIV(HtmlStyle.indexContainer, heading);
        HtmlTree ul = new HtmlTree(HtmlTag.UL);
        ul.setTitle(packagesLabel);
        for ( gw.gosudoc.com.sun.javadoc.PackageDoc aPackage : packages) {
            // Do not list the package if -nodeprecated option is set and the
            // package is marked as deprecated.
            if (aPackage != null &&
                (!(configuration.nodeprecated && utils.isDeprecated(aPackage)))) {
                ul.addContent(getPackage(aPackage));
            }
        }
        htmlTree.addContent(ul);
        body.addContent(htmlTree);
    }

    /**
     * Returns each package name as a separate link.
     *
     * @param pd PackageDoc
     * @return content for the package link
     */
    protected Content getPackage( PackageDoc pd) {
        Content packageLinkContent;
        Content packageLabel;
        if (!pd.name().isEmpty()) {
            packageLabel = getPackageLabel(pd.name());
            packageLinkContent = getHyperLink(pathString(pd,
                     DocPaths.PACKAGE_FRAME), packageLabel, "",
                    "packageFrame");
        } else {
            packageLabel = new StringContent("<unnamed package>");
            packageLinkContent = getHyperLink(DocPaths.PACKAGE_FRAME,
                    packageLabel, "", "packageFrame");
        }
        Content li = HtmlTree.LI(packageLinkContent);
        return li;
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavigationBarHeader(Content body) {
        Content headerContent;
        if (configuration.packagesheader.length() > 0) {
            headerContent = new RawHtml(replaceDocRootDir(configuration.packagesheader));
        } else {
            headerContent = new RawHtml(replaceDocRootDir(configuration.header));
        }
        Content heading = HtmlTree.HEADING(HtmlConstants.TITLE_HEADING, true,
                HtmlStyle.bar, headerContent);
        body.addContent(heading);
    }

    /**
     * Do nothing as there is no overview information in this page.
     */
    protected void addOverviewHeader(Content body) {
    }

    /**
     * Adds "All Classes" link for the top of the left-hand frame page to the
     * documentation tree.
     *
     * @param ul the Content object to which the "All Classes" link should be added
     */
    protected void addAllClassesLink(Content ul) {
        Content linkContent = getHyperLink(DocPaths.ALLCLASSES_FRAME,
                allclassesLabel, "", "packageFrame");
        Content li = HtmlTree.LI(linkContent);
        ul.addContent(li);
    }

    /**
     * {@inheritDoc}
     */
    protected void addNavigationBarFooter(Content body) {
        Content p = HtmlTree.P(getSpace());
        body.addContent(p);
    }
}
