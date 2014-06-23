/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.IType;

import java.lang.reflect.Field;

/**
 */
public class DynamicSymbol extends AbstractDynamicSymbol
{
  public DynamicSymbol( ICompilableType gsClass, ISymbolTable symTable, CharSequence strName, IType type, Object value )
  {
    super( symTable, strName, type, value );
    setScriptPart( new ScriptPartId( gsClass, null ) );
    setClassMember( true );
  }

  public ISymbol getLightWeightReference()
  {
    // We shouldn't need to copy the arguments because they should always have null values
    DynamicSymbol copy = new DynamicSymbol( getGosuClass(), _symTable, getName(), getType(), _value );
    copy.setName( (String)getName() );
    copy.setModifierInfo( getModifierInfo() );
    return copy;
  }

  public DynamicSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    if( !gsClass.isParameterizedType() || !TypeLord.hasTypeVariable( getType() ) )
    {
      return this;
    }

    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( gsClass, gsClass, true );
    IType type = TypeLord.getActualType( getType(), actualParamByVarName, true );
    DynamicSymbol dynamicSymbol = new DynamicSymbol( gsClass, _symTable, getName(), type, getValueDirectly() );
    dynamicSymbol.setModifierInfo( getModifierInfo() );
    return dynamicSymbol;
  }

  @Override
  public Object getValue()
  {
    try
    {
      return getStaticFieldFromBytecode().get( null );
    }
    catch( IllegalAccessException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void setValue( Object value )
  {
    try
    {
      getStaticFieldFromBytecode().set( null, value );
    }
    catch( IllegalAccessException e )
    {
      throw new RuntimeException( e );
    }
  }

  private Field getStaticFieldFromBytecode()
  {
    IGosuClass gsClass = getGosuClass();
    gsClass.isValid();
    try
    {
      return gsClass.getBackingClass().getField( getName() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public String toString()
  {
    return getName() + " : " + getType();
  }

}
