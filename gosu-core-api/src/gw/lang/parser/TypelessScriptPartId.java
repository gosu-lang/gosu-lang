/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

public class TypelessScriptPartId extends IScriptPartId
{
  private String _strPartId;
  private IGosuClass _runtimeType;

  public TypelessScriptPartId( String strPartId )
  {
    _strPartId = strPartId;
  }

  public IType getContainingType()
  {
    return null;
  }

  public String getId()
  {
    return _strPartId;
  }

  public String toString()
  {
    return _strPartId;
  }

  public String getContainingTypeName()
  {
    return null;
  }

  public void setRuntimeType( IGosuClass runtimeType )
  {
    _runtimeType = runtimeType;
  }
  public IGosuClass getRuntimeType()
  {
    return _runtimeType;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    TypelessScriptPartId that = (TypelessScriptPartId)o;
    return _strPartId.equals( that._strPartId );
  }

  public int hashCode()
  {
    return _strPartId.hashCode();
  }
}
