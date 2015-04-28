/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.parser.ScriptPartId;

/**
 */
public class ParameterizedDynamicPropertySymbol extends DynamicPropertySymbol
{
  private DynamicPropertySymbol _delegate;

  public ParameterizedDynamicPropertySymbol( DynamicPropertySymbol dpsDelegate, IGosuClassInternal owner )
  {
    super( dpsDelegate );
    _delegate = dpsDelegate;
    setModifierInfo( _delegate.getModifierInfo() );
    assignPossibleParametarizedGetterAndSetter( owner );
    setType( getActualType( dpsDelegate.getType(), owner ) );
    setScriptPart( new ScriptPartId( owner, null ) );
  }

  public DynamicPropertySymbol getDelegate()
  {
    return _delegate;  
  }

  private void assignPossibleParametarizedGetterAndSetter( IGosuClassInternal owner )
  {
    if( owner == null || !owner.isParameterizedType() )
    {
      return;
    }

    if( _dfsGetter != null && _dfsGetter.getGosuClass() != owner )
    {
      _dfsGetter = _dfsGetter.getParameterizedVersion( owner );
    }

    if( _dfsSetter != null && _dfsSetter.getGosuClass() != owner )
    {
      _dfsSetter = _dfsSetter.getParameterizedVersion( owner );
    }
  }

  private IType getActualType( IType propType, IGosuClassInternal ownerType )
  {
    if( ownerType.isParameterizedType() )
    {
      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( ownerType, ownerType );
      propType = TypeLord.getActualType( propType, actualParamByVarName, true );
    }
    return propType;
  }

  @Override
  public IReducedDynamicPropertySymbol createReducedSymbol() {
    return new ReducedParameterizedDynamicPropertySymbol(
            (ReducedDynamicPropertySymbol) getDelegate().createReducedSymbol(), this);
  }
}
