/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;


import gw.gosudoc.com.sun.javadoc.Doc;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;

/**
 * Build the mapping of each Unicode character with it's member lists
 * containing members names starting with it. Also build a list for all the
 * Unicode characters which start a member name. Member name is
 * classkind or field or method or constructor name.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.2
 * @see Character
 * @author Atul M Dambalkar
 */
@Deprecated
public class IndexBuilder {

    /**
     * Mapping of each Unicode Character with the member list containing
     * members with names starting with it.
     */
    private Map<Character,List<gw.gosudoc.com.sun.javadoc.Doc>> indexmap = new HashMap<>();

    /**
     * Don't generate deprecated information if true.
     */
    private boolean noDeprecated;

    /**
     * Build this Index only for classes?
     */
    private boolean classesOnly;

    /**
     * Indicates javafx mode.
     */
    private boolean javafx;

    // make ProgramElementDoc[] when new toArray is available
    protected final Object[] elements;

    private final Configuration configuration;
    /**
     * Constructor. Build the index map.
     *
     * @param configuration the current configuration of the doclet.
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise.
     */
    public IndexBuilder(Configuration configuration, boolean noDeprecated) {
        this(configuration, noDeprecated, false);
    }

    /**
     * Constructor. Build the index map.
     *
     * @param configuration the current configuration of the doclet.
     * @param noDeprecated  true if -nodeprecated option is used,
     *                      false otherwise.
     * @param classesOnly   Include only classes in index.
     */
    public IndexBuilder(Configuration configuration, boolean noDeprecated,
                        boolean classesOnly) {
        this.configuration  = configuration;
        if (classesOnly) {
            configuration.message.notice("doclet.Building_Index_For_All_Classes");
        } else {
            configuration.message.notice("doclet.Building_Index");
        }
        this.noDeprecated = noDeprecated;
        this.classesOnly = classesOnly;
        this.javafx = configuration.javafx;
        buildIndexMap(configuration.root);
        Set<Character> set = indexmap.keySet();
        elements =  set.toArray();
        Arrays.sort(elements);
    }

    /**
     * Sort the index map. Traverse the index map for all it's elements and
     * sort each element which is a list.
     */
    protected void sortIndexMap() {
        for (List<gw.gosudoc.com.sun.javadoc.Doc> docs : indexmap.values()) {
            Collections.sort(docs, configuration.utils.makeComparatorForIndexUse());
        }
    }

    /**
     * Get all the members in all the Packages and all the Classes
     * given on the command line. Form separate list of those members depending
     * upon their names.
     *
     * @param root Root of the documemt.
     */
    protected void buildIndexMap( gw.gosudoc.com.sun.javadoc.RootDoc root)  {
        gw.gosudoc.com.sun.javadoc.PackageDoc[] packages = root.specifiedPackages();
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes = root.classes();
        if (!classesOnly) {
            if (packages.length == 0) {
                Set<gw.gosudoc.com.sun.javadoc.PackageDoc> set = new HashSet<>();
                for ( gw.gosudoc.com.sun.javadoc.ClassDoc aClass : classes) {
                    gw.gosudoc.com.sun.javadoc.PackageDoc pd = aClass.containingPackage();
                    if (pd != null && pd.name().length() > 0) {
                        set.add(pd);
                    }
                }
                adjustIndexMap(set.toArray(packages));
            } else {
                adjustIndexMap(packages);
            }
        }
        adjustIndexMap(classes);
        if (!classesOnly) {
            for ( gw.gosudoc.com.sun.javadoc.ClassDoc aClass : classes) {
                if (shouldAddToIndexMap(aClass)) {
                    putMembersInIndexMap(aClass);
                }
            }
        }
        sortIndexMap();
    }

    /**
     * Put all the members(fields, methods and constructors) in the classdoc
     * to the indexmap.
     *
     * @param classdoc ClassDoc whose members will be added to the indexmap.
     */
    protected void putMembersInIndexMap( gw.gosudoc.com.sun.javadoc.ClassDoc classdoc) {
        adjustIndexMap(classdoc.fields());
        adjustIndexMap(classdoc.methods());
        adjustIndexMap(classdoc.constructors());
    }


    /**
     * Adjust list of members according to their names. Check the first
     * character in a member name, and then add the member to a list of members
     * for that particular unicode character.
     *
     * @param elements Array of members.
     */
    protected void adjustIndexMap( gw.gosudoc.com.sun.javadoc.Doc[] elements) {
        for ( gw.gosudoc.com.sun.javadoc.Doc element : elements) {
            if (shouldAddToIndexMap(element)) {
                String name = element.name();
                char ch = (name.length() == 0) ?
                          '*' :
                          Character.toUpperCase(name.charAt(0));
                Character unicode = ch;
                List<gw.gosudoc.com.sun.javadoc.Doc> list = indexmap.get(unicode);
                if (list == null) {
                    list = new ArrayList<>();
                    indexmap.put(unicode, list);
                }
                list.add(element);
            }
        }
    }

    /**
     * Should this doc element be added to the index map?
     */
    protected boolean shouldAddToIndexMap( gw.gosudoc.com.sun.javadoc.Doc element) {
        if (javafx) {
            if (element.tags("treatAsPrivate").length > 0) {
                return false;
            }
        }

        if (element instanceof gw.gosudoc.com.sun.javadoc.PackageDoc )
            // Do not add to index map if -nodeprecated option is set and the
            // package is marked as deprecated.
            return !(noDeprecated && configuration.utils.isDeprecated(element));
        else
            // Do not add to index map if -nodeprecated option is set and if the
            // Doc is marked as deprecated or the containing package is marked as
            // deprecated.
            return !(noDeprecated &&
                    (configuration.utils.isDeprecated(element) ||
                    configuration.utils.isDeprecated(((gw.gosudoc.com.sun.javadoc.ProgramElementDoc)element).containingPackage())));
    }

    /**
     * Return a map of all the individual member lists with Unicode character.
     *
     * @return Map index map.
     */
    public Map<Character,List<gw.gosudoc.com.sun.javadoc.Doc>> getIndexMap() {
        return indexmap;
    }

    /**
     * Return the sorted list of members, for passed Unicode Character.
     *
     * @param index index Unicode character.
     * @return List member list for specific Unicode character.
     */
    public List<Doc> getMemberList( Character index) {
        return indexmap.get(index);
    }

    /**
     * Array of IndexMap keys, Unicode characters.
     */
    public Object[] elements() {
        return elements;
    }
}
