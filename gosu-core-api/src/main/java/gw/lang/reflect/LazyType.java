/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.internal.gosu.parser.StringCache;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.TypeVarToTypeMap;
import gw.util.concurrent.LocklessLazyVar;

public class LazyType extends LocklessLazyVar<IType>
{
  private final CharSequence _typeName;
  private final ITypeUsesMap _typeUsesMap;

  public LazyType( String typeName )
  {
    _typeName = StringCache.get(typeName);
    _typeUsesMap = null;
  }

  public LazyType( CharSequence typeName, ITypeUsesMap typeUsesMap )
  {
    _typeName = typeName instanceof String ? StringCache.get((String)typeName) : typeName;
    _typeUsesMap = typeUsesMap;
  }

  public LazyType( IType entryType )
  {
    super();
    _typeName = entryType.getName();
    _typeUsesMap = null;
    initDirectly( entryType );
  }

  public String getName()
  {
    return _typeName.toString();
  }

  public String getNameFromType()
  {
    String retValue;
    try
    {
      IType type = get();
      if( type instanceof IMetaType )
      {
        retValue = "Type";
      }
      else
      {
        retValue = type.getName();
      }
    }
    catch( Exception e )
    {
      retValue = getName();
    }
    return retValue;
  }

  protected IType init()
  {
    try
    {
      return getType(_typeName.toString(), _typeUsesMap);
    }
    catch( ClassNotFoundException e )
    {
      return TypeSystem.getErrorType();
    }
  }

  private static IType getType(String strType, ITypeUsesMap _typeUsesMap) throws ClassNotFoundException {
    if( strType.contains( "<" ) || strType.startsWith( Keyword.KW_block.toString() )  )
    {
      TypeSystem.lock();
      try {
        return TypeSystem.parseType(strType, new TypeVarToTypeMap(), _typeUsesMap);
      } finally {
        TypeSystem.unlock();
      }
    }
    else if ( strType.startsWith( "entity." ) )
    {
      return TypeSystem.getByFullName( strType );
    }
    else if (strType.endsWith("[]")) {
      String baseTypeName = strType.substring(0, strType.length() - 2);
      IType baseType = getType(baseTypeName, _typeUsesMap);
      return (baseType == null) ? null : baseType.getArrayType();
    }
    else {
      return _typeUsesMap == null
              ? TypeSystem.getByRelativeName( strType )
              : TypeSystem.getByRelativeName( strType, _typeUsesMap );
    }
  }
}
