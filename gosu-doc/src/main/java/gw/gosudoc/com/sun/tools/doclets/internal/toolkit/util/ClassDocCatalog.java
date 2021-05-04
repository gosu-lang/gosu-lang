/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.PackageDoc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;

/**
 * This class acts as an artificial PackageDoc for classes specified
 * on the command line when running Javadoc.  For example, if you
 * specify several classes from package java.lang, this class will catalog
 * those classes so that we can retrieve all of the classes from a particular
 * package later.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 * @since 1.4
 */
@Deprecated
public class ClassDocCatalog {

    /**
     * Stores the set of packages that the classes specified on the command line
     * belong to.  Note that the default package is "".
     */
    private Set<String> packageSet;


    /**
     * Stores all classes for each package
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> allClasses;

    /**
     * Stores ordinary classes (excluding Exceptions and Errors) for each
     * package
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> ordinaryClasses;

    /**
     * Stores exceptions for each package
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> exceptions;

    /**
     * Stores enums for each package.
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> enums;

    /**
     * Stores annotation types for each package.
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> annotationTypes;

    /**
     * Stores errors for each package
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> errors;

    /**
     * Stores interfaces for each package
     */
    private Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> interfaces;

    private Configuration configuration;
    private Utils utils;

    /**
     * Construct a new ClassDocCatalog.
     *
     * @param classdocs the array of ClassDocs to catalog
     */
    public ClassDocCatalog ( gw.gosudoc.com.sun.javadoc.ClassDoc[] classdocs, Configuration config) {
        init();
        this.configuration = config;
        this.utils = config.utils;
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc classdoc : classdocs) {
            addClassDoc(classdoc);
        }
    }

    /**
     * Construct a new ClassDocCatalog.
     *
     */
    public ClassDocCatalog () {
        init();
    }

    private void init() {
        allClasses = new HashMap<>();
        ordinaryClasses = new HashMap<>();
        exceptions = new HashMap<>();
        enums = new HashMap<>();
        annotationTypes = new HashMap<>();
        errors = new HashMap<>();
        interfaces = new HashMap<>();
        packageSet = new HashSet<>();
    }

    /**
     * Add the given class to the catalog.
     * @param classdoc the ClassDoc to add to the catelog.
     */
    public void addClassDoc( gw.gosudoc.com.sun.javadoc.ClassDoc classdoc) {
        if (classdoc == null) {
            return;
        }
        addClass(classdoc, allClasses);
        if (classdoc.isOrdinaryClass()) {
            addClass(classdoc, ordinaryClasses);
        } else if (classdoc.isException()) {
            addClass(classdoc, exceptions);
        } else if (classdoc.isEnum()) {
            addClass(classdoc, enums);
        } else if (classdoc.isAnnotationType()) {
            addClass(classdoc, annotationTypes);
        } else if (classdoc.isError()) {
            addClass(classdoc, errors);
        } else if (classdoc.isInterface()) {
            addClass(classdoc, interfaces);
        }
    }

    /**
     * Add the given class to the given map.
     * @param classdoc the ClassDoc to add to the catelog.
     * @param map the Map to add the ClassDoc to.
     */
    private void addClass( gw.gosudoc.com.sun.javadoc.ClassDoc classdoc, Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> map) {

        gw.gosudoc.com.sun.javadoc.PackageDoc pkg = classdoc.containingPackage();
        if (pkg.isIncluded() || (configuration.nodeprecated && utils.isDeprecated(pkg))) {
            //No need to catalog this class if it's package is
            //included on the command line or if -nodeprecated option is set
            // and the containing package is marked as deprecated.
            return;
        }
        String key = utils.getPackageName(pkg);
        Set<gw.gosudoc.com.sun.javadoc.ClassDoc> s = map.get(key);
        if (s == null) {
            packageSet.add(key);
            s = new HashSet<>();
        }
        s.add(classdoc);
        map.put(key, s);

    }

    private gw.gosudoc.com.sun.javadoc.ClassDoc[] getArray( Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> m, String key) {
        Set<gw.gosudoc.com.sun.javadoc.ClassDoc> s = m.get(key);
        if (s == null) {
            return new gw.gosudoc.com.sun.javadoc.ClassDoc[] {};
        } else {
            return s.toArray(new gw.gosudoc.com.sun.javadoc.ClassDoc[] {});
        }
    }

    /**
     * Return all of the classes specified on the command-line that
     * belong to the given package.
     * @param pkgDoc the package to return the classes for.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] allClasses( PackageDoc pkgDoc) {
        return pkgDoc.isIncluded() ?
              pkgDoc.allClasses() :
              getArray(allClasses, utils.getPackageName(pkgDoc));
    }

    /**
     * Return all of the classes specified on the command-line that
     * belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] allClasses( String packageName) {
        return getArray(allClasses, packageName);
    }

    /**
     * Return the array of package names that this catalog stores
     * ClassDocs for.
     */
    public String[] packageNames() {
        return packageSet.toArray(new String[] {});
    }

    /**
     * Return true if the given package is known to this catalog.
     * @param packageName the name to check.
     * @return true if this catalog has any information about
     * classes in the given package.
     */
    public boolean isKnownPackage(String packageName) {
        return packageSet.contains(packageName);
    }


    /**
     * Return all of the errors specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] errors( String packageName) {
        return getArray(errors, packageName);
    }

    /**
     * Return all of the exceptions specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] exceptions( String packageName) {
        return getArray(exceptions, packageName);
    }

    /**
     * Return all of the enums specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] enums( String packageName) {
        return getArray(enums, packageName);
    }

    /**
     * Return all of the annotation types specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] annotationTypes( String packageName) {
        return getArray(annotationTypes, packageName);
    }

    /**
     * Return all of the interfaces specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] interfaces( String packageName) {
        return getArray(interfaces, packageName);
    }

    /**
     * Return all of the ordinary classes specified on the command-line
     * that belong to the given package.
     * @param packageName the name of the package specified on the
     * command-line.
     */
    public gw.gosudoc.com.sun.javadoc.ClassDoc[] ordinaryClasses( String packageName) {
        return getArray(ordinaryClasses, packageName);
    }
}
