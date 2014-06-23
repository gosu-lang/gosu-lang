/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IGosuClass;
import gw.util.GosuExceptionUtil;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

public class ScriptPartId extends IScriptPartId
{
  private final IType _type;
  private final String _strPartId;
  private final String _typeName;
  private IGosuClass _runtimeType;

  public ScriptPartId( IType type, String strPartId )
  {
    _type = type;
    _typeName = type == null ? null : type.getName();
    _strPartId = strPartId;
  }

  public ScriptPartId( String strFqType, String strPartId )
  {
    IType type = null;
    try
    {
      type = TypeSystem.getByFullName( strFqType );
    }
    catch( RuntimeException e )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      if( GosuExceptionUtil.findExceptionCause( e ) instanceof ClassNotFoundException )
      {
        type = null;
      }
    }
    _type = type;
    _typeName = type == null ? strFqType : type.getName();
    _strPartId = strPartId;
  }

  public void setRuntimeType( IGosuClass runtimeType )
  {
    _runtimeType = runtimeType;
  }
  public IGosuClass getRuntimeType()
  {
    return _runtimeType;
  }

  public String getContainingTypeName()
  {
    return _typeName;
  }

  public IType getContainingType()
  {
    return _type;
  }

  public String getId()
  {
    return _strPartId;
  }

  public String toString()
  {
    if (_type == null) {
      return _typeName; 
    } else {
      return _type.getName();
    }
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

    ScriptPartId that = (ScriptPartId)o;

    if( _strPartId != null ? !_strPartId.equals( that._strPartId ) : that._strPartId != null )
    {
      return false;
    }
    //noinspection RedundantIfStatement
    if( _type != that._type )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = _type != null ? _type.hashCode() : 7;
    result = 31 * result + (_strPartId != null ? _strPartId.hashCode() : 0);
    return result;
  }
}
