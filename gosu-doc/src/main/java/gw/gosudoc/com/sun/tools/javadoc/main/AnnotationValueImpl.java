/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.tools.javac.code.Attribute;
import gw.gosudoc.com.sun.javadoc.AnnotationValue;

import static com.sun.tools.javac.code.TypeTag.BOOLEAN;

/**
 * Represents a value of an annotation type element.
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
public class AnnotationValueImpl implements gw.gosudoc.com.sun.javadoc.AnnotationValue
{

    private final DocEnv env;
    private final Attribute attr;


    AnnotationValueImpl( DocEnv env, Attribute attr) {
        this.env = env;
        this.attr = attr;
    }

    /**
     * Returns the value.
     * The type of the returned object is one of the following:
     * <ul><li> a wrapper class for a primitive type
     *     <li> <code>String</code>
     *     <li> <code>Type</code> (representing a class literal)
     *     <li> <code>FieldDoc</code> (representing an enum constant)
     *     <li> <code>AnnotationDesc</code>
     *     <li> <code>AnnotationValue[]</code>
     * </ul>
     */
    public Object value() {
        ValueVisitor vv = new ValueVisitor();
        attr.accept(vv);
        return vv.value;
    }

    private class ValueVisitor implements Attribute.Visitor {
        public Object value;

        public void visitConstant(Attribute.Constant c) {
            if (c.type.hasTag(BOOLEAN)) {
                // javac represents false and true as integers 0 and 1
                value = Boolean.valueOf(
                                ((Integer)c.value).intValue() != 0);
            } else {
                value = c.value;
            }
        }

        public void visitClass(Attribute.Class c) {
            value = TypeMaker.getType(env,
                                      env.types.erasure(c.classType));
        }

        public void visitEnum(Attribute.Enum e) {
            value = env.getFieldDoc(e.value);
        }

        public void visitCompound(Attribute.Compound c) {
            value = new AnnotationDescImpl(env, c);
        }

        public void visitArray(Attribute.Array a) {
            gw.gosudoc.com.sun.javadoc.AnnotationValue vals[] = new AnnotationValue[a.values.length];
            for (int i = 0; i < vals.length; i++) {
                vals[i] = new AnnotationValueImpl(env, a.values[i]);
            }
            value = vals;
        }

        public void visitError(Attribute.Error e) {
            value = "<error>";
        }
    }

    /**
     * Returns a string representation of the value.
     *
     * @return the text of a Java language annotation value expression
     *          whose value is the value of this annotation type element.
     */
    @Override
    public String toString() {
        ToStringVisitor tv = new ToStringVisitor();
        attr.accept(tv);
        return tv.toString();
    }

    private class ToStringVisitor implements Attribute.Visitor {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public String toString() {
            return sb.toString();
        }

        public void visitConstant(Attribute.Constant c) {
            if (c.type.hasTag(BOOLEAN)) {
                // javac represents false and true as integers 0 and 1
                sb.append(((Integer)c.value).intValue() != 0);
            } else {
                sb.append( FieldDocImpl.constantValueExpression(c.value));
            }
        }

        public void visitClass(Attribute.Class c) {
            sb.append(c);
        }

        public void visitEnum(Attribute.Enum e) {
            sb.append(e);
        }

        public void visitCompound(Attribute.Compound c) {
            sb.append(new AnnotationDescImpl(env, c));
        }

        public void visitArray(Attribute.Array a) {
            // Omit braces from singleton.
            if (a.values.length != 1) sb.append('{');

            boolean first = true;
            for (Attribute elem : a.values) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                elem.accept(this);
            }
            // Omit braces from singleton.
            if (a.values.length != 1) sb.append('}');
        }

        public void visitError(Attribute.Error e) {
            sb.append("<error>");
        }
    }
}
