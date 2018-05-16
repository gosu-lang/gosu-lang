/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IFile;
import gw.internal.gosu.annotations.AnnotationMap;
import gw.lang.StrictGenerics;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.InnerClassCapableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaBackedTypeData;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LockingLazyVar;
import gw.util.concurrent.LocklessLazyVar;
import gw.util.perf.objectsize.IObjectSizeFilter;
import gw.util.perf.objectsize.ObjectSize;
import gw.util.perf.objectsize.ObjectSizeUtil;
import gw.util.perf.objectsize.UnmodifiableArraySet;

import java.beans.MethodDescriptor;
import java.io.InvalidClassException;
import java.io.ObjectStreamException;
import gw.util.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 */
class JavaType extends InnerClassCapableType implements IJavaTypeInternal
{
  //
  // Persistent fields. See readResolve()
  //
  private String _strName;
  private IType[] _typeParams;

  //
  // Non-persistent fields
  //
  transient protected IJavaClassInfo _classInfo;
  transient private ITypeInfo _typeInfo;
  transient private String _strRelativeName;
  transient private String _strSimpleName;
  transient volatile private Set<IType> _allTypesInHierarchy; //!! Do NOT make this a lazy var, it's init needs to be re-entrant
  transient private boolean _bArray;
  transient private boolean _bPrimitive;
  transient private LockingLazyVar<Boolean> _bHasSuperType = new LockingLazyVar<Boolean>() {
    @Override
    protected Boolean init() {
      return _classInfo.getSuperclass() != null &&
          //## hack: avoid cyclic refs from creating AnnotationClass
          !_classInfo.getName().equals( AnnotationMap.class.getName() );
    }
  };
  transient volatile private IType _superType;  //!! Do NOT make this a lazy var, it's init needs to be re-entrant
  transient private List<IType> _tempInterfaces;
  transient volatile private IType[] _interfaces; //!! Do NOT make this a lazy var, it's init needs to be re-entrant
  transient private LocklessLazyVar<IFunctionType> _functionalInterface;
  transient private IGosuClassInternal _adapterClass;
  transient private GenericTypeVariable[] _tempGenericTypeVars;
  transient private LockingLazyVar<GenericTypeVariable[]> _lazyGenericTypeVars = LockingLazyVar.make( this::assignGenericTypeVariables );
  transient private boolean _bDefiningGenericTypes;
  transient private ConcurrentMap<String, IJavaTypeInternal> _parameterizationByParamsName;
  transient volatile private IJavaTypeInternal _arrayType;
  private IJavaTypeInternal _componentType;
  transient private DefaultTypeLoader _typeLoader;
  transient private boolean _bDoesNotHaveExplicitTypeInfo;
  private Class<?> _explicitTypeInfoClass;
  transient private boolean _bDiscarded;
  private IJavaTypeInternal _typeRef;
  transient private int _tiChecksum;
  transient volatile private List<IJavaType> _innerClasses;
  transient private Boolean _bStrictGenerics;

