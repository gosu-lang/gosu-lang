/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeParameterListClause;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class TypeParameterListClause extends Expression implements ITypeParameterListClause
{
  private ITypeLiteralExpression[] _types;

  public TypeParameterListClause( ITypeLiteralExpression[] types )
  {
    _types = types;
    setType( JavaTypes.pVOID() );
  }

  @Override
  public ITypeLiteralExpression[] getTypes()
  {
    return _types;
  }


  public IType evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return (IType)super.evaluate();
    }

    throw new IllegalStateException();
  }

  @Override
  public String toString()
  {
    return "";
  }

}
