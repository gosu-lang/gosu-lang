/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;


import gw.gosudoc.com.sun.javadoc.Type;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;

/**
 * Build Class Hierarchy for all the Classes. This class builds the Class
 * Tree and the Interface Tree separately.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @see HashMap
 * @see List
 * @see gw.gosudoc.com.sun.javadoc.Type
 * @see gw.gosudoc.com.sun.javadoc.ClassDoc
 * @author Atul M Dambalkar
 */
@Deprecated
public class ClassTree {

    /**
     * List of baseclasses. Contains only java.lang.Object. Can be used to get
     * the mapped listing of sub-classes.
     */
    final private SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseclasses;

    /**
    * Mapping for each Class with their SubClasses
    */
    final private Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> subclasses = new HashMap<>();

    /**
     * List of base-interfaces. Contains list of all the interfaces who do not
     * have super-interfaces. Can be used to get the mapped listing of
     * sub-interfaces.
     */
    final private SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseinterfaces;

    /**
    * Mapping for each Interface with their SubInterfaces
    */
    final private Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> subinterfaces = new HashMap<>();

    final private SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseEnums;
    final private Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> subEnums = new HashMap<>();

    final private SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseAnnotationTypes;
    final private Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> subAnnotationTypes = new HashMap<>();

    /**
    * Mapping for each Interface with classes who implement it.
    */
    final private Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> implementingclasses = new HashMap<>();

    private final Configuration configuration;
    private final Utils utils;
    private final Comparator<gw.gosudoc.com.sun.javadoc.Doc> comparator;
    /**
     * Constructor. Build the Tree using the Root of this Javadoc run.
     *
     * @param configuration the configuration of the doclet.
     * @param noDeprecated Don't add deprecated classes in the class tree, if
     * true.
     */
    public ClassTree(Configuration configuration, boolean noDeprecated) {
        configuration.message.notice("doclet.Building_Tree");
        this.configuration = configuration;
        this.utils = configuration.utils;
        comparator = utils.makeGeneralPurposeComparator();
        baseAnnotationTypes = new TreeSet<>(comparator);
        baseEnums = new TreeSet<>(comparator);
        baseclasses = new TreeSet<>(comparator);
        baseinterfaces = new TreeSet<>(comparator);
        buildTree(configuration.root.classes());

    }

    /**
     * Constructor. Build the Tree using the Root of this Javadoc run.
     *
     * @param root Root of the Document.
     * @param configuration The current configuration of the doclet.
     */
    public ClassTree( gw.gosudoc.com.sun.javadoc.RootDoc root, Configuration configuration) {
        this.configuration = configuration;
        this.utils = configuration.utils;
        comparator = utils.makeGeneralPurposeComparator();
        baseAnnotationTypes = new TreeSet<>(comparator);
        baseEnums = new TreeSet<>(comparator);
        baseclasses = new TreeSet<>(comparator);
        baseinterfaces = new TreeSet<>(comparator);
        buildTree(root.classes());
    }

    /**
     * Constructor. Build the tree for the given array of classes.
     *
     * @param classes Array of classes.
     * @param configuration The current configuration of the doclet.
     */
    public ClassTree( gw.gosudoc.com.sun.javadoc.ClassDoc[] classes, Configuration configuration) {
        this.configuration = configuration;
        this.utils = configuration.utils;
        comparator = utils.makeGeneralPurposeComparator();
        baseAnnotationTypes = new TreeSet<>(comparator);
        baseEnums = new TreeSet<>(comparator);
        baseclasses = new TreeSet<>(comparator);
        baseinterfaces = new TreeSet<>(comparator);
        buildTree(classes);
    }

