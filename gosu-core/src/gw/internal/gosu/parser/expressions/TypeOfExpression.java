/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.MetaType;
import gw.lang.parser.expressions.ITypeOfExpression;
import gw.lang.reflect.IType;


/**
 * Represents a TypeOf expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class TypeOfExpression extends Expression implements ITypeOfExpression
{
  private Expression _expression;

  public TypeOfExpression()
  {
    super();
  }

  @Override
  public IType getTypeImpl()
  {
    // Note the static type of typeof must always be the raw/defaut metatype, Type<DefaultType>,
    // because it is the least upper bound of all meta types.
    return MetaType.DEFAULT_TYPE_TYPE.get();
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
    return "typeof " + getExpression().toString();
  }

}
