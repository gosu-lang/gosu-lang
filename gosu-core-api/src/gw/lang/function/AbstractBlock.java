/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

import gw.lang.parser.IBlockClass;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.TypeSystem;

public abstract class AbstractBlock implements IBlock
{
  @Override
  public IBlockExpression getParsedElement()
  {
    IBlockClass type = (IBlockClass)getIntrinsicType();
    return type.getBlock();
  }

  @Override
  public IFunctionType getFunctionType()
  {
    return (IFunctionType)getParsedElement().getType();
  }

  @Override
  public String toString() {
    return getParsedElement().toString();
  }

  @Override
  public IType getIntrinsicType()
  {
    return TypeSystem.get( getClass() );
  }
}
