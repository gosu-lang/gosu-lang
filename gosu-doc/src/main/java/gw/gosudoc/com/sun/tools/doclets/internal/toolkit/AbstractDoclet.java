/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit;

import javax.tools.StandardLocation;



import gw.gosudoc.com.sun.javadoc.LanguageVersion;
import gw.gosudoc.com.sun.tools.doclets.formats.html.HtmlDoclet;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders.AbstractBuilder;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.builders.BuilderFactory;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.*;

/**
 * An abstract implementation of a Doclet.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 */
@Deprecated
public abstract class AbstractDoclet {

    /**
     * The global configuration information for this run.
     */
    public Configuration configuration;
    /*
     *  a handle to our utility methods
     */
    protected Utils utils;

    /**
     * The only doclet that may use this toolkit is {@value}
     */
    private static final String TOOLKIT_DOCLET_NAME =
        HtmlDoclet.class.getName();

    /**
     * Verify that the only doclet that is using this toolkit is
     * {@value #TOOLKIT_DOCLET_NAME}.
     */
    private boolean isValidDoclet() {
        if (!getClass().getName().equals(TOOLKIT_DOCLET_NAME)) {
            configuration.message.error("doclet.Toolkit_Usage_Violation",
                TOOLKIT_DOCLET_NAME);
            return false;
        }
        return true;
    }

    /**
     * The method that starts the execution of the doclet.
     *
     * @param root   the {@link gw.gosudoc.com.sun.javadoc.RootDoc} that points to the source to document.
     * @return true if the doclet executed without error.  False otherwise.
     */
    public boolean startDoclet( gw.gosudoc.com.sun.javadoc.RootDoc root) {
        configuration = configuration();
        configuration.root = root;
        utils = configuration.utils;
        if (!isValidDoclet()) {
            return false;
        }
        try {
            startGeneration(root);
        } catch (Configuration.Fault f) {
            root.printError(f.getMessage());
            return false;
        } catch ( FatalError fe) {
            return false;
        } catch ( DocletAbortException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                if (cause.getLocalizedMessage() != null) {
                    root.printError(cause.getLocalizedMessage());
                } else {
                    root.printError(cause.toString());
                }
            }
            return false;
        } catch (Exception exc) {
            return false;
        }
        return true;
    }

    /**
     * Indicate that this doclet supports the 1.5 language features.
     * @return JAVA_1_5, indicating that the new features are supported.
     */
    public static gw.gosudoc.com.sun.javadoc.LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }


    /**
     * Create the configuration instance and returns it.
     * @return the configuration of the doclet.
     */
    public abstract Configuration configuration();

    /**
     * Start the generation of files. Call generate methods in the individual
     * writers, which will in turn generate the documentation files. Call the
     * TreeWriter generation first to ensure the Class Hierarchy is built
     * first and then can be used in the later generation.
     *
     * @see gw.gosudoc.com.sun.javadoc.RootDoc
     */
    private void startGeneration( gw.gosudoc.com.sun.javadoc.RootDoc root) throws Configuration.Fault, Exception {
        try
        {
            int length = root.classes().length;
            System.out.println(length);
        }
        catch( Throwable t )
        {
            throw new RuntimeException(t);
        }
        if (root.classes().length == 0) {
            configuration.message.
                error("doclet.No_Public_Classes_To_Document");
            return;
        }
        configuration.setOptions();
        configuration.getDocletSpecificMsg().notice("doclet.build_version",
            configuration.getDocletSpecificBuildDate());
        ClassTree classtree = new ClassTree(configuration, configuration.nodeprecated);

        generateClassFiles(root, classtree);
        configuration.utils.copyDocFiles(configuration, StandardLocation.SOURCE_PATH, DocPaths.DOC_FILES);

        PackageListWriter.generate(configuration);
        generatePackageFiles(classtree);

        generateOtherFiles(root, classtree);
        configuration.tagletManager.printReport();
    }

    /**
     * Generate additional documentation that is added to the API documentation.
     *
     * @param root      the RootDoc of source to document.
     * @param classtree the data structure representing the class tree.
     */
    protected void generateOtherFiles( gw.gosudoc.com.sun.javadoc.RootDoc root, ClassTree classtree) throws Exception {
        BuilderFactory builderFactory = configuration.getBuilderFactory();
        AbstractBuilder constantsSummaryBuilder = builderFactory.getConstantsSummaryBuider();
        constantsSummaryBuilder.build();
        AbstractBuilder serializedFormBuilder = builderFactory.getSerializedFormBuilder();
        serializedFormBuilder.build();
    }

    /**
     * Generate the package documentation.
     *
     * @param classtree the data structure representing the class tree.
     */
    protected abstract void generatePackageFiles(ClassTree classtree) throws Exception;

    /**
     * Generate the class documentation.
     *
     * @param classtree the data structure representing the class tree.
     */
    protected abstract void generateClassFiles( gw.gosudoc.com.sun.javadoc.ClassDoc[] arr, ClassTree classtree);

    /**
     * Iterate through all classes and construct documentation for them.
     *
     * @param root      the RootDoc of source to document.
     * @param classtree the data structure representing the class tree.
     */
    protected void generateClassFiles( gw.gosudoc.com.sun.javadoc.RootDoc root, ClassTree classtree) {
        generateClassFiles(classtree);
        gw.gosudoc.com.sun.javadoc.PackageDoc[] packages = root.specifiedPackages();
        for ( gw.gosudoc.com.sun.javadoc.PackageDoc pkg : packages) {
            generateClassFiles(pkg.allClasses(), classtree);
        }
    }

    /**
     * Generate the class files for single classes specified on the command line.
     *
     * @param classtree the data structure representing the class tree.
     */
    private void generateClassFiles(ClassTree classtree) {
        String[] packageNames = configuration.classDocCatalog.packageNames();
        for (String packageName : packageNames) {
            generateClassFiles(configuration.classDocCatalog.allClasses(
                    packageName), classtree);
        }
    }
}
