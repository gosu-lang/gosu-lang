/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.java.classinfo.JavaSourceDefaultValue;
import gw.lang.parser.IExpression;
import gw.lang.reflect.java.JavaSourceElement;
import gw.lang.Deprecated;
import gw.lang.GosuShop;
import gw.lang.PublishedName;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IExceptionNode;
import gw.lang.javadoc.IMethodNode;
import gw.lang.javadoc.IParamNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.ILocationInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.SimpleParameterInfo;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.ClassInfoUtil;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.JavaExceptionInfo;

import gw.lang.reflect.java.Parameter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class JavaMethodInfo extends JavaBaseFeatureInfo implements IJavaMethodInfo
{
  private static final int UNINITED = 0;
  private static final int TRUE_ENC = 1;
  private static final int FALSE_ENC = 2;

  private IJavaMethodDescriptor _md;
  private boolean _forceHidden;
  private IParameterInfo[] _params;
  private IType _retType;
  private IMethodCallHandler _callHandler;
  private IGenericTypeVariable[] _typeVars;
  private int _staticCache = UNINITED;
  private List<IExceptionInfo> _exceptions;
  private IDocRef<IMethodNode> _methodDocs = new IDocRef<IMethodNode>() {
    @Override
    public IMethodNode get() {
      if (getContainer() instanceof JavaTypeInfo) {
        IClassDocNode classDocs = ((JavaTypeInfo) getContainer()).getDocNode().get();
        return classDocs == null ? null : classDocs.getMethod(_md);
      } else {
        return null;
      }
    }
  };
  private String _name;
  private String _signature;

  /**
   * @param container Typically this will be the containing ITypeInfo
   * @param md        The method descriptor (from BeanInfo)
   */
  public JavaMethodInfo(IFeatureInfo container, IJavaMethodDescriptor md, boolean forceHidden) {
    super(container);
    _md = md;
    _forceHidden = forceHidden;
    _name = _md.getName();
    if (_md.getMethod().isAnnotationPresent( PublishedName.class)) {
      _name = (String) _md.getMethod().getAnnotation( PublishedName.class).getFieldValue("value");
    }
    _signature = makeSignature();
  }

  @Override
  public IParameterInfo[] getParameters()
  {
    if( _params == null )
    {
      _params = convertParameterDescriptors();
    }
    return _params;
  }

  @Override
  public IType getReturnType()
  {
    if( _retType != null )
    {
      return _retType;
    }

    IType retType = ClassInfoUtil.getActualReturnType( _md.getMethod().getGenericReturnType(), initTypeVarMap(), true );
    if( TypeSystem.isDeleted( retType ) )
    {
      return null;
    }

    retType = ClassInfoUtil.getPublishedType(retType, _md.getMethod().getEnclosingClass());

    retType = TypeLord.replaceRawGenericTypesWithDefaultParameterizedTypes( retType );

    _retType = retType;

    return retType;
  }

  public static TypeVarToTypeMap addEnclosingTypeParams( IType declaringClass, TypeVarToTypeMap actualParamByVarName )
  {
    while( declaringClass.getEnclosingType() != null && !Modifier.isStatic( declaringClass.getModifiers() ) )
    {
      declaringClass = declaringClass.getEnclosingType();
      IGenericTypeVariable[] typeVariables = declaringClass.getGenericTypeVariables();
      if( typeVariables != null )
      {
        for( IGenericTypeVariable typeVariable : typeVariables )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( typeVariable.getTypeVariableDefinition().getType(),
                                    typeVariable.getTypeVariableDefinition() != null
                                    ? typeVariable.getTypeVariableDefinition().getType()
                                    : new TypeVariableType( declaringClass, typeVariable ) );
        }
      }
    }
    return actualParamByVarName;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    List<IAnnotationInfo> annotations = super.getDeclaredAnnotations();
    if (getMethodDocs().get() != null && getMethodDocs().get().isDeprecated()) {
      annotations.add(GosuShop.getAnnotationInfoFactory().createJavaAnnotation(makeDeprecated(getMethodDocs().get().getDeprecated()), this));
    }
    return annotations;
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables()
  {
    if( _typeVars == null )
    {
      _typeVars = _md.getMethod().getTypeVariables( this );
    }
    return _typeVars;
  }


  @Override
  public IType getParameterizedReturnType( IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName =
      TypeLord.mapTypeByVarName( getOwnersType(), _md.getMethod().getEnclosingClass().getJavaType() );
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }
    return _md.getMethod().getGenericReturnType().getActualType( actualParamByVarName, true );
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    return getParameterizedParameterTypes2( null, typeParams );
  }
  public IType[] getParameterizedParameterTypes2( IType ownersType, IType... typeParams )
  {
    IType ot = ownersType == null ? getOwnersType() : ownersType;
    TypeVarToTypeMap actualParamByVarName =
      TypeLord.mapTypeByVarName( ot, _md.getMethod().getEnclosingClass().getJavaType() );
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }

    return ClassInfoUtil.getActualTypes(_md.getMethod().getGenericParameterTypes(), actualParamByVarName, true);
  }

  //  <T extends CharSequence> T[] foo( ArrayList<? extends T>[] s ) { return null; }
  @Override
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    return inferTypeParametersFromArgumentTypes2( null, argTypes );
  }
  @Override
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IType owningParameterizedType, IType... argTypes )
  {
    IJavaClassType[] genParamTypes = _md.getMethod().getGenericParameterTypes();
    IType ownersType = owningParameterizedType == null ? getOwnersType() : owningParameterizedType;
    IType[] types = convertTypes( genParamTypes, ownersType );
    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( ownersType, ownersType );
    IGenericTypeVariable[] typeVars = getTypeVariables();
    for( IGenericTypeVariable tv : typeVars )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      if( !TypeLord.isRecursiveType( tv.getTypeVariableDefinition().getType(), tv.getBoundingType() ) )
      {
        actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getBoundingType() );
      }
      else
      {
        actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), TypeLord.getPureGenericType( tv.getBoundingType() ) );
      }
    }

    TypeVarToTypeMap map = new TypeVarToTypeMap();

    for( int i = 0; i < argTypes.length; i++ )
    {
      if( types.length > i )
      {
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( types[i], argTypes[i], map );
        AbstractGenericMethodInfo.ensureInferredTypeAssignableToBoundingType( actualParamByVarName, map );
      }
    }
    return map;
