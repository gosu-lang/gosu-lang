/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.ConstructorDoc;
import gw.gosudoc.com.sun.tools.doclets.formats.html.ConfigurationImpl;

/**
 * Map all class uses for a given class.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.2
 * @author Robert G. Field
 */
@Deprecated
public class ClassUseMapper {

    private final ClassTree classtree;

    /**
     * Mapping of ClassDocs to set of PackageDoc used by that class.
     * Entries may be null.
     */
    public Map<String,Set<gw.gosudoc.com.sun.javadoc.PackageDoc>> classToPackage = new HashMap<>();

    /**
     * Mapping of Annotations to set of PackageDoc that use the annotation.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.PackageDoc>> classToPackageAnnotations = new HashMap<>();

    /**
     * Mapping of ClassDocs to set of ClassDoc used by that class.
     * Entries may be null.
     */
    public Map<String,Set<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToClass = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of ClassDoc which are direct or
     * indirect subclasses of that class.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToSubclass = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of ClassDoc which are direct or
     * indirect subinterfaces of that interface.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToSubinterface = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of ClassDoc which implement
     * this interface.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToImplementingClass = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of FieldDoc declared as that class.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.FieldDoc>> classToField = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of MethodDoc returning that class.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.MethodDoc>> classToMethodReturn = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of MethodDoc having that class
     * as an arg.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToMethodArgs = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of MethodDoc which throws that class.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToMethodThrows = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of ConstructorDoc having that class
     * as an arg.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToConstructorArgs = new HashMap<>();

    /**
     * Mapping of ClassDocs to list of ConstructorDoc which throws that class.
     * Entries may be null.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToConstructorThrows = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to constructors that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ConstructorDoc>> classToConstructorAnnotations = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to Constructor parameters that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToConstructorParamAnnotation = new HashMap<>();

    /**
     * The mapping of ClassDocs to Constructor arguments that use them as type parameters.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToConstructorDocArgTypeParam = new HashMap<>();

    /**
     * The mapping of ClassDocs to ClassDocs that use them as type parameters.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToClassTypeParam = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to ClassDocs that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> classToClassAnnotations = new HashMap<>();

    /**
     * The mapping of ClassDocs to ExecutableMemberDocs that use them as type parameters.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.MethodDoc>> classToExecMemberDocTypeParam = new HashMap<>();

    /**
     * The mapping of ClassDocs to ExecutableMemberDocs arguments that use them as type parameters.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToExecMemberDocArgTypeParam = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to ExecutableMemberDocs that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.MethodDoc>> classToExecMemberDocAnnotations = new HashMap<>();

    /**
     * The mapping of ClassDocs to ExecutableMemberDocs that have return type
     * with type parameters of that class.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.MethodDoc>> classToExecMemberDocReturnTypeParam = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to MethodDoc parameters that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc>> classToExecMemberDocParamAnnotation = new HashMap<>();

    /**
     * The mapping of ClassDocs to FieldDocs that use them as type parameters.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.FieldDoc>> classToFieldDocTypeParam = new HashMap<>();

    /**
     * The mapping of AnnotationTypeDocs to FieldDocs that use them.
     */
    public Map<String,List<gw.gosudoc.com.sun.javadoc.FieldDoc>> annotationToFieldDoc = new HashMap<>();

