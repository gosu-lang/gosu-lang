/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeBase;
import gw.util.concurrent.LockingLazyVar;

/**
 */
public class DynamicType extends TypeBase implements IDynamicType
{
  public static final String PKG = "dynamic";
  public static final String RNAME = "Dynamic";
  public static final String QNAME = PKG +'.'+ RNAME;

  private DynamicTypeLoader _typeLoader;
  private LockingLazyVar<DynamicTypeInfo> _typeInfo;
  private ITypeRef _typeRef;


  public DynamicType( DynamicTypeLoader typeLoader )
  {
    _typeLoader = typeLoader;
    _typeInfo =
      new LockingLazyVar<DynamicTypeInfo>()
      {
        @Override
        protected DynamicTypeInfo init()
        {
          return new DynamicTypeInfo( getOrCreateTypeReference() );
        }
      };
  }

  @Override
  public boolean isPlaceholder()
  {
    return true;
  }

  @Override
  public boolean isFinal() {
    return true;
  }

  @Override
  public String getName()
  {
    return QNAME;
  }

  @Override
  public String getRelativeName()
  {
    return RNAME;
  }

  @Override
  public String getNamespace()
  {
    return PKG;
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  @Override
  public IType getSupertype()
  {
    return null;
  }

  @Override
  public IType[] getInterfaces()
  {
    return IType.EMPTY_ARRAY;
  }

  @Override
  public ITypeInfo getTypeInfo()
  {
    return _typeInfo.get();
  }

  @Override
  public IType getComponentType() {
    return getOrCreateTypeReference();
  }

  @Override
  public boolean isAssignableFrom( IType type )
  {
    return !type.isPrimitive();
  }

  ITypeRef getOrCreateTypeReference()
  {
    if( _typeRef == null )
    {
      _typeRef = getTypeLoader().getModule().getModuleTypeLoader().getTypeRefFactory().create( this );
    }
    return _typeRef;
  }
}