//## todo: revisit this, attempting to eliminate types in contravariant params from interfering with inference from types in covariant positions (i saw it in a cartoon once)
//    List<IType> filtered = Arrays.asList( types ); //removeFunctionalTypesCoveredByNonFunctionalTypes( types );
//    for( int i = 0; i < argTypes.length; i++ )
//    {
//      if( types.length > i )
//      {
//        IType type = types[i];
//
//        if( filtered.contains( type ) )
//        {
//          TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( type, argTypes[i], map );
//          AbstractGenericMethodInfo.ensureInferredTypeAssignableToBoundingType( actualParamByVarName, map );
//        }
//      }
//    }
//    return map;
  }

//  private List<IType> removeFunctionalTypesCoveredByNonFunctionalTypes( IType[] types )
//  {
//    if( types.length < 2 )
//    {
//      return Arrays.asList( types );
//    }
//
//    Map<ITypeVariableType, List<Pair<IType, Boolean>>> covered = new HashMap<>();
//    for( IType type: types )
//    {
//      coverTypeVar( type, type, covered, false );
//    }
//    List<IType> res = new ArrayList<>();
//    for( List<Pair<IType, Boolean>> t: covered.values() )
//    {
//      res.addAll( t.stream().map( Pair::getFirst ).collect( Collectors.toList() ) );
//    }
//    return res;
//  }
//
//  private void coverTypeVar( IType saveType, IType type, Map<ITypeVariableType, List<Pair<IType, Boolean>>> coveredTypes, boolean bFunctional )
//  {
//    if( type instanceof ITypeVariableType )
//    {
//      List<Pair<IType, Boolean>> types = coveredTypes.get( type );
//      if( !bFunctional || types == null || types.isEmpty() )
//      {
//        if( types == null )
//        {
//          types = new ArrayList<>();
//          coveredTypes.put( (ITypeVariableType)type, types );
//        }
//        if( !bFunctional )
//        {
//          removeFunctionalTypes( types );
//        }
//        types.add( new Pair<>( saveType, bFunctional ) );
//      }
//      else if( bFunctional && containsOnlyFunctionalTypes( types ) )
//      {
//        types.add( new Pair<>( saveType, bFunctional ) );
//      }
//    }
//    else if( type.isParameterizedType() )
//    {
//      IFunctionType funcType = FunctionToInterfaceCoercer.getRepresentativeFunctionType( type );
//      if( funcType != null )
//      {
//        coverTypeVar( saveType, funcType, coveredTypes, bFunctional );
//      }
//      else
//      {
//        for( IType typeArg : type.getTypeParameters() )
//        {
//          coverTypeVar( saveType, typeArg, coveredTypes, bFunctional || isFunctionalInterface( type ) );
//        }
//      }
//    }
//    else if( type instanceof IFunctionType )
//    {
//      coverTypeVar( saveType, ((IFunctionType)type).getReturnType(), coveredTypes, bFunctional );
//      for( IType paramType: ((IFunctionType)type).getParameterTypes() )
//      {
//        coverTypeVar( saveType, paramType, coveredTypes, true );
//      }
//    }
//    else if( type.isArray() )
//    {
//      coverTypeVar( saveType, type.getComponentType(), coveredTypes, bFunctional );
//    }
//  }
//
//  private Boolean containsOnlyFunctionalTypes( List<Pair<IType, Boolean>> types )
//  {
//    return types.get( 0 ).getSecond();
//  }
//
//  private void removeFunctionalTypes( List<Pair<IType, Boolean>> types )
//  {
//    for( Iterator<Pair<IType, Boolean>> iter = types.iterator(); iter.hasNext(); )
//    {
//      Pair<IType, Boolean> pair = iter.next();
//      if( pair.getSecond() )
//      {
//        iter.remove();
//      }
//    }
//  }
//
//  private boolean isFunctionalInterface( IType type )
//  {
//    return FunctionToInterfaceCoercer.getSingleMethod( type ) != null;
//  }
//
//  private boolean covered( Map<ITypeVariableType, List<IType>> coveredTypes, IType tv )
//  {
//    List<IType> types = coveredTypes.get( tv );
//    return types != null && types.contains( tv );
//  }

  private IType[] convertTypes( IJavaClassType[] genParamTypes, IType ownersType )
  {
    IType ot = ownersType == null ? getOwnersType() : ownersType;
    TypeVarToTypeMap actualParamByVarName =
      TypeLord.mapTypeByVarName( ot, _md.getMethod().getEnclosingClass().getJavaType() );
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
    }
    IType[] types = new IType[genParamTypes.length];
    for( int i = 0; i < genParamTypes.length; i++ )
    {
      types[i] = genParamTypes[i].getActualType( actualParamByVarName, true );
    }
    return types;
  }

  @Override
  public IMethodCallHandler getCallHandler()
  {
    if( _callHandler != null )
    {
      return _callHandler;
    }
    IJavaClassMethod method = this._md.getMethod();
    if (!(method instanceof MethodJavaClassMethod)) {
      return null;
    }
    return _callHandler = new MethodCallAdapter( ((MethodJavaClassMethod)method).getJavaMethod() );
  }

  @Override
  public String getReturnDescription()
  {
    return getMethodDocs().get() == null ? "" : getMethodDocs().get().getReturnDescription();
  }

  @Override
  public List<IExceptionInfo> getExceptions()
  {
    if( _exceptions == null )
    {
      IJavaClassMethod method = _md.getMethod();
      IJavaClassInfo[] classes = method.getExceptionTypes();
      _exceptions = new ArrayList<>();
      for (int i = 0; i < classes.length; i++) {
        final IJavaClassInfo exceptionClass = classes[i];
        _exceptions.add(new JavaExceptionInfo(this, exceptionClass, new IDocRef<IExceptionNode>() {
          @Override
          public IExceptionNode get() {
            return getMethodDocs().get() == null ? null : getMethodDocs().get().getException(exceptionClass);
          }
        }));
      }
    }

    // merge in methods exceptions with the annotations
    return _exceptions;
  }

  @Override
  public String getName() {
    return _signature;
  }

  private String makeSignature() {
    String name = getDisplayName();
    name += TypeInfoUtil.getTypeVarList( this, true );
    name += "(";
    IParameterInfo[] parameterInfos = getParameters();
    if (parameterInfos.length > 0) {
      name += " ";
      for (int i = 0; i < parameterInfos.length; i++) {
        IParameterInfo iParameterInfo = parameterInfos[i];
        if (i != 0) {
          name += ", ";
        }
        name += iParameterInfo.getFeatureType().getName();
      }
      name += " ";
    }
    name += ")";
    return name;
  }

  @Override
  public String getDisplayName()
  {
    return _name;
  }

  @Override
  public String getShortDescription()
  {
    return getMethodDocs().get() == null ? null : getMethodDocs().get().getDescription();
  }

  @Override
  public String getDescription()
  {
    return getMethodDocs().get() == null ? null : getMethodDocs().get().getDescription();
  }

  @Override
  public boolean isHidden() {
    return _forceHidden || super.isHidden();
  }

  @Override
  protected boolean isDefaultEnumFeature()
  {
    if( getOwnersType().isEnum() )
    {
      String name = getName();
      return isStatic() && (name.equals( "values()" ) || name.equals( "valueOf( java.lang.String )" ));
    }
    else
    {
      return false;
    }
  }

  @Override
  public boolean isVisible(IScriptabilityModifier constraint) {
    return !_forceHidden && super.isVisible(constraint);
  }

  @Override
  public boolean isStatic()
  {
    if( _staticCache == UNINITED )
    {
      synchronized( this )
      {
        if( _staticCache == UNINITED )
        {
          if( Modifier.isStatic( _md.getMethod().getModifiers() ) )
          {
            _staticCache = TRUE_ENC;
          }
          else
          {
            _staticCache = FALSE_ENC;
          }
        }
      }
    }
    return _staticCache == TRUE_ENC;
  }

  @Override
  public boolean isPrivate()
  {
    return Modifier.isPrivate( _md.getMethod().getModifiers() );
  }

  @Override
  public boolean isInternal()
  {
    return !isPrivate() && !isPublic() && !isProtected();
  }

  @Override
  public boolean isProtected()
  {
    return Modifier.isProtected( _md.getMethod().getModifiers() );
  }

  @Override
  public boolean isPublic()
  {
    return Modifier.isPublic( _md.getMethod().getModifiers() );
  }

  @Override
  public boolean isAbstract()
  {
    return Modifier.isAbstract( _md.getMethod().getModifiers() );
  }

  @Override
  public boolean isFinal()
  {
    return Modifier.isFinal( _md.getMethod().getModifiers() );
  }

  @Override
  public boolean isDefaultImpl() {
    // Default methods are public non-abstract instance methods declared in an interface.
    return ((getModifiers() & (java.lang.reflect.Modifier.ABSTRACT | java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC)) ==
            java.lang.reflect.Modifier.PUBLIC) && getOwnersType().isInterface();
  }

  @Override
  public boolean isDeprecated()
  {
    return isJavadocDeprecated() || super.isDeprecated() || getMethod().isAnnotationPresent( Deprecated.class ) || getMethod().isAnnotationPresent( java.lang.Deprecated.class );
  }

  private boolean isJavadocDeprecated()
  {
    return (getModifiers() & Opcodes.ACC_DEPRECATED) > 0;
  }

  @Override
  public String getDeprecatedReason() {
    String deprecated = super.getDeprecatedReason();
    if (isDeprecated() && deprecated == null) {
      IAnnotationInfo gwDeprecated = getMethod().getAnnotation( Deprecated.class );
      return gwDeprecated == null ? null : (String) gwDeprecated.getFieldValue( "value" );
    }
    return deprecated;
  }

  @Override
  public boolean hasAnnotationDefault() {

    Object defaultValue = getMethod().getDefaultValue();
    return defaultValue != null &&
           defaultValue != JavaSourceDefaultValue.NULL;
  }

  @Override
  public Object getAnnotationDefault()
  {
    return getMethod().getDefaultValue();
  }

  private IParameterInfo[] convertParameterDescriptors()
  {
    IJavaClassType[] paramTypes = _md.getMethod().getGenericParameterTypes();
    return convertGenericParameterTypes( this, initTypeVarMap(), paramTypes, _md.getMethod().getEnclosingClass(), _md.getMethod().getParameterInfos() );
  }

  private TypeVarToTypeMap initTypeVarMap()
  {
    IType declaringClass = _md.getMethod().getEnclosingClass().getJavaType();
    TypeVarToTypeMap actualParamByVarName;
    if( isStatic() )
    {
      actualParamByVarName = TypeVarToTypeMap.EMPTY_MAP;
    }
    else
    {
      actualParamByVarName = TypeLord.mapTypeByVarName( getOwnersType(), declaringClass );
      actualParamByVarName = addEnclosingTypeParams( declaringClass, actualParamByVarName );
    }
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(),
                                tv.getTypeVariableDefinition() != null
                                ? tv.getTypeVariableDefinition().getType()
                                : new TypeVariableType( getOwnersType(), tv ) );
    }
    return actualParamByVarName;
  }

  static IParameterInfo[] convertGenericParameterTypes( IFeatureInfo container,
                                                        TypeVarToTypeMap actualParamByVarName,
                                                        IJavaClassType[] paramTypes,
                                                        IJavaClassInfo declaringClass,
                                                        List<Parameter> paramInfos )
  {
    if( paramTypes == null )
    {
      return null;
    }

    IParameterInfo[] pi = new IParameterInfo[paramTypes.length];
    for( int i = 0; i < paramTypes.length; i++ )
    {
      IType parameterType = null;

      if( paramTypes[i] != null )
      {
        parameterType = paramTypes[i].getActualType( actualParamByVarName, true );
      }

      if( parameterType == null )
      {
        parameterType = TypeSystem.getErrorType();
      }

      parameterType = ClassInfoUtil.getPublishedType(parameterType, declaringClass);

      parameterType = TypeLord.replaceRawGenericTypesWithDefaultParameterizedTypes( parameterType );

      pi[i] = new SimpleParameterInfo( container, parameterType, i, paramInfos.isEmpty() ? null : paramInfos.get( i ).getName() );
    }
    return pi;

  }

  private boolean isVarArgs()
  {
    return (getMethod().getModifiers() & 0x00000080) != 0;
  }

  @Override
  public IExpression[] getDefaultValueExpressions()
  {
    IParameterInfo[] parameters = getParameters();
    IExpression[] defaults = new IExpression[parameters.length];
    if( !isVarArgs() )
    {
      return defaults;
    }

    NewExpression expr = new NewExpression();
    IType type = parameters[parameters.length - 1].getFeatureType();
    type = TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( type );
    expr.setType( type );
    defaults[parameters.length-1] = expr;

    return defaults;
  }

  @Override
  public String[] getParameterNames()
  {
    List<String> names = new ArrayList<>();
    for( IParameterInfo pi: getParameters() )
    {
      names.add( pi.getName() );
    }
    return names.toArray( new String[names.size()] );
  }

  @Override
  public IJavaClassMethod getMethod()
  {
    return _md.getMethod();
  }

  @Override
  public String toString()
  {
    return getName();
  }

  @Override
  protected IJavaAnnotatedElement getAnnotatedElement()
  {
    return _md.getMethod();
  }

  @Override
  protected boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _md.isVisibleViaFeatureDescriptor(constraint);
  }

  @Override
  protected boolean isHiddenViaFeatureDescriptor() {
    return _md.isHiddenViaFeatureDescriptor();
  }

  @Override
  public IDocRef<IParamNode> getDocsForParam(final int paramIndex) {
    return new IDocRef<IParamNode>() {
      @Override
      public IParamNode get() {
        if (getMethodDocs().get() != null) {
          List<? extends IParamNode> list = getMethodDocs().get().getParams();
          if (list.size() > paramIndex) {
            return list.get(paramIndex);
          }
        }
        return null;
      }
    };
  }

  @Override
  public IDocRef<IMethodNode> getMethodDocs() {
    return _methodDocs;
  }

  @Override
  public Method getRawMethod() {
    return ((MethodJavaClassMethod)_md.getMethod()).getJavaMethod();
  }

  @Override
  public int getModifiers() {
    return _md.getMethod().getModifiers();
  }

  @Override
  public ILocationInfo getLocationInfo()
  {
    if( getMethod() instanceof JavaSourceElement )
    {
      return getMethod().getLocationInfo();
    }
    return super.getLocationInfo();
  }
}
