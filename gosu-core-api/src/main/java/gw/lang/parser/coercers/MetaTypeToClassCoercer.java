/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.IResolvingCoercer;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

public class MetaTypeToClassCoercer extends BaseCoercer implements IResolvingCoercer
{
  private static final MetaTypeToClassCoercer INSTANCE = new MetaTypeToClassCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value instanceof IHasJavaClass )
    {
      return ((IHasJavaClass)value).getBackingClass();
    }
    else
    {
      return coerceValue( typeToCoerceTo, ((IMetaType)value).getType() );
    }
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public IType resolveType( IType target, IType source )
  {
    IType type = source.getTypeParameters()[0];
    if( type instanceof IMetaType )
    {
      type = ((IMetaType)type).getType();
    }

    if( target.getGenericType() == JavaTypes.CLASS() )
    {
      return JavaTypes.CLASS().getParameterizedType( type );
    }
    else
    {
      return source;
    }
  }

  public int getPriority( IType to, IType from )
  {
    return 2;
  }

  public static MetaTypeToClassCoercer instance()
  {
    return INSTANCE;
  }
}
