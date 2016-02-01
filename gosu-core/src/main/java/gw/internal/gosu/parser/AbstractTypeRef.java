/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.*;
import gw.lang.reflect.module.IModule;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This is what the proxy methods look like.
 *
 *<pre><b>
 *public ITypeLoader getTypeLoader() {
 *  _reload();
 *  IType itype;
 *  try {
 *    itype = (IType) _getType();
 *  } catch (ClassCastException classcastexception) {
 *    throw new RuntimeException((new StringBuilder("Type interface changed.  Expected gw.internal.gosu.parser.IGosuClassInternal for ")).append(_getTypeNameInternal()).toString(), classcastexception);
 *  }
 *  return (ITypeLoader) itype.getTypeLoader();
 *}
 *</b></pre>
 */
public abstract class AbstractTypeRef extends ITypeRef implements Serializable
{
  transient private String _typeName;
  transient volatile protected IType _type;
  transient private IModule _module;
  transient private ITypeLoader _loader;
  transient private String _pureGenericTypeName;
  transient private IType _componentType;
  transient private IType[] _typeParameters;
  transient private boolean _bParameterized;
  transient private int _mdChecksum;
  transient private volatile int _hashCode = Integer.MIN_VALUE;
  transient volatile private boolean _bStale;
  transient volatile private boolean _bReloading;
  transient private boolean _deleted;
  transient private boolean _bReloadable = true;

  private void writeObject(ObjectOutputStream os) throws IOException {
    os.writeObject(_getTypeName());
    os.writeBoolean(_bParameterized);
    os.writeObject(_componentType == null ? null : _componentType.getName());
    os.writeObject(_pureGenericTypeName);
    os.writeInt(_typeParameters == null ? 0 : _typeParameters.length);
    if (_typeParameters != null) {
      for (int i = 0; i < _typeParameters.length; i++) {
        os.writeObject(_typeParameters[i].getName());
      }
    }
  }