  public static IJavaTypeInternal get( Class cls, DefaultTypeLoader loader )
  {
    IJavaType type = TYPES_BY_CLASS.get( cls );

    if( type == null || ((ITypeRef)type)._shouldReload() )
    {
      TypeSystem.lock();
      try
      {
        type = TYPES_BY_CLASS.get( cls );
        if( type == null || ((ITypeRef)type)._shouldReload() )
        {
          type = define( cls, loader );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return (IJavaTypeInternal)type;
  }

  public static IJavaType getPrimitiveType( String strPrimitiveClassName )
  {
    return TypeLoaderAccess.PRIMITIVE_TYPES_BY_NAME.get().get(strPrimitiveClassName);
  }

  private static IJavaTypeInternal define( Class cls, DefaultTypeLoader loader )
  {
    IJavaTypeInternal type;
    if( cls.isEnum() )
    {
      JavaType rawType = new JavaEnumType( new ClassJavaClassInfo(cls, loader.getModule()), loader );
      type = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( rawType );
      rawType._typeRef = type;
    }
    else
    {
      JavaType rawType = new JavaType( new ClassJavaClassInfo(cls, loader.getModule()), loader );
      IJavaTypeInternal extendedType = JavaTypeExtensions.maybeExtendType(rawType);
      type = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( extendedType );
      rawType._typeRef = type;
    }
    TYPES_BY_CLASS.put( cls, type );
    return type;
  }

  public static IJavaTypeInternal create(IJavaClassInfo cls, DefaultTypeLoader loader)
  {
    IJavaTypeInternal type;
    if( cls.isEnum() )
    {
      JavaType rawType = new JavaEnumType( cls, loader );
      type = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( rawType );
      rawType._typeRef = type;
    }
    else
    {
      JavaType rawType = new JavaType( cls, loader );
      IJavaTypeInternal extendedType = JavaTypeExtensions.maybeExtendType(rawType);
      type = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( extendedType );
      rawType._typeRef = type;
    }
    return type;
  }

  public boolean isDefiningGenericTypes()
  {
    return _bDefiningGenericTypes;
  }

  public GenericTypeVariable[] assignGenericTypeVariables()
  {
    if( _typeParams != null && _typeParams.length > 0 )
    {
      return assignTypeVarsFromTypeParams( _typeParams );
    }
    else if( _classInfo.getTypeParameters().length > 0 )
    {
      _bDefiningGenericTypes = true;
      try
      {
        assignGenericTypeVarPlaceholders();
        return GenericTypeVariable.convertTypeVars( thisRef(), null, _classInfo.getTypeParameters() );
      }
      finally
      {
        _bDefiningGenericTypes = false;
      }
    }
    else
    {
      return GenericTypeVariable.EMPTY_TYPEVARS;
    }
  }
  
  private void assignGenericTypeVarPlaceholders()
  {
    _tempGenericTypeVars = new GenericTypeVariable[_classInfo.getTypeParameters().length];
    final int n = _classInfo.getTypeParameters().length;
    for( int i = 0; i < n; i++ )
    {
      _tempGenericTypeVars[i] = new GenericTypeVariable( String.valueOf( 'A' ) + i, JavaTypes.OBJECT() );
    }
  }

  public JavaType(IJavaClassInfo cls, DefaultTypeLoader loader) {
    init(cls, loader);
    _strName = computeQualifiedName();
  }

  private void init(IJavaClassInfo cls, DefaultTypeLoader loader) {
    _classInfo = cls;
    _bArray = cls.isArray();
    _bPrimitive = cls.isPrimitive();
    _typeLoader = loader;
    _tiChecksum = TypeSystem.getSingleRefreshChecksum();
    _strName = computeQualifiedName();
    _functionalInterface =
        LocklessLazyVar.make( () -> {
          if( !isInterface() )
          {
            return null;
          }
          if( (isGenericType() || isParameterizedType()) &&
              getName().startsWith( "java." ) &&
              !getTypeInfo().hasAnnotation( JavaTypes.FUNCTIONAL_INTERFACE() ) )
          {
            // Avoid mistaking some Java interfaces for functional interfaces e.g., Comparable, Iterable, etc.
            return null;
          }

          return FunctionToInterfaceCoercer.getRepresentativeFunctionType( getTheRef() );
        } );
  }

  JavaType( Class cls, DefaultTypeLoader loader )
  {
    init(TypeSystem.getJavaClassInfo(cls, loader.getModule()), loader);
    _strName = computeQualifiedName();
  }

  private JavaType( IJavaClassInfo cls, IType[] typeParams, DefaultTypeLoader loader )
  {
    init(cls, loader);
    _typeParams = typeParams;
    _strName = computeQualifiedName();
  }

  private JavaType( IJavaClassInfo arrayClass, IJavaTypeInternal componentType, DefaultTypeLoader loader )
  {
    init(arrayClass, loader);
    _componentType = componentType;
    _strName = computeQualifiedName();
  }

  public JavaType(IJavaClassInfo classInfo, DefaultTypeLoader loader, IType[] typeParams) {
    init(classInfo, loader);
    _typeParams = typeParams;
    _strName = computeQualifiedName();
  }

  /**
   * Note a gosu class can be BOTH parameterzied AND generic. For example,
   * <p/>
   * class Bar<T> {
   * function blah() : T {...}
   * }
   * class Foo<T extends CharSequence> extends Bar<T> {}
   * <p/>
   * The class Bar<T> here is parameterized by the type var from Foo, yet it is
   * still a generic class. The blah() method in Foo's typeinfo must have a
   * return type consistent with Foo's type var upper bound, CharSequence.
   *
   * //## todo: maybe we don't need this concept any longer? i.e., parameterization should work correctly regardless.
   * @param typeParams type parameters
   * @return generic type variables
   */
  private GenericTypeVariable[] assignTypeVarsFromTypeParams( IType[] typeParams )
  {
    List<GenericTypeVariable> genTypeVars = new ArrayList<GenericTypeVariable>();
    for( IType typeParam : typeParams )
    {
      if( typeParam instanceof TypeVariableType )
      {
        genTypeVars.add( (GenericTypeVariable)((TypeVariableType)typeParam).getTypeVarDef().getTypeVar() );
      }
    }
    return genTypeVars.toArray( new GenericTypeVariable[genTypeVars.size()] );
  }

  public DefaultTypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  public String getName()
  {
    return _strName;
  }

  String computeQualifiedName() {
    if (isArray()) {
      IType cType = getComponentType();
      return cType.getName() + "[]";
    } else {
      String strName = _classInfo.getName().replace('$', '.');
      if (isParameterizedType()) {
        strName += TypeLord.getNameOfParams(_typeParams, false, false);
      }
      return strName;
    }
  }

  public String getDisplayName()
  {
    return CommonServices.getEntityAccess().getLocalizedTypeName(thisRef());
  }

  public String getRelativeName()
  {
    if( _strRelativeName != null )
    {
      return _strRelativeName;
    }

    if( isArray() )
    {
      return getComponentType().getRelativeName() + "[]";
    }

    String strQualifiedClassName = _classInfo.getName();
    int iDotIndex = strQualifiedClassName.lastIndexOf( '.' );
    String strRelativeName = strQualifiedClassName.substring( iDotIndex + 1 ).replace( '$', '.' );

    if( isParameterizedType() )
    {
      strRelativeName += TypeLord.getNameOfParams( _typeParams, true, false );
    }

    return _strRelativeName = strRelativeName;
  }

  public String getSimpleName()
  {
    if( _strSimpleName != null )
    {
      return _strSimpleName;
    }

    if( isArray() )
    {
      return getComponentType().getSimpleName() + "[]";
    }

    String strQualifiedClassName = _classInfo.getName();
    strQualifiedClassName = strQualifiedClassName.replace( '$', '.' );
    int iDotIndex = strQualifiedClassName.lastIndexOf( '.' );
    String strSimpleName = strQualifiedClassName.substring( iDotIndex + 1 );

    if( isParameterizedType() )
    {
      strSimpleName += TypeLord.getNameOfParams( _typeParams, true, false );
    }

    return _strSimpleName = strSimpleName;
  }

  public String getNamespace()
  {
    return _classInfo.getNamespace();
  }

  public boolean isArray()
  {
    return _bArray;
  }

  public boolean isPrimitive()
  {
    return _bPrimitive;
  }

  public Object makeArrayInstance( int iLength )
  {
    Class intrinsicClass = getIntrinsicClass();
    if (intrinsicClass == null) {
      int i = 0;
    }
    return Array.newInstance(intrinsicClass, iLength );
  }

  public boolean isAssignableFrom( IType type )
  {
    if (TypeSystem.isDeleted(type)) {
      return false;
    }

    IType pThis = thisRef();

    // Short-circuit if the types are the same
    if( type == pThis )
    {
      return true;
    }

    // Short-circuit if the given type is null
    if( type == null )
    {
      return false;
    }

    // Everything is assignable from Object, except for primitives
    if( getBackingClassInfo().getName().equals( Object.class.getName() ) )
    {
      return type == JavaTypes.pVOID() || !type.isPrimitive();
    }

    if( isArray() && type.isArray() )
    {
      // Note an array of primitives and an array of non-primitives are never assignable
      return getComponentType().isPrimitive() == type.getComponentType().isPrimitive() &&
             getComponentType().isAssignableFrom( type.getComponentType() );
    }

    if( isArray() )
    {
      return false;
    }

    if( isInterface() && type instanceof IGosuClass && ((IGosuClass)type).isStructure() )
    {
      // A structure *not* implicitly assignable to an interface because there is no hierarchy, just a structure
      // (force an explicit cast if the runtime type is expected to directly implement the interface)
      return false;
    }

    final Set<? extends IType> allTypesInHierarchy = type.getAllTypesInHierarchy();
    @SuppressWarnings({"SuspiciousMethodCalls"})
    boolean isAssignable = allTypesInHierarchy != null && allTypesInHierarchy.contains(pThis);
    if( !isAssignable )
    {
      isAssignable = TypeLord.areGenericOrParameterizedTypesAssignable( pThis, type );
    }
    return isAssignable || isAssignableFromJavaBackedType(type);
  }

  private boolean isAssignableFromJavaBackedType(IType type) {
    //HACK! for now we assume that the entity info is class-based so we only
    // do this if this class info is also class-based 
    boolean isAssignable = false;
    if( !(type instanceof IJavaTypeInternal) && type instanceof IJavaBackedTypeData && !(type instanceof IGosuFragment))
    {
      // Handle case e.g., where we are comparing an entity type with a java type, 
      // we want the backing class of the entity to be assignable to the java class 
      // if the java classes are assignable
      IJavaClassInfo backingClassInfo = ((IJavaBackedTypeData) type).getBackingClassInfo();
      isAssignable = backingClassInfo != null && getBackingClassInfo().isAssignableFrom(backingClassInfo);
    }
    return isAssignable;
  }

  /**
   * Note eventhough some classes are indeed immutable (e.g. java.lang.String)
   * there's no such info in a java class, so we default to mutable.
   */
  public boolean isMutable()
  {
    return true;
  }

  public Class getIntrinsicClass()
  {
    return _classInfo.getBackingClass();
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return _classInfo;
  }

  public Class getBackingClass()
  {
    return getIntrinsicClass();
  }

  public ITypeInfo getTypeInfo()
  {
    if( ExecutionMode.isIDE() && TypeSystem.getJreModule() != TypeSystem.getGlobalModule() ) {
      // Enforce Guidwewire's legacy type shadowing rules where, for example, a
      // type in an App module such as PX shadows a type in PL having the same name.
      // This isn't kosher in general because the type in PL is the one that is
      // referenced and resolved in PL and the one for which the code was designed.
      // But we have a history of allowing modules to shadow types by name, allowing
      // for additional features and behavior.
      if (!getTypeLoader().getModule().equals(TypeSystem.getCurrentModule())) {
        final IType reResolveByFullName = TypeSystem.getByFullNameIfValid(getName());
        if (reResolveByFullName!=null && !equals(reResolveByFullName)) {
          return reResolveByFullName.getTypeInfo();
        }
      }
    }

    ITypeInfo typeInfo = _typeInfo;
    if( typeInfo == null || hasAncestorBeenUpdated() )
    {
      TypeSystem.lock();
      try
      {
        typeInfo = _typeInfo;
        if( typeInfo == null || hasAncestorBeenUpdated() )
        {
          typeInfo = loadTypeInfo();
          _typeInfo = typeInfo;
          if( !isParameterizedType() && _adapterClass != null &&
              haveAncestorsBeenUpdated(this, _adapterClass.getTypeInfoChecksum(), new HashSet<IType>()) )
          {
            _adapterClass = createAdapterClass();
          }
          _tiChecksum = TypeSystem.getSingleRefreshChecksum();
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    
    return typeInfo;
  }

  public IGosuClassInternal getAdapterClass()
  {
    if( isParameterizedType() )
    {
      return getGenericType().getAdapterClass();
    }

    if( _adapterClass == null )
    {
      return null;
    }

    if( _adapterClass.isStale())
    {
      _adapterClass = createAdapterClass();
      return _adapterClass;
    }
                                        // must check against adapter's checksum
    if( haveAncestorsBeenUpdated( this, _adapterClass.getTypeInfoChecksum(), new HashSet<IType>() ) )
    {
      _typeInfo = null;   // clear the typeinfo
      _tiChecksum = TypeSystem.getSingleRefreshChecksum(); // and update the checksum (the type info will be created on next request)
      _adapterClass = createAdapterClass();  // recreate the adapter class
    }
    return _adapterClass;
  }

  public IGosuClassInternal getAdapterClassDirectly()
  {
    return _adapterClass;
  }

  private ITypeInfo loadTypeInfo()
  {
    ITypeInfo typeInfo = getExplicitTypeInfo();
    if( typeInfo == null )
    {
      typeInfo = convertToTypeInfo();
    }
    return typeInfo;
  }

  public void unloadTypeInfo()
  {
    TypeSystem.lock();
    try
    {
      if( _parameterizationByParamsName != null )
      {
        for( IJavaType parameteredClass : new ArrayList<IJavaType>(_parameterizationByParamsName.values()) )
        {
          parameteredClass.unloadTypeInfo();
        }
        _parameterizationByParamsName.clear();
      }
      if( _adapterClass != null )
      {
        _adapterClass.unloadTypeInfo();
      }
      if( _arrayType != null )
      {
        _arrayType.unloadTypeInfo();
      }
      _lazyGenericTypeVars.clearNoLock();

      _allTypesInHierarchy = null;
      _typeInfo = null;
    }
    finally
    {
      TypeSystem.unlock();
    }

  }

  public boolean isInterface()
  {
    return _classInfo.isInterface() || _classInfo.isAnnotation();
  }

  public boolean isEnum()
  {
    return _classInfo.isEnum();
  }

  public IType[] getInterfaces()
  {
    if( _interfaces != null )
    {
      return _interfaces;
    }

    TypeSystem.lock();
    try
    {
      if( _interfaces != null )
      {
        return _interfaces;
      }

      boolean bReentered = _tempInterfaces != null;
      if( !bReentered )
      {
        _tempInterfaces = new ArrayList<IType>();
      }

      ArrayList<IType> interfaces;
      IJavaClassType[] genericInterfaces = _classInfo.getGenericInterfaces();
      if( genericInterfaces.length == 0 )
      {
        interfaces = EMPTY_TYPE_LIST;
      }
      else
      {
        IJavaClassInfo[] notParameterizedInterfaces = _classInfo.getInterfaces();
        if (notParameterizedInterfaces.length != genericInterfaces.length) {
          throw new RuntimeException("There should be as many generic interface as non-parameterized interfaces.");
        }
        interfaces = new ArrayList<IType>( genericInterfaces.length );
        for( int i = _tempInterfaces.size(); i < genericInterfaces.length; i++ )
        {
          IJavaClassType interfaceType = genericInterfaces[i];
          if( interfaceType == null || notParameterizedInterfaces[i] == null )
          {
            continue;
          }

          IType notParameterizedInterface = notParameterizedInterfaces[i].getJavaType();
          if (notParameterizedInterface == null) {
            throw new NullPointerException("notParameterizedInterface java type is null for " + notParameterizedInterfaces[i]);
          }
          _tempInterfaces.add( notParameterizedInterface );
          if( !isParameterizedType() && interfaceType instanceof IJavaClassInfo && !notParameterizedInterface.isGenericType() )
          {
            interfaces.add( notParameterizedInterface );
          }
          else
          {
            TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( thisRef(), thisRef() );
            if( actualParamByVarName.isEmpty() )
            {
              actualParamByVarName = TypeLord.mapTypeByVarName( notParameterizedInterface, notParameterizedInterface );
            }
            IType parameterizedIface = interfaceType.getActualType( actualParamByVarName, true );
            if( parameterizedIface.isGenericType() && !parameterizedIface.isParameterizedType() ) {
              parameterizedIface = TypeLord.getDefaultParameterizedType( parameterizedIface );
            }
            interfaces.add( parameterizedIface );
            _tempInterfaces.remove( notParameterizedInterface );
            _tempInterfaces.add( parameterizedIface );
          }
        }
      }
      if( bReentered )
      {
        return _tempInterfaces.toArray( new IType[_tempInterfaces.size()] );
      }
      _tempInterfaces = null;
      interfaces.trimToSize();
      _interfaces = interfaces.toArray(new IType[interfaces.size()]);
    }
    finally
    {
      TypeSystem.unlock();
    }
    return _interfaces;
  }

  public IType getSupertype()
  {
    if( !_bHasSuperType.get() )
    {
      return null;
    }

    if( _superType != null )
    {
      return notDeletedSupertype();
    }

    TypeSystem.lock();
    try
    {
      if( _superType != null )
      {
        return notDeletedSupertype();
      }

      IJavaClassInfo superclass = _classInfo.getSuperclass();
      IType notParameterizedSuperType;
      if( superclass != null )
      {
        IType superType = superclass.getJavaType();
        if( superType instanceof IErrorType )
        {
          return TypeSystem.getErrorType();
        }
        notParameterizedSuperType = superType;
      }
      else if( _classInfo != null )
      {
        notParameterizedSuperType = TypeSystem.get( _classInfo.getSuperclass() );
      }
      else
      {
        // ?
        notParameterizedSuperType = JavaTypes.OBJECT();
      }

      // Temporarily set supertype to raw type to short-circuit re-entrant calls to this method (for recursive types)
      _superType = notParameterizedSuperType;

      IJavaClassType genericSuperclass = _classInfo.getGenericSuperclass();
      if( genericSuperclass instanceof IJavaClassInfo )
      {
        if( _classInfo.isEnum() ) {
          // JavaSourceType doesn't give us the generic superclass of an enum, so we make up for that here
          _superType = _superType.getParameterizedType( thisRef() );
        }

        // Super is not generic, we're done
        return notDeletedSupertype();
      }

      // Get fully parameterized version of generic supertype...

      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( thisRef(), thisRef() );
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = TypeLord.mapTypeByVarName( notParameterizedSuperType, notParameterizedSuperType );
      }
      if(genericSuperclass != null) {
        _superType = genericSuperclass.getActualType( actualParamByVarName, true );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
    return notDeletedSupertype();
  }

  private IType notDeletedSupertype() {
    if (TypeSystem.isDeleted(_superType)) {
      _superType = TypeSystem.getErrorType();
    }
    //## todo: this seems unnecessary
    // Ensure we return a non-raw generic type here
    return _superType.isGenericType() && !_superType.isParameterizedType()
           ? TypeLord.getDefaultParameterizedType( _superType )
           : _superType;
  }

  public List<IJavaType> getInnerClasses()
  {
    if( _innerClasses != null ) {
      return _innerClasses;
    }

    IJavaClassInfo[] innerClasses = getBackingClassInfo().getDeclaredClasses();
    if( innerClasses.length == 0 )
    {
      return _innerClasses = Collections.emptyList();
    }

    List<IJavaType> inners = new ArrayList<IJavaType>( 2 );
    for( IJavaClassInfo c : innerClasses )
    {
      String name = c.getName().replace('$', '.');
      IJavaType inner = _typeLoader.getInnerType(name);
      if (inner == null) {
        throw new IllegalStateException("Cannot load inner class " + name);
      }
      inners.add(inner);
    }
    return _innerClasses = inners;
  }

  @Override
  public IType getInnerClass( CharSequence name )
  {
    if( name == null )
    {
      return null;
    }
    String strRelativeName = name.toString();
    int dotIndex = strRelativeName.indexOf( '.' );
    IJavaTypeInternal innerClass;
    if( dotIndex == -1 )
    {
      innerClass = getInnerClassSimple( strRelativeName );
    }
    else
    {
      innerClass = getInnerClassSimple( strRelativeName.substring( 0, dotIndex ) );
      if( innerClass != null )
      {
        innerClass = (IJavaTypeInternal)innerClass.getInnerClass( strRelativeName.substring( dotIndex + 1, strRelativeName.length() ) );
      }
    }
    return innerClass;
  }

  private IJavaTypeInternal getInnerClassSimple( String simpleName )
  {
    IJavaTypeInternal enclosingType = this;
    for( IJavaType javaType : enclosingType.getInnerClasses() )
    {
      if( ((IJavaTypeInternal)javaType).getSimpleName().equals( simpleName ) )
      {
        return (IJavaTypeInternal)javaType;
      }
    }
    return null;
  }
  
  @Override
  public ISourceFileHandle getSourceFileHandle() {
    return _classInfo.getSourceFileHandle();
  }

  @Override
  public List<? extends IType> getLoadedInnerClasses() {
    return getInnerClasses();
  }

  @Override
  public ClassType getClassType() {
    return ClassType.JavaClass;
  }

  public IType getEnclosingType()
  {
    return _classInfo.getEnclosingType();
  }

  public IJavaTypeInternal getGenericType()
  {
    if( isParameterizedType() )
    {
      return (IJavaTypeInternal)TypeSystem.get( _classInfo, getTypeLoader().getModule() );
    }
    else if( isGenericType() )
    {
      return thisRef();
    }
    else
    {
      return null;
    }
  }

  public boolean isFinal()
  {
    return Modifier.isFinal( _classInfo.getModifiers() );
  }

  public boolean isParameterizedType()
  {
    return _typeParams != null && _typeParams.length > 0;
  }

  public boolean isGenericType()
  {
    return _bDefiningGenericTypes || (_lazyGenericTypeVars.get() != null && _lazyGenericTypeVars.get().length > 0);
  }

  public GenericTypeVariable[] getGenericTypeVariables()
  {
    if( !isGenericType() )
    {
      return null;
    }

    if (_bDefiningGenericTypes) {
      return _tempGenericTypeVars;
    }

    return _lazyGenericTypeVars.get();
  }

  public IType[] getTypeParameters()
  {
    return _typeParams;
  }

  public IType getParameterizedType( IType... paramTypes )
  {
    if( paramTypes == null || paramTypes.length == 0 )
    {
      throw new IllegalArgumentException( "Parameter types required." );
    }

    if( isParameterizedType() )
    {
      return TypeLord.getPureGenericType( thisRef() ).getParameterizedType( paramTypes );
    }

    if( _parameterizationByParamsName == null )
    {
      TypeSystem.lock();
      try
      {
        if( _parameterizationByParamsName == null )
        {
          _parameterizationByParamsName = new ConcurrentHashMap<String, IJavaTypeInternal>( 2 );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    paramTypes = TypeSystem.boxPrimitiveTypeParams( paramTypes );
    String strNameOfParams = TypeLord.getNameOfParams( paramTypes, false, true, true );
    IJavaTypeInternal parameterizedType = _parameterizationByParamsName.get( strNameOfParams );
    if( parameterizedType == null )
    {
      TypeSystem.lock();
      try
      {
        parameterizedType = _parameterizationByParamsName.get( strNameOfParams );
        if( parameterizedType == null )
        {
          if( _classInfo != null )
          {
            parameterizedType = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( new JavaType( _classInfo, paramTypes, _typeLoader ) );
          }
          else
          {
            parameterizedType = (IJavaTypeInternal)TypeSystem.getOrCreateTypeReference( new JavaType( _classInfo, getTypeLoader(), paramTypes ) );
          }
          _parameterizationByParamsName.put( strNameOfParams, parameterizedType );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return parameterizedType;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    if( _allTypesInHierarchy != null )
    {
      return _allTypesInHierarchy;
    }
    TypeSystem.lock();
    try
    {
      if( _allTypesInHierarchy != null )
      {
        return _allTypesInHierarchy;
      }

      if( _classInfo.isArray() )
      {
        Set<IType> types = TypeLord.getAllClassesInClassHierarchyAsIntrinsicTypes( _classInfo );
        types.addAll( new HashSet<IType>( TypeLord.getArrayVersionsOfEachType( getComponentType().getAllTypesInHierarchy() ) ) );
        _allTypesInHierarchy = Collections.unmodifiableSet( types );
      }
      else
      {
        _allTypesInHierarchy = TypeLord.getAllClassesInClassHierarchyAsIntrinsicTypes( _classInfo );
        Set<IType> includeGenericTypes = new HashSet<IType>( _allTypesInHierarchy );
        addGenericTypes( thisRef(), includeGenericTypes );
        _allTypesInHierarchy = Collections.unmodifiableSet( includeGenericTypes );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
    return _allTypesInHierarchy;
  }

  private void addGenericTypes( IType type, Set<IType> includeGenericTypes)
  {
    if( type == null )
    {
      return;
    }

    if( type.isGenericType() || type.isParameterizedType() )
    {
      TypeLord.addAllClassesInClassHierarchy( type, includeGenericTypes, true );
    }
    else
    {
      addGenericTypes(type.getSupertype(), includeGenericTypes);
      IType[] interfaces = type.getInterfaces();
      if( interfaces != null )
      {
        for( IType iface: interfaces )
        {
          addGenericTypes( iface, includeGenericTypes );
        }
      }
    }
  }

  private IJavaTypeInternal thisRef()
  {
    if (_typeRef == null) {
      ITypeRef ref = TypeSystem.getOrCreateTypeReference(this);
      try {
        _typeRef = (IJavaTypeInternal) ref;
      } catch(ClassCastException e) {
        throw new RuntimeException("Could not reresolve the java type " + this.getName() + ".\n" +
                "The actual type of the reference was " + ref.getClass(), e);
      }
    }
    return _typeRef;
  }

  public IType getArrayType()
  {
    if( _arrayType == null )
    {
      TypeSystem.lock();
      try
      {
        if( _arrayType == null )
        {
          IJavaTypeInternal thisRef = thisRef();
          IModule module = getTypeLoader().getModule();
          if( module != null )
          {
            TypeSystem.pushModule( getTypeLoader().getModule() );
          }
          try
          {
            boolean bParameterized = TypeLord.getCoreType( thisRef ).isParameterizedType();
            if(_classInfo != null) {
              IJavaClassInfo arrayClass = _classInfo.getArrayType();
              if( bParameterized )
              {
                _arrayType = new JavaType( arrayClass, thisRef, _typeLoader ).thisRef();
              }
              else
              {
                _arrayType = create( arrayClass, _typeLoader );
                _arrayType.setComponentType( thisRef );
              }
            } else {
              if(bParameterized) {
                _arrayType = new JavaType(_classInfo.getArrayType(), getTypeLoader(), getTypeParameters()).thisRef();
              } else {
                _arrayType = new JavaType(_classInfo.getArrayType(), getTypeLoader()).thisRef();
              }
            }
          }
          finally
          {
            if( module != null )
            {
              TypeSystem.popModule( module );
            }
          }
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _arrayType;
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return Array.get( array, iIndex );
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    try {
      Array.set( array, iIndex, value );
    } catch (IllegalArgumentException ex) {
      HashSet<Class> interfaces = new HashSet<Class>();
      if (value != null) {
        getInterfaces(value.getClass(), interfaces);
      }
      throw new IllegalArgumentException("array element type mismatch.  Expected an element of type " +
              (array == null ? "<null>" : array.getClass()) + " got " +
              interfaces);
    }
  }

  private void getInterfaces(Class clazz, Set<Class> classes) {
    if (clazz == null || classes.contains(clazz)) {
      return;
    }
    classes.add(clazz);
    getInterfaces(clazz.getSuperclass(), classes);
    for (Class aClass : clazz.getInterfaces()) {
      getInterfaces(aClass, classes);
    }
  }
  
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return Array.getLength( array );
  }

  public IJavaTypeInternal getComponentType()
  {
    IModule module = getTypeLoader().getModule();
    TypeSystem.pushModule( module );
    try
    {
      if (isArray()) {
        if (_componentType == null) {
          IType type = TypeSystem.get(_classInfo.getComponentType());
          _componentType = (IJavaTypeInternal) type;
        }
        return _componentType;
      } else {
        return null;
      }
    }
    finally
    {
      TypeSystem.popModule( module );
    }
  }
  public void setComponentType( IJavaTypeInternal componentType )
  {
    _componentType = componentType;
  }

  @Override
  public int getTypeInfoChecksum() {
    if( isParameterizedType() )
    {
      return getGenericType().getTypeInfoChecksum();
    }
    return _tiChecksum;
  }

  @Override
  public boolean hasAncestorBeenUpdated() {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return false;
    }
    return haveAncestorsBeenUpdated(this, _tiChecksum, new HashSet<IType>());
  }

  private static boolean haveAncestorsBeenUpdated(IJavaTypeInternal type, int tiChecksum, Set<IType> visited) {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return false;
    }
    final IType supertype = type.getSupertype();
    if (supertype instanceof IJavaTypeInternal && !visited.contains(supertype) && hasBeenUpdated((IJavaTypeInternal) supertype, tiChecksum, visited)) {
      return true;
    }
    for (IType anInterface : type.getInterfaces()) {
      if (anInterface instanceof IJavaTypeInternal) {
        final IJavaTypeInternal iFace = (IJavaTypeInternal) anInterface;
        if (!visited.contains(iFace) && hasBeenUpdated(iFace, tiChecksum, visited)) {
          return true;
        }
      } else {
        // delegate types can be interfaces of Java types apparently !
      }
    }
    return false;
  }

  public static boolean hasBeenUpdated(IJavaTypeInternal type, int tiChecksum, Set<IType> visited) {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return false;
    }
    visited.add(type);
    if (TypeSystem.isDeleted(type)) {
      return true;
    } else {
      return type.getTypeInfoChecksum() > tiChecksum || haveAncestorsBeenUpdated(type, tiChecksum, visited);
    }
  }

  public Object writeReplace()
  {
    // Force _strName to initialize
    getName();
    return this;
  }

  public Object readResolve() throws ObjectStreamException
  {
    try
    {
      int iIndex = _strName.indexOf( '<' );
      if( iIndex > 0 )
      {
        String strGenName = _strName.substring( 0, iIndex );
        IType type = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( strGenName );
        return type.getParameterizedType( _typeParams );
      }
      else
      {
        return TypeLoaderAccess.instance().getIntrinsicTypeByFullName( _strName );
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      // Hack attempt to temporarily get around error with pcf parsed elements map containing classes not available in studio e.g., org.jgroups.ChannelClosedException
      if( _strName.endsWith( "Exception" ) )
      {
        System.out.println( "Exception subclass not found: " + _strName + ". Substituting with Exception.class." );
        return JavaTypes.EXCEPTION();
      }
      throw new InvalidClassException( _strName, cnfe.getMessage() );
    }
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return _classInfo.getModifiers();
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( _classInfo.getModifiers() );
  }

  @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
  public boolean equals( Object o )
  {
    // Because these are privately constructed and cached...
    return thisRef() == o;
  }

  public int hashCode()
  {
    return _classInfo.hashCode();
  }

  public String toString()
  {
    return getName();
  }

  private static Set<String> SPECIAL_TYPE_INFO = new HashSet<String>() {{
    add("gw.lang.ScriptParameters");
    add("com.guidewire.studio.api.automation.IAutomatable");
    add("gw.datatype.gw.datatype.DataTypes");
    add("gw.i18n.ILocale");
    add("gw.datatype.DataTypes");
  }};

  public ITypeInfo getExplicitTypeInfo()
  {
    if(_bDoesNotHaveExplicitTypeInfo)
    {
      return null;
    }

    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // Don't lookup explicit beaninfo in standard Gosu
      _bDoesNotHaveExplicitTypeInfo = true;
      return null;
    }

    String name = getName();
    if (!Character.isLetter(name.charAt(name.length() - 1))) {
      _bDoesNotHaveExplicitTypeInfo = true;
      return null;
    }
    try
    {
      if( SPECIAL_TYPE_INFO.contains(name) )
      {
        if( _explicitTypeInfoClass == null )
        {
          _explicitTypeInfoClass = CommonServices.getEntityAccess().getPluginClassLoader().loadClass(name + "TypeInfo");
        }
        return (ITypeInfo)_explicitTypeInfoClass.newInstance();
      }
      _bDoesNotHaveExplicitTypeInfo = true;
      return null;
    }
    catch( Throwable t )
    {
      if( t instanceof ClassNotFoundException )
      {
        _bDoesNotHaveExplicitTypeInfo = true;
        return null;
      }
      else
      {
        throw new RuntimeException( t );
      }
    }
  }

  private ITypeInfo convertToTypeInfo(  )
  {
    return new JavaTypeInfo( thisRef(), _classInfo);
  }

  public void setAdapterClass( IGosuClassInternal adapterClass )
  {
    if( isParameterizedType() )
    {
      getGenericType().setAdapterClass( adapterClass );
      return;
    }
    _adapterClass = adapterClass;
  }

  public IGosuClassInternal createAdapterClass()
  {
    if( isParameterizedType() )
    {
      return (IGosuClassInternal)getGenericType().createAdapterClass();
    }
    _adapterClass = GosuClassProxyFactory.instance().createImmediately( thisRef() );
    return _adapterClass;
  }

  public static IJavaType[] convertClassArray( Class[] args )
  {
    IJavaType[] returnArray = new IJavaType[args.length];
    for( int i = 0; i < args.length; i++ )
    {
      returnArray[i] = (IJavaType) TypeSystem.get( args[i] );
    }
    return returnArray;
  }

  public static void unloadTypes()
  {
    Collection<IJavaType> types = TYPES_BY_CLASS.values();
    for( IJavaType type : types )
    {
      type.unloadTypeInfo();
    }
    TYPES_BY_CLASS.clear();
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
  public IJavaClassInfo getConcreteClass() {
    return _classInfo;
  }

  @Override
  public IType getTypeFromJavaBackedType() {
    return TypeSystem.getTypeFromJavaBasedType(thisRef());
  }

  @Override
  public ObjectSize getRetainedMemory() {
    return ObjectSizeUtil.deepSizeOf(this, new IObjectSizeFilter() {
      @Override
      public boolean skipField(Field field) {
        return
                field.getType().equals(Class.class) ||
                field.getType().equals(MethodDescriptor.class) ||
                field.getName().equals("_typeLoader") ||
                field.getName().equals("_container")
            ;
      }

      @Override
      public boolean skipObject(Object obj) {
        Class<? extends Object> objClass = obj.getClass();
        return
            objClass.equals(Class.class) ||
            (obj instanceof IType && obj != JavaType.this) ||
            (obj instanceof IModule) ||
            (obj.getClass().getName().startsWith("org.eclipse")) ||
            (obj.getClass().getName().startsWith("java.lang.reflect")) ||
            (obj.getClass().getName().startsWith("java.beans")) 
            || (obj instanceof IJavaClassInfo && obj != JavaType.this._classInfo)
            ;
      }
    }, 100000);
  }

  @Override
  public IType[] getLoaderParameterizedTypes() {
    if (_parameterizationByParamsName != null) {
      Collection<IJavaTypeInternal> values = _parameterizationByParamsName.values();
      return values.toArray(new IType[values.size()]);
    } else {
      return IType.EMPTY_ARRAY;
    }
  }

  public boolean isStrictGenerics()
  {
    if( _bStrictGenerics != null )
    {
      return _bStrictGenerics;
    }

    return _bStrictGenerics = getBackingClass() != null && Arrays.stream( getBackingClass().getAnnotations() )
      .anyMatch( anno -> anno.getClass() == StrictGenerics.class );
  }

  @Override
  public IFile[] getSourceFiles() {
    if (getSourceFileHandle() == null) {
      return IFile.EMPTY_ARRAY;
    } else {
      return new IFile[] {getSourceFileHandle().getFile()};
    }
  }

  @Override
  public boolean isAnnotation() {
    return _classInfo.isAnnotation();
  }

  @Override
  public IFunctionType getFunctionalInterface()
  {
    return _functionalInterface.get();
  }

  @Override
  protected IType getTheRef() {
    return thisRef();
  }

  @Override
  public boolean isCompilable()
  {
    return getBackingClassInfo().isCompilable();
  }

  @Override
  public byte[] compile()
  {
    return getBackingClassInfo().compile();
  }
}
