/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IConstructorFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.Keyword;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IConstructorInfo;

/**
 */
public class SuperConstructorFunctionSymbol extends DynamicFunctionSymbol implements IConstructorFunctionSymbol
{
  public SuperConstructorFunctionSymbol( DynamicFunctionSymbol dfs )
  {
    super( dfs );
    setName( getSignatureName( getDisplayName() ) );
  }

  public ISymbol getLightWeightReference()
  {
    return this;
  }

  public String getDisplayName()
  {
    return Keyword.KW_super.toString();
  }

  @Override
  public IConstructorInfo getConstructorInfo()
  {
    return getGosuClass().getTypeInfo().getConstructor( ((FunctionType)getType()).getParameterTypes() );
  }
  
  public IReducedDynamicFunctionSymbol createReducedSymbol() {
    return new ReducedSuperConstructorFunctionSymbol(this);
  }
  
}