  private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
    _module = TypeSystem.getGlobalModule();
    _typeName = (String) in.readObject();
    _bParameterized = in.readBoolean();
    String componentName = (String) in.readObject();
    if (componentName != null) {
      _componentType = TypeLord.parseType(componentName, TypeVarToTypeMap.EMPTY_MAP);
    }
    _pureGenericTypeName = (String) in.readObject();
    int paramsLength = in.readInt();
    _typeParameters = new IType[paramsLength];
    for (int i = 0; i < paramsLength; i++) {
      final String fullyQualifiedName = (String) in.readObject();
      _typeParameters[i] = TypeLord.parseType(fullyQualifiedName, TypeVarToTypeMap.EMPTY_MAP);
    }
    _mdChecksum = TypeSystem.getRefreshChecksum();
  }

  public Object readResolve() throws ObjectStreamException
  {
    if( _type instanceof AbstractTypeRef )
    {
      return _type;
    }
    return this;
  }

  void _setType( IType type )
  {
    if( type == null )
    {
      throw new TypeMayHaveBeenDeletedException( "Failed to re-resolve type " + _typeName, this );
    }
    if( type instanceof AbstractTypeRef )
    {
      throw new TypeResolveException( "Proxying a proxy is not permitted: " + _type.getClass().getName() );
    }
    if( type.isDiscarded() )
    {
      throw new TypeResolveException( type.getName() + " has been discarded and cannot be referenced" );
    }

    if( _type != null && _type != type )
    {
      _type.setDiscarded( true );
    }

    if( _type != type ||
        _bStale ||
        _mdChecksum != TypeSystem.getRefreshChecksum() )
    {
      TypeSystem.lock();
      try
      {
        _type = type;
        _module = type.getTypeLoader().getModule();
        _loader = type.getTypeLoader();
        _typeName = _type.getName();
        _bParameterized = _type.isParameterizedType();
        if( _bParameterized )
        {
          _pureGenericTypeName = TypeLord.getPureGenericType(_type).getName();
          _typeParameters = _type.getTypeParameters();
        }
        if( _type.isArray() )
        {
          _componentType = _type.getComponentType();
        }
        _bStale = false;
        _deleted = false;
        _mdChecksum = TypeSystem.getRefreshChecksum();
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  final public boolean isStale()
  {
    // Pcf fragments can't be re-resolved.  If this flag is set, the type should never be reported as stale
    if (!isReloadable()) {
      return false;
    }

    if( _componentType != null && !(_componentType instanceof INonLoadableType) )
    {
      return false;
    }

    boolean bStale = false;
    if( _type == null || (_mdChecksum != TypeSystem.getRefreshChecksum() /*&& !isDefaultType()*/))
    {
      bStale = true;
    }
    if( bStale || _bStale )
    {
      // Do NOT reload any types if we are clearing cache
      bStale = !getModule().getModuleTypeLoader().getTypeRefFactory().isClearing();
    }
    return bStale;
  }

  public boolean isReloadable() {
    return _bReloadable;
  }
  public void setReloadable(boolean bReloadable) {
    _bReloadable = bReloadable;
  }

  public IModule getModule() {
    IModule envModule = _module.getExecutionEnvironment().getModule( _module.getName() );
    if (_module != null && envModule != _module) {
      throw new TypeResolveException("This is rather tragic. The module once existed, now it cannot be found." +
                                     "\nProject: " + _module.getExecutionEnvironment().getProject().getNativeProject() +
                                     "\nOld module: " + _module  +
                                     "\nNew module: " + envModule +
                                     "\nType: " + _typeName );
    }
    return envModule;
  }

  public ITypeLoader getTypeLoaderDirectly() {
    return _loader;
  }

  public void _setStale(RefreshKind refreshKind)
  {
    // Fragments and any blocks or inner classes thereof, can't be re-resolved.
    // If this flag is set, _setStale() is therefor a no-op
    if (!isReloadable()) {
      return;
    }

    if( _componentType != null &&
        !(_componentType instanceof INonLoadableType) &&
        refreshKind != RefreshKind.DELETION)
    {
      return;
    }

    _type = null;
    _bStale = true;

    if (refreshKind == RefreshKind.DELETION) {
      _deleted = true;
    }
  }

  public void unloadTypeInfo()
  {
    if( _type == null )
    {
      return;
    }

    _getType().unloadTypeInfo();
  }

  public boolean equals( Object obj )
  {
    if (isDeleted()) {
      return this == obj;
    }

    _reload();

    if( obj instanceof ITypeRef )
    {
      AbstractTypeRef ref2 = (AbstractTypeRef) obj;
      return this == obj ||
             // handle odd case where two separate type refs have the same type
             (!ref2.isDeleted() && _getType() == ref2._getType()) ||
             // Shadowing support. Allow types with same name override each other. Note we call _getTypeName() first to make this bail out faster because _getTypeNameLong() is slow and this is a hotspot
             (!ref2.isDeleted() && _getTypeName().equals( ref2._getTypeName() ) && _getTypeNameLong().equals( ref2._getTypeNameLong()));
    }

    IType type = _getType();
    return _type != null && type.equals( obj );
  }

  public int hashCode()
  {
    if( _hashCode == Integer.MIN_VALUE )
    {
      synchronized( this )
      {
        if( _hashCode == Integer.MIN_VALUE )
        {
          //int sysHash = System.identityHashCode( this );
          int nameHash = _getTypeNameLong().hashCode();
          if( nameHash == Integer.MIN_VALUE )
          {
            nameHash = Integer.MAX_VALUE;
          }
          _hashCode = nameHash;
        }
      }
    }
    return _hashCode;
  }

  public boolean _shouldReload()
  {
    return isStale();
  }

  final protected IType _getType()
  {
    checkNotDeleted();
    IType type = _type;
    while( type == null )
    {
      type = _type;
      if( type == null && !_bReloading )
      {
        TypeSystem.lock();
        try
        {
          if( _type == null && !_bReloading )
          {
            _bReloading = true;
            try
            {
              _resolveType();
            }
            finally
            {
              _bReloading = false;
            }
          }
          type = _type;
        }
        finally
        {
          TypeSystem.unlock();
        }
      }
    }
    return type;
  }

  public boolean isDeleted() {
    return _deleted;
  }

  private void checkNotDeleted() {
    if (_deleted) {
      throw new TypeResolveException("This type has been deleted.");
    }
  }

  public Class<? extends IType> _getClassOfRef()
  {
    return _getType().getClass();
  }

  final protected void _reload()
  {
    checkNotDeleted();
    if( !isStale() )
    {
      return;
    }

    TypeSystem.lock();
    try
    {
      if( !isStale() )
      {
        return;
      }

      if( !_bReloading )
      {
        _bReloading = true;
        try
        {
          _resolveType();
        }
        finally
        {
          _bReloading = false;
          _bStale = false;
        }
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void _resolveType()
  {
    checkNotDeleted();
    IType type = null;
    String strType = _getTypeName();

    if (strType == null) {
      throw new TypeResolveException("This is rather tragic, how are we to resolve a reference to a type with no name, huh?");
    }

    if( strType.startsWith( ".Proxy" ) )
    {
      strType = "$" + strType.substring( 1 );
    }

    IModule module = getModule();
    if( _type == null && _componentType != null )
    {
      TypeSystem.pushModule(module);
      try {
        type = _componentType.getArrayType();
      } finally {
        TypeSystem.popModule(module);
      }
    }
    if( (_type == null && _bParameterized) || (_type != null && _type.isParameterizedType()) )
    {
      if( _type == null )
      {
        TypeSystem.pushModule(module);
        try {
          IType theType = TypeSystem.getByFullNameIfValid(_pureGenericTypeName);
          if (theType == null) {
            throw new TypeMayHaveBeenDeletedException("This is rather tragic. The generic type was once valid, now it cannot be found: " + _typeName, this);
          }
          type = theType.getParameterizedType(_typeParameters);
        } finally {
          TypeSystem.popModule(module);
        }
      }
      else
      {
        TypeSystem.pushModule(module);
        try {
          type = TypeLord.getPureGenericType(_type).getParameterizedType( _type.getTypeParameters() );
        } finally {
          TypeSystem.popModule(module);
        }
      }
    }

    if( type == null )
    {
      type = TypeSystem.getByFullNameIfValid( strType, module );
    }

    if( type instanceof AbstractTypeRef )
    {
      // Note TypeRefFactory.create() reassigns the type if a type ref already exists, which must be the case here
      // i.e., the fact that we are resolving in this type ref handler is proof that a type ref already exists for the type.
      // So _type should already be assigned... but just to be safe...
      IType rawType = ((AbstractTypeRef)type)._getType(); // Must get type irrespective of call depth because we are assigning it here
      if( _type != rawType )
      {
        _setType( rawType );
        _mdChecksum = TypeSystem.getRefreshChecksum();
      }
    }
    else
    {
      _setType( type );
      _mdChecksum = TypeSystem.getRefreshChecksum();
    }

    if( _type == null )
    {
      throw new TypeResolveException("This is rather tragic. The type was once valid, now it cannot be found: " + _typeName);
    }
  }

  public String _getTypeName() {
    return _type != null ? _type.getName() : _typeName;
  }

  public String _getTypeNameLong() {
    return _type != null ? TypeLord.getNameWithBoundQualifiedTypeVariables(_type, false) : _typeName;
  }

  public int _getIndexForSortingFast(String key) {
    if(_type == null) {
      return _typeName.length();
    }

    if(key.endsWith("]")) {
      return 5;
    } else {
      int a = key.indexOf("<");
      if(a != -1) {
        return a + 2;
      }
    }
    return key.length();
  }

  public boolean isDiscarded()
  {
    return false;
  }

  public void setDiscarded( boolean bDiscarded )
  {
  }

  /**
   * This method is called reflectively.
   */
  public String _getTypeNameInternal() {
    return _typeName;
  }

  public boolean isTypeRefreshedOutsideOfLock(IType type) {
    return !isStale() && _type != null && _type != type;
  }

}
