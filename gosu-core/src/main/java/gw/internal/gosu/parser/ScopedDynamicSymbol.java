/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IAttributeSource;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IType;

/**
 */
public class ScopedDynamicSymbol extends AbstractDynamicSymbol
{
  private static final String PREFIX = "_gosu_global_";

  private GlobalScope _scope;
  private IAttributeSource _attrSource;
  private String _strAttr;
  private String _typePrefixName;

  public ScopedDynamicSymbol( ISymbolTable symTable, String strName, String typePrefixName, IType type, GlobalScope scope )
  {
    super( symTable, strName, type );
    _typePrefixName = typePrefixName;
    _strAttr = PREFIX + _typePrefixName + strName;
    setScope( scope );
  }

  public GlobalScope getScope()
  {
    return _scope;
  }

  public void setScope( GlobalScope scope )
  {
    if( scope == GlobalScope.EXECUTION )
    {
      throw new IllegalArgumentException( "EXECUTION scope is reserved for non-global scoped symbols." );
    }

    _scope = scope;
    resetAttributeSource();
  }

  public Object getValue()
  {
    return _attrSource.getAttribute( getAttributeName() );
  }

  public Object getValueDirectly()
  {
    return getValue();
  }

  public void setValue( Object value )
  {
    _attrSource.setAttribute( getAttributeName(), value );
  }

  public void setValueDirectly( Object value )
  {
    setValue( value );
  }

  public ISymbol getLightWeightReference()
  {
    ScopedDynamicSymbol copy = new ScopedDynamicSymbol( _symTable, getName(), _typePrefixName, getType(), _scope );
    copy.setScriptPart( getScriptPart() );
    copy.setName( getName() );
    return copy;
  }

  public ScopedDynamicSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    if( !gsClass.isParameterizedType() || !TypeLord.hasTypeVariable( getType() ) )
    {
      return this;
    }

    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( gsClass, gsClass );
    IType type = TypeLord.getActualType( getType(), actualParamByVarName, true );
    return new ScopedDynamicSymbol( _symTable, getName(), _typePrefixName, type, _scope );
  }

  private void resetAttributeSource()
  {
    _attrSource = CommonServices.getEntityAccess().getAttributeSource( _scope );
    if( _attrSource == null )
    {
      throw new IllegalStateException( "No attribute source available for: " + _scope );
    }
  }

  public String getAttributeName()
  {
    return _strAttr + "_" + getType().getName();
  }

  @Override
  public String getFullDescription() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean hasTypeVariables() {
    return false;
  }
}
