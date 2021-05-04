/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Position;
import gw.gosudoc.com.sun.javadoc.PackageDoc;

/**
 * This class holds the information from one run of javadoc.
 * Particularly the packages, classes and options specified
 * by the user.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.2
 * @author Robert Field
 * @author Atul M Dambalkar
 * @author Neal Gafter (rewrite)
 */
@Deprecated
public class RootDocImpl extends DocImpl implements gw.gosudoc.com.sun.javadoc.RootDoc
{

    /**
     * list of classes specified on the command line.
     */
    private List<gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl> cmdLineClasses;

    /**
     * list of packages specified on the command line.
     */
    private List<gw.gosudoc.com.sun.tools.javadoc.main.PackageDocImpl> cmdLinePackages;

    /**
     * a collection of all options.
     */
    private List<String[]> options;

    /**
     * Constructor used when reading source files.
     *
     * @param env the documentation environment, state for this javadoc run
     * @param classes list of classes specified on the commandline
     * @param packages list of package names specified on the commandline
     * @param options list of options
     */
    public RootDocImpl( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, List<JCClassDecl> classes, List<String> packages, List<String[]> options) {
        super(env, null);
        this.options = options;
        setPackages(env, packages);
        setClasses(env, classes);
    }

    /**
     * Constructor used when reading class files.
     *
     * @param env the documentation environment, state for this javadoc run
     * @param classes list of class names specified on the commandline
     * @param options list of options
     */
    public RootDocImpl( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, List<String> classes, List<String[]> options) {
        super(env, null);
        this.options = options;
        cmdLinePackages = List.nil();
        ListBuffer<gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl> classList = new ListBuffer<>();
        for (String className : classes) {
            gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl c = env.loadClass(className);
            if (c == null)
                env.error(null, "javadoc.class_not_found", className);
            else
                classList = classList.append(c);
        }
        cmdLineClasses = classList.toList();
    }

    /**
     * Initialize classes information. Those classes are input from
     * command line.
     *
     * @param env the compilation environment
     * @param classes a list of ClassDeclaration
     */
    private void setClasses( DocEnv env, List<JCClassDecl> classes) {
        ListBuffer<gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl> result = new ListBuffer<>();
        for (JCClassDecl def : classes) {
            //### Do we want modifier check here?
            if (env.shouldDocument(def.sym)) {
                gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl cd = env.getClassDoc(def.sym);
                if (cd != null) {
                    cd.isIncluded = true;
                    result.append(cd);
                } //else System.out.println(" (classdoc is null)");//DEBUG
            } //else System.out.println(" (env.shouldDocument() returned false)");//DEBUG
        }
        cmdLineClasses = result.toList();
    }

    /**
     * Initialize packages information.
     *
     * @param env the compilation environment
     * @param packages a list of package names (String)
     */
    private void setPackages( DocEnv env, List<String> packages) {
        ListBuffer<gw.gosudoc.com.sun.tools.javadoc.main.PackageDocImpl> packlist = new ListBuffer<>();
        for (String name : packages) {
            gw.gosudoc.com.sun.tools.javadoc.main.PackageDocImpl pkg = env.lookupPackage(name);
            if (pkg != null) {
                pkg.isIncluded = true;
                packlist.append(pkg);
            } else {
                env.warning(null, "main.no_source_files_for_package", name);
            }
        }
        cmdLinePackages = packlist.toList();
    }

    /**
     * Command line options.
     *
     * <pre>
     * For example, given:
     *     javadoc -foo this that -bar other ...
     *
     * This method will return:
     *      options()[0][0] = "-foo"
     *      options()[0][1] = "this"
     *      options()[0][2] = "that"
     *      options()[1][0] = "-bar"
     *      options()[1][1] = "other"
     * </pre>
     *
     * @return an array of arrays of String.
     */
    public String[][] options() {
        return options.toArray(new String[options.length()][]);
    }

    /**
     * Packages specified on the command line.
     */
    public gw.gosudoc.com.sun.javadoc.PackageDoc[] specifiedPackages() {
        return (gw.gosudoc.com.sun.javadoc.PackageDoc[])cmdLinePackages
            .toArray(new PackageDocImpl[cmdLinePackages.length()]);
    }

