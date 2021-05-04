/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.util.List;
import gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc;

import static com.sun.tools.javac.code.TypeTag.ARRAY;

/**
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Deprecated
public class TypeMaker {

    public static gw.gosudoc.com.sun.javadoc.Type getType( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Type t) {
        return getType(env, t, true);
    }

    /**
     * @param errToClassDoc  if true, ERROR type results in a ClassDoc;
     *          false preserves legacy behavior
     */
    public static gw.gosudoc.com.sun.javadoc.Type getType( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Type t,
                                                           boolean errorToClassDoc) {
        return getType(env, t, errorToClassDoc, true);
    }

    public static gw.gosudoc.com.sun.javadoc.Type getType( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Type t,
                                                           boolean errToClassDoc, boolean considerAnnotations) {
        try {
            return getTypeImpl(env, t, errToClassDoc, considerAnnotations);
        } catch (CompletionFailure cf) {
            /* Quietly ignore completion failures and try again - the type
             * for which the CompletionFailure was thrown shouldn't be completed
             * again by the completer that threw the CompletionFailure.
             */
            return getType(env, t, errToClassDoc, considerAnnotations);
        }
    }

    @SuppressWarnings("fallthrough")
    private static gw.gosudoc.com.sun.javadoc.Type getTypeImpl( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Type t,
                                                                boolean errToClassDoc, boolean considerAnnotations) {
        if (env.legacyDoclet) {
            t = env.types.erasure(t);
        }

        if (considerAnnotations && t.isAnnotated()) {
            return new AnnotatedTypeImpl(env, t);
        }

        switch (t.getTag()) {
        case CLASS:
            if ( ClassDocImpl.isGeneric((ClassSymbol)t.tsym)) {
                return env.getParameterizedType((ClassType)t);
            } else {
                return env.getClassDoc((ClassSymbol)t.tsym);
            }
        case WILDCARD:
            Type.WildcardType a = (Type.WildcardType)t;
            return new WildcardTypeImpl(env, a);
        case TYPEVAR: return new TypeVariableImpl(env, (TypeVar)t);
        case ARRAY: return new ArrayTypeImpl(env, t);
        case BYTE: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.byteType;
        case CHAR: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.charType;
        case SHORT: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.shortType;
        case INT: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.intType;
        case LONG: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.longType;
        case FLOAT: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.floatType;
        case DOUBLE: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.doubleType;
        case BOOLEAN: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.booleanType;
        case VOID: return gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType.voidType;
        case ERROR:
            if (errToClassDoc)
                return env.getClassDoc((ClassSymbol)t.tsym);
            // FALLTHRU
        default:
            return new gw.gosudoc.com.sun.tools.javadoc.main.PrimitiveType(t.tsym.getQualifiedName().toString());
        }
    }

    /**
     * Convert a list of javac types into an array of javadoc types.
     */
    public static gw.gosudoc.com.sun.javadoc.Type[] getTypes( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, List<Type> ts) {
        return getTypes(env, ts, new gw.gosudoc.com.sun.javadoc.Type[ts.length()]);
    }

    /**
     * Like the above version, but use and return the array given.
     */
    public static gw.gosudoc.com.sun.javadoc.Type[] getTypes( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, List<Type> ts,
                                                              gw.gosudoc.com.sun.javadoc.Type res[]) {
        int i = 0;
        for (Type t : ts) {
            res[i++] = getType(env, t);
        }
        return res;
    }

    public static String getTypeName(Type t, boolean full) {
        switch (t.getTag()) {
        case ARRAY:
            StringBuilder s = new StringBuilder();
            while (t.hasTag(ARRAY)) {
                s.append("[]");
                t = ((ArrayType)t).elemtype;
            }
            s.insert(0, getTypeName(t, full));
            return s.toString();
        case CLASS:
            return ClassDocImpl.getClassName((ClassSymbol)t.tsym, full);
        default:
            return t.tsym.getQualifiedName().toString();
        }
    }

    /**
     * Return the string representation of a type use.  Bounds of type
     * variables are not included; bounds of wildcard types are.
     * Class names are qualified if "full" is true.
     */
    static String getTypeString( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Type t, boolean full) {
        // TODO: should annotations be included here?
        switch (t.getTag()) {
        case ARRAY:
            StringBuilder s = new StringBuilder();
            while (t.hasTag(ARRAY)) {
                s.append("[]");
                t = env.types.elemtype(t);
            }
            s.insert(0, getTypeString(env, t, full));
            return s.toString();
        case CLASS:
            return ParameterizedTypeImpl.
                        parameterizedTypeToString(env, (ClassType)t, full);
        case WILDCARD:
            Type.WildcardType a = (Type.WildcardType)t;
            return WildcardTypeImpl.wildcardTypeToString(env, a, full);
        default:
            return t.tsym.getQualifiedName().toString();
        }
    }

    /**
     * Return the formal type parameters of a class or method as an
     * angle-bracketed string.  Each parameter is a type variable with
     * optional bounds.  Class names are qualified if "full" is true.
     * Return "" if there are no type parameters or we're hiding generics.
     */
    static String typeParametersString( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, Symbol sym, boolean full) {
        if (env.legacyDoclet || sym.type.getTypeArguments().isEmpty()) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (Type t : sym.type.getTypeArguments()) {
            s.append(s.length() == 0 ? "<" : ", ");
            s.append( TypeVariableImpl.typeVarToString(env, (TypeVar)t, full));
        }
        s.append(">");
        return s.toString();
    }

    /**
     * Return the actual type arguments of a parameterized type as an
     * angle-bracketed string.  Class name are qualified if "full" is true.
     * Return "" if there are no type arguments or we're hiding generics.
     */
    static String typeArgumentsString( gw.gosudoc.com.sun.tools.javadoc.main.DocEnv env, ClassType cl, boolean full) {
        if (env.legacyDoclet || cl.getTypeArguments().isEmpty()) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (Type t : cl.getTypeArguments()) {
            s.append(s.length() == 0 ? "<" : ", ");
            s.append(getTypeString(env, t, full));
        }
        s.append(">");
        return s.toString();
    }


    private static class ArrayTypeImpl implements gw.gosudoc.com.sun.javadoc.Type
    {

        Type arrayType;

        DocEnv env;

        ArrayTypeImpl( DocEnv env, Type arrayType) {
            this.env = env;
            this.arrayType = arrayType;
        }

        private gw.gosudoc.com.sun.javadoc.Type skipArraysCache = null;

        public gw.gosudoc.com.sun.javadoc.Type getElementType() {
            return TypeMaker.getType(env, env.types.elemtype(arrayType));
        }

        private gw.gosudoc.com.sun.javadoc.Type skipArrays() {
            if (skipArraysCache == null) {
                Type t;
                for (t = arrayType; t.hasTag(ARRAY); t = env.types.elemtype(t)) { }
                skipArraysCache = TypeMaker.getType(env, t);
            }
            return skipArraysCache;
        }

        /**
         * Return the type's dimension information, as a string.
         * <p>
         * For example, a two dimensional array of String returns '[][]'.
         */
        public String dimension() {
            StringBuilder dimension = new StringBuilder();
            for (Type t = arrayType; t.hasTag(ARRAY); t = env.types.elemtype(t)) {
                dimension.append("[]");
            }
            return dimension.toString();
        }

        /**
         * Return unqualified name of type excluding any dimension information.
         * <p>
         * For example, a two dimensional array of String returns 'String'.
         */
        public String typeName() {
            return skipArrays().typeName();
        }

        /**
         * Return qualified name of type excluding any dimension information.
         *<p>
         * For example, a two dimensional array of String
         * returns 'java.lang.String'.
         */
        public String qualifiedTypeName() {
            return skipArrays().qualifiedTypeName();
        }

        /**
         * Return the simple name of this type excluding any dimension information.
         */
        public String simpleTypeName() {
            return skipArrays().simpleTypeName();
        }

        /**
         * Return this type as a class.  Array dimensions are ignored.
         *
         * @return a ClassDocImpl if the type is a Class.
         * Return null if it is a primitive type..
         */
        public gw.gosudoc.com.sun.javadoc.ClassDoc asClassDoc() {
            return skipArrays().asClassDoc();
        }

        /**
         * Return this type as a <code>ParameterizedType</code> if it
         * represents a parameterized type.  Array dimensions are ignored.
         */
        public gw.gosudoc.com.sun.javadoc.ParameterizedType asParameterizedType() {
            return skipArrays().asParameterizedType();
        }

        /**
         * Return this type as a <code>TypeVariable</code> if it represents
         * a type variable.  Array dimensions are ignored.
         */
        public gw.gosudoc.com.sun.javadoc.TypeVariable asTypeVariable() {
            return skipArrays().asTypeVariable();
        }

        /**
         * Return null, as there are no arrays of wildcard types.
         */
        public gw.gosudoc.com.sun.javadoc.WildcardType asWildcardType() {
            return null;
        }

        /**
         * Return null, as there are no annotations of the type
         */
        public gw.gosudoc.com.sun.javadoc.AnnotatedType asAnnotatedType() {
            return null;
        }

        /**
         * Return this type as an <code>AnnotationTypeDoc</code> if it
         * represents an annotation type.  Array dimensions are ignored.
         */
        public AnnotationTypeDoc asAnnotationTypeDoc() {
            return skipArrays().asAnnotationTypeDoc();
        }

        /**
         * Return true if this is an array of a primitive type.
         */
        public boolean isPrimitive() {
            return skipArrays().isPrimitive();
        }

        /**
         * Return a string representation of the type.
         *
         * Return name of type including any dimension information.
         * <p>
         * For example, a two dimensional array of String returns
         * <code>String[][]</code>.
         *
         * @return name of type including any dimension information.
         */
        @Override
        public String toString() {
            return qualifiedTypeName() + dimension();
        }
    }
}
