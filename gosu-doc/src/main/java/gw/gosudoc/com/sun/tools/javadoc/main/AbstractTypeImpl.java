/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.tools.javac.code.Type;
import gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc;


/**
 * Abstract implementation of <code>Type</code>, with useful
 * defaults for the methods in <code>Type</code> (and a couple from
 * <code>ProgramElementDoc</code>).
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
abstract class AbstractTypeImpl implements gw.gosudoc.com.sun.javadoc.Type
{

    protected final DocEnv env;
    protected final Type type;

    protected AbstractTypeImpl( DocEnv env, Type type) {
        this.env = env;
        this.type = type;
    }

    public String typeName() {
        return type.tsym.name.toString();
    }

    public String qualifiedTypeName() {
        return type.tsym.getQualifiedName().toString();
    }

    public gw.gosudoc.com.sun.javadoc.Type getElementType() {
        return null;
    }

    public String simpleTypeName() {
        return type.tsym.name.toString();
    }

    public String name() {
        return typeName();
    }

    public String qualifiedName() {
        return qualifiedTypeName();
    }

    public String toString() {
        return qualifiedTypeName();
    }

    public String dimension() {
        return "";
    }

    public boolean isPrimitive() {
        return false;
    }

    public gw.gosudoc.com.sun.javadoc.ClassDoc asClassDoc() {
        return null;
    }

    public gw.gosudoc.com.sun.javadoc.TypeVariable asTypeVariable() {
        return null;
    }

    public gw.gosudoc.com.sun.javadoc.WildcardType asWildcardType() {
        return null;
    }

    public gw.gosudoc.com.sun.javadoc.ParameterizedType asParameterizedType() {
        return null;
    }

    public AnnotationTypeDoc asAnnotationTypeDoc() {
        return null;
    }

    public gw.gosudoc.com.sun.javadoc.AnnotatedType asAnnotatedType() {
        return null;
    }
}
