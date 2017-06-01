/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IFile;
import gw.internal.gosu.coercer.FunctionToInterfaceClassGenerator;
import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.internal.gosu.ir.TransformingCompiler;
import gw.internal.gosu.parser.expressions.TypeVariableDefinition;
import gw.internal.gosu.parser.expressions.TypeVariableDefinitionImpl;
import gw.internal.gosu.parser.statements.ClassFileStatement;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.DelegateStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.InternalAPI;
import gw.lang.Returns;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISource;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.PostCompilationAnalysis;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.resources.Res;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.InnerClassCapableType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IEnhancementIndex;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;
import gw.util.StringPool;
import gw.util.concurrent.LockingLazyVar;

import java.io.File;
import java.io.InvalidClassException;
import java.io.ObjectStreamException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class GosuClass extends InnerClassCapableType implements IGosuClassInternal
{
  private static final long serialVersionUID = 5L;

  private String _strFullName;
  private IType[] _typeParams;

  transient protected GosuClassParseInfo _parseInfo;
  transient private String _strNamespace;
  transient private String _strRelativeName;
  transient private GosuClassTypeLoader _typeLoader;
  transient private boolean _bInterface;
  transient private boolean _bStructure;
  transient private boolean _bEnum;
  transient private Map<CharSequence, IGosuClassInternal> _mapInnerClasses;
  transient private volatile Set<IType> _setTypes;
  transient private IType[] _interfaces;
  transient private IType _superType;
  transient private IType _enclosingType;
  transient private IJavaType _proxiedJavaClassInGosuProxy; // only relevant when this GosuClass is a _proxy_
  transient private volatile SoftReference<Class<?>> _javaClass;
  transient private IGosuClassInternal _genericClass;
  transient private Map<String, IGosuClassInternal> _parameterizationByParamsName;
  transient private volatile GosuClassTypeInfo _typeInfo;
  transient private IType _gsArrayClass;
  transient private volatile Boolean _bHasSessionVarStatements;
  transient private int _iMdChecksum;
  transient private int _iTiChecksum;
  transient private CompilationState _compilationState;
  transient private boolean _bEditorParser;
  transient private ISourceFileHandle _sourceFileHandle;
  transient private List<IGosuClassInternal> _subtypes;
  transient private String _description;
  transient private String _defaultConstructorName;
  transient private boolean _bCannotCaptureSymbols;
  transient private Boolean _hasError;
  transient private boolean _bDiscarded;
  transient private boolean _bInitializing;
  transient private List<IGosuClass> _blocks;
  transient private LockingLazyVar<Boolean> _valid;
  transient private ITypeRef _typeRef;
  transient private List<ITypeVariableDefinition> _typeVarDefs;
  transient private GenericTypeVariable[] _genTypeVar;
  transient private GosuParser _parser;
  transient private ITypeUsesMap _typeUsesMap;
  transient private Boolean _bStrictGenerics;
  transient private ModifierInfo _modifierInfo;
  transient private boolean _bHasAssertions;
  transient private boolean _bUsesQueryUsageSiteValidation; //## UNACCEPTABLE!!!

  public GosuClass( String strNamespace, String strRelativeName, GosuClassTypeLoader classTypeLoader,
                    ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
//    System.out.println("NEW: "+ strNamespace + ": " + sourceFile.getClass().getSimpleName() + " :::: " + sourceFile.getSource().getSource().replace("\n", " "));
    initLazyVars();

    if( strNamespace == null )
    {
      throw new IllegalArgumentException( "Namespace must be non-null" );
    }
    if( strRelativeName == null )
    {
      throw new IllegalArgumentException( "Relative name must be non-null" );
    }

    _bInitializing = true;
    try
    {
      _compilationState = new CompilationState();
      _iMdChecksum = TypeSystem.getRefreshChecksum();
      _iTiChecksum = TypeSystem.getSingleRefreshChecksum();
      _strNamespace = StringPool.get( strNamespace );
      _strRelativeName = strRelativeName;
      _typeLoader = classTypeLoader;
      _sourceFileHandle = sourceFile;
      _strFullName = (GosuStringUtil.isEmpty( _strNamespace ) ? "" : (_strNamespace + '.')) + _strRelativeName;
      _mapInnerClasses = Collections.emptyMap();
      _interfaces = EMPTY_TYPE_ARRAY;
      _defaultConstructorName = _strRelativeName + "()";
      _blocks = Collections.emptyList();
      _typeUsesMap = typeUsesMap;
      _modifierInfo = new ModifierInfo( Modifier.PUBLIC );
    }
    finally
    {
      _bInitializing = false;
    }
    getOrCreateTypeReference();
  }

  protected GosuClass( IGosuClass genericClass, IType[] typeParams )
  {
    this( genericClass, typeParams, true );
  }
  protected GosuClass( IGosuClass genericClass, IType[] typeParams, boolean bCopyState  )
  {
    initLazyVars();
    try
    {
      IGosuClassInternal pureGenericClass = (IGosuClassInternal)TypeLord.getPureGenericType( genericClass );
      pureGenericClass.compileHeaderIfNeeded();

      _iMdChecksum = TypeSystem.getRefreshChecksum();
      _iTiChecksum = TypeSystem.getSingleRefreshChecksum();
      _genericClass = pureGenericClass;
      _typeParams = typeParams;
      copyGenericState( bCopyState );
    }
    finally
    {
      _bInitializing = false;
    }
    getOrCreateTypeReference();
  }

  public GosuClassParseInfo getParseInfo()
  {
    return _parseInfo;
  }

  @Override
  public boolean hasAssertions()
  {
    return _bHasAssertions;
  }

  public void setHasAssertions( boolean bHasAssertions )
  {
    _bHasAssertions = bHasAssertions;
  }

  public Object dontEverCallThis()
  {
    return this;
  }

  public void copyGenericState( boolean bCopyHierarchy )
  {
    _iMdChecksum = TypeSystem.getRefreshChecksum();
    _iTiChecksum = TypeSystem.getSingleRefreshChecksum();

    GosuClass realGenericClass = (GosuClass)_genericClass.dontEverCallThis();
    _strNamespace = realGenericClass._strNamespace;
    _typeLoader = realGenericClass._typeLoader;
    _mapInnerClasses = realGenericClass._mapInnerClasses;
    // Must synchronize so that _staticScope is guaranteed to be non-null, see initialize()
    TypeSystem.lock();
    try
    {
      _strRelativeName = realGenericClass._strRelativeName + TypeLord.getNameOfParams( _typeParams, true, false );
      _strFullName = realGenericClass._strFullName + TypeLord.getNameOfParams( _typeParams, false, false );

      _defaultConstructorName = realGenericClass.getRelativeName() + "()";

      _bInterface = realGenericClass._bInterface;
      _bStructure = realGenericClass._bStructure;

      _compilationState = realGenericClass._compilationState;
      _hasError = realGenericClass._hasError;

      _enclosingType = realGenericClass._enclosingType;
      _sourceFileHandle = realGenericClass._sourceFileHandle;
      _bHasSessionVarStatements = realGenericClass._bHasSessionVarStatements;

      _bEnum = realGenericClass._bEnum;

      _parseInfo = realGenericClass._parseInfo;
      _typeUsesMap = realGenericClass._typeUsesMap;

      //transient private Set<IType> _setTypes; // lazy
      //transient private IType _javaStubType; // lazy
      //transient private GosuClassTypeInfo _typeInfo; // lazy
      //transient private GosuArrayClass _gsArrayClass; // lazy

      if( bCopyHierarchy )
      {
        copyHierarchyInfo();
      }
      _blocks = realGenericClass._blocks;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void copyHierarchyInfo()
  {
    TypeSystem.lock();
    try
    {
      assignParameterizedInterfaces();
      assignParameterizedSuperType();
      assignParameterizedJavaTypeIfProxy();

      assignTypeVarsFromTypeParams( getTypeParameters() );
//## see ParamInnerClass below
//      assignParameterizedInnerClasses();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

//## see ParamInnerClass below
//  private void assignParameterizedInnerClasses()
//  {
//    List<CharSequence> innerClassNames = new ArrayList<CharSequence>( _mapInnerClasses.keySet() );
//    for( CharSequence relativeInnerClassName: innerClassNames ) {
//      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOrCreateTypeReference(), getOrCreateTypeReference(), true );
//      IGosuClassInternal innerGsClass = _mapInnerClasses.get( relativeInnerClassName );
//      innerGsClass = (IGosuClassInternal) TypeLord.getActualType(innerGsClass, actualParamByVarName, true);
//      _mapInnerClasses.put( relativeInnerClassName, innerGsClass );
//    }
//  }

  private void assignParameterizedJavaTypeIfProxy()
  {
    if( isProxy() )
    {
      IJavaTypeInternal javaType = (IJavaTypeInternal) _genericClass.getJavaType();
      if( javaType == null )
      {
        return;
      }

      if( isParameterizedType() )
      {
        getOrCreateTypeReference();
        javaType = (IJavaTypeInternal) javaType.getParameterizedType( getTypeParameters() );
      }

      setJavaType(javaType);
    }
  }

  private void assignParameterizedSuperType()
  {
    IType genSuperType = _genericClass.getSupertype();
    if( genSuperType == null )
    {
      return;
    }

    if( genSuperType instanceof IJavaType )
    {
      IJavaTypeInternal javaGenSuperType = (IJavaTypeInternal)genSuperType;
      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOrCreateTypeReference(), getOrCreateTypeReference() );
      setSuperType( TypeLord.getActualType( javaGenSuperType, actualParamByVarName, true ) );
      ((IJavaTypeInternal)_superType).setAdapterClass( javaGenSuperType.getAdapterClass() );
    }
    else if( genSuperType instanceof IGosuClassInternal )
    {
      IGosuClassInternal gsGenSuperType = (IGosuClassInternal)genSuperType;
      TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOrCreateTypeReference(), getOrCreateTypeReference() );
      setSuperType( TypeLord.getActualType( gsGenSuperType, actualParamByVarName, true ) );
    }
  }

  private void assignParameterizedInterfaces()
  {
    GosuClass realGenericClass = (GosuClass)_genericClass.dontEverCallThis();
    IType[] interfaces = realGenericClass._interfaces;
    if( interfaces.length == 0 )
    {
      _interfaces = EMPTY_TYPE_ARRAY;
      return;
    }
    if( _interfaces == null )
    {
      _interfaces = EMPTY_TYPE_ARRAY;
    }
    for( IType genInterface : interfaces )
    {
      if( TypeLord.hasTypeVariable( genInterface ) )
      {
        if( genInterface instanceof IJavaType)
        {
          IJavaTypeInternal javaGenInterface = (IJavaTypeInternal)genInterface;
          TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOrCreateTypeReference(), getOrCreateTypeReference() );
          genInterface = TypeLord.getActualType( javaGenInterface, actualParamByVarName, true );
          ((IJavaTypeInternal)genInterface).setAdapterClass( javaGenInterface.getAdapterClass() );
        }
        else if( genInterface instanceof IGosuClassInternal )
        {
          IGosuClassInternal gsGenInterface = (IGosuClassInternal)genInterface;
          TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOrCreateTypeReference(), getOrCreateTypeReference() );
          genInterface = TypeLord.getActualType( gsGenInterface, actualParamByVarName, true );
        }
      }
      addInterface( genInterface );
    }
  }

  public String getName()
  {
    return _strFullName;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return _strRelativeName;
  }

  public String getNamespace()
  {
    return _strNamespace;
  }

  public void setNamespace( String namespace )
  {
    _strNamespace = namespace == null ? null : StringPool.get( namespace );
  }

  public GosuClassTypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  public IType getSupertype()
  {
    compileHeaderIfNeeded();
    if( TypeSystem.isDeleted(_superType) )
    {
      return TypeSystem.getErrorType(_superType.getName());
    }
    else
    {
      if( _superType == null && isParameterizedType() && _genericClass.getSupertype() != null )
      {
        assignParameterizedSuperType();
      }
      return _superType;
    }
  }

  public IGosuClassInternal getGenericType()
  {
    return isParameterizedType()
           ? _genericClass
           : isGenericType()
             ? (IGosuClassInternal) getOrCreateTypeReference()
             : null;
  }

  public boolean isFinal()
  {
    compileHeaderIfNeeded();
    return Modifier.isFinal( getModifiers() );
  }

  public boolean isInterface()
  {
    compileHeaderIfNeeded();
    return _bInterface;
  }
  public void setInterface( boolean bInterface )
  {
    _bInterface = bInterface;
  }

  public boolean isStructure()
  {
    compileHeaderIfNeeded();
    return _bStructure;
  }
  public void setStructure( boolean bStructure )
  {
    _bStructure = bStructure;
  }

  public boolean isEnum()
  {
    compileHeaderIfNeeded();
    return _bEnum;
  }
  public void setEnum()
  {
    _bEnum = true;
    if( getEnclosingType() != null )
    {
      markStatic();
    }
    addInterface(TypeSystem.get(IEnumValue.class, TypeSystem.getGlobalModule()));
  }

  public List<String> getEnumConstants()
  {
    if( !isEnum() )
    {
      return Collections.emptyList();
    }

    List<String> enumConstants = new ArrayList<String>();
    List<IVarStatement> fields = getStaticFields();
    for( IVarStatement f : fields )
    {
      if( f.isEnumConstant() )
      {
        enumConstants.add( f.getIdentifierName() );
      }
    }
    return enumConstants;
  }

  public List<IEnumValue> getEnumValues()
  {
    //noinspection unchecked
    return (List<IEnumValue>)getTypeInfo().getProperty("AllValues").getAccessor().getValue(null);
  }

  public IEnumValue getEnumValue( String strName )
  {
    return (IEnumValue) getTypeInfo().getMethod( "valueOf", JavaTypes.STRING() ).getCallHandler().handleCall( null, strName );
  }

  public IType[] getInterfaces()
  {
    compileHeaderIfNeeded();

    maybeAssignInterfacesForParameterizedClass();

    return _interfaces;
  }

  private void maybeAssignInterfacesForParameterizedClass()
  {
    IType[] interfaces = _interfaces;
    if( (interfaces == null || interfaces.length == 0) && isParameterizedType() )
    {
      IType[] genInterfaces = _genericClass.getInterfaces();
      if( genInterfaces != null && genInterfaces.length > 0 )
      {
        if( interfaces == null || interfaces.length != genInterfaces.length )
        {
          assignParameterizedInterfaces();
        }
      }
    }
  }

  public void addInterface( IType type )
  {
    ArrayList<IType> interfaces = new ArrayList<IType>();
    for (IType i : _interfaces) {
      interfaces.add(i);
    }

    for( int i = 0; i < interfaces.size(); i++ )
    {
      IType iface = interfaces.get( i );
      if( TypeLord.getPureGenericType( iface ).equals( TypeLord.getPureGenericType( type ) ) )
      {
        // Remove duplicate generic interface
        //
        // Normally this won't happen, but...
        // if the class is a recursive type (via generics), we have to short-circuit
        // the type when we first parse it, hence the pure generic type for the
        // interface is added during the first header parse. It's safe to remove it here
        // assuming we're adding a properly parameterized type e.g.,
        //   class Foo<T extends Bar<T>> implements Comparable<Foo<T>>
        //
        interfaces.remove( i );
      }
    }
    if( !interfaces.contains( type ) )
    {
      interfaces.add(type);
    }

    _interfaces = interfaces.toArray(new IType[interfaces.size()]);
  }

  public IJavaType getJavaType()
  {
    if( _proxiedJavaClassInGosuProxy != null )
    {
      return _proxiedJavaClassInGosuProxy;
    }
    if( getEnclosingType() != null && isProxy() )
    {
      IJavaType javaType = ((IGosuClass) getEnclosingType()).getJavaType();
      if( javaType != null )
      {
        IType proxiedJavaClass = javaType.getInnerClass(getRelativeName());
        setJavaType((IJavaType) proxiedJavaClass);
        return (IJavaType) proxiedJavaClass;
      }
    }
    return null;
  }

  public void setJavaType( IJavaType javaType )
  {
    _proxiedJavaClassInGosuProxy = javaType;
  }

  public IType findProxiedClassInHierarchy()
  {
    if( getSuperClass() != null )
    {
      return getSuperClass().findProxiedClassInHierarchy();
    }
    return null;
  }

  public boolean isParameterizedType()
  {
    //!! Do not call following method:
    // compileDeclarationsIfNeeded();

    return _typeParams != null && _typeParams.length > 0;
  }

  public boolean isGenericType()
  {
    compileHeaderIfNeeded();

    List<ITypeVariableDefinition> typeVarDefs = getTypeVarDefs();
    return typeVarDefs != null && !typeVarDefs.isEmpty();
  }

  public GenericTypeVariable[] getGenericTypeVariables()
  {
    compileHeaderIfNeeded();

    if( _genTypeVar == null )
    {
      if( _typeVarDefs == null || // Hack to short-circuit case where header compilation is reentrant e.g., parsing an inner class w/o closing brace as a field initializer value.
          _typeVarDefs.isEmpty() )
      {
        return _genTypeVar = GenericTypeVariable.EMPTY_TYPEVARS;
      }
      _genTypeVar = new GenericTypeVariable[_typeVarDefs.size()];

//## see ParamInnerClass
//      if( _typeVarDefs.isEmpty() )
//      {
//        addFakeGenericTypeVarIfInnerClassHasNoTypeVars();
//      }
      for( int i = 0; i < _typeVarDefs.size(); i++ )
      {
        _genTypeVar[i] = (GenericTypeVariable) _typeVarDefs.get(i).getTypeVar();
      }
    }
    return _genTypeVar;
  }

//## ParamInnerClass (PL-28025)
//## support case where inner class of a generic class is NOT directly generic, yet we need to have separate copies to propogate the enclosing type's type vars
//## e.g., class InnerClass extends List<T> where T is from enclosing class -- we need to pass along T for each parameterized instance of enclosing type
//  private void addFakeGenericTypeVarIfInnerClassHasNoTypeVars() {
//    if( !isStatic() && getEnclosingType() != null && getEnclosingType().isGenericType() ) {
//      TypeVariableDefinition fakeTypeVar = new TypeVariableDefinition( getEnclosingType(), false );
//      fakeTypeVar.setBoundingType( JavaTypes.OBJECT() );
//      fakeTypeVar.setName( "Fake_T" );
//      _typeVarDefs.add( fakeTypeVar );
//    }
//  }

  public IGosuClassInternal getParameterizedType( IType... paramTypes )
  {
    compileHeaderIfNeeded();

//    if( getGenericTypeVariables().length != paramTypes.length )
//    {
//      throw new IllegalStateException();
//    }
    for( IType t: paramTypes )
    {
      if( t == null )
      {
        throw new IllegalStateException( "Type parameters must be non-null" );
      }
    }

    if( isParameterizedType() )
    {
      return (IGosuClassInternal)TypeLord.getPureGenericType(getOrCreateTypeReference()).getParameterizedType( paramTypes );
    }

    if(paramTypes.length == 0)
    {
      throw new IllegalArgumentException( "Parameter types required. Invoked on type: " + _strFullName);
    }

    if( _parameterizationByParamsName == null )
    {
      TypeSystem.lock();
      try
      {
        if( _parameterizationByParamsName == null )
        {
          _parameterizationByParamsName = new ConcurrentHashMap<String, IGosuClassInternal>( 2 );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }

    paramTypes = TypeSystem.boxPrimitiveTypeParams( paramTypes );
    String strNameOfParams = TypeLord.getNameOfParams( paramTypes, false, true );
    IGosuClassInternal parameterizedClass = _parameterizationByParamsName.get( strNameOfParams );
    if( parameterizedClass == null )
    {
      TypeSystem.lock();
      try
      {
        parameterizedClass = _parameterizationByParamsName.get( strNameOfParams );
        if( parameterizedClass == null )
        {
          parameterizedClass = makeCopy( paramTypes );
          _parameterizationByParamsName.put( strNameOfParams, parameterizedClass );
          parameterizedClass.copyHierarchyInfo();
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return parameterizedClass;
  }

  protected IGosuClassInternal makeCopy( IType... paramTypes )
  {
    return (IGosuClassInternal)getOrCreateTypeReference( new GosuClass( (IGosuClass)getOrCreateTypeReference(), paramTypes, false ) );
  }

  public IType[] getTypeParameters()
  {
    compileHeaderIfNeeded();

    return _typeParams;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    if( isCompilingHeader() )
    {
      return Collections.emptySet();
    }

    compileHeaderIfNeeded();

    if( !isHeaderCompiled() )
    {
      return Collections.emptySet();
    }

    if( _setTypes == null )
    {
      TypeSystem.lock();
      try
      {
        if( _setTypes == null )
        {
          _setTypes = createAllTypesInHierarchy();
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _setTypes;
  }

  private Set<IType> createAllTypesInHierarchy()
  {
    Set<IType> types;
    if( isArray() )
    {
      types = TypeLord.getArrayVersionsOfEachType( getComponentType().getAllTypesInHierarchy() );
    }
    else
    {
      types = TypeLord.getAllClassesInClassHierarchyAsIntrinsicTypes(getOrCreateTypeReference());
      if( !isInterface() )
      {
        if( isParameterizedType() )
        {
          // Will indirectly call handleProxyClassAssignment() on generic type
          getGenericType().getAllTypesInHierarchy();
        }
        else if( ProxyUtil.isProxy( this ) )
        {
          types.addAll( TypeSystem.getByFullName( getName().substring( PROXY_PREFIX.length() + 1 ) ).getAllTypesInHierarchy() );
        }
      }
    }
    return types;
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
    if( _gsArrayClass == null )
    {
      // Only sync if it's null.  If so, then check again to avoid a race
      TypeSystem.lock();
      try
      {
        if( _gsArrayClass == null )
        {
          //noinspection UnnecessaryLocalVariable
          IGosuArrayClass gsArrayClass =
            (IGosuArrayClass)getOrCreateTypeReference( new GosuArrayClass( getOrCreateTypeReference(), getTypeLoader() ) );
          _gsArrayClass = gsArrayClass;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _gsArrayClass;
  }

  public Object makeArrayInstance( int iLength )
  {
    return Array.newInstance( isStructure() ? JavaTypes.OBJECT().getBackingClass() : getBackingClass(), iLength );
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return Array.get( array, iIndex );
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    Array.set( array, iIndex, value );
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return Array.getLength( array );
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    IType pThis = getOrCreateTypeReference();

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

    if( isArray() && type.isArray() )
    {
      return getComponentType().isAssignableFrom( type.getComponentType() );
    }
    else if( isArray() || type.isArray() )
    {
      return false;
    }
    else if( isInterface() && !isStructure() && type instanceof IGosuClass && ((IGosuClass)type).isStructure() )
    {
      // A structure *not* implicitly assignable to an interface because there is no hierarchy, just a structure
      // (force an explicit cast if the runtime type is expected to directly implement the interface)
      return false;
    }
    else if( type.getAllTypesInHierarchy().contains( pThis ) )
    {
      return true;
    }
    else if( TypeLord.areGenericOrParameterizedTypesAssignable( pThis, type ) )
    {
        // We check *structural* assignability for the case where this is a structure or @StrictGenerics class/interface and
        // covariant assignability may be inappropriate.  In other words structural assignability verifies contravariance
        // if necessary.
        return (isGenericType() && !isParameterizedType()) || // a pure generic class has no type parameters, therefore no contravariance to check
               !isStrictGenerics() && !isStructure(); // structures and @StrictGenerics marked interfaces need to be checked further for contravariance
    }
    return false;
  }

  public boolean isStrictGenerics()
  {
    if( _bStrictGenerics != null )
    {
      return _bStrictGenerics;
    }

    return _bStrictGenerics = getGosuAnnotations().stream()
      .anyMatch( anno -> anno.getType() == JavaTypes.STRICT_GENERICS() );
  }

  public boolean isMutable()
  {
    return true;
  }

  public boolean isValid()
  {
    try
    {
      compileDefinitionsIfNeeded();
    }
    catch( ErrantGosuClassException egce )
    {
      return false;
    }
    return _valid.get();
  }

  public boolean isStatic()
  {
    return Modifier.isStatic(getModifiers());
  }

  public ModifierInfo getModifierInfo()
  {
    if( isParameterizedType() )
    {
      return (ModifierInfo)getGenericType().getModifierInfo();
    }
    compileHeaderIfNeeded();
    return _modifierInfo;
  }
  public void setModifierInfo( ModifierInfo modifierInfo )
  {
    if( isParameterizedType() )
    {
      getGenericType().setModifierInfo( modifierInfo);
      return;
    }

    GosuClass.filterJavaDocAnnotations( modifierInfo.getAnnotations() );
    _modifierInfo = modifierInfo;
  }

  public int getModifiers()
  {
    return getModifierInfo().getModifiers();
  }

  public void markStatic()
  {
    getModifierInfo().setModifiers( Modifier.setStatic( getModifiers(), true ) );
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( getModifiers() );
  }

  public GosuClassTypeInfo getTypeInfo()
  {
    if( _typeInfo == null || hasAncestorBeenUpdated() )
    {
      TypeSystem.lock();
      try {
        if (_typeInfo == null || hasAncestorBeenUpdated() ) {
          IGosuClassInternal self = (IGosuClassInternal) getOrCreateTypeReference();
          _typeInfo = new GosuClassTypeInfo(self);
          IGosuClassInternal superClass = self.getSuperClass();
          if(superClass != null) {
            superClass.getTypeInfo();
          }
          self.compileDeclarationsIfNeeded();
          _iTiChecksum = TypeSystem.getSingleRefreshChecksum();
        }
      } finally {
        TypeSystem.unlock();
      }
    }
    else
    {
      // To be certain we never return a stale typeinfo
      compileDeclarationsIfNeeded();
    }
    return _typeInfo;
  }

  public boolean hasAncestorBeenUpdated() {
    IGosuClassInternal genThis = (IGosuClassInternal) TypeLord.getPureGenericType( getOrCreateTypeReference() );
    return haveAncestorsBeenUpdated(this, genThis.getTypeInfoChecksum());
  }

  private boolean haveAncestorsBeenUpdated(IGosuClassInternal type, int tiCheckSum) {
     final IGosuClassInternal supertype = type.getSuperClass();
     if (supertype != null && (TypeSystem.isDeleted(supertype) || hasBeenUpdated(supertype, tiCheckSum)))
     {
       return true;
     }

     for (IType anInterface : type.getInterfaces())
     {
       if (TypeSystem.isDeleted(anInterface ))
       {
         return true;
       }

       if (anInterface instanceof IJavaTypeInternal)
       {
         final IJavaTypeInternal iFace = (IJavaTypeInternal) anInterface;
         if (JavaType.hasBeenUpdated(iFace, tiCheckSum, new HashSet<IType>()))
         {
           return true;
         }
       }
       else if (anInterface instanceof IGosuClassInternal)
       {
         if (hasBeenUpdated((IGosuClassInternal) anInterface, tiCheckSum))
         {
           return true;
         }
       }
     }
     return false;
   }

  private boolean hasBeenUpdated(IGosuClassInternal type, int tiChecksum) {
    if( !ExecutionMode.get().isRefreshSupportEnabled() ) {
      return false;
    }
    IGosuClassInternal genType = TypeLord.getPureGenericType( type );
    if (genType.isProxy()) {
      IJavaTypeInternal javaType = (IJavaTypeInternal)genType.getJavaType();
      return javaType.getTypeInfoChecksum() > tiChecksum || javaType.hasAncestorBeenUpdated();
    } else {
      return genType.getTypeInfoChecksum() > tiChecksum || haveAncestorsBeenUpdated(genType, tiChecksum);
    }
  }

  public void unloadTypeInfo()
  {
    if( _typeInfo != null )
    {
      _typeInfo.unload();
    }
    _valid.clear();
    if( _parameterizationByParamsName != null )
    {
      _parameterizationByParamsName.clear();
//## Note we can't just clear the tyep info because the type params could get stale e.g., _proxy_.Map<Foo, MyGosuClass>
//      for( GosuClass parameteredClass : _parameterizationByParamsName.values() )
//      {
//        parameteredClass.unloadTypeInfo();
//      }
    }
  }

  public Object readResolve() throws ObjectStreamException
  {
    try
    {
      String strProxyPrefix = PROXY_PREFIX + '.';
      if( getName().startsWith( strProxyPrefix ) )
      {
        String strJavaName = getName().substring( strProxyPrefix.length() );
        IJavaTypeInternal javaType = (IJavaTypeInternal)TypeSystem.getByFullName( strJavaName );
        return javaType.getAdapterClass();
      }
      int iIndex = getName().indexOf( '<' );
      if( iIndex > 0 )
      {
        String strGenName = getName().substring( 0, iIndex );
        IType type = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( strGenName );
        return type.getParameterizedType( _typeParams );
      }
      else
      {
        return TypeLoaderAccess.instance().getIntrinsicTypeByFullName( getName() );
      }
    }
    catch( ClassNotFoundException e )
    {
      throw new InvalidClassException( e.getMessage() );
    }
  }

  public String getId()
  {
    return getName();
  }

  public String getSource()
  {
    ICompilableTypeInternal enclosingType = getEnclosingType();
    if (enclosingType != null) {
      return enclosingType.getSource();
    } else {
      return _sourceFileHandle.getSource().getSource();
    }
  }

  public boolean isStale()
  {
    // If this is a synthetic type off of a java proxy then don't refresh.
    return (_iMdChecksum != TypeSystem.getRefreshChecksum() && !isProxy());
  }

  public int getTypeInfoChecksum() {
    return _iTiChecksum;
  }

  public boolean isProxy()
  {
    return ProxyUtil.isProxy( this );
  }

  protected ITypeRef getOrCreateTypeReference()
  {
    if( _typeRef == null )
    {
      _typeRef = getOrCreateTypeReference( this );
    }
    return _typeRef;
  }
  protected ITypeRef getOrCreateTypeReference( IType type )
  {
    return getTypeLoader().getModule().getModuleTypeLoader().getTypeRefFactory().create( type );
  }

  public boolean isSubClass( IType gsSubType )
  {
    if( gsSubType == null )
    {
      return false;
    }

    if( gsSubType instanceof IGosuClassInternal )
    {
      IGosuClassInternal gsSuperClass = TypeLord.getPureGenericType( ( (IGosuClassInternal)gsSubType).getSuperClass() );
      return gsSuperClass == TypeLord.getPureGenericType( getOrCreateTypeReference() ) ||
             isSubClass( gsSuperClass );
    }
    return false;
  }

  public boolean isCompiled()
  {
    return isDeclarationsCompiled() && isDefinitionsCompiled();
  }

  public List<DynamicFunctionSymbol> getConstructorFunctions()
  {
    compileDeclarationsIfNeeded();

    Map<String, DynamicFunctionSymbol> ctors = getParseInfo().getConstructorFunctions();
    //noinspection unchecked
    return ctors.isEmpty() ? Collections.emptyList() : getUnmodifiableValues( ctors );
  }

  private List getUnmodifiableValues( Map functionSymbolMap )
  {
    //noinspection unchecked
    return Collections.unmodifiableList(new ArrayList(functionSymbolMap.values()));
  }

  public DynamicFunctionSymbol getConstructorFunction( String name )
  {
    compileDeclarationsIfNeeded();

    return getParseInfo().getConstructorFunctions().get(name);
  }

  public DynamicFunctionSymbol getDefaultConstructor()
  {
    compileDeclarationsIfNeeded();

    return getConstructorFunction(_defaultConstructorName);
  }

  public List<DynamicFunctionSymbol> getStaticFunctions()
  {
    compileDeclarationsIfNeeded();

    return Collections.unmodifiableList(getParseInfo().getStaticFunctions());
  }

  public Map<CharSequence, IGosuClassInternal> getInnerClassesMap()
  {
    compileHeaderIfNeeded();

    return _mapInnerClasses;
  }

  public List<IGosuClassInternal> getInnerClasses()
  {
    compileHeaderIfNeeded();

    return new ArrayList<IGosuClassInternal>(_mapInnerClasses.values());
  }

  public Map<CharSequence, ? extends IGosuClass> getKnownInnerClassesWithoutCompiling()
  {
    return _mapInnerClasses;
  }

  @Override
  public IGosuClass getBlock( int i )
  {
    return getBlocks().get(i);
  }

  public void addInnerClass( IGosuClassInternal innerGsClass )
  {
    innerGsClass.setCreateEditorParser(isCreateEditorParser());
    if( _mapInnerClasses == Collections.EMPTY_MAP )
    {
      _mapInnerClasses = new LinkedHashMap<CharSequence, IGosuClassInternal>( 2 );
    }
    // We put in types relative to ours.  I.e. if we are foo.SomeClass and we have an inner class named Bar, we put in Bar.
    _mapInnerClasses.put( StringPool.get( innerGsClass.getName().substring( getName().length() + 1 ) ), innerGsClass );
  }
  public void removeInnerClass( IGosuClassInternal innerGsClass )
  {
    if( !_mapInnerClasses.isEmpty() )
    {
      _mapInnerClasses.remove( innerGsClass.getRelativeName() );
    }
  }

  public IType resolveRelativeInnerClass( String strRelativeInnerClassName, boolean bForce )
  {
    if( !bForce && (!getCompilationState().isHeaderCompiled() || getCompilationState().isReparsingHeader()) )
    {
      // A class' header cannot reference inner classes inside of itself
      ICompilableTypeInternal outerClass = getEnclosingType();
      // Force the outer class to resolve the name, since it must be header compiled (it may say its not, but only because it's still compiler its inner class' headers).
      return outerClass == null ? null : outerClass.resolveRelativeInnerClass( strRelativeInnerClassName, true );
    }

    // Now try to resolve the inner class name relative to this class and its enclosing class[s] and its hierarchy
    return super.resolveRelativeInnerClass( strRelativeInnerClassName, bForce );
  }

  @SuppressWarnings({"unchecked"})
  public List<DynamicFunctionSymbol> getMemberFunctions()
  {
    compileDeclarationsIfNeeded();

    Map<String, DynamicFunctionSymbol> memberFunctions = getParseInfo().getMemberFunctions();
    return memberFunctions.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList( new ArrayList( memberFunctions.values() ) );
  }

  public DynamicFunctionSymbol getMemberFunction( IFunctionType funcType, String signature, boolean bContravariant )
  {
    DynamicFunctionSymbol dfs = getParseInfo().getMemberFunctions().get( signature );
    if( dfs == null )
    {
      dfs = getMemberFunction( funcType, bContravariant );
    }
    return dfs;
  }

  public List<DynamicFunctionSymbol> getMemberFunctions( String names )
  {
    Collection<DynamicFunctionSymbol> dfss = getParseInfo().getMemberFunctions().values();
    List<DynamicFunctionSymbol> returnDFSs = new ArrayList<DynamicFunctionSymbol>( );
    for( DynamicFunctionSymbol dfs : dfss )
    {
      if( names.equals( dfs.getDisplayName() ) )
      {
        returnDFSs.add( dfs );
      }
    }
    return returnDFSs;
  }

  public DynamicFunctionSymbol getMemberFunction( IFunctionType funcType, boolean bContravariant )
  {
    String name = funcType.getDisplayName();
    for( DynamicFunctionSymbol dfs : getParseInfo().getMemberFunctions().values() )
    {
      if( name.equals( dfs.getDisplayName() ) )
      {
        if( isParameterizedType() )
        {
          dfs = dfs.getParameterizedVersion( (IGosuClass)getOrCreateTypeReference() );
        }
        if( isAssignable( funcType, dfs, bContravariant ) )
        {
          return dfs;
        }
      }
    }
    return null;
  }

  private boolean isAssignable( IFunctionType funcType, IDynamicFunctionSymbol dfs, boolean bContravariant )
  {
    if( dfs == null )
    {
      return false;
    }
    if( funcType.isAssignableFrom( dfs.getType(), bContravariant ) )
    {
      return true;
    }
    if( dfs.getBackingDfs() != dfs &&
        isAssignable( funcType, dfs.getBackingDfs(), bContravariant ) )
    {
      return true;
    }
    return dfs.getSuperDfs() != dfs &&
           isAssignable( funcType, dfs.getSuperDfs(), bContravariant );
  }

  public DynamicPropertySymbol getStaticProperty( String name )
  {
    compileDeclarationsIfNeeded();

    for( DynamicPropertySymbol dps :getParseInfo().getStaticProperties() )
    {
      if( name.equals( dps.getName() ) )
      {
        return dps;
      }
    }
    return null;
  }

  public List<DynamicPropertySymbol> getStaticProperties()
  {
    compileDeclarationsIfNeeded();

    return Collections.unmodifiableList( getParseInfo().getStaticProperties() );
  }

  @SuppressWarnings({"unchecked"})
  public List<DynamicPropertySymbol> getMemberProperties()
  {
    compileDeclarationsIfNeeded();

    Map<String, DynamicPropertySymbol> properties = getParseInfo().getMemberProperties();
    return properties.isEmpty() ? Collections.emptyList() : getUnmodifiableValues( properties );
  }

  public DynamicPropertySymbol getMemberProperty( String name )
  {
    return getParseInfo().getMemberProperties().get(name);
  }

  @SuppressWarnings({"unchecked"})
  public List<IVarStatement> getStaticFields()
  {
    compileDeclarationsIfNeeded();

    Map<String, VarStatement> fields = getParseInfo().getStaticFields();
    return fields.isEmpty() ? Collections.emptyList() : getUnmodifiableValues( fields );
  }

  public VarStatement getStaticField( String name )
  {
    return getParseInfo().getStaticFields().get(name);
  }

  public Map<CharSequence, ISymbol> getMemberFieldIndexByName()
  {
    compileDeclarationsIfNeeded();

    return getParseInfo().getMemberFieldIndexByName();
  }

  @SuppressWarnings({"unchecked"})
  public List<IVarStatement> getMemberFields()
  {
    compileDeclarationsIfNeeded();

    Map<String, VarStatement> fields = getParseInfo().getMemberFields();
    return fields.isEmpty() ? Collections.emptyList() : getUnmodifiableValues( fields );
  }

  public Map<String, VarStatement> getMemberFieldsMap()
  {
    compileDeclarationsIfNeeded();

    return getParseInfo().getMemberFields();
  }

  public Symbol getStaticThisSymbol()
  {
    compileDeclarationsIfNeeded();

    return getParseInfo().getStaticThisSymbol();
  }

  public Map<String, ICapturedSymbol> getCapturedSymbols()
  {
    compileDefinitionsIfNeeded();

    return getParseInfo().getCapturedSymbols();
  }

  public ICapturedSymbol getCapturedSymbol( String strName )
  {
    return getCapturedSymbols().get(strName);
  }

  public void addCapturedSymbol( ICapturedSymbol sym )
  {
    compileDefinitionsIfNeeded();

    getParseInfo().addCapturedSymbolSilent(sym);
  }

  public boolean ensureDefaultConstructor( ISymbolTable symbolTable, GosuParser parser )
  {
    return !getConstructorFunctions().isEmpty() || getParseInfo().addDefaultConstructor( symbolTable, parser );
  }

  /**
   * @return The ClassStatement (root ParsedElement) for this GosuClass<p>
   *         <b>NOTE:</b>  Because GosuClass is lazily compiled, the ClassStatement returned is
   *         NOT guaranteed to be fully compiled by this method, and may return only the
   *         declarations of the class.  If you wish to access the fully compiled ClassStatement
   *         you must call {@link #compileDefinitionsIfNeeded()}.
   */
  public ClassStatement getClassStatement()
  {
    compileDeclarationsIfNeeded();

    return getParseInfo().getClassStatement();
  }

  public ClassStatement getClassStatementWithoutCompile()
  {
    return getParseInfo().getClassStatement();
  }

  public String toString()
  {
    return getName();
  }

  public void setSuperType( IType superType )
  {
    _superType = superType;
  }

  public void setEnclosingType( IType enclosingType )
  {
    _enclosingType = enclosingType;
  }

  public IType getEnclosingTypeReference()
  {
    return _enclosingType;
  }

  public boolean isAnonymous()
  {
    return getRelativeName().startsWith( ANONYMOUS_PREFIX ) && getEnclosingTypeReference() != null;
  }

  public int getDepth()
  {
    if( getEnclosingType() != null )
    {
      return getEnclosingType().getDepth() + 1;
    }
    return 0;
  }

  public void compileDefinitionsIfNeeded()
  {
    compileDefinitionsIfNeeded( false );
  }

  public void compileDefinitionsIfNeeded( boolean bForce )
  {
    boolean bHasError = false;
    
    if( !isDefinitionsCompiled() )
    {
      ((GosuClass)getPureGenericClass().dontEverCallThis())._hasError = null;

      if( isParameterizedType() )
      {
        getGenericType().compileDefinitionsIfNeeded();
        return;
      }

      if( getEnclosingType() instanceof IGosuClass && !getRelativeName().startsWith( FunctionToInterfaceClassGenerator.PROXY_FOR ) )
      {
        getEnclosingType().compileDefinitionsIfNeeded( bForce );
        return;
      }

      TypeSystem.lock();
      try
      {
        if(isTypeRefreshedOutsideOfLock()) {
          return;
        }

        if( !isCompilingDefinitions() && !isDefinitionsCompiled() )
        {
          //noinspection CaughtExceptionImmediatelyRethrown
          try
          {
            compileDeclarationsIfNeeded();
            if( !isDeclarationsCompiled() ) {
            	return;
            }
          }
          catch( ErrantGosuClassException e )
          {
            if( !bForce )
            {
              throw e;
            }
            // Force = Continue even if there are decl errors; we want as much error info as possible
          }

          setCompilingDefinitions( true );
          try
          {
            if( shouldResolve() )
            {
              IGosuClassInternal gosuClass = (IGosuClassInternal) getOrCreateTypeReference();
              GosuParser parser = makeParserForPhase();
              GosuClassParser classParser = new GosuClassParser( parser );
              classParser.parseDefinitions( gosuClass );
            }

            postAnalyze();

            if( getClassStatement().hasParseIssues() || hasParseIssuesInUsesStatements() )
            {
              updateParseResultsException();
            }
          }
          finally
          {
            setCompilingDefinitions( false );
            _sourceFileHandle.getSource().stopCachingSource();
          }
        }
      }
      finally
      {
        try
        {
          if( !isTypeRefreshedOutsideOfLock() )
          {
            ((GosuClass)getPureGenericClass().dontEverCallThis())._hasError = null;
            bHasError = hasError();

  //          _sourceFileHandle.cleanAfterCompile();
            getParseInfo().maybeClearDebugInfo();
          }
        }
        finally
        {
          TypeSystem.unlock();
        }
      }
    }

    if( bHasError )
    {
      throw new ErrantGosuClassException( (IGosuClass) getOrCreateTypeReference() );
    }
  }

  private boolean isTypeRefreshedOutsideOfLock() {
    return getOrCreateTypeReference().isTypeRefreshedOutsideOfLock( this );
  }

  private void postAnalyze()
  {
    if( !isAnonymous()  )
    {
      if( getEnclosingType() == null )
      {
        // Only analyze from top-level class
        PostCompilationAnalysis.maybeAnalyze( getClassStatement().getClassFileStatement(), getClassStatement() );
      }
    }
  }

  private boolean hasParseIssuesInUsesStatements()
  {
    if( (getTypeUsesMap() != null) &&
        (getTypeUsesMap().getUsesStatements() != null) )
    {
      for( IUsesStatement usesStatement : getTypeUsesMap().getUsesStatements() )
      {
        if( usesStatement.hasParseIssues() )
        {
          return true;
        }
      }
    }
    return false;
  }

  @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
  private void updateParseResultsException()
  {
    if( getParseResultsException() == null )
    {
      //noinspection ThrowableInstanceNeverThrown
      ClassFileStatement classFileStmt = getClassStatement().getClassFileStatement();
      setParseResultsException( new ParseResultsException( classFileStmt == null ? getClassStatement() : classFileStmt ) );
    }
    else
    {
      Statement stmt = (Statement)getParseResultsException().getParsedElement();
      if( stmt == null || !stmt.hasParseExceptions() )
      {
        // Update the ClassFileStmt in with the definition-compiled version only if
        // the decl-compiled version does not have errors
        stmt = getParseInfo().getClassFileStatement();
        if( stmt == null ) {
          // can happen if inner class is sythetic (generated at compile-time)
          stmt = getParseInfo().getClassStatement();
        }
      }
      getParseResultsException().reset( stmt );
    }
  }

  public void compileDeclarationsIfNeeded()
  {
    if( shouldCompileDeclarations() )
    {
      if( isParameterizedType() )
      {
        getGenericType().compileDeclarationsIfNeeded();
        return;
      }

      ICompilableTypeInternal enclosingType = getEnclosingType();
      if( enclosingType instanceof IGosuClass && !getRelativeName().startsWith( FunctionToInterfaceClassGenerator.PROXY_FOR ) )
      {
        enclosingType.compileDeclarationsIfNeeded();
        return;
      }

      TypeSystem.lock();
      try
      {
        if( !isCompilingHeader() && !isCompilingDeclarations() && shouldCompileDeclarations() )
        {
          try
          {
            compileHeaderIfNeeded();
          }
          catch( ErrantGosuClassException e )
          {
            // Ignore
          }

          setCompilingDeclarations( true );
          try
          {
            if( !isDeclarationsCompiled() )
            {
              IGosuClassInternal gosuClass = (IGosuClassInternal) getOrCreateTypeReference();
              GosuParser parser = makeParserForPhase();
              GosuClassParser classParser = new GosuClassParser( parser );
              classParser.parseDeclarations(gosuClass);
            }
            getParseInfo().maybeClearDebugInfo();

            forceTypeInfoInitialization();
          }
          finally
          {
            setCompilingDeclarations( false );
          }
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  public void forceTypeInfoInitialization()
  {
    if( !isDeclarationsCompiled() )
    {
      return;
    }

    if( !getClassStatement().hasParseExceptions() )
    {
      getTypeInfo().forceInit();

      for( IGosuClass innerGsClass : getKnownInnerClassesWithoutCompiling().values() )
      {
        if( innerGsClass.isDeclarationsCompiled() )
        {
          ((IGosuClassInternal)innerGsClass).forceTypeInfoInitialization();
        }
      }
    }
  }

  public void syncGenericAndParameterizedClasses()
  {
    if( isParameterizedType() )
    {
      copyGenericState( true );
    }
    else if( isGenericType() )
    {
      if( _parameterizationByParamsName != null )
      {
        // Make a copy to guard against concurrent modification
        Collection<IGosuClassInternal> values = _parameterizationByParamsName.values();
        IGosuClassInternal[] gosuClasses = values.toArray( new IGosuClassInternal[values.size()] );
        for( IGosuClassInternal gosuClass : gosuClasses )
        {
          gosuClass.copyGenericState( true );
        }
      }
    }
  }

  public Collection<IGosuClassInternal> getParameterizedTypes() {
    return _parameterizationByParamsName == null ? Collections.<IGosuClassInternal>emptyList() : _parameterizationByParamsName.values();
  }

  @Override
  public void setAnnotations(List<IGosuAnnotation> annotations) {
    filterJavaDocAnnotations( annotations );
    getModifierInfo().setAnnotations(annotations);
  }

  private static void filterJavaDocAnnotations( List<IGosuAnnotation> annotations )
  {
    for( Iterator<IGosuAnnotation> it = annotations.iterator(); it.hasNext(); )
    {
      IGosuAnnotation annotation = it.next();
      if( annotation instanceof GosuDocAnnotation)
      {
        if( JavaTypes.THROWS().equals( annotation.getType() ) ||
            JavaTypes.getGosuType( Returns.class ).equals( annotation.getType() ) ||
            JavaTypes.PARAMS().equals( annotation.getType() ) ||
            JavaTypes.PARAM().equals( annotation.getType() ) )
        {
          it.remove();
        }
      }
    }
  }

  @Override
  public IType getEnclosingNonBlockType()
  {
    ICompilableTypeInternal type =  getEnclosingType();
    while( type instanceof IBlockClassInternal )
    {
      type = type.getEnclosingType();
    }
    return type;
  }

  public void compileHeaderIfNeeded()
  {
    if( isHeaderCompiled() )
    {
      return;
    }

    if( isParameterizedType() )
    {
      getGenericType().compileHeaderIfNeeded();
      return;
    }

    if( getEnclosingType() instanceof IGosuClass && !getRelativeName().startsWith( FunctionToInterfaceClassGenerator.PROXY_FOR ) )
    {
      getEnclosingType().compileHeaderIfNeeded();

      IGosuClassInternal gosuClass = (IGosuClassInternal) getOrCreateTypeReference();
      gosuClass.createNewParseInfo();
      return;
    }

    TypeSystem.lock();
    try
    {
      if( !isCompilingHeader() && !isHeaderCompiled() )
      {
        setCompilingHeader( true );
        try
        {
          GosuClassCompilingStack.pushCompilingType( getOrCreateTypeReference() );
          try
          {
            if( getSuperClass() != null )
            {
              try
              {
                getSuperClass().compileHeaderIfNeeded();
              }
              catch( ErrantGosuClassException e )
              {
                // Super type has an error, but we don't care so long as we can compile
              }
            }
          }
          finally
          {
            GosuClassCompilingStack.popCompilingType();
          }

          if( !isHeaderCompiled() )
          {
            IGosuClassInternal gosuClass = (IGosuClassInternal) getOrCreateTypeReference();
            GosuParser parser = makeParserForPhase();
            GosuClassParser classParser = new GosuClassParser( parser );
            classParser.parseHeader( gosuClass, false, false, false );
          }
        }
        finally
        {
          setCompilingHeader( false );
        }
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private boolean shouldCompileDeclarations()
  {
    return !isDeclarationsCompiled() || !_compilationState.isInnerDeclarationsCompiled();
  }

  public CompilationState getCompilationState()
  {
    return _compilationState;
  }

  public boolean isCompilingHeader()
  {
    return getCompilationState().isCompilingHeader();
  }

  public void setCompilingHeader( boolean bCompilingHeader )
  {
    getCompilationState().setCompilingHeader( bCompilingHeader );
  }

  public boolean isHeaderCompiled()
  {
    return getCompilationState().isHeaderCompiled();
  }

  public void setHeaderCompiled()
  {
    getCompilationState().setHeaderCompiled();
  }

  public boolean isCompilingDeclarations()
  {
    return getCompilationState().isCompilingDeclarations();
  }

  public void setCompilingDeclarations( boolean bCompilingDeclarations )
  {
    getCompilationState().setCompilingDeclarations( bCompilingDeclarations );
  }

  public boolean isDeclarationsCompiled()
  {
    return getCompilationState().isDeclarationsCompiled();
  }

  public boolean isDeclarationsBypassed()
  {
    return getCompilationState().isDeclarationsBypassed();
  }
  public void setDeclarationsBypassed()
  {
    getCompilationState().setDeclarationsBypassed();
  }

  public boolean isInnerDeclarationsCompiled()
  {
    return getCompilationState().isInnerDeclarationsCompiled();
  }

  public void setDeclarationsCompiled()
  {
    getCompilationState().setDeclarationsCompiled();
  }

  public void setInnerDeclarationsCompiled()
  {
    getCompilationState().setInnerDeclarationsCompiled();
  }

  @Override
  public boolean isCompilingDefinitions()
  {
    return getCompilationState().isCompilingDefinitions();
  }

  @Override
  public boolean shouldFullyCompileAnnotations()
  {
    return getCompilationState().isCompilingDefinitions();
  }

  public void setCompilingDefinitions( boolean bCompilingDefinitions )
  {
    getCompilationState().setCompilingDefinitions( bCompilingDefinitions );
  }

  public boolean isDefinitionsCompiled()
  {
    return getCompilationState().isDefinitionsCompiled();
  }

  public void setDefinitionsCompiled()
  {
    getCompilationState().setDefinitionsCompiled();
  }

  public ISourceFileHandle getSourceFileHandle()
  {
    return _sourceFileHandle;
  }

  @Override
  public List<IType> getLoadedInnerClasses() {
    if( isDeclarationsCompiled() )
    {
      Collection<? extends IGosuClass> innerClasses = getKnownInnerClassesWithoutCompiling().values();
      return new ArrayList<IType>(innerClasses);
    }
    else
    {
      return Collections.emptyList();
    }
  }

  @Override
  public ClassType getClassType() {
    return ClassType.Class;
  }

  public List<? extends IGosuAnnotation> getGosuAnnotations()
  {
    return getModifierInfo().getAnnotations();
  }

  /**
   * When changing the places from which this method is called run pc's
   * gw.smoketest.pc.job.common.effectivetime.VisibleEffectiveTimeTest
   * cause it will break!
   */
  public GosuClassParseInfo createNewParseInfo() {
    if( _parseInfo == null ) {
      _parseInfo = new GosuClassParseInfo((IGosuClassInternal) getOrCreateTypeReference());
    }
    return _parseInfo;
  }

  public boolean isTestClass()
  {
    return _sourceFileHandle.isTestClass();
  }

  public IGosuClassInternal getInnerClass( CharSequence relativeName )
  {
    String strRelativeName = relativeName == null ? null : relativeName.toString();
    if( strRelativeName != null && strRelativeName.startsWith( ANONYMOUS_PREFIX ) )
    {
      try
      {
        compileDefinitionsIfNeeded();
      }
      catch( ErrantGosuClassException e )
      {
        // gulp
      }
    }

    int dotIndex = strRelativeName == null ? -1 : strRelativeName.indexOf( '.' );
    IGosuClassInternal innerClass;
    if( dotIndex == -1 )
    {
      innerClass = getInnerClassesMap().get( strRelativeName );
    }
    else
    {
      innerClass = getInnerClassesMap().get( strRelativeName.subSequence( 0, dotIndex ).toString() );
      if( innerClass != null )
      {
        innerClass = (IGosuClassInternal)innerClass.getInnerClass( strRelativeName.subSequence( dotIndex + 1, strRelativeName.length() ).toString() );
      }
    }

    if( innerClass == null )
    {
      innerClass = maybeLoadBlockToInterfaceProxy( strRelativeName );
      if( innerClass != null )
      {
        boolean b = getInnerClassesMap().get(strRelativeName) == innerClass;
        assert b;
      }
    }
    return innerClass;
  }

  private IGosuClassInternal maybeLoadBlockToInterfaceProxy( String relativeName )
  {
    if( relativeName.startsWith( FunctionToInterfaceClassGenerator.PROXY_FOR ) )
    {
      return (IGosuClassInternal)FunctionToInterfaceClassGenerator.getBlockToInterfaceConversionClass( relativeName, getOrCreateTypeReference() );
    }
    return null;
  }

  public VarStatement getMemberField( String charSequence )
  {
    return getParseInfo().getMemberFields().get(charSequence);
  }

  public boolean shouldKeepDebugInfo()
  {
    return !isProxy();
  }

  public IGosuClassInternal getSuperClass()
  {
    compileHeaderIfNeeded();

    IType superType = getSupertype();
    if( superType instanceof IGosuClassInternal )
    {
      return (IGosuClassInternal)superType;
    }
    else if( superType instanceof IJavaType)
    {
      return Util.getGosuClassFrom( superType );
    }
    return null;
  }

  public ICompilableTypeInternal getEnclosingType()
  {
    // Do NOT compile decls here. It's not needed and it'll break inner class compilation.
    return (_enclosingType == null || (_enclosingType instanceof ErrorType)) || !(_enclosingType instanceof ICompilableTypeInternal)
           ? null
           : (ICompilableTypeInternal)_enclosingType;
  }

  @Override
  public boolean hasBackingClass()
  {
    return (_javaClass != null && _javaClass.get() != null) || (_proxiedJavaClassInGosuProxy != null && !_proxiedJavaClassInGosuProxy.getBackingClassInfo().isAnnotation());
  }
  @Override
  public void unloadBackingClass()
  {
    SingleServingGosuClassLoader.clearCache( getName() );
    if( _javaClass != null )
    {
      TypeSystem.lock();
      try
      {
        _javaClass = null;
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }
  @Override
  public Class<?> getBackingClass()
  {
    if( isParameterizedType() )
    {
      return getGenericType().getBackingClass();
    }

    if( _proxiedJavaClassInGosuProxy != null &&
        !_proxiedJavaClassInGosuProxy.getBackingClassInfo().isAnnotation() )
    {
      // If this is a Gosu proxy, return the underlying java class
      return _proxiedJavaClassInGosuProxy.getIntrinsicClass();
    }

    Class clazz = _javaClass == null ? null : _javaClass.get();
    if( clazz == null )
    {
      TypeSystem.lock();
      try
      {
        clazz = _javaClass == null ? null : _javaClass.get();
        if( clazz == null )
        {
          clazz = GosuClassLoader.instance().defineClass( (IGosuClassInternal)getOrCreateTypeReference(), false );
          // Keeping in soft reference, otherwise in the case of single serving loaded class,
          // it forever pins the loader in memory preventing the class (and loader) from getting GCed.
          _javaClass = new SoftReference<>( clazz );
        }
      }
      catch( ClassNotFoundException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return clazz;
  }

  public boolean isCompilable()
  {
    return _proxiedJavaClassInGosuProxy == null ||
           _proxiedJavaClassInGosuProxy.getBackingClassInfo().isAnnotation();
  }

  public byte[] compile()
  {
    if( isParameterizedType() )
    {
      return getGenericType().compile();
    }

    if( _proxiedJavaClassInGosuProxy != null &&
        !_proxiedJavaClassInGosuProxy.getBackingClassInfo().isAnnotation() )
    {
      // This is a Gosu proxy, which corresponds to a Java class
      throw new RuntimeException();
    }

    return TransformingCompiler.compileClass( (IGosuClassInternal)getOrCreateTypeReference(), false );
  }

  @Override
  public ISymbol getExternalSymbol( String strName )
  {
    if( getEnclosingType() != null )
    {
      return getEnclosingType().getExternalSymbol( strName );
    }
    return null;
  }

  public void putClassMembers( GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic )
  {
    putClassMembers( getTypeLoader(), owner, table, gsContextClass, bStatic );
  }
  public void putClassMembers( GosuClassTypeLoader loader, GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic )
  {
    // Visit interfaces first so that concrete impls stamp over them,
    // then visit super classes so that subclass members stamp over super ones.

    //noinspection ForLoopReplaceableByForEach
    compileDeclarationsIfNeeded();
    for( int i = 0; i < _interfaces.length; i++ )
    {
      IType type = _interfaces[i];
      if( !(type instanceof ErrorType) )
      {
        IGosuClassInternal gsClass = Util.getGosuClassFrom( type );
        if( gsClass != null && gsClass != getOrCreateTypeReference() )
        {
          gsClass.putClassMembers( owner, table, gsContextClass, bStatic );
        }
      }
    }
    if( getEnclosingType() instanceof IGosuClassInternal &&
        ((IGosuClassInternal)getEnclosingType()).isHeaderCompiled() && TypeLord.encloses( getEnclosingType(), owner.getGosuClass() ) )
    {
      getEnclosingType().putClassMembers( loader, owner, table, gsContextClass, bStatic || isStatic() );
    }
    if( getSuperClass() != null )
    {
      getSuperClass().putClassMembers( owner, table, gsContextClass, bStatic );
      if( getSuperClass().isProxy() )
      {
        addJavaEnhancements( owner, table, gsContextClass, bStatic, getSuperClass().getJavaType() );
      }
    }

    putEnhancements( owner, table, gsContextClass, bStatic, loader.getModule(), getOrCreateTypeReference() );

    boolean bSuperClass = gsContextClass != getOrCreateTypeReference();

    putStaticFields( table, gsContextClass, bSuperClass );
    if( gsContextClass == null ||
        !isInterface() ||
        getOrCreateTypeReference( gsContextClass ) == getOrCreateTypeReference() ) // Static interface methods/properties are NOT inherited
    {
      putStaticFunctions( owner, table, gsContextClass, bSuperClass );
      putStaticProperties( table, gsContextClass, bSuperClass );
    }
    if( !bStatic )
    {
      putFields( table, gsContextClass, bSuperClass );
      putFunctions( owner, table, gsContextClass, bSuperClass );
      putProperties( table, gsContextClass, bSuperClass );
      putConstructors( owner, table, bSuperClass );
    }
  }

  private void addJavaEnhancements( GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic, IJavaType type )
  {
    IType superType = type.getSupertype();
    if( superType instanceof IJavaType )
    {
      addJavaEnhancements( owner, table, gsContextClass, bStatic, (IJavaType)superType );
    }
    for( IType iface : type.getInterfaces() )
    {
      if (iface instanceof IJavaType) {
        addJavaEnhancements( owner, table, gsContextClass, bStatic, (IJavaType)iface );
      }
    }
    putEnhancements( owner, table, gsContextClass, bStatic, getTypeLoader().getModule(), type );
  }

  private void putEnhancements(GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic, IModule module, IType type)
  {
    IModule[] moduleTraversalList = module.getModuleTraversalList();
    for (IModule m : moduleTraversalList) {
      GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader(m);
      if( loader != null ) {
        for( IGosuEnhancement enhancement : loader.getEnhancementIndex().getEnhancementsForType(type) ) {
          ((IGosuEnhancementInternal)enhancement).putClassMembers( loader, owner, table, gsContextClass, bStatic );
        }
      }
    }
  }
  
  private void putFunctions( GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    for( DynamicFunctionSymbol dfs : getMemberFunctions() )
    {
      if( !bSuperClass || (isAccessible( gsContextClass, dfs ) && !isHidden( dfs )) )
      {
        if( isParameterizedType() )
        {
          dfs = dfs.getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( dfs );
        if( owner != null )
        {
          owner.putDfsDeclInSetByName( dfs );
        }
      }
    }
  }

  private boolean isHidden( DynamicFunctionSymbol dfs )
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      // essentially isHidden() checks for @InternalAPI, which is evil and not part of standard Gosu
      return false;
    }

    if( isCompilingDeclarationsFor( dfs.getScriptPart() ) ) {
      return false;
    }
    IAttributedFeatureInfo mi = dfs.getMethodOrConstructorInfo( true );
    return mi != null && mi.isHidden();
  }

  private boolean isHidden( DynamicPropertySymbol dps )
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      // essentially isHidden() checks for @InternalAPI, which is evil and not part of standard Gosu
      return false;
    }

    DynamicFunctionSymbol getterDfs = dps.getGetterDfs();
    if( isCompilingDeclarationsFor( dps.getScriptPart() ) ) {
      return false;
    }
    IAttributedFeatureInfo miGetter = getterDfs == null ? null : getterDfs.getMethodOrConstructorInfo( true );
    return miGetter != null && miGetter.isHidden();
  }

  private boolean isHidden( IVarStatement varStmt )
  {
    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      // essentially isHidden() checks for @InternalAPI, which is evil and not part of standard Gosu
      return false;
    }

    if( isCompilingDeclarationsFor( varStmt.getScriptPart() ) ) {
      return false;
    }

    // @InternalAPI check
    for( IGosuAnnotation ann: ((VarStatement)varStmt).getAnnotations() ) {
      if( ann.getType().getName().equals( InternalAPI.class.getName() ) ) {
        return true;
      }
    }
    return false;
  }

  private boolean isCompilingDeclarationsFor( IScriptPartId scriptPart ) {
    if( scriptPart != null ) {
      IGosuClass type = (IGosuClass)scriptPart.getContainingType();
      return type != null && type.isCompilingDeclarations();
    }
    return false;
  }

  private void putProperties( ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    for( DynamicPropertySymbol dps : getMemberProperties() )
    {
      if( !bSuperClass || (isAccessible( gsContextClass, dps ) && !isHidden( dps )) )
      {
        if( isParameterizedType() )
        {
          dps = dps.getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( dps );
      }
    }
  }

  private void putConstructors( GosuParser owner, ISymbolTable table, boolean bSuperClass )
  {
    for( DynamicFunctionSymbol dfs : getConstructorFunctions() )
    {
      if( !bSuperClass )
      {
        if( isParameterizedType() )
        {
          dfs = dfs.getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( dfs );
        if( owner != null )
        {
          owner.putDfsDeclInSetByName( dfs );
        }
      }
    }
  }

  private void putStaticFunctions( GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    List<DynamicFunctionSymbol> staticFunctions = getStaticFunctions();
    for (int i = 0; i < staticFunctions.size(); i++) {
      DynamicFunctionSymbol dfs = staticFunctions.get(i);
      if( !bSuperClass || (isAccessible( gsContextClass, dfs ) && !isHidden( dfs )) )
      {
        if( isParameterizedType() )
        {
          dfs = dfs.getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( dfs );
        if( owner != null )
        {
          owner.putDfsDeclInSetByName( dfs );
        }
      }
    }
  }

  private void putStaticProperties( ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    List<DynamicPropertySymbol> staticProperties = getStaticProperties();
    for (int i = 0; i < staticProperties.size(); i++) {
      DynamicPropertySymbol dps = staticProperties.get(i);
      if( !bSuperClass || (isAccessible( gsContextClass, dps ) && !isHidden( dps )) )
      {
        if( isParameterizedType() )
        {
          dps = dps.getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( dps );
      }
    }
  }

  private void putStaticFields( ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    for( IVarStatement varStmt : getStaticFields() )
    {
      if( !bSuperClass || (isAccessible( gsContextClass, varStmt ) && !isHidden( varStmt )) )
      {
        ISymbol existingSymbol = table.getSymbol( varStmt.getSymbol().getName() );
        if( existingSymbol != null && !areSymbolsFromSameDeclaration(varStmt, existingSymbol))
        {
          table.putSymbol( new AmbiguousSymbol( varStmt.getSymbol().getName() ) );
        }
        else
        {
          table.putSymbol( varStmt.getSymbol() );
        }
      }
    }
  }

  private boolean areSymbolsFromSameDeclaration(IVarStatement varStmt, ISymbol existingSymbol) {
    boolean sameDeclaringType = GosuObjectUtil.equals(existingSymbol.getScriptPart(), varStmt.getSymbol().getScriptPart());
    if( sameDeclaringType ) {
      return true;
    }
    if( existingSymbol.getScriptPart() != null )
    {
      IGosuClassInternal existingDeclaringType = (IGosuClassInternal)existingSymbol.getScriptPart().getContainingType();
      if( isProxy() && existingDeclaringType.isProxy() )
      {
        // This class is a Java proxy and so is the declaring class of the existing symbol.  In this case we need to get
        // the JavaType corresponding with this class' proxy and find where the existing symbol comes from within the Java
        // hierarchy.
        IPropertyInfo pi = ((IRelativeTypeInfo)getJavaType().getTypeInfo()).getProperty( getTheRef(), existingSymbol.getName() );
        return pi != null && pi.getOwnersType() == existingDeclaringType.getJavaType();
      }
    }
    return false;
  }

  private void putFields( ISymbolTable table, IGosuClassInternal gsContextClass, boolean bSuperClass )
  {
    for( IVarStatement varStmt : getMemberFields() )
    {
      if( !bSuperClass || (isAccessible( gsContextClass, varStmt ) && !isHidden( varStmt )) )
      {
        ISymbol symbol = varStmt.getSymbol();
        if( isParameterizedType() && symbol instanceof AbstractDynamicSymbol )
        {
          symbol = ((AbstractDynamicSymbol)symbol).getParameterizedVersion( (IGosuClass) getOrCreateTypeReference());
        }
        table.putSymbol( symbol );
      }
    }
  }

  public boolean isAccessible( IGosuClassInternal compilingClass, AbstractDynamicSymbol ads )
  {
    return getOuterMostEnclosingClass( compilingClass ) == getOuterMostEnclosingClass( getOrCreateTypeReference() ) ||
           ads.isPublic() ||
           ads.isProtected() ||
           (ads.isInternal() && (isProxy() ? getNamespace().endsWith( compilingClass.getNamespace() ) :
                                             getNamespace().equals( compilingClass.getNamespace() )));
  }

  private boolean isAccessible( IGosuClassInternal compilingClass, IVarStatement varStmt )
  {
    return getOuterMostEnclosingClass( compilingClass ) == getOuterMostEnclosingClass( getOrCreateTypeReference() ) ||
           varStmt.isPublic() ||
           varStmt.isProtected() ||
           (varStmt.isInternal() && (isProxy() ? getNamespace().endsWith( compilingClass.getNamespace() ) :
                                                 getNamespace().equals( compilingClass.getNamespace() )));
  }

  private IType getOuterMostEnclosingClass( IType innerClass )
  {
    IType outerMost = innerClass;
    while( outerMost != null && outerMost.getEnclosingType() != null )
    {
      outerMost = outerMost.getEnclosingType();
    }
    return outerMost;
  }

  public void setParseResultsException( ParseResultsException pe )
  {
    getParseInfo().setParseResultsException(pe);
    _valid.clear();
  }

  public boolean hasError()
  {
    Boolean hasError = _hasError != null ? _hasError : ((GosuClass)getPureGenericClass().dontEverCallThis())._hasError;
    if( !isCompilingDefinitions() && hasError != null )
    {
      return hasError;
    }

    TypeSystem.lock();

    try {
      compileDeclarationsIfNeeded();
    } finally {
      TypeSystem.unlock();
    }

    ParseResultsException pe = getParseResultsException();
    return ((GosuClass)getPureGenericClass().dontEverCallThis())._hasError = (pe != null && pe.hasParseExceptions()) ? Boolean.TRUE : Boolean.FALSE;
  }

  public boolean hasWarnings() {
    ParseResultsException pe = getParseResultsException();
    return pe != null && pe.hasParseWarnings();
  }

  private IGosuClassInternal getPureGenericClass()
  {
    return (IGosuClassInternal)TypeLord.getPureGenericType(getOrCreateTypeReference());
  }

  public ParseResultsException getParseResultsException()
  {
    return getParseInfo() == null ? null : getParseInfo().getParseResultsException();
  }

  public int compareTo( Object o )
  {
    return getName().compareTo(((IType) o).getName());
  }

  public boolean shouldResolve()
  {
    return !isCompiled() && isDeclarationsCompiled();
  }

  private GosuParser makeParserForPhase()
  {
    createNewParseInfo();
    CompiledGosuClassSymbolTable symbolTable = CompiledGosuClassSymbolTable.instance();
    GosuParser parser = getOrCreateParser( symbolTable );
    ISource source = _sourceFileHandle.getSource();
    parser.setScript( source );
    _parseInfo.updateSource( source.getSource() );
    if( ExecutionMode.isIDE() ) {
      parser.setThrowParseExceptionForWarnings(true);
      parser.setDontOptimizeStatementLists(true);
      parser.setWarnOnCaseIssue(true);
      parser.setEditorParser(true);
    }
    assignTypeUsesMap( parser );
    return parser;
  }

  public ITypeUsesMap getTypeUsesMap() {
    return _typeUsesMap;
  }

  public void setTypeUsesMap( ITypeUsesMap usesMap )
  {
    _typeUsesMap = usesMap;
  }

  public void assignTypeUsesMap( GosuParser parser )
  {
    if( _typeUsesMap != null )
    {
      parser.setTypeUsesMap(_typeUsesMap);
      if( _strNamespace != null )
      {
        parser.getTypeUsesMap().addToTypeUses( _strNamespace + ".*" );
      }
    }
    else
    {
      ICompilableTypeInternal gsOuter = getEnclosingType();
      if( gsOuter != null )
      {
        gsOuter.assignTypeUsesMap( parser );
      }
      else if( _strNamespace != null )
      {
        parser.getTypeUsesMap().addToTypeUses( _strNamespace + ".*" );
      }
    }
  }

  /**
   * !! Used only when compiling from an edtior !!
   */
  public void setEditorParser( GosuParser parser )
  {
    _parser = parser;
  }
  public void setCreateEditorParser(boolean bEditorParser) {
    _bEditorParser = bEditorParser;
  }
  public boolean isCreateEditorParser() {
    return _bEditorParser;
  }

  @Override
  public int getAnonymousInnerClassCount()
  {
    Collection<? extends IGosuClass> innerClasses = getKnownInnerClassesWithoutCompiling().values();
    int iCount = 0;
    for( IGosuClass innerClass : innerClasses )
    {
      if( innerClass.isAnonymous() )
      {
        iCount++;
      }
    }
    return iCount;
  }

  /**
   * !! The parser is only set when this class is parsed in an editor in an IDE !!
   */
  public GosuParser getParser()
  {
    return _parser;
  }

  protected GosuParser getOrCreateParser(CompiledGosuClassSymbolTable symbolTable)
  {
    if( _parser != null ){
      return _parser;
    } else {
      GosuParser parser = createParser(symbolTable);
      if (ExecutionMode.isIDE()) {
        _parser = parser;
      }
      return parser;
    }
  }

  private GosuParser createParser(CompiledGosuClassSymbolTable symbolTable) {
    GosuParser parser = (GosuParser) GosuParserFactory.createParser(symbolTable, ScriptabilityModifiers.SCRIPTABLE);
    parser.pushScriptPart( new ScriptPartId( getOrCreateTypeReference(), null ) );
    ICompilableTypeInternal enclosingType = getEnclosingType();
    if( enclosingType != null ) {
      IGosuParser enclosingParser = enclosingType.getParser();
      if ( enclosingParser != null ) {
        parser.setThrowParseExceptionForWarnings( enclosingParser.isThrowParseResultsExceptionForWarnings() );
        parser.setWarnOnCaseIssue(enclosingParser.isThrowParseResultsExceptionForWarnings());
        parser.setEditorParser(enclosingParser.isThrowParseResultsExceptionForWarnings());
      } else {
        parser.setThrowParseExceptionForWarnings(enclosingType.isCreateEditorParser());
        parser.setWarnOnCaseIssue(enclosingType.isCreateEditorParser());
        parser.setEditorParser(enclosingType.isCreateEditorParser());
      }
    } else {
      parser.setThrowParseExceptionForWarnings(_bEditorParser);
      parser.setWarnOnCaseIssue(_bEditorParser);
      parser.setEditorParser(_bEditorParser);
    }
    return parser;
  }

  public void addDelegateImpls( ISymbolTable symTable, GosuClassParser parser )
  {
    List<DynamicPropertySymbol> props = new ArrayList<DynamicPropertySymbol>();
    List<DelegateFunctionSymbol> meths = new ArrayList<DelegateFunctionSymbol>();
    Map<IFunctionType, IFunctionType> delegated = new HashMap<IFunctionType, IFunctionType>();
    IGosuClassInternal pThis = (IGosuClassInternal) getOrCreateTypeReference();
    for( VarStatement varStmt : getMemberFieldsMap().values() )
    {
      if( varStmt instanceof DelegateStatement )
      {
        DelegateStatement delegateStmt = (DelegateStatement)varStmt;
        List<IFunctionType> unimpled = new ArrayList<IFunctionType>();
        for( IType iface : delegateStmt.getConstituents() )
        {
          IGosuClassInternal gsInterface = Util.getGosuClassFrom( iface );
          if( gsInterface != null )
          {
            unimpled = getUnimplementedMethods( gsInterface, pThis, unimpled, true, false );
            for( IMethodInfo mi : gsInterface.getTypeInfo().getMethods() )
            {
              if( mi.isDefaultImpl() )
              {
                //## todo: see if there are other
                continue;
              }
              GosuMethodInfo gmi = (GosuMethodInfo)mi;
              IFunctionType type = new FunctionType( gmi );
              if( unimpled.contains( type ) )
              {
                DelegateFunctionType dft = new DelegateFunctionType( gmi );
                if( delegated.containsKey( dft ) )
                {
                  IFunctionType existing = delegated.get( dft );
                  parser.addDeclaredNameParseError( delegateStmt, Res.MSG_DELEGATE_METHOD_CONFLICT,
                                                    existing.getEnclosingType() + "#" + existing.getParamSignature(),
                                                    dft.getEnclosingType() + "#" + dft.getParamSignature());
                  continue;
                }
                delegated.put( dft, dft );
                ReducedDynamicFunctionSymbol dfs = gmi.getDfs();

                mi = (IMethodInfo)dfs.getMethodOrConstructorInfo();
                if( !(iface instanceof IGosuClass) )
                {
                  String strMethodName = mi.getDisplayName();
                   mi = iface.getTypeInfo().getMethod( strMethodName, ((IFunctionType)dfs.getType()).getParameterTypes() );
                  if( mi == null )
                  {
                    if( dfs.getDisplayName().startsWith( "@" ) )
                    {
                      mi = iface.getTypeInfo().getMethod( "get" + strMethodName.substring( 1 ) );
                      if( mi == null )
                      {
                        mi = iface.getTypeInfo().getMethod( "is" + strMethodName.substring( 1 ) );
                      }
                    }
                    if( mi == null )
                    {
                      mi = iface.getTypeInfo().getCallableMethod( strMethodName, ((IFunctionType)dfs.getType()).getParameterTypes() );
                    }
                    if( mi == null )
                    {
                      throw new IllegalStateException( "Did not find method info for: " + dfs.getMethodOrConstructorInfo() );
                    }
                  }
                }
                DelegateFunctionSymbol delegateFs = new DelegateFunctionSymbol( pThis, symTable, dfs, mi, delegateStmt );
                if( delegateFs.getName().startsWith( "@" ) )
                {
                  // Handle case where the dfs is a property getter or setter
                  DynamicPropertySymbol dps = parser.getOrCreateDynamicPropertySymbol(
                    getParseInfo().getClassStatement(), pThis, delegateFs, delegateFs.getReturnType() != GosuParserTypes.NULL_TYPE() );
                  //parser.processPropertySymbol( dps, pThis );
                  props.add( dps );
                }
                else
                {
                  //parser.processFunctionSymbol( delegateFs, pThis );
                  meths.add( delegateFs );
                }
              }
            }
          }
        }
      }
    }
    for( DynamicPropertySymbol dps : props )
    {
      parser.processPropertySymbol( dps, pThis );
    }
    for( DelegateFunctionSymbol delegateFs : meths )
    {
      parser.processFunctionSymbol( delegateFs, pThis );
    }
  }

  public List<IFunctionType> getUnimplementedMethods()
  {
    List<IFunctionType> emptyList = Collections.emptyList();
//    if( isAbstract() )
//    {
//      return emptyList;
//    }
    return getUnimplementedMethods( emptyList, (IGosuClassInternal) getOrCreateTypeReference(), isAbstract() );
  }

  public List<IFunctionType> getUnimplementedMethods( List<IFunctionType> unimpled, IGosuClassInternal implClass, boolean bAcceptAbstract )
  {
    for( IType iface : _interfaces )
    {
      if( iface instanceof ErrorType )
      {
        continue;
      }

      // Ignore the IEnumValue interface on enums; all the associated functions are implemented
      // by the compiler
      if( isEnum() && iface == JavaTypes.getGosuType( IEnumValue.class ) )
      {
        continue;
      }

      if( iface == JavaTypes.IPROGRAM_INSTANCE() )
      {
        continue;
      }

      iface = Util.getGosuClassFrom( iface );
      if( iface != null )
      {
        unimpled = getUnimplementedMethods( (IGosuClassInternal)iface, implClass, unimpled, true, bAcceptAbstract );
      }
    }
    if( !bAcceptAbstract )
    {
      IType superType = getSupertype();
      if( superType != null && superType.isAbstract() )
      {
        IGosuClassInternal gsSuper = Util.getGosuClassFrom( superType );
        if (gsSuper != null) {
          unimpled = getUnimplementedMethods( gsSuper, implClass, unimpled, false, false );
          unimpled = gsSuper.getUnimplementedMethods( unimpled, implClass, bAcceptAbstract );
        }
      }
    }
    return unimpled;
  }

  public static List<IFunctionType> getUnimplementedMethods( IGosuClass gsIface, IGosuClass implClass, List<IFunctionType> unimpled, boolean ensurePublic, boolean bAcceptAbstract )
  {
    if( gsIface.isGenericType() && !gsIface.isParameterizedType() )
    {
      return Collections.emptyList();
    }

    for( IMethodInfo mi : gsIface.getTypeInfo().getMethods( gsIface ) )
    {
      if( !mi.isAbstract() && !mi.isDefaultImpl() )
      {
        continue;
      }
      if( mi.getOwnersType() == IGosuClassInternal.Util.getGosuClassFrom( JavaTypes.IGOSU_OBJECT() ) )
      {
        continue;
      }
      IFunctionType ifaceFuncType = new FunctionType( mi );
      DynamicFunctionSymbol implDfs = getImplDfs( (IGosuClassInternal)implClass, ifaceFuncType, bAcceptAbstract );
      if( (implDfs == null || !isAssignable( implDfs, ifaceFuncType ) || (ensurePublic && !implDfs.isPublic()) ) && !isObjectMethod( mi ) )
      {
        if( !handleParameterizedDfs( implClass, mi, ifaceFuncType, bAcceptAbstract ) )
        {
          if( unimpled.isEmpty() )
          {
            unimpled = new ArrayList<IFunctionType>( 8 );
          }
          // System.out.println("UNIMPLED: " + ifaceFuncType.getParamSignature() + " from " + gsIface.getName() + " in impl class " + implClass.getName() );
          unimpled.add( ifaceFuncType );
        }
      }
    }
    return unimpled;
  }

  private static boolean handleParameterizedDfs( IGosuClass implClass, IMethodInfo mi, IFunctionType ifaceFuncType, boolean bAcceptAbstract )
  {
    DynamicFunctionSymbol implDfs;
    if( mi instanceof GosuMethodInfo )
    {
      ReducedDynamicFunctionSymbol miDfs = ((GosuMethodInfo)mi).getDfs();
      if( miDfs != null )
      {
        while( miDfs instanceof ReducedParameterizedDynamicFunctionSymbol )
        {
          implDfs = getImplDfs( (IGosuClassInternal)implClass, (IFunctionType)miDfs.getBackingDfs().getType(), bAcceptAbstract );
          if( implDfs == null || !isAssignable( implDfs, ifaceFuncType ) )
          {
            miDfs = (ReducedDynamicFunctionSymbol) miDfs.getBackingDfs();
          }
          else
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean isAssignable( DynamicFunctionSymbol implDfs, IFunctionType ifaceFuncType )
  {
    if( ifaceFuncType.isAssignableFrom( implDfs.getType(), false ) )
    {
      return true;
    }
    DynamicFunctionSymbol superDfs = implDfs.getSuperDfs();
    if( superDfs != null && isAssignable( superDfs, ifaceFuncType ) )
    {
      return true;
    }
    DynamicFunctionSymbol delegateDfs = (DynamicFunctionSymbol) implDfs.getBackingDfs();
    //noinspection RedundantIfStatement
    if( delegateDfs != null && delegateDfs != implDfs && isAssignable( delegateDfs, ifaceFuncType ) )
    {
      return true;
    }
    return false;
  }

  static boolean isObjectMethod( IMethodInfo mi )
  {
    IParameterInfo[] params = mi.getParameters();
    IType[] paramTypes = new IType[params.length];
    for( int i = 0; i < params.length; i++ )
    {
      paramTypes[i] = params[i].getFeatureType();
    }
    IRelativeTypeInfo ti = (IRelativeTypeInfo)JavaTypes.OBJECT().getTypeInfo();
    String name = mi.getDisplayName();
    name = name.equals( "@Class" ) ? "getClass" : name;
    IMethodInfo objMethod = ti.getMethod( JavaTypes.OBJECT(), name, paramTypes );
    return objMethod != null;
  }

  private static DynamicFunctionSymbol getImplDfs( IGosuClassInternal implClass, IFunctionType ifaceFuncType, boolean bAcceptAbstract )
  {
    if( implClass == null )
    {
      return null;
    }

    String signature = ifaceFuncType.getParamSignatureForCurrentModule();

    DynamicFunctionSymbol implDfs = implClass.getMemberFunction( ifaceFuncType, signature, false );
    if( implDfs != null && implClass.isParameterizedType() )
    {
      implDfs = implDfs.getParameterizedVersion( implClass );
    }

    if( implDfs == null && signature.charAt( 0 ) == '@' )
    {
      implDfs = findVarPropertyAccessorDfs( signature, implClass );
    }
    if( implDfs == null )
    {
      implDfs = getImplDfs( implClass.getSuperClass(), ifaceFuncType, bAcceptAbstract );
    }
    if( !bAcceptAbstract && implDfs != null && implDfs.isAbstract() )
    {
      implDfs = null;
    }
    return implDfs;
  }

  private static DynamicFunctionSymbol findVarPropertyAccessorDfs( String signature, IGosuClassInternal implClass )
  {
    String strPropName = signature;
    strPropName = strPropName.substring( 1, strPropName.indexOf( '(' ) );
    DynamicPropertySymbol dps = implClass.getMemberProperty( strPropName );
    if( dps != null )
    {
      if( dps.getGetterDfs() != null && dps.getGetterDfs().getName().equals( signature ) )
      {
        return dps.getGetterDfs();
      }
      else
      {
        if( dps.getSetterDfs() != null && dps.getSetterDfs().getName().equals( signature ) )
        {
          return dps.getSetterDfs();
        }
      }
    }
    return null;
  }

  public void setFullDescription( String description )
  {
    _description = description;
  }

  public String getFullDescription()
  {
    return _description == null ? "" : _description;
  }

  public List<? extends IType> getSubtypes()
  {
    // WARNING:  This cache relies on the fact that we refresh the complete type system when something changes
    if( _subtypes == null )
    {
      _subtypes = new ArrayList<IGosuClassInternal>();
      Set<? extends CharSequence> typeNames = getTypeLoader().getAllTypeNames();
      for( CharSequence typeName : typeNames )
      {
        IType type = TypeSystem.getByFullNameIfValid(typeName.toString());
        if (type instanceof IGosuClassInternal) {
          IGosuClassInternal gosuClass = (IGosuClassInternal) type;
          if( getOrCreateTypeReference() != gosuClass && isAssignableFrom( gosuClass ) )
          {
            _subtypes.add( gosuClass );
          }
        }
      }
    }

    return _subtypes;
  }

  public boolean isDiscarded()
  {
    return _bDiscarded && !_bInitializing;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    if( bDiscarded )
    {
      unloadBackingClass();
    }
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

  public boolean isCannotCaptureSymbols()
  {
    return _bCannotCaptureSymbols;
  }

  public void setCannotCaptureSymbols( boolean bCannotCaptureSymbols )
  {
    _bCannotCaptureSymbols = bCannotCaptureSymbols;
  }
  @Override
  public List<IGosuClass> getBlocks()
  {
    if( !isValid() )
    {
      return Collections.emptyList();
    }
    else
    {
      return _blocks;
    }
  }

  @Override
  public int getBlockCount() {
    return _blocks.size();
  }

  public void addBlock( IBlockClass block )
  {
    if( _blocks == Collections.EMPTY_LIST )
    {
      _blocks = new ArrayList<IGosuClass>();
    }
    addInnerClass( (IGosuClassInternal) block );
    _blocks.add( block );
  }
  public void removeBlock( IBlockClass block )
  {
    if( !_blocks.isEmpty() )
    {
      removeInnerClass( (IGosuClassInternal) block );
      _blocks.remove( block );
    }
  }

  private void initLazyVars()
  {
    _valid =
      new LockingLazyVar<Boolean>()
      {
        @Override protected Boolean init()
        {
          return getClassStatement() != null && !getClassStatement().hasParseExceptions() ? Boolean.TRUE : Boolean.FALSE;
        }
      };
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
   */
  public void assignTypeVarsFromTypeParams( IType[] typeParams )
  {
    _typeVarDefs = new ArrayList<ITypeVariableDefinition>( 2 );
    for( IType typeParam : typeParams )
    {
      if( typeParam instanceof TypeVariableType )
      {
        TypeVariableDefinitionImpl typeVarDef = getTypeVarDefImpl(((TypeVariableType)typeParam).getTypeVarDef());
        _typeVarDefs.add( typeVarDef );
      }
    }

    if( _typeVarDefs.isEmpty() )
    {
      _typeVarDefs = Collections.emptyList();
    }
  }

  public List<ITypeVariableDefinition> getTypeVarDefs()
  {
    return _typeVarDefs;
  }

  public void setGenericTypeVariables( List<ITypeVariableDefinition> typeVarExprList )
  {
    if( typeVarExprList.isEmpty() )
    {
      typeVarExprList = Collections.emptyList();
    }
    _typeVarDefs = new ArrayList<ITypeVariableDefinition>(typeVarExprList.size());
    for( int i = 0; i < typeVarExprList.size(); i++ )
    {
      _typeVarDefs.add( getTypeVarDefImpl( typeVarExprList.get( i ) ) );
    }
    if( _genTypeVar != null && _genTypeVar.length > 0 )
    {
      // Update Type Vars (this can happen with recursive types)
      for( int i = 0; i < _typeVarDefs.size(); i++ )
      {
        _genTypeVar[i] = (GenericTypeVariable) _typeVarDefs.get(i).getTypeVar();
      }
    }
  }

  private TypeVariableDefinitionImpl getTypeVarDefImpl(ITypeVariableDefinition typeVarDef) {
    while (!(typeVarDef instanceof TypeVariableDefinitionImpl)) {
      typeVarDef = ((TypeVariableDefinition)typeVarDef).getTypeVarDef();
    }
    return (TypeVariableDefinitionImpl) typeVarDef;
  }

  public IFunctionStatement getFunctionStatement(IMethodInfo method) {
    for (IDynamicFunctionSymbol dfs : getMemberFunctions()) {
      if (method.getName().equals(dfs.getName()) && equalArgs(method.getParameters(), dfs.getArgs())) {
        return dfs.getDeclFunctionStmt();
      }
    }
    for (IDynamicFunctionSymbol dfs : getStaticFunctions()) {
      if (method.getName().equals(dfs.getName()) && equalArgs(method.getParameters(), dfs.getArgs())) {
        return dfs.getDeclFunctionStmt();
      }
    }
    return null;
  }

  private boolean equalArgs(IParameterInfo[] parameters, List<ISymbol> args) {
    if (parameters.length != args.size()) {
      return false;
    }
    for (int i = 0; i < parameters.length; i++) {
      if (!parameters[i].getFeatureType().equals(args.get(i).getType())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public IType[] getLoaderParameterizedTypes() {
    if(isHeaderCompiled() && isGenericType() )
    {
      Collection<IGosuClassInternal> types = getParameterizedTypes();
      return types.toArray(new IType[types.size()]);
    } else {
      return new IType[0];
    }
  }

  @Override
  public IFile[] getSourceFiles() {
    ISourceFileHandle sourceFileHandle = getSourceFileHandle();
    String filePath = sourceFileHandle == null ? null : sourceFileHandle.getFilePath();
    if (filePath != null) {
      return new IFile[] {CommonServices.getFileSystem().getIFile(new File(filePath))};
    } else {
      return IFile.EMPTY_ARRAY;
    }
  }

  @Override
  public boolean isAnnotation() {
    return Modifier.isAnnotation( getModifiers() )
    //## todo: kill IANNOTATION check if/after we completely remove support for old-style Gosu annotations
    || JavaTypes.IANNOTATION().isAssignableFrom(this);
  }
}
