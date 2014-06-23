/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.lang.parser.IConstructorFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IConstructorInfo;

public class ThisConstructorFunctionSymbol extends DynamicFunctionSymbol implements IConstructorFunctionSymbol
{
  private DynamicFunctionSymbol _dfsDelegate;

  public ThisConstructorFunctionSymbol( DynamicFunctionSymbol dfsDelegate )
  {
    super( dfsDelegate );
    _dfsDelegate = dfsDelegate;
    setName( getSignatureName( getDisplayName() ) );
  }

  public ISymbol getLightWeightReference()
  {
    return this;
  }

  public String getDisplayName()
  {
    return Keyword.KW_this.toString();
  }

  public MethodCallStatement getInitializer()
  {
    return _dfsDelegate.getInitializer();
  }

  @Override
  public IConstructorInfo getConstructorInfo() {
    return (IConstructorInfo) _dfsDelegate.getMethodOrConstructorInfo();
  }
  
  public IReducedDynamicFunctionSymbol createReducedSymbol() {
    return new ReducedThisConstructorFunctionSymbol(this);
  }

  public DynamicFunctionSymbol getDelegate() {
    return _dfsDelegate;
  }
}
