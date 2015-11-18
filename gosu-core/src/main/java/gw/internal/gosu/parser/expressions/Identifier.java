/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.CapturedSymbol;
import gw.internal.gosu.parser.DynamicPropertySymbol;
import gw.internal.gosu.parser.DynamicSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.ScopedDynamicSymbol;
import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.internal.gosu.parser.statements.VarStatement;


import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.java.ICompileTimeConstantValue;

import java.lang.reflect.Method;


/**
 * Gosu identifier expression.
 */
public class Identifier extends Expression implements IIdentifierExpression
{
  private ISymbol _symbol;


  public Identifier()
  {
  }

  public ISymbol getSymbol()
  {
    return _symbol;
  }

  public void setSymbol( ISymbol symbol, ISymbolTable symTable )
  {
    // Make a copy of the symbol without the value so that the parsed expression
    // doesn't hold onto pointers it shouldn't
    _symbol = symbol.getLightWeightReference();
    if( _symbol.hasDynamicSymbolTable() )
    {
      _symbol.setDynamicSymbolTable( symTable );
    }
  }

  @Override
  public boolean isCompileTimeConstant()
  {
//## todo: reverting change 537145 to see if this is what is crushing suites in TH (causing OutOfMemoryError with permgen)
    ISymbol symbol = getSymbol();
    return (!(symbol instanceof DynamicSymbol) &&
            !(symbol instanceof DynamicPropertySymbol) &&
            !symbol.isLocal()) ||
            isStaticFinalInitializedCompileTimeConstant();
//
//    return symbol.isStatic() && symbol.isFinal();
  }


  public boolean isStaticFinalInitializedCompileTimeConstant()
  {
    ISymbol symbol = getSymbol();
    if ( symbol instanceof DynamicSymbol && symbol.isStatic() && symbol.isFinal() )
    {
      IGosuClassInternal gsClass = (IGosuClassInternal)symbol.getGosuClass();
      if ( !gsClass.isValid() )
      {
        return false;
      }
      VarStatement varStmt = gsClass.getStaticField( symbol.getName() );
      if( varStmt != null &&  varStmt.getAsExpression() != null )
      {
        return varStmt.getAsExpression().isCompileTimeConstant() || Modifier.isEnum( varStmt.getModifiers() );
      }
    }
    return false;
  }

  public Object evaluate()
  {
    ISymbol symbol = getSymbol();

    if( isCompileTimeConstant() && symbol instanceof DynamicSymbol )
    {
      IGosuClassInternal gsClass = (IGosuClassInternal)symbol.getGosuClass();
      gsClass.isValid();
      VarStatement varStmt = gsClass.getStaticField( symbol.getName() );
      if( varStmt != null )
      {
        if( Modifier.isEnum( varStmt.getModifiers() ) )
        {
          return varStmt.getIdentifierName();
        }
        return varStmt.getAsExpression().evaluate();
      }
      throw new IllegalStateException( "Should have found field for: " + symbol.getName() );
    }

    IType type = symbol.getType();
    Object value = symbol.getValue();

    if( type instanceof IFunctionType )
    {
      // If this symbol simply wraps another symbol, return the wrapped symbol
      if( value instanceof Symbol )
      {
        return value;
      }
      // If this symbol is a root invokable symbol, return the symbol itself, rather than the value
      if( value instanceof Method || value instanceof IParsedElement )
      {
        return symbol;
      }
    }

    if( symbol instanceof DynamicPropertySymbol )
    {
      DynamicPropertySymbol dps = (DynamicPropertySymbol)symbol;
      IPropertyInfo pi = dps.getPropertyInfo();
      while( pi instanceof IPropertyInfoDelegate )
      {
        pi = ((IPropertyInfoDelegate)pi).getSource();
      }
      return ((ICompileTimeConstantValue)pi).doCompileTimeEvaluation();
    }

    if( symbol instanceof CompileTimeExpressionParser.CompileTimeFieldSymbol )
    {
      return ((ICompileTimeConstantValue)symbol).doCompileTimeEvaluation();
    }
    return value;
  }

  @Override
  public String toString()
  {
    return getSymbol().getName();
  }


  public boolean isLocalVariable() {
    ISymbol symbol = getSymbol();
    while (symbol instanceof CapturedSymbol) {
      symbol =  ((CapturedSymbol)symbol).getReferredSymbol();
    }
    return !(symbol instanceof ScopedDynamicSymbol) &&
           !(symbol instanceof DynamicSymbol) &&
           !(symbol instanceof CapturedSymbol) &&
           symbol.getIndex() >= 0;
  }

}
