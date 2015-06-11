/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.types.ConstructorType;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.reflect.*;
import gw.lang.reflect.features.*;
import gw.lang.reflect.features.BoundPropertyChainReference;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.GosuObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a feature literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public class FeatureLiteral extends Expression implements IFeatureLiteralExpression
{
  private static final int MAX_BLOCK_ARGS = 16;
  private IExpression _root;
  IFeatureInfo _feature;
  private IType[] _parameterTypes;
  private List<IExpression> _boundArgs;
  private IBlockType _blockType;

  public FeatureLiteral( Expression rootExpr )
  {
    _root = rootExpr;
  }

  public boolean resolveProperty( String propName )
  {
    IType typeToResolveAgainst = getRootTypeToResolveFeaturesAgainst();

    IPropertyInfo property;
    if( typeToResolveAgainst.getTypeInfo() instanceof IRelativeTypeInfo )
    {
      property = ((IRelativeTypeInfo)typeToResolveAgainst.getTypeInfo()).getProperty( typeToResolveAgainst, propName );
    }
    else
    {
      property = typeToResolveAgainst.getTypeInfo().getProperty( propName );
    }

    boolean foundProp = property != null;
    _feature = foundProp ? property : ErrorType.getInstance().getTypeInfo().getProperty( propName );

    resolveExpressionType();

    return foundProp;
  }

  public boolean resolveMethod( String methodName, List<IType> typesList )
  {
    IType[] argTypes = typesList.toArray( new IType[typesList.size()] );

    IType typeToResolveAgainst = getRootTypeToResolveFeaturesAgainst();
    ITypeInfo typeInfo = typeToResolveAgainst.getTypeInfo();
    IMethodInfo methodInfo;

    if( typeInfo instanceof IRelativeTypeInfo )
    {
      methodInfo = ITypeInfo.FIND.callableMethod(((IRelativeTypeInfo)typeInfo).getMethods( typeToResolveAgainst), methodName, argTypes );
    }
    else
    {
      methodInfo = ITypeInfo.FIND.callableMethod( typeInfo.getMethods(), methodName, argTypes );
    }

    methodInfo = ensureExactMatch( methodInfo, argTypes );

    if( methodInfo == null && argTypes.length == 0 )
    {
      methodInfo = getSingleMethodWithName( methodName, typeToResolveAgainst, typeInfo );
    }

    boolean foundMethod = methodInfo != null;

    methodInfo = foundMethod ? methodInfo : ErrorType.getInstance().getTypeInfo().getMethod( methodName, argTypes );

    setFeature( methodInfo, null );

    return foundMethod;
  }

  public boolean resolveConstructor( List<IType> typesList )
  {
    IType[] argTypes = typesList.toArray( new IType[typesList.size()] );

    IType typeToResolveAgainst = getRootTypeToResolveFeaturesAgainst();
    ITypeInfo typeInfo = typeToResolveAgainst.getTypeInfo();
    IConstructorInfo constructorInfo;

    if( typeInfo instanceof IRelativeTypeInfo )
    {
      constructorInfo = ((IRelativeTypeInfo)typeInfo).getConstructor( typeToResolveAgainst, argTypes );
    }
    else
    {
      constructorInfo = typeInfo.getConstructor( argTypes );
    }

    constructorInfo = ensureExactMatch( constructorInfo, argTypes );

    if( constructorInfo == null && argTypes.length == 0 )
    {
      constructorInfo = getSingleConsructor( typeToResolveAgainst, typeInfo );
    }

    boolean foundConstructor = constructorInfo != null;

    constructorInfo = foundConstructor ? constructorInfo : ErrorType.getInstance().getTypeInfo().getConstructor( argTypes );

    setFeature( constructorInfo, null );

    return foundConstructor;
  }

  public void setFeature( IHasParameterInfos feature, List<IExpression> arguments )
  {
    _feature = feature;
    _boundArgs = arguments;
    resolveExpressionType();
  }

  @Override
  public String toString()
  {
    return _root.toString() + featureRepresentation();
  }

  public boolean isConstructorLiteral()
  {
    return _feature instanceof IConstructorInfo;
  }

  public boolean isMethodLiteral()
  {
    return _feature instanceof IMethodInfo;
  }

  public boolean isPropertyLiteral()
  {
    return _feature instanceof IPropertyInfo;
  }

  public IExpression getRoot()
  {
    return _root;
  }

  public IExpression getFinalRoot()
  {
    IExpression root = getRoot();
    if( root instanceof IFeatureLiteralExpression )
    {
      return ((IFeatureLiteralExpression)root).getFinalRoot();
    }
    return root;
  }

  public IType getFinalRootType()
  {
    if( _root instanceof TypeLiteral )
    {
      return ((TypeLiteral)_root).getType().getType();
    }
    else if( _root instanceof FeatureLiteral )
    {
      return ((FeatureLiteral)_root).getFinalRootType();
    }
    else
    {
      return _root.getType();
    }
  }

  public IType getRootType()
  {
    if( _root instanceof TypeLiteral )
    {
      return ((TypeLiteral)_root).getType().getType();
    }
    else if( _root instanceof FeatureLiteral )
    {
      return ((FeatureLiteral)_root).getFeatureReturnType();
    }
    else
    {
      return _root.getType();
    }
  }

  public String getPropertyName()
  {
    return _feature.getName();
  }

  public String getMethodName()
  {
    return _feature.getDisplayName();
  }

  public List<IExpression> getBoundArgs()
  {
    return _boundArgs;
  }

  public boolean isBound()
  {
    if( _root instanceof FeatureLiteral )
    {
      return ((FeatureLiteral)_root).isBound();
    }
    else
    {
      return !(_root instanceof TypeLiteral);
    }
  }

  public IType[] getParameterTypes()
  {
    return _parameterTypes;
  }

  public boolean isStaticish()
  {
    if( _feature instanceof IMethodInfo )
    {
      return ((IMethodInfo)_feature).isStatic();
    }
    else if( _feature instanceof IPropertyInfo )
    {
      return ((IPropertyInfo)_feature).isStatic();
    }
    else
    {
      return _feature instanceof IConstructorInfo;
    }
  }

  public IFeatureInfo getFeature()
  {
    return _feature;
  }

  public List<? extends IInvocableType> getFunctionTypes( String name )
  {
    if( name.equals( Keyword.KW_construct.toString() ) )
    {
      return getConstructorTypes();
    }
    else
    {
      return getMethodTypes( name );
    }
  }

  //============================================================
  //  Internals
  //============================================================

  private IMethodInfo getSingleMethodWithName( String methodName, IType typeToResolveAgainst, ITypeInfo typeInfo )
  {
    MethodList methods;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      methods = ((IRelativeTypeInfo)typeInfo).getMethods( typeToResolveAgainst );
    }
    else
    {
      methods = typeInfo.getMethods();
    }

    IMethodInfo match = null;
    for( IMethodInfo possibleMatch : methods )
    {
      if( possibleMatch.getDisplayName().equals( methodName ) )
      {
        if( match != null )
        {
          return null; // more than one, bail
        }
        else
        {
          match = possibleMatch;
        }
      }
    }
    return match;
  }

  private IConstructorInfo getSingleConsructor( IType typeToResolveAgainst, ITypeInfo typeInfo )
  {
    List<? extends IConstructorInfo> constructors;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      constructors = ((IRelativeTypeInfo)typeInfo).getConstructors( typeToResolveAgainst );
    }
    else
    {
      constructors = typeInfo.getConstructors();
    }

    return constructors.size() == 1 ? constructors.get( 0 ) : null;
  }

  private <T extends IHasParameterInfos> T ensureExactMatch( T methodInfo, IType[] argTypes )
  {
    if( methodInfo == null )
    {
      return null;
    }
    IParameterInfo[] parameters = methodInfo.getParameters();
    if( argTypes.length != parameters.length )
    {
      return null;
    }

    IType[] boundParamTypes = boundGenericFunctionTypeVariables( methodInfo, getParameterTypes( methodInfo ) );

    for( int i = 0; i < boundParamTypes.length; i++ )
    {
      IType parameterType = boundParamTypes[i];
      if( !GosuObjectUtil.equals( parameterType, argTypes[i] ) )
      {
        return null;
      }
    }
    return methodInfo;
  }

  private IType[] boundGenericFunctionTypeVariables( IHasParameterInfos methodInfo, IType[] parametersTypes )
  {
    List<IType> typesToBound = getFunctionTypeVarsToBound( methodInfo );
    List<IType> boundParamTypes = new ArrayList<>();
    for( IType parameter : parametersTypes )
    {
      boundParamTypes.add( TypeLord.boundTypes( parameter, typesToBound ) );
    }
    return boundParamTypes.toArray( new IType[boundParamTypes.size()] );
  }

  private List<IType> getFunctionTypeVarsToBound( IHasParameterInfos methodInfo )
  {
    List<IType> typesToBound = new ArrayList<>();
    if( methodInfo instanceof IGenericMethodInfo )
    {
      IGenericTypeVariable[] typeVars = ((IGenericMethodInfo)methodInfo).getTypeVariables();
      for( IGenericTypeVariable typeVar : typeVars )
      {
        typesToBound.add( typeVar.getTypeVariableDefinition().getType() );
      }
    }
    return typesToBound;
  }

  private IBlockType makeBlockType( IType returnType, IType[] params, List<String> argNames )
  {
    if( params.length > MAX_BLOCK_ARGS )
    {
      return new BlockType( returnType, Arrays.copyOfRange( params, 0, 15 ), argNames, Collections.<IExpression>emptyList() );
    }
    else
    {
      return new BlockType( returnType, params, argNames, Collections.<IExpression>emptyList() );
    }
  }

  private List<? extends IInvocableType> getMethodTypes( String name )
  {
    ArrayList<IInvocableType> functionTypes = new ArrayList<>();
    IType rootType = getRootTypeToResolveFeaturesAgainst();
    ITypeInfo typeInfo = rootType.getTypeInfo();
    List<? extends IMethodInfo> methods;
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      methods = ((IRelativeTypeInfo)typeInfo).getMethods( rootType );
    }
    else
    {
      methods = typeInfo.getMethods();
    }
    for( IMethodInfo mi : methods )
    {
      if( mi.getDisplayName().equals( name ) )
      {
        functionTypes.add( new FunctionType( mi ) );
      }
    }
    return functionTypes;
  }

  private List<? extends IInvocableType> getConstructorTypes()
  {
    ArrayList<IInvocableType> constructorTypes = new ArrayList<>();
    IType rootType = getRootTypeToResolveFeaturesAgainst();
    ITypeInfo typeInfo = rootType.getTypeInfo();

    List<? extends IConstructorInfo> constructors;

    if( typeInfo instanceof IRelativeTypeInfo )
    {
      constructors = ((IRelativeTypeInfo)typeInfo).getConstructors( rootType );
    }
    else
    {
      constructors = typeInfo.getConstructors();
    }

    for( IConstructorInfo ci : constructors )
    {
      constructorTypes.add( new ConstructorType( ci ) );
    }

    return constructorTypes;
  }

  private void resolveExpressionType()
  {
    if( _feature instanceof IPropertyInfo )
    {
      setType( resolvePropertyLiteralType( (IPropertyInfo)_feature ) );
    }
    else if( _feature instanceof IMethodInfo )
    {
      setType( resolveMethodLiteralType( (IMethodInfo)_feature ) );
    }
    else if( _feature instanceof IConstructorInfo )
    {
      setType( resolveConstructorLiteralType( (IConstructorInfo)_feature ) );
    }
    else
    {
      setType( ErrorType.getInstance() );
    }
  }

  private IType resolvePropertyLiteralType( IPropertyInfo propertyInfo )
  {
    _parameterTypes = new IType[0];
    IType propertyReferenceType;
    if( getRoot() instanceof FeatureLiteral )
    {
      propertyReferenceType = TypeSystem.get( isBound() ? BoundPropertyChainReference.class : PropertyChainReference.class );
    }
    else
    {
      propertyReferenceType = TypeSystem.get( isBound() ? BoundPropertyReference.class : PropertyReference.class );
    }
    return propertyReferenceType.getParameterizedType( getFinalRootType(), propertyInfo.getFeatureType() );
  }

  private IType resolveMethodLiteralType( IMethodInfo methodInfo )
  {
    IType rawType = TypeSystem.get( isBound() ? BoundMethodReference.class : MethodReference.class );

    _parameterTypes = boundGenericFunctionTypeVariables( methodInfo, getParameterTypes( methodInfo ) );

    _blockType = makeBlockType( TypeLord.boundTypes( methodInfo.getReturnType(), getFunctionTypeVarsToBound( methodInfo ) ),
                                adjustParametersForFeature( methodInfo, _parameterTypes ), argNames( methodInfo ) );

    return rawType.getParameterizedType( getFinalRootType(), _blockType );
  }

  private IType resolveConstructorLiteralType( IConstructorInfo constructorInfo )
  {
    IType rawType = TypeSystem.get( ConstructorReference.class );

    _parameterTypes = boundGenericFunctionTypeVariables( constructorInfo, getParameterTypes( constructorInfo ) );

    _blockType = makeBlockType( constructorInfo.getOwnersType(),
                                adjustParametersForFeature( constructorInfo, _parameterTypes ),
                                argNames(constructorInfo) );

    return rawType.getParameterizedType( getFinalRootType(), _blockType );
  }

  private List<String> argNames( IHasParameterInfos hasParams )
  {
    if( hasBoundArgs( hasParams ) )
    {
      return Collections.emptyList();
    }
    ArrayList<String> names = new ArrayList<>();
    for( int i = 0; i < hasParams.getParameters().length && i <= MAX_BLOCK_ARGS; i++ )
    {
      IParameterInfo iParameterInfo = hasParams.getParameters()[i];
      names.add( iParameterInfo.getName() );
    }
    return names;
  }


  private IType[] adjustParametersForFeature( IHasParameterInfos feature, IType[] params )
  {
    if( hasBoundArgs( feature ) )
    {
      params = new IType[0]; // clear out parameters if there are bound values
    }
    if( hasImplicitFirstArg() )
    {
      ArrayList<IType> combined = new ArrayList<>();
      combined.add( getRootType() );
      combined.addAll( Arrays.asList( params ) );
      params = combined.toArray( new IType[combined.size()] );
    }
    return params;
  }

  private boolean hasBoundArgs( IHasParameterInfos feature )
  {
    return _boundArgs != null && _boundArgs.size() > 0 && _boundArgs.size() == feature.getParameters().length;
  }

  private boolean hasImplicitFirstArg()
  {
    return !isBound() && !isStaticish();
  }

  private IType[] getParameterTypes( IHasParameterInfos hasParameterInfos )
  {
    IParameterInfo[] parameters = hasParameterInfos.getParameters();
    List<IType> types = new ArrayList<>();
    for( IParameterInfo parameter : parameters )
    {
      types.add( parameter.getFeatureType() );
    }
    return types.toArray( new IType[types.size()] );
  }


  private String featureRepresentation()
  {
    return _feature == null ? "<ERROR>" : _feature.getName();
  }

  private IType getRootTypeToResolveFeaturesAgainst()
  {
    if( _root instanceof FeatureLiteral )
    {
      return ((FeatureLiteral)_root).getFeatureReturnType();
    }
    else if( _root instanceof TypeLiteral )
    {
      return ((TypeLiteral)_root).getType().getType();
    }
    else
    {
      return _root.getType();
    }
  }

  private IType getFeatureReturnType()
  {
    if( _feature instanceof IPropertyInfo )
    {
      return ((IPropertyInfo)_feature).getFeatureType();
    }
    else if( _feature instanceof IMethodInfo )
    {
      return ((IMethodInfo)_feature).getReturnType();
    }
    else if( _feature instanceof IConstructorInfo )
    {
      return ((IConstructorInfo)_feature).getType();
    }
    else
    {
      return ErrorType.getInstance();
    }
  }
}