    private final Utils utils;
    public ClassUseMapper( ConfigurationImpl configuration, ClassTree classtree) {
        gw.gosudoc.com.sun.javadoc.RootDoc root = configuration.root;
        this.classtree = classtree;
        utils = configuration.utils;
        // Map subclassing, subinterfacing implementing, ...
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc doc : classtree.baseclasses()) {
            subclasses(doc);
        }
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc doc : classtree.baseinterfaces()) {
            // does subinterfacing as side-effect
            implementingClasses(doc);
        }
        // Map methods, fields, constructors using a class.
        gw.gosudoc.com.sun.javadoc.ClassDoc[] classes = root.classes();
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc aClass : classes) {
            gw.gosudoc.com.sun.javadoc.PackageDoc pkg = aClass.containingPackage();
            mapAnnotations(classToPackageAnnotations, pkg, pkg);
            gw.gosudoc.com.sun.javadoc.ClassDoc cd = aClass;
            mapTypeParameters(classToClassTypeParam, cd, cd);
            mapAnnotations(classToClassAnnotations, cd, cd);
            gw.gosudoc.com.sun.javadoc.FieldDoc[] fields = cd.fields();
            for ( gw.gosudoc.com.sun.javadoc.FieldDoc fd : fields) {
                mapTypeParameters(classToFieldDocTypeParam, fd, fd);
                mapAnnotations(annotationToFieldDoc, fd, fd);
                if (!fd.type().isPrimitive()) {
                    add(classToField, fd.type().asClassDoc(), fd);
                }
            }
            gw.gosudoc.com.sun.javadoc.ConstructorDoc[] cons = cd.constructors();
            for ( ConstructorDoc con : cons) {
                mapAnnotations(classToConstructorAnnotations, con, con);
                mapExecutable(con);
            }
            gw.gosudoc.com.sun.javadoc.MethodDoc[] meths = cd.methods();
            for ( gw.gosudoc.com.sun.javadoc.MethodDoc md : meths) {
                mapExecutable(md);
                mapTypeParameters(classToExecMemberDocTypeParam, md, md);
                mapAnnotations(classToExecMemberDocAnnotations, md, md);
                if (!(md.returnType().isPrimitive() || md.returnType() instanceof gw.gosudoc.com.sun.javadoc.TypeVariable)) {
                    mapTypeParameters(classToExecMemberDocReturnTypeParam,
                                      md.returnType(), md);
                    add(classToMethodReturn, md.returnType().asClassDoc(), md);
                }
            }
        }
    }

    /**
     * Return all subclasses of a class AND fill-in classToSubclass map.
     */
    private Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> subclasses( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> ret = classToSubclass.get(cd.qualifiedName());
        if (ret == null) {
            ret = new TreeSet<>(utils.makeComparatorForClassUse());
            SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> subs = classtree.subclasses(cd);
            if (subs != null) {
                ret.addAll(subs);
                for ( gw.gosudoc.com.sun.javadoc.ClassDoc sub : subs) {
                    ret.addAll(subclasses(sub));
                }
            }
            addAll(classToSubclass, cd, ret);
        }
        return ret;
    }

    /**
     * Return all subinterfaces of an interface AND fill-in classToSubinterface map.
     */
    private Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> subinterfaces( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> ret = classToSubinterface.get(cd.qualifiedName());
        if (ret == null) {
            ret = new TreeSet<>(utils.makeComparatorForClassUse());
            SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> subs = classtree.subinterfaces(cd);
            if (subs != null) {
                ret.addAll(subs);
                for ( gw.gosudoc.com.sun.javadoc.ClassDoc sub : subs) {
                    ret.addAll(subinterfaces(sub));
                }
            }
            addAll(classToSubinterface, cd, ret);
        }
        return ret;
    }

    /**
     * Return all implementing classes of an interface (including
     * all subclasses of implementing classes and all classes
     * implementing subinterfaces) AND fill-in both classToImplementingClass
     * and classToSubinterface maps.
     */
    private Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> implementingClasses( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> ret = classToImplementingClass.get(cd.qualifiedName());
        if (ret == null) {
            ret = new TreeSet<>(utils.makeComparatorForClassUse());
            SortedSet<gw.gosudoc.com.sun.javadoc.ClassDoc> impl = classtree.implementingclasses(cd);
            if (impl != null) {
                ret.addAll(impl);
                for ( gw.gosudoc.com.sun.javadoc.ClassDoc anImpl : impl) {
                    ret.addAll(subclasses(anImpl));
                }
            }
            for ( gw.gosudoc.com.sun.javadoc.ClassDoc doc : subinterfaces(cd)) {
                ret.addAll(implementingClasses(doc));
            }
            addAll(classToImplementingClass, cd, ret);
        }
        return ret;
    }

    /**
     * Determine classes used by a method or constructor, so they can be
     * inverse mapped.
     */
    private void mapExecutable( gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc em) {
        boolean isConstructor = em.isConstructor();
        Set<gw.gosudoc.com.sun.javadoc.Type> classArgs = new TreeSet<>(utils.makeTypeComparator());
        for ( gw.gosudoc.com.sun.javadoc.Parameter param : em.parameters()) {
            gw.gosudoc.com.sun.javadoc.Type pcd = param.type();
            // ignore primitives and typevars, typevars are handled elsewhere
            if ((!param.type().isPrimitive()) && !(pcd instanceof gw.gosudoc.com.sun.javadoc.TypeVariable)) {
                 // avoid dups
                if (classArgs.add(pcd)) {
                    add(isConstructor ? classToConstructorArgs : classToMethodArgs,
                            pcd.asClassDoc(), em);
                    mapTypeParameters(isConstructor
                                ? classToConstructorDocArgTypeParam
                                : classToExecMemberDocArgTypeParam,
                            pcd, em);
                }
            }
            mapAnnotations(isConstructor
                        ? classToConstructorParamAnnotation
                        : classToExecMemberDocParamAnnotation,
                    param, em);

        }
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc anException : em.thrownExceptions()) {
            add(isConstructor ? classToConstructorThrows : classToMethodThrows,
                anException, em);
        }
    }

    private <T> List<T> refList(Map<String,List<T>> map, gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        List<T> list = map.get(cd.qualifiedName());
        if (list == null) {
            list = new ArrayList<>();
            map.put(cd.qualifiedName(), list);
        }
        return list;
    }

    private Set<gw.gosudoc.com.sun.javadoc.PackageDoc> packageSet( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        Set<gw.gosudoc.com.sun.javadoc.PackageDoc> pkgSet = classToPackage.get(cd.qualifiedName());
        if (pkgSet == null) {
            pkgSet = new TreeSet<>();
            classToPackage.put(cd.qualifiedName(), pkgSet);
        }
        return pkgSet;
    }

    private Set<gw.gosudoc.com.sun.javadoc.ClassDoc> classSet( gw.gosudoc.com.sun.javadoc.ClassDoc cd) {
        Set<gw.gosudoc.com.sun.javadoc.ClassDoc> clsSet = classToClass.get(cd.qualifiedName());
        if (clsSet == null) {
            clsSet = new TreeSet<>();
            classToClass.put(cd.qualifiedName(), clsSet);
        }
        return clsSet;
    }

    private <T extends gw.gosudoc.com.sun.javadoc.ProgramElementDoc> void add( Map<String,List<T>> map, gw.gosudoc.com.sun.javadoc.ClassDoc cd, T ref) {
        // add to specified map
        refList(map, cd).add(ref);

        // add ref's package to package map and class map
        packageSet(cd).add(ref.containingPackage());

        classSet(cd).add(ref instanceof gw.gosudoc.com.sun.javadoc.MemberDoc ?
                ((gw.gosudoc.com.sun.javadoc.MemberDoc)ref).containingClass() :
                    (gw.gosudoc.com.sun.javadoc.ClassDoc)ref);
    }

    private void addAll( Map<String,List<gw.gosudoc.com.sun.javadoc.ClassDoc>> map, gw.gosudoc.com.sun.javadoc.ClassDoc cd, Collection<gw.gosudoc.com.sun.javadoc.ClassDoc> refs) {
        if (refs == null) {
            return;
        }
        // add to specified map
        refList(map, cd).addAll(refs);

        Set<gw.gosudoc.com.sun.javadoc.PackageDoc> pkgSet = packageSet(cd);
        Set<gw.gosudoc.com.sun.javadoc.ClassDoc> clsSet = classSet(cd);
        // add ref's package to package map and class map
        for ( gw.gosudoc.com.sun.javadoc.ClassDoc cls : refs) {
            pkgSet.add(cls.containingPackage());
            clsSet.add(cls);

        }
    }

    /**
     * Map the ClassDocs to the ProgramElementDocs that use them as
     * type parameters.
     *
     * @param map the map the insert the information into.
     * @param doc the doc whose type parameters are being checked.
     * @param holder the holder that owns the type parameters.
     */
    private <T extends gw.gosudoc.com.sun.javadoc.ProgramElementDoc> void mapTypeParameters( Map<String,List<T>> map, Object doc,
                                                                                             T holder) {
        gw.gosudoc.com.sun.javadoc.TypeVariable[] typeVariables;
        if (doc instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
            typeVariables = ((gw.gosudoc.com.sun.javadoc.ClassDoc) doc).typeParameters();
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.WildcardType ) {
            for ( gw.gosudoc.com.sun.javadoc.Type extendsBound : ((gw.gosudoc.com.sun.javadoc.WildcardType) doc).extendsBounds()) {
                addTypeParameterToMap(map, extendsBound, holder);
            }
            for ( gw.gosudoc.com.sun.javadoc.Type superBound : ((gw.gosudoc.com.sun.javadoc.WildcardType) doc).superBounds()) {
                addTypeParameterToMap(map, superBound, holder);
            }
            return;
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.ParameterizedType ) {
            for ( gw.gosudoc.com.sun.javadoc.Type typeArgument : ((gw.gosudoc.com.sun.javadoc.ParameterizedType) doc).typeArguments()) {
                addTypeParameterToMap(map, typeArgument, holder);
            }
            return;
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc ) {
            typeVariables = ((gw.gosudoc.com.sun.javadoc.ExecutableMemberDoc) doc).typeParameters();
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.FieldDoc ) {
            gw.gosudoc.com.sun.javadoc.Type fieldType = ((gw.gosudoc.com.sun.javadoc.FieldDoc) doc).type();
            mapTypeParameters(map, fieldType, holder);
            return;
        } else {
            return;
        }
        for ( gw.gosudoc.com.sun.javadoc.TypeVariable typeVariable : typeVariables) {
            for ( gw.gosudoc.com.sun.javadoc.Type bound : typeVariable.bounds()) {
                addTypeParameterToMap(map, bound, holder);
            }
        }
    }

    /**
     * Map the AnnotationType to the ProgramElementDocs that use them as
     * type parameters.
     *
     * @param map the map the insert the information into.
     * @param doc the doc whose type parameters are being checked.
     * @param holder the holder that owns the type parameters.
     */
    private <T extends gw.gosudoc.com.sun.javadoc.ProgramElementDoc> void mapAnnotations( Map<String,List<T>> map, Object doc,
                                                                                          T holder) {
        gw.gosudoc.com.sun.javadoc.AnnotationDesc[] annotations;
        boolean isPackage = false;
        if (doc instanceof gw.gosudoc.com.sun.javadoc.ProgramElementDoc ) {
            annotations = ((gw.gosudoc.com.sun.javadoc.ProgramElementDoc) doc).annotations();
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.PackageDoc ) {
            annotations = ((gw.gosudoc.com.sun.javadoc.PackageDoc) doc).annotations();
            isPackage = true;
        } else if (doc instanceof gw.gosudoc.com.sun.javadoc.Parameter ) {
            annotations = ((gw.gosudoc.com.sun.javadoc.Parameter) doc).annotations();
        } else {
            throw new DocletAbortException("should not happen");
        }
        for ( gw.gosudoc.com.sun.javadoc.AnnotationDesc annotation : annotations) {
            gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc annotationDoc = annotation.annotationType();
            if (isPackage)
                refList(map, annotationDoc).add(holder);
            else
                add(map, annotationDoc, holder);
        }
    }


    /**
     * Map the AnnotationType to the ProgramElementDocs that use them as
     * type parameters.
     *
     * @param map the map the insert the information into.
     * @param doc the doc whose type parameters are being checked.
     * @param holder the holder that owns the type parameters.
     */
    private <T extends gw.gosudoc.com.sun.javadoc.PackageDoc> void mapAnnotations( Map<String,List<T>> map, gw.gosudoc.com.sun.javadoc.PackageDoc doc,
                                                                                   T holder) {
        for ( gw.gosudoc.com.sun.javadoc.AnnotationDesc annotation : doc.annotations()) {
            gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc annotationDoc = annotation.annotationType();
            refList(map, annotationDoc).add(holder);
        }
    }

    private <T extends gw.gosudoc.com.sun.javadoc.ProgramElementDoc> void addTypeParameterToMap( Map<String,List<T>> map, gw.gosudoc.com.sun.javadoc.Type type,
                                                                                                 T holder) {
        if (type instanceof gw.gosudoc.com.sun.javadoc.ClassDoc ) {
            add(map, (gw.gosudoc.com.sun.javadoc.ClassDoc) type, holder);
        } else if (type instanceof gw.gosudoc.com.sun.javadoc.ParameterizedType ) {
            add(map, ((gw.gosudoc.com.sun.javadoc.ParameterizedType) type).asClassDoc(), holder);
        }
        mapTypeParameters(map, type, holder);
    }
}
