/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.internal.gosu.parser.StringCache;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IExpression;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.TypeVarToTypeMap;
import gw.util.concurrent.LockingLazyVar;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class FunctionType extends AbstractType implements IFunctionType, IGenericMethodInfo
{
  private static final IGenericTypeVariable[] EMPTY_TYPE_VARS = new IGenericTypeVariable[0];
  private static final IType[] EMPTY_ARGS = new IType[0];

  private IType _retType;
  private volatile IType[] _paramTypes;
  private IMethodInfo _mi;
  private String _strFunctionName;
  private IScriptPartId _scriptPart;
  private IType _owningParameterizedType;
  private volatile IGenericTypeVariable[] _typeVars;
  private int _iModifiers;
  private IType _enclosingType;
  transient private FunctionTypeInfo _typeInfo;
  transient protected Set<IType> _allTypesInHierarchy;
  transient private String _signature;
  volatile transient private Map<String, ParameterizedFunctionType> _parameterizationByParamsName;
  private LockingLazyVar<FunctionArrayType> _arrType =
    new LockingLazyVar<FunctionArrayType>()
    {
      @Override
      protected FunctionArrayType init()
      {
        return new FunctionArrayType( FunctionType.this, getFunctionClass(), getTypeLoader() );
      }
    };

  /**
   * Construct a FunctionType with the specified return type and parameter types array.
   *
   * @param strFunctionName The name of the function
   * @param retType         The return type of the function.
   * @param paramTypes      The parameter types. Can be null if no params.
   */
  public FunctionType( String strFunctionName, IType retType, IType[] paramTypes )
  {
    _retType = retType;
    if (_retType == null) {
      _retType = JavaTypes.pVOID();
    }
    _paramTypes = paramTypes == null || paramTypes.length == 0 ? EMPTY_ARGS : paramTypes;
    setName(strFunctionName);
    _mi = null;
    _allTypesInHierarchy = Collections.<IType>singleton( this );
    _typeVars = EMPTY_TYPE_VARS;
  }

  /**
   * Construct a generic FunctionType with the specified return type, parameter
   * types, and generic type variables.
   *
   * @param strFunctionName The name of the function
   * @param retType         The return type of the function.
   * @param paramTypes      The parameter types. Can be null if no params.
   * @param typeVars        The generic type variables. If null, does not create a
   *                        generic function type.
   */
  public FunctionType( String strFunctionName, IType retType, IType[] paramTypes, IGenericTypeVariable[] typeVars )
  {
    this( strFunctionName, retType, paramTypes );
    if( typeVars != null )
    {
      _typeVars = typeVars;
      for( IGenericTypeVariable gtv : typeVars )
      {
        gtv.getTypeVariableDefinition().setEnclosingType( this );
      }
    }
  }

  public FunctionType( IMethodInfo mi )
  {
    this(mi, false);
  }

  public FunctionType( IMethodInfo mi, boolean lazyTypes )
  {
    _mi = mi;

    if (!lazyTypes) {
      initLazyMethodInfoState_NoLock();
    }
    setName( mi.getDisplayName() );

    IMethodInfo backingMi = mi;
    while( backingMi instanceof IMethodInfoDelegate )
    {
      backingMi = ((IMethodInfoDelegate)backingMi).getSource();
    }
    if( backingMi instanceof IDFSBackedFeatureInfo )
    {
      _iModifiers = ((IDFSBackedFeatureInfo)backingMi).getDfs().getModifiers();
    }
    _allTypesInHierarchy = Collections.<IType>singleton( this );
  }

  private void setName(String name) {
    _strFunctionName = StringCache.get( name );
  }

  private void initLazyMethodInfoState()
  {
    TypeSystem.lock();
    try
    {
      initLazyMethodInfoState_NoLock();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void initLazyMethodInfoState_NoLock()
  {
    if( _paramTypes == null )
    {
      IParameterInfo[] pd = _mi.getParameters();
      int iArgs = pd.length;
      _paramTypes = new IType[iArgs];
      for( int i = 0; i < iArgs; i++ )
      {
        _paramTypes[i] = pd[i].getFeatureType();
      }
      if( _paramTypes.length == 0 )
      {
        _paramTypes = EMPTY_ARGS;
      }
      _typeVars = EMPTY_TYPE_VARS;
      if( _mi instanceof IGenericMethodInfo )
      {
        _typeVars = ((IGenericMethodInfo)_mi).getTypeVariables();
      }
      clearParamSignature();
    }
    _retType = _mi.getReturnType();
    if( _retType == null )
    {
      _retType = JavaTypes.pVOID();
    }
  }

  public FunctionType( FunctionType source, IType gsClass )
  {
    if( gsClass.isParameterizedType() )
    {
      _owningParameterizedType = gsClass;
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( gsClass, gsClass );

      IGenericTypeVariable[] tvs = source.getTypeVariables();
      if( tvs != null )
      {
        for( IGenericTypeVariable tv : tvs )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
        }
      }
      assignTypeVars( source.getGenericTypeVariables(), actualParamByVarName );
      assignReturnTypeFromTypeParams( source, actualParamByVarName, true );
      assignParamTypesFromTypeParams( source, actualParamByVarName, true );
    }
    else
    {
      _retType = source.getReturnType();
      if (_retType == null) {
        _retType = JavaTypes.pVOID();
      }
      _paramTypes = source._paramTypes;
      //clearParamSignature(); TODO - reenable if you dare
    }
    copyFields( source );
  }

  protected FunctionType(FunctionType source, IType returnType, IType[] paramTypes) {
    _retType = returnType;
    _paramTypes = paramTypes;
    copyFields( source );
  }

  protected FunctionType( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    assignReturnTypeFromTypeParams( source, actualParamByVarName, bKeepTypeVars );
    assignParamTypesFromTypeParams( source, actualParamByVarName, bKeepTypeVars );
    copyFields( source );
    clearParamSignature();
  }

  protected void copyFields( FunctionType source )
  {
    _mi = source._mi;
    _strFunctionName = source._strFunctionName;
    _scriptPart = source._scriptPart;
    _typeVars = _typeVars == null ? source.getGenericTypeVariables() : _typeVars;
    _typeInfo = source._typeInfo;
    _allTypesInHierarchy = source._allTypesInHierarchy;
    _signature = source._signature;
    _parameterizationByParamsName = source._parameterizationByParamsName;
    _enclosingType = source._enclosingType;
    _iModifiers = source._iModifiers;
  }

  /**
   * Create a new FunctionType based on the type parameters assigned to the type
   * vars in actualParamByVarName.
   * <p>
   * It is important to note that this form of parameterization is different
   * from calling getParameterizedType(). The latter creates a parameterization
   * of a generic function e.g., function foo&lt;T&gt;() where T is a type var
   * on the function.
   * <p>
   * This here method is for parameterizing a function type that has references
   * to external type vars e.g., class Fred&lt;T&gt; { function foo( t : T ){} }
   * where T is a type var on the containing class.
   *
   * @param source The raw form of a FunctionType having possible references to
   *   type vars defined in its containing class.
   * @param actualParamByVarName A map of concrete types by type var name
   *   e.g., "T" -> String
   * @param bKeepTypeVars Indicates whether or not a type var referenced in the
   *   source FunctionType that cannot be resolved via the actualParamByVarName
   *   map should remain as a TypeVariableType or should be converted to its
   *   bounding type.
   * @return A parameterized version of the source FunctionType
   */
  public FunctionType parameterize( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return new FunctionType( source, actualParamByVarName, bKeepTypeVars );
  }

  private void assignParamTypesFromTypeParams( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    IType[] genParamTypes = source.getParameterTypes();
    if( genParamTypes != null )
    {
      _paramTypes = new IType[genParamTypes.length];
      for( int j = 0; j < genParamTypes.length; j++ )
      {
        _paramTypes[j] = TypeSystem.getActualType( genParamTypes[j], actualParamByVarName, bKeepTypeVars );
        if( _paramTypes[j] == null )
        {
          _paramTypes[j] = genParamTypes[j];
        }
      }
      if (_paramTypes.length == 0) {
        _paramTypes = EMPTY_ARGS;
      }
      clearParamSignature();
    }
  }

  private void assignTypeVars( IGenericTypeVariable[] gtvs, TypeVarToTypeMap actualParamByVarName )
  {
    IGenericTypeVariable[] newGtvs = new IGenericTypeVariable[gtvs.length];
    for( int i = 0; i < gtvs.length; i++ )
    {
      IGenericTypeVariable gtv = gtvs[i];
      newGtvs[i] = gtv.remapBounds( actualParamByVarName );
    }
    _typeVars = newGtvs;
  }

  private void assignReturnTypeFromTypeParams( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    _retType = TypeSystem.getActualType( source.getReturnType(), actualParamByVarName, bKeepTypeVars );
    if( _retType == null )
    {
      _retType = source.getReturnType();
    }
    if (_retType == null) {
      _retType = JavaTypes.pVOID();
    }
  }

  /**
   * @return The instrinic type of this FunctionType's return type.
   */
  public IType getIntrinsicType()
  {
    return getReturnType();
  }

  public IType getReturnType()
  {
    if (_retType == null) {
      initLazyMethodInfoState();
    }
    return _retType;
  }

  public void setRetType( IType retType )
  {
    _retType = retType == null ? JavaTypes.pVOID() : retType;
  }

  public IType[] getParameterTypes()
  {
    if (_paramTypes == null) {
      initLazyMethodInfoState();
    }
    return _paramTypes;
  }

  public void setArgumentTypes( IType[] paramTypes )
  {
    _paramTypes = paramTypes == null || paramTypes.length == 0 ? EMPTY_ARGS : paramTypes;
    clearParamSignature();
  }

  public IMethodInfo getMethodInfo()
  {
    if( _mi == null )
    {
      if( getScriptPart() != null )
      {
        IType type = getScriptPart().getContainingType();
        if( type instanceof IGosuClass )
        {
          IGosuClass gsClass = (IGosuClass)type;
          _mi = gsClass.getTypeInfo().getMethod( type, getName(), getParameterTypes() );
        } else if (type != null) {
          _mi = type.getTypeInfo().getMethod(getName(), getParameterTypes());
        }
      }
    }
    return _mi;
  }

  public IFeatureInfo getMethodOrConstructorInfo()
  {
    IMethodInfo mi = getMethodInfo();
    if( mi == null )
    {
      if( getScriptPart() != null )
      {
        IType type = getScriptPart().getContainingType();
        if( type instanceof IGosuClass && type.getRelativeName().equals( getName() ) )
        {
          IGosuClass gsClass = (IGosuClass)type;
          return gsClass.getTypeInfo().getConstructor( type, getParameterTypes() );
        }
      }
    }
    return mi;
  }

  private void clearParamSignature()
  {
    _signature = null;
  }

  public String getParamSignature()
  {
    if( _signature == null )
    {
      IType[] paramTypes = getParameterTypes();
      if( paramTypes.length == 0 )
      {
        return _signature = _strFunctionName + "()";
      }

      String strParams = _strFunctionName + "(";
      for( int i = 0; i < paramTypes.length; i++ )
      {
        strParams += (i == 0 ? "" : ", " ) + (paramTypes[i] == null ? "" : paramTypes[i].getName());
      }
      strParams += ")";

      _signature = strParams;
    }

    return _signature;
  }

  public String getParamSignatureForCurrentModule() {
    String sig;
    IType[] paramTypes = getParameterTypes();
    if( paramTypes.length == 0 )
    {
      sig = _strFunctionName + "()";
    }
    else
    {
      String strParams = _strFunctionName + "(";
      for( int i = 0; i < paramTypes.length; i++ )
      {
        strParams += (i == 0 ? "" : ", " ) + (paramTypes[i] == null ? "" : getParamTypeNameFromJavaBackedType(paramTypes[i]));
      }
      strParams += ")";

      sig = strParams;
    }
    return sig;
  }

  public static String getParamTypeNameFromJavaBackedType(IType paramType) {
    return TypeSystem.getTypeFromJavaBackedType(paramType).getName();
  }

  public String getName()
  {
    return _strFunctionName;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return getName();
  }

  public String getNamespace()
  {
    IType enclosingType = getEnclosingType();
    return enclosingType != null
           ? enclosingType.getName()
           : null;
  }

  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  public boolean isInterface()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    return EMPTY_TYPE_ARRAY;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType getSupertype()
  {
    return null;
  }

  public IType getEnclosingType()
  {
    IType type = null;
    if( _scriptPart != null && (type = _scriptPart.getContainingType()) != null )
    {
      return type;
    }
    final IFeatureInfo methodInfo = getMethodOrConstructorInfo();
    if( methodInfo != null )
    {
      return methodInfo.getOwnersType();
    }
    return _enclosingType;
  }

  public IType getGenericType()
  {
    return isGenericType() ? this : null;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return getGenericTypeVariables().length > 0;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    if (_typeVars == null) {
      initLazyMethodInfoState();
    }
    return _typeVars;
  }

  public ParameterizedFunctionType getParameterizedType( IType... typeParams )
  {
    if( typeParams == null || typeParams.length == 0 )
    {
      throw new IllegalArgumentException( "Parameter types required." );
    }

    if( _parameterizationByParamsName == null )
    {
      TypeSystem.lock();
      try
      {
        if( _parameterizationByParamsName == null )
        {
          _parameterizationByParamsName = new ConcurrentHashMap<String, ParameterizedFunctionType>( 2 );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }

    String strNameOfParams = TypeSystem.getNameOfParams( typeParams, false, true );
    ParameterizedFunctionType parameterizedType = _parameterizationByParamsName.get( strNameOfParams );
    if( parameterizedType == null )
    {
      parameterizedType = new ParameterizedFunctionType( this, typeParams );
      _parameterizationByParamsName.put( strNameOfParams, parameterizedType );
    }
    return parameterizedType;
  }

  public IFunctionType inferParameterizedTypeFromArgTypesAndContextType(IType[] argTypes, IType ctxType)
  {
    TypeVarToTypeMap map = TypeVarToTypeMap.EMPTY_MAP;
    if( argTypes.length > 0 )
    {
      if( getMethodInfo() == null )
      {
        map = inferTypeParametersFromArgumentTypes( argTypes );
      }
      else
      {
        map = ((IGenericMethodInfo)getMethodInfo()).inferTypeParametersFromArgumentTypes2( _owningParameterizedType, argTypes );
      }
    }

    IGenericTypeVariable[] typeVars = getGenericTypeVariables();
    if( typeVars.length == 0 )
    {
      return this;
    }

    IType[] typeParams = new IType[typeVars.length];
    for( int i = 0; i < typeVars.length; i++ )
    {
      IGenericTypeVariable typeVar = typeVars[i];
      IType inferredType = map.get( typeVar.getTypeVariableDefinition().getType() );
      if( inferredType == null && ctxType != null )
      {
        //try to infer type from context type
        TypeVarToTypeMap returnTypeVars = new TypeVarToTypeMap();
        TypeSystem.inferTypeVariableTypesFromGenParamTypeAndConcreteType( getReturnType(), ctxType, returnTypeVars, false );
        ITypeVariableType typeVarType = typeVar.getTypeVariableDefinition().getType();
        inferredType = returnTypeVars.get( typeVarType );
        if( inferredType == null || !typeVar.getBoundingType().isAssignableFrom( inferredType ) )
        {
          inferredType = TypeSystem.replaceTypeVariableTypeParametersWithBoundingTypes( typeVar.getBoundingType(), getEnclosingType() );
        }
      }
      typeParams[i] = inferredType;
      if( typeParams[i] == null )
      {
        return this;
      }
    }
    return getParameterizedType( typeParams );
  }

  public IType[] getTypeParameters()
  {
    return null;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    return _allTypesInHierarchy;
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
    return _arrType.get();
  }

  public Object makeArrayInstance( int iLength )
  {
    return TypeSystem.get( getFunctionClass() ).makeArrayInstance( iLength );
  }

  private IJavaClassInfo getFunctionClass()
  {
    return TypeSystem.getGosuClassLoader().getFunctionClassForArity( getReturnType() != JavaTypes.pVOID(), getParameterTypes().length ).getBackingClassInfo();
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return TypeSystem.get( getFunctionClass() ).getArrayComponent( array, iIndex );
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    TypeSystem.get( getFunctionClass() ).setArrayComponent( array, iIndex, value );
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return TypeSystem.get( getFunctionClass() ).getArrayLength( array );
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    return isAssignableFrom( type, true );
  }
  public boolean isAssignableFrom( IType type, boolean bContravariant )
  {
    if( this == type )
    {
      return true;
    }

    if( type instanceof IBlockClass )
    {
      return isAssignableFrom( ((IBlockClass)type).getBlockType() );
    }
    if( type instanceof FunctionType )
    {
      FunctionType that = (FunctionType)type;
      //contravariant arg types
      if( areParamsCompatible( this, that, bContravariant ) )
      {
        //covariant return types
        return areReturnTypesAssignable( that );
      }
    }
    return false;
  }

  protected boolean areReturnTypesAssignable( FunctionType from ) {
    IType toType = getReturnType();
    IType fromType = from.getReturnType();
    if( isThisReturnTypeNotVoidThatReturnTypeVoid( toType, fromType ) ) {
      return false;
    }
    return toType == fromType ||
           toType.isAssignableFrom( fromType ) ||
           StandardCoercionManager.isStructurallyAssignable( toType, fromType ) ||
           StandardCoercionManager.arePrimitiveTypesAssignable( toType, fromType ) ||
           toType == GosuParserTypes.NULL_TYPE();
  }

  private boolean isThisReturnTypeNotVoidThatReturnTypeVoid( IType toType, IType fromType ) {
    return toType != JavaTypes.pVOID() && toType != JavaTypes.VOID() && (fromType == JavaTypes.pVOID() || fromType == JavaTypes.VOID());
  }

  public boolean areParamsCompatible( IFunctionType fromType ) {
    return areParamsCompatible( this, fromType );
  }

  public static boolean areParamsCompatible( IFunctionType toType, IFunctionType fromType )
  {
    return areParamsCompatible( toType, fromType, true );
  }
  private static boolean areParamsCompatible( IFunctionType toType, IFunctionType fromType, boolean bContravariant )
  {
    IType[] toParams = toType.getParameterTypes();
    IType[] fromParams = fromType.getParameterTypes();

    if( toParams.length != fromParams.length )
    {
      return false;
    }

    for( int i = 0; i < fromParams.length; i++ )
    {
      IType toParamType = toParams[i];
      IType fromParamType = fromParams[i];
      if( bContravariant )
      {
        if( !StandardCoercionManager.arePrimitiveTypesAssignable( fromParamType, toParamType ) ) {
              //## todo: this condition re type vars is a hack; we need to tighten this up
          if( !(toParamType instanceof ITypeVariableType) &&
              !fromParamType.isAssignableFrom( toParamType ) &&
              !StandardCoercionManager.isStructurallyAssignable( fromParamType, toParamType ) &&
              !fromParamType.isDynamic() && !toParamType.isDynamic() )
          {
            return false;
          }
        }
      }
      else if( !fromParamType.equals( toParamType ) && !(toParamType instanceof ITypeVariableType) )
      {
        return false;
      }
    }
    return true;
  }

  public static IType[] findContravariantParams( IType[] lhsParams, IType[] rhsParams )
  {
    if( lhsParams.length != rhsParams.length )
    {
      return null;
    }

    IType[] types = new IType[lhsParams.length];
    for( int i = 0; i < rhsParams.length; i++ )
    {
      IType myParamType = lhsParams[i];
      IType otherParamType = rhsParams[i];
      if( StandardCoercionManager.arePrimitiveTypesAssignable( otherParamType, myParamType ) )
      {
        types[i] = myParamType;
      }
      else if( StandardCoercionManager.arePrimitiveTypesAssignable( myParamType, otherParamType ) )
      {
        types[i] = otherParamType;
      }
      else if( otherParamType.isAssignableFrom( myParamType ) )
      {
        types[i] = myParamType;
      }
      else if( myParamType.isAssignableFrom( myParamType ) )
      {
        types[i] = otherParamType;
      }
      else if( otherParamType.isDynamic() || myParamType.isDynamic() )
      {
        types[i] = IDynamicType.instance();
      }
      else
      {
        return null;
      }
    }
    return types;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    return _typeInfo == null ? _typeInfo = new FunctionTypeInfo( this ) : _typeInfo;
  }

  public void unloadTypeInfo()
  {
    _typeInfo = null;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return this;
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    if( _iModifiers == 0 )
    {
      return _mi != null ? Modifier.getModifiersFrom( _mi ) : Modifier.PUBLIC;
    }
    return _iModifiers;
  }
  public void setModifiers( int iModifiers )
  {
    _iModifiers = iModifiers;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public IScriptPartId getScriptPart()
  {
    return _scriptPart;
  }

  @Override
  public IType newInstance( IType[] paramTypes, IType returnType )
  {
    FunctionType functionType = new FunctionType(this._strFunctionName, returnType, paramTypes, cloneTypeVars() );
    functionType._iModifiers = _iModifiers;
    if (getScriptPart() == null && _mi != null) {
      if (_mi instanceof MethodInfoDelegate) {
        functionType.setScriptPart(new ScriptPartId(((MethodInfoDelegate)_mi).getSource().getOwnersType(), null));
      } else {
        functionType.setScriptPart(new ScriptPartId(_mi.getOwnersType(), null));
      }
    } else {
      functionType.setScriptPart(getScriptPart());
    }
    return functionType;
  }

  private IGenericTypeVariable[] cloneTypeVars() {
    IGenericTypeVariable[] typeVars = new IGenericTypeVariable[_typeVars.length];
    for (int i = 0; i < typeVars.length; i++) {
      typeVars[i] = _typeVars[i].copy();
    }
    return typeVars;
  }

  public void setScriptPart( IScriptPartId scriptPart )
  {
    _scriptPart = scriptPart;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !getClass().isInstance( o ) )
    {
      return false;
    }

    final FunctionType funcType = (FunctionType)o;

    // Name
    if( !funcType.getDisplayName().equals( getDisplayName() ) &&
        !(o instanceof IBlockType) &&
        !(this instanceof IBlockType))
    {
      return false;
    }

    // Enclosing Type
    if( !areEnclosingTypesEqual( funcType ) )
    {
      return false;
    }

    // Function Type Variables
    if( isGenericType() != funcType.isGenericType() )
    {
      return false;
    }
    if( isGenericType() )
    {
      IGenericTypeVariable[] gtvs = getGenericTypeVariables();
      IGenericTypeVariable[] thatGtvs = funcType.getGenericTypeVariables();
      if( gtvs.length != thatGtvs.length )
      {
        return false;
      }
      for( int i = 0; i < gtvs.length; i++ )
      {
        if( !gtvs[i].equals( thatGtvs[i]) )
        {
          return false;
        }
      }
    }

    // Parameter Types
    if( funcType.getParameterTypes().length != getParameterTypes().length )
    {
      return false;
    }
    for( int i = 0; i < getParameterTypes().length; i++ )
    {
      if( !areSameTypes( getParameterTypes()[i], funcType.getParameterTypes()[i] ) )
      {
        return false;
      }
    }

    // Return Type
    return areSameTypes( getReturnType(), funcType.getReturnType() );
  }

  protected boolean areEnclosingTypesEqual( FunctionType funcType )
  {
    if( areSameTypes( getEnclosingType(), funcType.getEnclosingType() ) )
    {
      return true;
    }
    // crappy fix for when block types are compared as parameter types for methodinfo lookup
    return getEnclosingType() == null || funcType.getEnclosingType() == null;
  }

  protected boolean areSameTypes( IType t1, IType t2 )
  {
    return t1 instanceof INonLoadableType
           ? t1.equals( t2 )
           : t1 == t2;
  }

  public int hashCode()
  {
    int result = getDisplayName().hashCode();
    for( int i = 0; i < getParameterTypes().length; i++ )
    {
      if( getParameterTypes()[i] instanceof INonLoadableType )
      {
        result = 31 * result + getParameterTypes()[i].hashCode();
      }
      else
      {
        result = 31 * result + getParameterTypes()[i].getName().hashCode();
      }
    }

    if( getReturnType() instanceof INonLoadableType )
    {
      result = 31 * result + getReturnType().hashCode();
    }
    else
    {
      result = 31 * result + getReturnType().getName().hashCode();
    }
    return result;
  }

  public String toString()
  {
    return getParamSignature().toString() + ":" + getReturnType().getName();
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IType owningParameterizedType, IType... argTypes )
  {
    return inferTypeParametersFromArgumentTypes( argTypes );
  }
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    IType[] genParamTypes = getParameterTypes();
    TypeVarToTypeMap map = new TypeVarToTypeMap();
    for( int i = 0; i < argTypes.length; i++ )
    {
      if( genParamTypes.length > i )
      {
        TypeSystem.inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamTypes[i], argTypes[i], map, false );
      }
    }
    return map;
  }

  public IGenericTypeVariable[] getTypeVariables()
  {
    return getGenericTypeVariables();
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }
    return TypeSystem.getActualType( getReturnType(), actualParamByVarName, false );
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    return getParameterizedParameterTypes2( null, typeParams );
  }
  public IType[] getParameterizedParameterTypes2( IType ownersType, IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }

    IType[] genParamTypes = getParameterTypes();
    IType[] paramTypes = new IType[genParamTypes.length];
    for( int j = 0; j < genParamTypes.length; j++ )
    {
      paramTypes[j] = TypeSystem.getActualType( genParamTypes[j], actualParamByVarName, false );
    }
    return paramTypes;
  }

  public FunctionType getRuntimeType()
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();

    actualParamByVarName = mapTypes( actualParamByVarName, getParameterTypes() );
    actualParamByVarName = mapTypes( actualParamByVarName, getReturnType() );

    return actualParamByVarName.size() != 0
           ? parameterize( this, actualParamByVarName, false )
           : this;
  }

  //Move Intrinsic type helper up here 
  private TypeVarToTypeMap mapTypes( TypeVarToTypeMap actualParamByVarName, IType... types )
  {
    for( int i = 0; i < types.length; i++ )
    {
      IType type = types[i];
      if( type instanceof ITypeVariableType )
      {
        actualParamByVarName.put( (ITypeVariableType)types[i], types[i] );
      }
      if( type instanceof ITypeVariableArrayType )
      {
        mapTypes( actualParamByVarName, type.getComponentType() );
      }
      if( type.isParameterizedType() )
      {
        IType[] parameters = type.getTypeParameters();
        mapTypes( actualParamByVarName, parameters );
      }
      if( type instanceof IFunctionType )
      {
        IFunctionType funType = (IFunctionType)type;
        mapTypes( actualParamByVarName, funType.getReturnType() );
        IType[] paramTypes = funType.getParameterTypes();
        for( IType paramType : paramTypes )
        {
          mapTypes( actualParamByVarName, paramType );
        }
      }
    }
    return actualParamByVarName;
  }

  public boolean isDiscarded()
  {
    return false;
  }

  public void setDiscarded( boolean bDiscarded )
  {
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
  public IExpression[] getDefaultValueExpressions()
  {
    if( getMethodOrConstructorInfo() instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)getMethodOrConstructorInfo()).getDefaultValueExpressions();
    }
    return IExpression.EMPTY_ARRAY;
  }

  @Override
  public boolean hasOptionalParams()
  {
    for( IExpression o : getDefaultValueExpressions() )
    {
      if( o != null )
      {
        return true;
      }
    }
    return false;
  }

  public String[] getParameterNames()
  {
    IFeatureInfo miOrCi = getMethodOrConstructorInfo();
    if( miOrCi instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)miOrCi).getParameterNames();
    }
    return new String[0];
  }

  public IType getOwningParameterizedType()
  {
    return _owningParameterizedType;
  }
  public void setEnclosingType( IType gosuClass )
  {
    _enclosingType = gosuClass;
  }
}
