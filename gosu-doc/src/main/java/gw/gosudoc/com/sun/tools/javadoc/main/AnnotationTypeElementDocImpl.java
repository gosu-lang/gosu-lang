/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol.*;
import gw.gosudoc.com.sun.javadoc.AnnotationTypeElementDoc;

/**
 * Represents an element of an annotation type.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Scott Seligman
 * @since 1.5
 */

@Deprecated
public class AnnotationTypeElementDocImpl
        extends MethodDocImpl implements AnnotationTypeElementDoc
{

    public AnnotationTypeElementDocImpl( DocEnv env, MethodSymbol sym) {
        super(env, sym);
    }

    public AnnotationTypeElementDocImpl( DocEnv env, MethodSymbol sym, TreePath treePath) {
        super(env, sym, treePath);
    }

    /**
     * Returns true, as this is an annotation type element.
     * (For legacy doclets, return false.)
     */
    public boolean isAnnotationTypeElement() {
        return !isMethod();
    }

    /**
     * Returns false.  Although this is technically a method, we don't
     * consider it one for this purpose.
     * (For legacy doclets, return true.)
     */
    public boolean isMethod() {
        return env.legacyDoclet;
    }

    /**
     * Returns false, even though this is indeed abstract.  See
     * MethodDocImpl.isAbstract() for the (il)logic behind this.
     */
    public boolean isAbstract() {
        return false;
    }

    /**
     * Returns the default value of this element.
     * Returns null if this element has no default.
     */
    public gw.gosudoc.com.sun.javadoc.AnnotationValue defaultValue() {
        return (sym.defaultValue == null)
               ? null
               : new AnnotationValueImpl(env, sym.defaultValue);
    }
}
