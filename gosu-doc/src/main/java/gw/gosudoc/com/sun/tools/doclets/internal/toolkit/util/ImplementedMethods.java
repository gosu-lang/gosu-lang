/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util;

import java.util.*;

import gw.gosudoc.com.sun.javadoc.Type;
import gw.gosudoc.com.sun.tools.doclets.internal.toolkit.Configuration;

/**
 * For a given class method, build an array of interface methods which it
 * implements.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Atul M Dambalkar
 */
@Deprecated
public class ImplementedMethods {

    private final Map<gw.gosudoc.com.sun.javadoc.MethodDoc, gw.gosudoc.com.sun.javadoc.Type> interfaces = new HashMap<>();
    private final List<gw.gosudoc.com.sun.javadoc.MethodDoc> methlist = new ArrayList<>();
    private final Configuration configuration;
    private final Utils utils;
    private final gw.gosudoc.com.sun.javadoc.ClassDoc classdoc;
    private final gw.gosudoc.com.sun.javadoc.MethodDoc method;

    public ImplementedMethods( gw.gosudoc.com.sun.javadoc.MethodDoc method, Configuration configuration) {
        this.method = method;
        this.configuration = configuration;
        this.utils = configuration.utils;
        classdoc = method.containingClass();
    }

    /**
     * Return the array of interface methods which the method passed in the
     * constructor is implementing. The search/build order is as follows:
     * <pre>
     * 1. Search in all the immediate interfaces which this method's class is
     *    implementing. Do it recursively for the superinterfaces as well.
     * 2. Traverse all the superclasses and search recursively in the
     *    interfaces which those superclasses implement.
     *</pre>
     *
     * @return MethodDoc[] Array of implemented methods.
     */
    public gw.gosudoc.com.sun.javadoc.MethodDoc[] build( boolean sort) {
        buildImplementedMethodList(sort);
        return methlist.toArray(new gw.gosudoc.com.sun.javadoc.MethodDoc[methlist.size()]);
    }

    public gw.gosudoc.com.sun.javadoc.MethodDoc[] build() {
        return build(true);
    }

    public gw.gosudoc.com.sun.javadoc.Type getMethodHolder( gw.gosudoc.com.sun.javadoc.MethodDoc methodDoc) {
        return interfaces.get(methodDoc);
    }

    /**
     * Search for the method in the array of interfaces. If found check if it is
     * overridden by any other subinterface method which this class
     * implements. If it is not overidden, add it in the method list.
     * Do this recursively for all the extended interfaces for each interface
     * from the array passed.
     */
    private void buildImplementedMethodList(boolean sort) {
        List<gw.gosudoc.com.sun.javadoc.Type> intfacs = utils.getAllInterfaces(classdoc, configuration, sort);
        for ( Type interfaceType : intfacs) {
            gw.gosudoc.com.sun.javadoc.MethodDoc found = utils.findMethod(interfaceType.asClassDoc(), method);
            if (found != null) {
                removeOverriddenMethod(found);
                if (!overridingMethodFound(found)) {
                    methlist.add(found);
                    interfaces.put(found, interfaceType);
                }
            }
        }
    }

    /**
     * Search in the method list and check if it contains a method which
     * is overridden by the method as parameter.  If found, remove the
     * overridden method from the method list.
     *
     * @param method Is this method overriding a method in the method list.
     */
    private void removeOverriddenMethod( gw.gosudoc.com.sun.javadoc.MethodDoc method) {
        gw.gosudoc.com.sun.javadoc.ClassDoc overriddenClass = method.overriddenClass();
        if (overriddenClass != null) {
            for (int i = 0; i < methlist.size(); i++) {
                gw.gosudoc.com.sun.javadoc.ClassDoc cd = methlist.get(i).containingClass();
                if (cd == overriddenClass || overriddenClass.subclassOf(cd)) {
                    methlist.remove(i);  // remove overridden method
                    return;
                }
            }
        }
    }

    /**
     * Search in the already found methods' list and check if it contains
     * a method which is overriding the method parameter or is the method
     * parameter itself.
     *
     * @param method MethodDoc Method to be searched in the Method List for
     * an overriding method.
     */
    private boolean overridingMethodFound( gw.gosudoc.com.sun.javadoc.MethodDoc method) {
        gw.gosudoc.com.sun.javadoc.ClassDoc containingClass = method.containingClass();
        for ( gw.gosudoc.com.sun.javadoc.MethodDoc listmethod : methlist) {
            if (containingClass == listmethod.containingClass()) {
                // it's the same method.
                return true;
            }
            gw.gosudoc.com.sun.javadoc.ClassDoc cd = listmethod.overriddenClass();
            if (cd == null) {
                continue;
            }
            if (cd == containingClass || cd.subclassOf(containingClass)) {
                return true;
            }
        }
        return false;
    }
}
