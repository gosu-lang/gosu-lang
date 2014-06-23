/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.ITypeIsExpression;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.reflect.java.JavaTypes;


/**
 * Represents a typeis expression in the Gosu grammar:
 * <pre>
 * <i>typeis-expression</i>
 *   &lt;conditional-or-expression&gt; <b>typeis</b> &lt;type-literal&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class TypeIsExpression extends Expression implements ITypeIsExpression
{
  private Expression _lhs;
  private TypeLiteral _rhs;

  /**
   * Base constructor sets type to BooleanType.
   */
  public TypeIsExpression()
  {
    setType( JavaTypes.pBOOLEAN() );
  }

  /**
   * @return The expression for the left-hand-side operand.
   */
  public Expression getLHS()
  {
    return _lhs;
  }

  /**
   * @param e The expression for the left-hand-side operand.
   */
  public void setLHS( Expression e )
  {
    _lhs = e;
  }

  /**
   * @return The expression for the right-hand-side operand.
   */
  public TypeLiteral getRHS()
  {
    return _rhs;
  }

  /**
   * @param e The expression for the right-hand-side operand.
   */
  public void setRHS( TypeLiteral e )
  {
    _rhs = e;
  }

  /**
   * Perform a type (or instanceof) comparison.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return getLHS().toString()
           + " typeis "
           + getRHS().toString();
  }

}