    /**
     * Classes and interfaces specified on the command line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] specifiedClasses() {
        ListBuffer<gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl> classesToDocument = new ListBuffer<>();
        for ( gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl cd : cmdLineClasses) {
            cd.addAllClasses(classesToDocument, true);
        }
        return (gw.gosudoc.com.sun.javadoc.ClassDoc[])classesToDocument.toArray(new gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl[classesToDocument.length()]);
    }

    /**
     * Return all classes and interfaces (including those inside
     * packages) to be documented.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] classes() {
        ListBuffer<gw.gosudoc.com.sun.tools.javadoc.main.ClassDocImpl> classesToDocument = new ListBuffer<>();
        for ( ClassDocImpl cd : cmdLineClasses) {
            cd.addAllClasses(classesToDocument, true);
        }
        for ( PackageDocImpl pd : cmdLinePackages) {
            pd.addAllClassesTo(classesToDocument);
        }
        return classesToDocument.toArray(new ClassDocImpl[classesToDocument.length()]);
    }

    /**
     * Return a ClassDoc for the specified class/interface name
     *
     * @param qualifiedName qualified class name
     *                        (i.e. includes package name).
     *
     * @return a ClassDocImpl holding the specified class, null if
     * this class is not referenced.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc classNamed( String qualifiedName) {
        return env.lookupClass(qualifiedName);
    }

    /**
     * Return a PackageDoc for the specified package name
     *
     * @param name package name
     *
     * @return a PackageDoc holding the specified package, null if
     * this package is not referenced.
     */
    public PackageDoc packageNamed( String name) {
        return env.lookupPackage(name);
    }

    /**
     * Return the name of this Doc item.
     *
     * @return the string <code>"*RootDocImpl*"</code>.
     */
    public String name() {
        return "*RootDocImpl*";
    }

    /**
     * Return the name of this Doc item.
     *
     * @return the string <code>"*RootDocImpl*"</code>.
     */
    public String qualifiedName() {
        return "*RootDocImpl*";
    }

    /**
     * Return true if this Doc is include in the active set.
     * RootDocImpl isn't even a program entity so it is always false.
     */
    public boolean isIncluded() {
        return false;
    }

    /**
     * Print error message, increment error count.
     *
     * @param msg message to print
     */
    public void printError(String msg) {
        env.printError(msg);
    }

    /**
     * Print error message, increment error count.
     *
     * @param msg message to print
     */
    public void printError( gw.gosudoc.com.sun.javadoc.SourcePosition pos, String msg) {
        env.printError(pos, msg);
    }

    /**
     * Print warning message, increment warning count.
     *
     * @param msg message to print
     */
    public void printWarning(String msg) {
        env.printWarning(msg);
    }

    /**
     * Print warning message, increment warning count.
     *
     * @param msg message to print
     */
    public void printWarning( gw.gosudoc.com.sun.javadoc.SourcePosition pos, String msg) {
        env.printWarning(pos, msg);
    }

    /**
     * Print a message.
     *
     * @param msg message to print
     */
    public void printNotice(String msg) {
        env.printNotice(msg);
    }

    /**
     * Print a message.
     *
     * @param msg message to print
     */
    public void printNotice( gw.gosudoc.com.sun.javadoc.SourcePosition pos, String msg) {
        env.printNotice(pos, msg);
    }

    /**
     * Return the path of the overview file and null if it does not exist.
     * @return the path of the overview file and null if it does not exist.
     */
    private JavaFileObject getOverviewPath() {
        for (String[] opt : options) {
            if (opt[0].equals("-overview")) {
                if (env.fileManager instanceof StandardJavaFileManager) {
                    StandardJavaFileManager fm = (StandardJavaFileManager) env.fileManager;
                    return fm.getJavaFileObjects(opt[1]).iterator().next();
                }
            }
        }
        return null;
    }

    /**
     * Do lazy initialization of "documentation" string.
     */
    @Override
    protected String documentation() {
        if (documentation == null) {
            JavaFileObject overviewPath = getOverviewPath();
            if (overviewPath == null) {
                // no doc file to be had
                documentation = "";
            } else {
                // read from file
                try {
                    documentation = readHTMLDocumentation(
                        overviewPath.openInputStream(),
                        overviewPath);
                } catch (IOException exc) {
                    documentation = "";
                    env.error(null, "javadoc.File_Read_Error", overviewPath.getName());
                }
            }
        }
        return documentation;
    }

    /**
     * Return the source position of the entity, or null if
     * no position is available.
     */
    @Override
    public gw.gosudoc.com.sun.javadoc.SourcePosition position() {
        JavaFileObject path;
        return ((path = getOverviewPath()) == null) ?
            null :
            SourcePositionImpl.make(path, Position.NOPOS, null);
    }

    /**
     * Return the locale provided by the user or the default locale value.
     */
    public Locale getLocale() {
        return env.doclocale.locale;
    }

    /**
     * Return the current file manager.
     */
    public JavaFileManager getFileManager() {
        return env.fileManager;
    }

    public void initDocLint(Collection<String> opts, Collection<String> customTagNames,
            String htmlVersion) {
        env.initDoclint(opts, customTagNames, htmlVersion);
    }

    public JavaScriptScanner initJavaScriptScanner( boolean allowScriptInComments) {
        return env.initJavaScriptScanner(allowScriptInComments);
    }

    public boolean isFunctionalInterface( gw.gosudoc.com.sun.javadoc.AnnotationDesc annotationDesc) {
        return annotationDesc.annotationType().qualifiedName().equals(
                env.syms.functionalInterfaceType.toString());
    }

    public boolean showTagMessages() {
        return env.showTagMessages();
    }
}
