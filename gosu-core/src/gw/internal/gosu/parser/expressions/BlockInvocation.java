/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;

import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IBlockInvocation;
import gw.lang.reflect.IBlockType;
import gw.util.GosuStringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Gosu block invocation expression.
 */
public class BlockInvocation extends Expression implements IBlockInvocation
{
  private IExpression _root;
  private List<IExpression> _args;
  private int[] _namedArgOrder;


  public BlockInvocation( IExpression root )
  {
    _root = root;
    _args = Collections.emptyList();
  }

  public void setArgs( List<IExpression> args )
  {
    _args = args;
  }

  public List<IExpression> getArgs()
  {
    return _args;
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
    return _root.toString() + "(" + GosuStringUtil.join( _args, ", " ) + ")";
  }


  public IExpression getRoot()
  {
    return _root;
  }
  
  public IBlockType getBlockType() {
    return (IBlockType) _root.getType();
  }

  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
  public void setNamedArgOrder( int[] namedArgOrder )
  {
    _namedArgOrder = namedArgOrder;
  }
}