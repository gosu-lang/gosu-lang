/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.util.ArrayList;
import java.util.List;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaEnumType;
import gw.util.GosuObjectUtil;

/**
 * Provides an implementation of IEnumeratedType for java enums
 */
public class JavaEnumType extends JavaType implements IJavaEnumTypeInternal, IJavaEnumType
{
  private List<IEnumValue> _enumVals;

  public JavaEnumType( Class cls )
  {
    super( cls );
  }

  public JavaEnumType(IJavaClassInfo cls ) {
    super( cls );
  }

  public IType getEnumType()
  {
    return TypeSystem.getOrCreateTypeReference( this );
  }

  public List<String> getEnumConstants()
  {
    List<String> enumConstants = new ArrayList<String>();
    for( IEnumValue v : getEnumValues() )
    {
      enumConstants.add( v.getCode() );
    }
    return enumConstants;
  }

  public List<IEnumValue> getEnumValues()
  {
    if( _enumVals != null )
    {
      return _enumVals;
    }

    ArrayList<IEnumValue> values = new ArrayList<IEnumValue>();
    Object[] enumConstants = getBackingClassInfo().getEnumConstants();
    for( Object enumConstant : enumConstants )
    {
      if( enumConstant instanceof IEnumValue )
      {
        values.add( (IEnumValue)enumConstant );
      }
      else if( enumConstant instanceof Enum )
      {
        Enum e = (Enum)enumConstant;
        values.add( new EnumAdapter( e ) );
      }
    }
    return _enumVals = values;
  }

  public IEnumValue getEnumValue( String strName )
  {
    for( IEnumValue val : getEnumValues() )
    {
      if( GosuObjectUtil.equals( val.getCode(), strName ) )
      {
        return val;
      }
    }

    return null;
  }

  private class EnumAdapter implements IEnumValue, IGosuObject
  {
    private Enum _enum;

    public EnumAdapter( Enum e )
    {
      _enum = e;
    }

    public String getCode()
    {
      return _enum.name();
    }

    public Object getValue()
    {
      return _enum;
    }

    public int getOrdinal()
    {
      return _enum.ordinal();
    }

    public String getDisplayName()
    {
      return getCode();
    }

    public IType getIntrinsicType()
    {
      return TypeSystem.getOrCreateTypeReference( JavaEnumType.this );
    }
  }
}
