/*
 * This file is a shadowed version of the older javadoc codebase on which gosudoc is based; borrowed from jdk 9.
 */

package gw.gosudoc.com.sun.tools.javadoc.main;

import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import gw.gosudoc.com.sun.javadoc.ConstructorDoc;

/**
 * Represents a constructor of a java class.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @since 1.2
 * @author Robert Field
 * @author Neal Gafter (rewrite)
 */

@Deprecated
public class ConstructorDocImpl
        extends ExecutableMemberDocImpl implements ConstructorDoc
{

    /**
     * constructor.
     */
    public ConstructorDocImpl( DocEnv env, MethodSymbol sym) {
        super(env, sym);
    }

    /**
     * constructor.
     */
    public ConstructorDocImpl( DocEnv env, MethodSymbol sym, TreePath treePath) {
        super(env, sym, treePath);
    }

    /**
     * Return true if it is a constructor, which it is.
     *
     * @return true
     */
    public boolean isConstructor() {
        return true;
    }

    /**
     * Get the name.
     *
     * @return the name of the member.
     */
    public String name() {
        ClassSymbol c = sym.enclClass();
        return c.name.toString();
    }

    /**
     * Get the name.
     *
     * @return the qualified name of the member.
     */
    public String qualifiedName() {
        return sym.enclClass().getQualifiedName().toString();
    }

    /**
     * Returns a string representation of this constructor.  Includes the
     * qualified signature and any type parameters.
     * Type parameters precede the class name, as they do in the syntax
     * for invoking constructors with explicit type parameters using "new".
     * (This is unlike the syntax for invoking methods with explicit type
     * parameters.)
     */
    public String toString() {
        return typeParametersString() + qualifiedName() + signature();
    }
}
