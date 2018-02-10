/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.AbstractType;
import gw.lang.reflect.DefaultNonLoadableArrayType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import manifold.api.type.TypeName;

/**
 */
public class NamespaceType extends AbstractType implements INamespaceType
{
  private String _strFqNamespace;
  private String _strRelativeNamespace;
  private String _strParent;
  private boolean _bDiscarded;
  private IModule _module;
  private Set<TypeName> _children;

  public NamespaceType(String strFqNamespace, IModule module)
  {
    _strFqNamespace = strFqNamespace;
    _module = module;
    assignRelativePackageAndParent();
  }

  private void assignRelativePackageAndParent()
  {
    int iLastDot = _strFqNamespace.lastIndexOf( '.' );
    if( iLastDot >= 0 )
    {
      _strRelativeNamespace = _strFqNamespace.substring( iLastDot + 1 );
      _strParent = _strFqNamespace.substring( 0, iLastDot );
    }
    else
    {
      _strRelativeNamespace = _strFqNamespace;
      _strParent = "";
    }
  }

  public String getName()
  {
    return _strFqNamespace;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return _strRelativeNamespace;
  }

  public String getNamespace()
  {
    return _strParent;
  }

  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  public boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    return EMPTY_TYPE_ARRAY;
  }

  public INamespaceType getSupertype()
  {
    return getEnclosingType();
  }

  public INamespaceType getEnclosingType()
  {
    if( _strParent == null )
    {
      return null;
    }

    return TypeSystem.getNamespace( _strParent );
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return true;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public GenericTypeVariable[] getGenericTypeVariables()
  {
    return GenericTypeVariable.EMPTY_TYPEVARS;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return IType.EMPTY_ARRAY;
  }

  public Set<? extends IType> getAllTypesInHierarchy()
  {
    return Collections.singleton( this );
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public IType getArrayType()
  {
    return new DefaultNonLoadableArrayType( this, JavaTypes.OBJECT().getBackingClassInfo(), getTypeLoader() );
  }

  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException();
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return null;
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return 0;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    return type == this;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    return null;
  }

  public void unloadTypeInfo()
  {
  }

  public Object readResolve() throws ObjectStreamException
  {
    return TypeSystem.getNamespace( _strFqNamespace );
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType()
  {
    return false;
  }

  public Set<IType> getCompoundTypeComponents()
  {
    return null;
  }

  @Override
  public String toString() {
    return _strFqNamespace; 
  }

  @Override
  public Set<TypeName> getChildren(IType whosaskin) {
    if (_children == null) {
      Set<TypeName> allNames = new HashSet<TypeName>();
      allNames.addAll(TypeSystem.getGlobalModule().getModuleTypeLoader().getTypeNames(_strFqNamespace));
      for (IModule module : _module.getModuleTraversalList()) {
        if (module != TypeSystem.getGlobalModule()) {
          allNames.addAll(module.getModuleTypeLoader().getTypeNames(_strFqNamespace));
        }
      }
      _children = allNames;
    }
    return _children;
  }

  @Override
  public IModule getModule() {
    return _module;
  }

}
