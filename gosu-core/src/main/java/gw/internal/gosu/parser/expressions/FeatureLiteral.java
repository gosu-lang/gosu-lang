/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.ErrorTypeInfo;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.types.ConstructorType;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IInvocableType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.features.BoundComplexPropertyChainReference;
import gw.lang.reflect.features.BoundMethodReference;
import gw.lang.reflect.features.BoundPropertyReference;
import gw.lang.reflect.features.BoundSimplePropertyChainReference;
import gw.lang.reflect.features.ComplexPropertyChainReference;
import gw.lang.reflect.features.ConstructorReference;
import gw.lang.reflect.features.MethodChainReference;
import gw.lang.reflect.features.MethodReference;
import gw.lang.reflect.features.PropertyReference;
import gw.lang.reflect.features.SimplePropertyChainReference;
import gw.lang.reflect.java.JavaTypes;

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
  private IExpression _root;
  IFeatureInfo _feature;
  private List<IExpression> _boundArgs;

  public FeatureLiteral( Expression rootExpr )
  {
    _root = rootExpr;
  }

  public boolean resolveProperty( String propName )
  {
    IType typeToResolveAgainst = getRootTypeToResolveAgainst();
    IPropertyInfo property;
    if( typeToResolveAgainst.getTypeInfo() instanceof IRelativeTypeInfo )
    {
      property = ((IRelativeTypeInfo)typeToResolveAgainst.getTypeInfo()).getProperty( typeToResolveAgainst, propName );
    }
    else
    {
      property = typeToResolveAgainst.getTypeInfo().getProperty( propName );
    }
    if( property == null )
    {
      _feature = ErrorType.getInstance().getTypeInfo().getProperty( propName );
      return false;
    }
    else
    {
      _feature = property;
      return true;
    }
  }

  public boolean resolveMethod( String methodName, IType... argTypes )
  {
    IType typeToResolveAgainst = getRootTypeToResolveAgainst();
    IMethodInfo methodInfo;
    ITypeInfo typeInfo = typeToResolveAgainst.getTypeInfo();
    if( typeInfo instanceof IRelativeTypeInfo )
    {
      methodInfo = ((IRelativeTypeInfo)typeInfo).getMethod( typeToResolveAgainst, methodName, argTypes );
    }
    else
    {
      methodInfo = typeInfo.getMethod( methodName, argTypes );
    }
    if( methodInfo == null && argTypes.length == 0)
    {
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        methodInfo = findSingleMethodMatchingName( methodName, ((IRelativeTypeInfo)typeInfo).getMethods( typeToResolveAgainst ) );
      }
      else
      {
        methodInfo = findSingleMethodMatchingName( methodName, typeInfo.getMethods() );
      }
    }
    if( methodInfo == null )
    {
      _feature = ErrorType.getInstance().getTypeInfo().getMethod( methodName, argTypes );
      return false;
    }
    else
    {
      _feature = methodInfo;
      return true;
    }
  }

  private IMethodInfo findSingleMethodMatchingName( String methodName, List<? extends IMethodInfo> methods )
  {
    IMethodInfo match = null;
    for( IMethodInfo possibleMatch : methods )
    {
      if( possibleMatch.getDisplayName().equals( methodName ) )
      {
        if( match != null )
        {
          return null;
        }
        else
        {
          match = possibleMatch;
        }
      }
    }
    return match;
  }

  public boolean resolveConstructor( IType... argTypes )
  {
    IType typeToResolveAgainst = getRootTypeToResolveAgainst();
    ITypeInfo typeInfo = typeToResolveAgainst.getTypeInfo();
    IConstructorInfo constructorInfo = null;
    if( !(typeInfo instanceof ErrorTypeInfo) )
    {
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        constructorInfo = ((IRelativeTypeInfo) typeInfo).getConstructor( typeToResolveAgainst, argTypes );
      }
      else
      {
        constructorInfo = typeInfo.getConstructor( argTypes );
      }
    }
    if( constructorInfo == null )
    {
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        List<? extends IConstructorInfo> constructors = ((IRelativeTypeInfo)typeInfo).getConstructors( typeToResolveAgainst );
        if( constructors != null && constructors.size() == 1 )
        {
          constructorInfo = constructors.get( 0 );
        }
      }
      else
      {
        List<? extends IConstructorInfo> constructors = typeInfo.getConstructors();
        if( constructors != null && constructors.size() == 1 )
        {
          constructorInfo = constructors.get( 0 );
        }
      }
    }
    if( constructorInfo == null )
    {
      _feature = JavaTypes.OBJECT().getTypeInfo().getConstructor();
      return false;
    }
    else
    {
      _feature = constructorInfo;
      return true;
    }
  }

  @Override
  public String toString()
  {
    return _root.toString() + featureRepresentation();
  }

  private String featureRepresentation()
  {
    return _feature == null ? "<ERROR>" : _feature.getName();
  }

  private IType getRootTypeToResolveAgainst()
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

  public IType getFeatureReturnType()
  {
    if( isPropertyLiteral() )
    {
      return ((IPropertyInfo)_feature).getFeatureType();
    }
    else if( isMethodLiteral() )
    {
      return ((IMethodInfo)_feature).getReturnType();
    }
    else if( isConstructorLiteral() )
    {
      return ((IConstructorInfo)_feature).getType();
    }
    else
    {
      return ErrorType.getInstance();
    }
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

  public void resolveType()
  {
    setType( getExpressionType() );
  }

  public IExpression getRoot()
  {
    return _root;
  }

  public IExpression getFinalRoot() {
    IExpression root = getRoot();
    if( root instanceof IFeatureLiteralExpression )
    {
      return ((IFeatureLiteralExpression)root).getFinalRoot();
    }
    return root;
  }


  public IType getFinalRootType() {
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


  public IType[] getParameterTypes()
  {
    ArrayList<IType> types = new ArrayList<IType>();

    IParameterInfo[] parameterInfos;
    if( _feature instanceof IMethodInfo )
    {
      parameterInfos = ((IMethodInfo)_feature).getParameters();
    }
    else if( _feature instanceof IConstructorInfo )
    {
      parameterInfos = ((IConstructorInfo)_feature).getParameters();
    }
    else
    {
      parameterInfos = new IParameterInfo[0];
    }

    for( IParameterInfo pi : parameterInfos )
    {
      types.add( pi.getFeatureType() );
    }
    return types.toArray( new IType[types.size()] );
  }

  public IType getExpressionType() {
    if( isPropertyLiteral() )
    {
      IExpression root = getRoot();
      Class clazz;
      if( root instanceof TypeLiteral )
      {
        clazz = PropertyReference.class;
      }
      else if( root instanceof FeatureLiteral )
      {
        if( JavaTypes.getGosuType( PropertyReference.class ).isAssignableFrom( root.getType() ) ||
            JavaTypes.getGosuType( SimplePropertyChainReference.class ).isAssignableFrom( root.getType() ))
        {
          clazz = SimplePropertyChainReference.class;
        }
        else if( JavaTypes.getGosuType( BoundPropertyReference.class ).isAssignableFrom( root.getType() ) ||
                  JavaTypes.getGosuType( BoundSimplePropertyChainReference.class ).isAssignableFrom( root.getType() )  )
        {
          clazz = BoundSimplePropertyChainReference.class;
        }
        else
        {
          if( ((FeatureLiteral)root).isBound() )
          {
            clazz = BoundComplexPropertyChainReference.class;
          }
          else
          {
            clazz = ComplexPropertyChainReference.class;
          }
        }
      }
      else
      {
        clazz = BoundPropertyReference.class;
      }
      IType rawType = TypeSystem.get( clazz );
      IType parameterizedType = rawType.getParameterizedType( getFinalRootType(), getFeatureReturnType() );
      return parameterizedType;
    }
    else if( isMethodLiteral() )
    {
      IExpression root = getRoot();
      Class clazz;
      if( root instanceof TypeLiteral )
      {
        clazz = MethodReference.class;
      }
      else if( root instanceof FeatureLiteral )
      {
        clazz = MethodChainReference.class;
      }
      else
      {
        clazz = BoundMethodReference.class;
      }
      IType rawType = TypeSystem.get( clazz );
      IMethodInfo mi = (IMethodInfo)_feature;
      IType bt = makeBlockType(mi.getReturnType(), getFeatureReferenceParameters());
      return rawType.getParameterizedType(getFinalRootType(), bt);
    }
    else if( isConstructorLiteral() )
    {
      if( getRoot() instanceof TypeLiteral )
      {
        Class<ConstructorReference> clazz = ConstructorReference.class;
        IType parameterizedType = TypeSystem.get( clazz ).getParameterizedType( getFinalRootType(), makeBlockType( getFinalRootType(), getFeatureReferenceParameters() ) );
        return parameterizedType;
      }
      else
      {
        return ErrorType.getInstance();        
      }
    }
    else
    {
      return ErrorType.getInstance();
    }
  }

  private IType makeBlockType( IType returnType, IType[] params )
  {
    if( params.length > 16 )
    {
      return JavaTypes.OBJECT();
    }
    else
    {
      return new BlockType( returnType, params, Collections.<String>emptyList(), Collections.<IExpression>emptyList() );
    }
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
    else return _feature instanceof IConstructorInfo;
  }

  public IType[] getFeatureReferenceParameters() {
    ArrayList<IType> actualParams = new ArrayList<IType>();
    IExpression root = getRoot();
    if( root instanceof TypeLiteral )
    {
      if( _feature instanceof IPropertyInfo && !((IPropertyInfo)_feature).isStatic() )
      {
        actualParams.add( ((TypeLiteral)root).evaluate() );
      }
      if( _feature instanceof IMethodInfo && !((IMethodInfo)_feature).isStatic() )
      {
        actualParams.add( ((TypeLiteral)root).evaluate() );
      }
    }
    else if( root instanceof FeatureLiteral )
    {
      actualParams.addAll( Arrays.asList( ((FeatureLiteral)root).getFeatureReferenceParameters() ) );
    }
    if( _boundArgs == null )
    {
      if( _feature instanceof IConstructorInfo )
      {
        for( IParameterInfo pi : ((IConstructorInfo)_feature).getParameters() )
        {
          actualParams.add( pi.getFeatureType() );
        }
      }
      else if( _feature instanceof IMethodInfo )
      {
        for( IParameterInfo mi : ((IMethodInfo)_feature).getParameters() )
        {
          actualParams.add( mi.getFeatureType() );
        }
      }
    }
    return actualParams.toArray( new IType[actualParams.size()] );
  }

  public IFeatureInfo getFeature()
  {
    return _feature;
  }

  public List<? extends IInvocableType> getFunctionTypes( String name )
  {
    ArrayList<IInvocableType> lst = new ArrayList<IInvocableType>();
    IType rootType = getRootTypeToResolveAgainst();
    if( name.equals( Keyword.KW_construct.toString() ) )
    {
      ITypeInfo typeInfo = rootType.getTypeInfo();
      List<? extends IConstructorInfo> constructors;
      if (typeInfo instanceof IRelativeTypeInfo) {
        constructors = ((IRelativeTypeInfo) typeInfo).getConstructors(rootType);
      } else {
        constructors = typeInfo.getConstructors();
      }
      for( IConstructorInfo ci : constructors)
      {
        lst.add( new ConstructorType( ci ) );
      }
    }
    else
    {
      ITypeInfo typeInfo = rootType.getTypeInfo();
      List<? extends IMethodInfo> methods;
      if (typeInfo instanceof IRelativeTypeInfo) {
        methods = ((IRelativeTypeInfo) typeInfo).getMethods(rootType);
      } else {
        methods = typeInfo.getMethods();
      }
      for( IMethodInfo mi : methods )
      {
        if( mi.getDisplayName().equals( name ) )
        {
          lst.add( new FunctionType( mi ) );
        }
      }
    }
    return lst;
  }

  public void setBoundFeature( IFeatureInfo fi, List<IExpression> arguments )
  {
    _feature = fi;
    _boundArgs = arguments;
  }

  public List<IExpression> getBoundArgs()
  {
    return _boundArgs;
  }

  public boolean isBound()
  {
    if( _root instanceof FeatureLiteral )
    {
      return ((FeatureLiteral) _root).isBound();
    }
    else
    {
      return !(_root instanceof TypeLiteral);
    }
  }
}
