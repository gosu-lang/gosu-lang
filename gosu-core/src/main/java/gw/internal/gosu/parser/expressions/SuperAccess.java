/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IMapAccessExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;


/**
 * super'[' <type-literal> ']'
 *
 * where <type-literal> must be a type from the class's declared super or interfaces
 */
public final class SuperAccess extends Expression implements IMapAccessExpression
{
  private Identifier _rootExpression;
  private TypeLiteral _keyExpression;

  public Identifier getRootExpression()
  {
    return _rootExpression;
  }

  public void setRootExpression( Identifier rootExpression )
  {
    _rootExpression = rootExpression;
  }

  public TypeLiteral getKeyExpression()
  {
    return _keyExpression;
  }
  public void setKeyExpression( TypeLiteral keyExpression )
  {
    _keyExpression = keyExpression;
  }

  public boolean isNullSafe()
  {
    return false;
  }

  /**
   * Evaluates the expression.
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
    return getRootExpression().toString() + "[" + getKeyExpression().toString() + "]";
  }

  public IType getComponentType()
  {
    return getType();
  }

  public IType getKeyType()
  {
    return JavaTypes.ITYPE();
  }
}
