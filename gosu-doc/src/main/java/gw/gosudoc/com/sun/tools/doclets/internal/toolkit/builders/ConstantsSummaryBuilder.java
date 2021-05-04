/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders;

import java.io.*;
import java.util.*;



import gw.gosudoc.com.sun.javadoc.PackageDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Content;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.VisibleMemberMap;

/**
 * Builds the Constants Summary Page.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @author Bhavesh Patel (Modified)
 * @since 1.5
 */
@Deprecated
public class ConstantsSummaryBuilder extends AbstractBuilder {

    /**
     * The root element of the constant summary XML is {@value}.
     */
    public static final String ROOT = "ConstantSummary";

    /**
     * The maximum number of package directories shown in the constant
     * value index.
     */
    public static final int MAX_CONSTANT_VALUE_INDEX_LENGTH = 2;

    /**
     * The writer used to write the results.
     */
    protected final ConstantsSummaryWriter writer;

    /**
     * The set of ClassDocs that have constant fields.
     */
    protected final Set<gw.gosudoc.com.sun.javadoc.ClassDoc> classDocsWithConstFields;

    /**
     * The set of printed package headers.
     */
    protected Set<String> printedPackageHeaders;

    /**
     * The current package being documented.
     */
    private gw.gosudoc.com.sun.javadoc.PackageDoc currentPackage;

    /**
     * The current class being documented.
     */
    private gw.gosudoc.com.sun.javadoc.ClassDoc currentClass;

    /**
     * The content tree for the constant summary documentation.
     */
    private Content contentTree;

    /**
     * True if first package is listed.
     */
    private boolean first = true;

    /**
     * Construct a new ConstantsSummaryBuilder.
     *
     * @param context       the build context.
     * @param writer        the writer for the summary.
     */
    private ConstantsSummaryBuilder(Context context,
            ConstantsSummaryWriter writer) {
        super(context);
        this.writer = writer;
        this.classDocsWithConstFields = new HashSet<>();
    }

    /**
     * Construct a ConstantsSummaryBuilder.
     *
     * @param context       the build context.
     * @param writer        the writer for the summary.
     */
    public static ConstantsSummaryBuilder getInstance(Context context,
            ConstantsSummaryWriter writer) {
        return new ConstantsSummaryBuilder(context, writer);
    }

    /**
     * {@inheritDoc}
     */
    public void build() throws IOException {
        if (writer == null) {
            //Doclet does not support this output.
            return;
        }
        build(layoutParser.parseXML(ROOT), contentTree);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return ROOT;
    }

    /**
     * Build the constant summary.
     *
     * @param node the XML element that specifies which components to document
     * @param contentTree the content tree to which the documentation will be added
     */
    public void buildConstantSummary(XMLNode node, Content contentTree) throws Exception {
        contentTree = writer.getHeader();
        buildChildren(node, contentTree);
        writer.addFooter(contentTree);
        writer.printDocument(contentTree);
        writer.close();
    }

    /**
     * Build the list of packages.
     *
     * @param node the XML element that specifies which components to document
     * @param contentTree the content tree to which the content list will be added
     */
    public void buildContents(XMLNode node, Content contentTree) {
        Content contentListTree = writer.getContentsHeader();
        printedPackageHeaders = new HashSet<>();
        for ( gw.gosudoc.com.sun.javadoc.PackageDoc pkg : configuration.packages) {
            if (hasConstantField(pkg) && !hasPrintedPackageIndex(pkg.name())) {
                writer.addLinkToPackageContent(pkg,
                                               parsePackageName(pkg.name()),
                                               printedPackageHeaders, contentListTree);
            }
        }
        writer.addContentsList(contentTree, contentListTree);
    }

    /**
     * Build the summary for each documented package.
     *
     * @param node the XML element that specifies which components to document
     * @param contentTree the tree to which the summaries will be added
     */
    public void buildConstantSummaries(XMLNode node, Content contentTree) {
        printedPackageHeaders = new HashSet<>();
        Content summariesTree = writer.getConstantSummaries();
        for ( gw.gosudoc.com.sun.javadoc.PackageDoc aPackage : configuration.packages) {
            if (hasConstantField(aPackage)) {
                currentPackage = aPackage;
                //Build the documentation for the current package.
                buildChildren(node, summariesTree);
                first = false;
            }
        }
        writer.addConstantSummaries(contentTree, summariesTree);
    }

    /**
     * Build the header for the given package.
     *
     * @param node the XML element that specifies which components to document
     * @param summariesTree the tree to which the package header will be added
     */
    public void buildPackageHeader(XMLNode node, Content summariesTree) {
        String parsedPackageName = parsePackageName(currentPackage.name());
        if (! printedPackageHeaders.contains(parsedPackageName)) {
            writer.addPackageName(parsePackageName(currentPackage.name()), summariesTree, first);
            printedPackageHeaders.add(parsedPackageName);
        }
    }

