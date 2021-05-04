/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import gw.gosudoc.com.sun.javadoc.Type;

/**
 * ParameterImpl information.
 * This includes a parameter type and parameter name.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Kaiyang Liu (original)
 * @author Robert Field (rewrite)
 * @author Scott Seligman (generics, annotations)
 */
@Deprecated
class ParameterImpl implements gw.gosudoc.com.sun.javadoc.Parameter
{

    private final DocEnv env;
    private final VarSymbol sym;
    private final gw.gosudoc.com.sun.javadoc.Type type;

    /**
     * Constructor of parameter info class.
     */
    ParameterImpl( DocEnv env, VarSymbol sym) {
        this.env = env;
        this.sym = sym;
        this.type = TypeMaker.getType(env, sym.type, false);
    }

    /**
     * Get the type of this parameter.
     */
    public Type type() {
        return type;
    }

    /**
     * Get local name of this parameter.
     * For example if parameter is the short 'index', returns "index".
     */
    public String name() {
        return sym.toString();
    }

    /**
     * Get type name of this parameter.
     * For example if parameter is the short 'index', returns "short".
     */
    public String typeName() {
        return (type instanceof gw.gosudoc.com.sun.javadoc.ClassDoc || type instanceof gw.gosudoc.com.sun.javadoc.TypeVariable)
                ? type.typeName()       // omit formal type params or bounds
                : type.toString();
    }

    /**
     * Returns a string representation of the parameter.
     * <p>
     * For example if parameter is the short 'index', returns "short index".
     *
     * @return type name and parameter name of this parameter.
     */
    public String toString() {
        return typeName() + " " + sym;
    }

    /**
     * Get the annotations of this parameter.
     * Return an empty array if there are none.
     */
    public gw.gosudoc.com.sun.javadoc.AnnotationDesc[] annotations() {
        gw.gosudoc.com.sun.javadoc.AnnotationDesc res[] = new gw.gosudoc.com.sun.javadoc.AnnotationDesc[sym.getRawAttributes().length()];
        int i = 0;
        for (Attribute.Compound a : sym.getRawAttributes()) {
            res[i++] = new AnnotationDescImpl(env, a);
        }
        return res;
    }
}
