/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;

import java.util.Map;

/**
 */
public class ReducedParameterizedDynamicPropertySymbol extends ReducedDynamicPropertySymbol
{
  private ReducedDynamicPropertySymbol _delegate;

  public ReducedParameterizedDynamicPropertySymbol(ReducedDynamicPropertySymbol delegate, DynamicPropertySymbol dpsDelegate)
  {
    super( dpsDelegate);
    _delegate = delegate;
    //setModifierInfo( dpsDelegate.getModifierInfo() );
//    getGosuClass().getParseInfo().setModifierInfo(delegate._gosuPropertyInfo, dpsDelegate.getModifierInfo());
    setType(getActualType( dpsDelegate.getType(), dpsDelegate.getGosuClass() ));
//    setScriptPart( new ScriptPartId( owner, null ) );
  }

  public ReducedDynamicPropertySymbol getDelegate()
  {
    return _delegate;  
  }

  private IType getActualType( IType propType, IGosuClassInternal ownerType )
  {
    if( ownerType.isParameterizedType() )
    {
      TypeVarToTypeMap actualParamByVarName =TypeLord.mapTypeByVarName( ownerType, ownerType, true );
      propType = TypeLord.getActualType( propType, actualParamByVarName, true );
    }
    return propType;
  }
}