    /**
     * Generate mapping for the sub-classes for every class in this run.
     * Return the sub-class list for java.lang.Object which will be having
     * sub-class listing for itself and also for each sub-class itself will
     * have their own sub-class lists.
     *
     * @param classes all the classes in this run.
     * @param configuration the current configuration of the doclet.
     */
    private void buildTree( gw.gosudoc.com.sun.javadoc.ClassDoc[] classes) {
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc aClass : classes) {
            // In the tree page (e.g overview-tree.html) do not include
            // information of classes which are deprecated or are a part of a
            // deprecated package.
            if (configuration.nodeprecated &&
                    (utils.isDeprecated(aClass) ||
                    utils.isDeprecated(aClass.containingPackage()))) {
                continue;
            }

            if (configuration.javafx
                    && aClass.tags("treatAsPrivate").length > 0) {
                continue;
            }

            if (aClass.isEnum()) {
                processType(aClass, configuration, baseEnums, subEnums);
            } else if (aClass.isClass()) {
                processType(aClass, configuration, baseclasses, subclasses);
            } else if (aClass.isInterface()) {
                processInterface(aClass);
            } else if (aClass.isAnnotationType()) {
                processType(aClass, configuration, baseAnnotationTypes,
                    subAnnotationTypes);
            }
        }
    }

    /**
     * For the class passed map it to it's own sub-class listing.
     * For the Class passed, get the super class,
     * if superclass is non null, (it is not "java.lang.Object")
     *    get the "value" from the hashmap for this key Class
     *    if entry not found create one and get that.
     *    add this Class as a sub class in the list
     *    Recurse till hits java.lang.Object Null SuperClass.
     *
     * @param cd class for which sub-class mapping to be generated.
     * @param configuration the current configurtation of the doclet.
     */
    private void processType( gw.gosudoc.com.sun.javadoc.ClassDoc cd, Configuration configuration,
                              Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> bases, Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> subs) {
        gw.gosudoc.com.sun.javadoc.ClassDoc superclass = utils.getFirstVisibleSuperClassCD(cd, configuration);
        if (superclass != null) {
            if (!add(subs, superclass, cd)) {
                return;
            } else {
                processType(superclass, configuration, bases, subs);
            }
        } else {     // cd is java.lang.Object, add it once to the list
            if (!bases.contains(cd)) {
                bases.add(cd);
            }
        }
        List<gw.gosudoc.com.sun.javadoc.Type> intfacs = utils.getAllInterfaces(cd, configuration);
        for ( Type intfac : intfacs) {
            add(implementingclasses, intfac.asClassDoc(), cd);
        }
    }

    /**
     * For the interface passed get the interfaces which it extends, and then
     * put this interface in the sub-interface list of those interfaces. Do it
     * recursively. If a interface doesn't have super-interface just attach
     * that interface in the list of all the baseinterfaces.
     *
     * @param cd Interface under consideration.
     */
    private void processInterface( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        gw.gosudoc.com.sun.javadoc.ClassDoc[] intfacs = cd.interfaces();
        if (intfacs.length > 0) {
            for ( gw.gosudoc.com.sun.javadoc.ClassDoc intfac : intfacs) {
                if (!add(subinterfaces, intfac, cd)) {
                    return;
                } else {
                    processInterface(intfac);   // Recurse
                }
            }
        } else {
            // we need to add all the interfaces who do not have
            // super-interfaces to baseinterfaces list to traverse them
            if (!baseinterfaces.contains(cd)) {
                baseinterfaces.add(cd);
            }
        }
    }

    /**
     * Adjust the Class Tree. Add the class interface  in to it's super-class'
     * or super-interface's sub-interface list.
     *
     * @param map the entire map.
     * @param superclass java.lang.Object or the super-interface.
     * @param cd sub-interface to be mapped.
     * @returns boolean true if class added, false if class already processed.
     */
    private boolean add( Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> map, gw.gosudoc.com.sun.javadoc.ClassDoc superclass, gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> list = map.get(superclass);
        if (list == null) {
            list = new TreeSet<>(comparator);
            map.put(superclass, list);
        }
        if (list.contains(cd)) {
            return false;
        } else {
            list.add(cd);
        }
        return true;
    }

    /**
     * From the map return the list of sub-classes or sub-interfaces. If list
     * is null create a new one and return it.
     *
     * @param map The entire map.
     * @param cd class for which the sub-class list is requested.
     * @returns List Sub-Class list for the class passed.
     */
    private SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> get( Map<gw.gosudoc.com.sun.javadoc.ClassDoc, SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc>> map, gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> aset = map.get(cd);
        if (aset == null) {
            return new TreeSet<>(comparator);
        }
        return aset;
    }

    /**
     *  Return the sub-class list for the class passed.
     *
     * @param cd class whose sub-class list is required.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> subclasses( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        return get(subclasses, cd);
    }

    /**
     *  Return the sub-interface list for the interface passed.
     *
     * @param cd interface whose sub-interface list is required.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> subinterfaces( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        return get(subinterfaces, cd);
    }

    /**
     *  Return the list of classes which implement the interface passed.
     *
     * @param cd interface whose implementing-classes list is required.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> implementingclasses( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> result = get(implementingclasses, cd);
        SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> intfcs = allSubs(cd, false);

        //If class x implements a subinterface of cd, then it follows
        //that class x implements cd.
        Iterator<gw.gosudoc.com.sun.javadoc.ClassDoc> subInterfacesIter = intfcs.iterator();
        while (subInterfacesIter.hasNext()) {
            Iterator<gw.gosudoc.com.sun.javadoc.ClassDoc> implementingClassesIter
                    = implementingclasses(subInterfacesIter.next()).iterator();
            while (implementingClassesIter.hasNext()) {
                gw.gosudoc.com.sun.javadoc.ClassDoc c = implementingClassesIter.next();
                if (!result.contains(c)) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    /**
     *  Return the sub-class/interface list for the class/interface passed.
     *
     * @param cd class/interface whose sub-class/interface list is required.
     * @param isEnum true if the subclasses should be forced to come from the
     * enum tree.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> subs( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean isEnum) {
        if (isEnum) {
            return get(subEnums, cd);
        } else if (cd.isAnnotationType()) {
            return get(subAnnotationTypes, cd);
        } else if (cd.isInterface()) {
            return get(subinterfaces, cd);
        } else if (cd.isClass()) {
            return get(subclasses, cd);
        } else {
            return null;
        }

    }

    /**
     * Return a list of all direct or indirect, sub-classes and subinterfaces
     * of the ClassDoc argument.
     *
     * @param cd ClassDoc whose sub-classes or sub-interfaces are requested.
     * @param isEnum true if the subclasses should be forced to come from the
     * enum tree.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> allSubs( gw.gosudoc.com.sun.javadoc.ClassDoc cd, boolean isEnum) {
        // new entries added to the list are searched as well
        List<gw.gosudoc.com.sun.javadoc.ClassDoc> list = new ArrayList<>(subs(cd, isEnum));
        for (int i = 0; i < list.size(); i++) {
            cd = list.get(i);
            SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> tlist = subs(cd, isEnum);
            for ( gw.gosudoc.com.sun.javadoc.ClassDoc tcd : tlist) {
                if (!list.contains(tcd)) {
                    list.add(tcd);
                }
            }
        }
        SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> oset = new TreeSet<>(comparator);
        oset.addAll(list);
        return oset;
    }

    /**
     *  Return the base-classes list. This will have only one element namely
     *  thw classdoc for java.lang.Object, since this is the base class for all
     *  classes.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseclasses() {
        return baseclasses;
    }

    /**
     *  Return the list of base interfaces. This is the list of interfaces
     *  which do not have super-interface.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseinterfaces() {
        return baseinterfaces;
    }

    /**
     *  Return the list of base enums. This is the list of enums
     *  which do not have super-enums.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseEnums() {
        return baseEnums;
    }

    /**
     *  Return the list of base annotation types. This is the list of
     *  annotation types which do not have super-annotation types.
     */
    public SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> baseAnnotationTypes() {
        return baseAnnotationTypes;
    }
}
