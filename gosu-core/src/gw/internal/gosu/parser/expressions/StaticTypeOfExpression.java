/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.reflect.IType;
import gw.lang.parser.expressions.IStaticTypeOfExpression;
import gw.internal.gosu.parser.MetaType;

/**
 * Represents a StaticTypeOf expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class StaticTypeOfExpression extends Expression implements IStaticTypeOfExpression
{
  private Expression _expression;

  public StaticTypeOfExpression()
  {
    super();
  }

  @Override
  public IType getTypeImpl()
  {
    return _expression == null ? MetaType.DEFAULT_TYPE_TYPE.get() : MetaType.get( _expression.getType() );
  }

  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression e )
  {
    _expression = e;
    setType( MetaType.get( e.getType() ) );
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public Object evaluate()
  {
    return getExpression().getType();
  }

  @Override
  public String toString()
  {
    return "statictypeof " + getExpression().toString();
  }

}