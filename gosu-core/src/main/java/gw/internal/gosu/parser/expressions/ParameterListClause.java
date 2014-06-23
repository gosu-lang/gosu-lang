/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IParameterListClause;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ParameterListClause extends Expression implements IParameterListClause
{
  public ParameterListClause()
  {
    setType( JavaTypes.pVOID() );
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