    /**
     * Build the summary for the current class.
     *
     * @param node the XML element that specifies which components to document
     * @param summariesTree the tree to which the class constant summary will be added
     */
    public void buildClassConstantSummary(XMLNode node, Content summariesTree) {
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes = currentPackage.name().length() > 0 ?
            currentPackage.allClasses() :
            configuration.classDocCatalog.allClasses(
                DocletConstants.DEFAULT_PACKAGE_NAME);
        Arrays.sort(classes);
        Content classConstantTree = writer.getClassConstantHeader();
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc doc : classes) {
            if (!classDocsWithConstFields.contains(doc) ||
                !doc.isIncluded()) {
                continue;
            }
            currentClass = doc;
            //Build the documentation for the current class.
            buildChildren(node, classConstantTree);
        }
        writer.addClassConstant(summariesTree, classConstantTree);
    }

    /**
     * Build the summary of constant members in the class.
     *
     * @param node the XML element that specifies which components to document
     * @param classConstantTree the tree to which the constant members table
     *                          will be added
     */
    public void buildConstantMembers(XMLNode node, Content classConstantTree) {
        new ConstantFieldBuilder(currentClass).buildMembersSummary(node, classConstantTree);
    }

    /**
     * Return true if the given package has constant fields to document.
     *
     * @param pkg   the package being checked.
     * @return true if the given package has constant fields to document.
     */
    private boolean hasConstantField( PackageDoc pkg) {
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes
                = (pkg.name().length() > 0)
                  ? pkg.allClasses()
                  : configuration.classDocCatalog.allClasses(DocletConstants.DEFAULT_PACKAGE_NAME);
        boolean found = false;
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc doc : classes) {
            if (doc.isIncluded() && hasConstantField(doc)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Return true if the given class has constant fields to document.
     *
     * @param classDoc the class being checked.
     * @return true if the given package has constant fields to document.
     */
    private boolean hasConstantField ( gw.gosudoc.com.sun.javadoc.ClassDoc classDoc) {
        VisibleMemberMap visibleMemberMapFields = new VisibleMemberMap(classDoc,
            VisibleMemberMap.FIELDS, configuration);
        List<?> fields = visibleMemberMapFields.getLeafClassMembers(configuration);
        for (Object f : fields) {
            gw.gosudoc.com.sun.javadoc.FieldDoc field = (gw.gosudoc.com.sun.javadoc.FieldDoc) f;
            if (field.constantValueExpression() != null) {
                classDocsWithConstFields.add(classDoc);
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the given package name has been printed.  Also
     * return true if the root of this package has been printed.
     *
     * @param pkgname the name of the package to check.
     */
    private boolean hasPrintedPackageIndex(String pkgname) {
        String[] list = printedPackageHeaders.toArray(new String[] {});
        for (String packageHeader : list) {
            if (pkgname.startsWith(packageHeader)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Print the table of constants.
     *
     * @author Jamie Ho
     * @since 1.4
     */
    private class ConstantFieldBuilder {

        /**
         * The map used to get the visible variables.
         */
        protected VisibleMemberMap visibleMemberMapFields = null;

        /**
         * The map used to get the visible variables.
         */
        protected VisibleMemberMap visibleMemberMapEnumConst = null;

        /**
         * The classdoc that we are examining constants for.
         */
        protected gw.gosudoc.com.sun.javadoc.ClassDoc classdoc;

        /**
         * Construct a ConstantFieldSubWriter.
         * @param classdoc the classdoc that we are examining constants for.
         */
        public ConstantFieldBuilder( gw.gosudoc.com.sun.javadoc.ClassDoc classdoc) {
            this.classdoc = classdoc;
            visibleMemberMapFields = new VisibleMemberMap(classdoc,
                VisibleMemberMap.FIELDS, configuration);
            visibleMemberMapEnumConst = new VisibleMemberMap(classdoc,
                VisibleMemberMap.ENUM_CONSTANTS, configuration);
        }

        /**
         * Builds the table of constants for a given class.
         *
         * @param node the XML element that specifies which components to document
         * @param classConstantTree the tree to which the class constants table
         *                          will be added
         */
        protected void buildMembersSummary(XMLNode node, Content classConstantTree) {
            List<gw.gosudoc.com.sun.javadoc.FieldDoc> members = new ArrayList<>(members());
            if (members.size() > 0) {
                Collections.sort(members);
                writer.addConstantMembers(classdoc, members, classConstantTree);
            }
        }

        /**
         * Return the list of visible constant fields for the given classdoc.
         * @return the list of visible constant fields for the given classdoc.
         */
        protected List<gw.gosudoc.com.sun.javadoc.FieldDoc> members() {
            List<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> l = visibleMemberMapFields.getLeafClassMembers(configuration);
            l.addAll(visibleMemberMapEnumConst.getLeafClassMembers(configuration));
            Iterator<gw.gosudoc.com.sun.javadoc.ProgramElementDoc> iter;

            if(l != null){
                iter = l.iterator();
            } else {
                return null;
            }
            List<gw.gosudoc.com.sun.javadoc.FieldDoc> inclList = new LinkedList<>();
            gw.gosudoc.com.sun.javadoc.FieldDoc member;
            while(iter.hasNext()){
                member = (gw.gosudoc.com.sun.javadoc.FieldDoc)iter.next();
                if(member.constantValue() != null){
                    inclList.add(member);
                }
            }
            return inclList;
        }
    }

    /**
     * Parse the package name.  We only want to display package name up to
     * 2 levels.
     */
    private String parsePackageName(String pkgname) {
        int index = -1;
        for (int j = 0; j < MAX_CONSTANT_VALUE_INDEX_LENGTH; j++) {
            index = pkgname.indexOf(".", index + 1);
        }
        if (index != -1) {
            pkgname = pkgname.substring(0, index);
        }
        return pkgname;
    }
}
