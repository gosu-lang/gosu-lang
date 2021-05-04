/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Attribute.TypeCompound;
import com.sun.tools.javac.util.List;
import gw.gosudoc.com.sun.javadoc.WildcardType;

/**
 * Implementation of <code>AnnotatedType</code>, which
 * represents an annotated type.
 *
 * @author Mahmood Ali
 * @since 1.8
 */
@Deprecated
public class AnnotatedTypeImpl
        extends gw.gosudoc.com.sun.tools.javadoc.main.AbstractTypeImpl implements gw.gosudoc.com.sun.javadoc.AnnotatedType
{

    AnnotatedTypeImpl( DocEnv env, com.sun.tools.javac.code.Type type) {
        super(env, type);
    }

    /**
     * Get the annotations of this program element.
     * Return an empty array if there are none.
     */
    @Override
    public gw.gosudoc.com.sun.javadoc.AnnotationDesc[] annotations() {
        List<? extends TypeCompound> tas = type.getAnnotationMirrors();
        if (tas == null ||
                tas.isEmpty()) {
            return new gw.gosudoc.com.sun.javadoc.AnnotationDesc[0];
        }
        gw.gosudoc.com.sun.javadoc.AnnotationDesc res[] = new gw.gosudoc.com.sun.javadoc.AnnotationDesc[tas.length()];
        int i = 0;
        for (Attribute.Compound a : tas) {
            res[i++] = new AnnotationDescImpl(env, a);
        }
        return res;
    }

    @Override
    public gw.gosudoc.com.sun.javadoc.Type underlyingType() {
        return TypeMaker.getType(env, type, true, false);
    }

    @Override
    public gw.gosudoc.com.sun.javadoc.AnnotatedType asAnnotatedType() {
        return this;
    }

    @Override
    public String toString() {
        return typeName();
    }

    @Override
    public String typeName() {
        return this.underlyingType().typeName();
    }

    @Override
    public String qualifiedTypeName() {
        return this.underlyingType().qualifiedTypeName();
    }

    @Override
    public String simpleTypeName() {
        return this.underlyingType().simpleTypeName();
    }

    @Override
    public String dimension() {
        return this.underlyingType().dimension();
    }

    @Override
    public boolean isPrimitive() {
        return this.underlyingType().isPrimitive();
    }

    @Override
    public gw.gosudoc.com.sun.javadoc.ClassDoc asClassDoc() {
        return this.underlyingType().asClassDoc();
    }

    @Override
    public gw.gosudoc.com.sun.javadoc.TypeVariable asTypeVariable() {
        return this.underlyingType().asTypeVariable();
    }

    @Override
    public WildcardType asWildcardType() {
        return this.underlyingType().asWildcardType();
    }

    @Override
    public gw.gosudoc.com.sun.javadoc.ParameterizedType asParameterizedType() {
        return this.underlyingType().asParameterizedType();
    }
}
