/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.GosuObjectFunctionSymbol;
import gw.internal.gosu.parser.CannotExecuteGosuException;


import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.java.JavaTypes;


/**
 */
public final class MethodCallExpression extends Expression implements IMethodCallExpression
{
  private IFunctionSymbol _symbol;
  private Expression[] _args;
  private int _iArgPos;
  private IFunctionType _funcType;
  private int[] _namedArgOrder;


  public MethodCallExpression()
  {
  }

  public IFunctionSymbol getFunctionSymbol()
  {
    return _symbol;
  }

  public void setFunctionSymbol( IFunctionSymbol symbol )
  {
    _symbol = symbol;

    specialHandlingFor_IGosuObject_BasedMethods( symbol );
  }

  private void specialHandlingFor_IGosuObject_BasedMethods( IFunctionSymbol symbol )
  {
    if( symbol.getType() instanceof IFunctionType )
    {
      IFunctionType ft = (IFunctionType)symbol.getType();
      if( ft.getScriptPart() != null &&
          ft.getScriptPart().getContainingType() ==
            IGosuClassInternal.Util.getGosuClassFrom( JavaTypes.IGOSU_OBJECT() ) )
      {
        _symbol = new GosuObjectFunctionSymbol( (IGosuClassInternal)ft.getScriptPart().getContainingType(),
                                                   (DynamicFunctionSymbol)_symbol );
      }
    }
  }

  public Expression[] getArgs()
  {
    return _args;
  }

  public void setArgs( Expression[] args )
  {
    if( args != null && args.length == 0 )
    {
      args = null;
    }
    _args = args;
  }

  public int getArgPosition()
  {
    return _iArgPos;
  }

  public void setArgPosition( int iArgPos )
  {
    _iArgPos = iArgPos;
  }

  public void setFunctionType( IFunctionType funcType )
  {
    _funcType = funcType;
  }

  public IFunctionType getFunctionType()
  {
    return _funcType;
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
    String strOut = (_symbol == null ? "#err" : _symbol.getDisplayName()) + "(";

    if( _args != null && _args.length > 0 )
    {
      strOut += " ";
      for( int i = 0; i < _args.length; i++ )
      {
        if( i != 0 )
        {
          strOut += ", ";
        }
        strOut += _args[i].toString();
      }
      strOut += " ";
    }

    return strOut += ")";
  }

  public boolean isBlockInvocation()
  {
    return _symbol.getType() instanceof IBlockType;
  }

  public boolean isFromJava()
  {
    return _symbol.isFromJava();
  }

  public void setNamedArgOrder( int[] namedArgOrder )
  {
    _namedArgOrder = namedArgOrder;
  }
  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
}
