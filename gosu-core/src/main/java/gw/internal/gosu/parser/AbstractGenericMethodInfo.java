/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.Throws;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 */
public class AbstractGenericMethodInfo extends GosuBaseAttributedFeatureInfo implements IGenericMethodInfo
{
  private static final GosuMethodParamInfo[] EMPTY_PARAMS = new GosuMethodParamInfo[0];

  private ReducedDynamicFunctionSymbol _dfs;
  private GosuMethodParamInfo[] _params;
  private List<IExceptionInfo> _exceptions;

  public AbstractGenericMethodInfo( IFeatureInfo container, DynamicFunctionSymbol dfs )
  {
    super( container );
    _dfs = (ReducedDynamicFunctionSymbol) dfs.createReducedSymbol();
    ((GosuClassTypeInfo)getGosuClass().getTypeInfo()).setModifierInfo( this, dfs.getModifierInfo() );
  }

  public String getName()
  {
    return getDfs().getName();
  }

  @Override
  public String getDisplayName()
  {
    return getDfs().getDisplayName();
  }

  @Override
  public String getDescription()
  {
    return getDfs().getFullDescription();
  }

  public boolean isStatic()
  {
    return getDfs().isStatic();
  }

  @Override
  public boolean isPrivate()
  {
    return getDfs().isPrivate();
  }

  @Override
  public boolean isInternal()
  {
    return getDfs().isInternal();
  }

  @Override
  public boolean isProtected()
  {
    return getDfs().isProtected();
  }

  @Override
  public boolean isPublic()
  {
    return getDfs().isPublic();
  }

  @Override
  public boolean isAbstract()
  {
    return getDfs().isAbstract();
  }

  @Override
  public boolean isFinal()
  {
    return getDfs().isFinal();
  }

  @Override
  public boolean isDefaultImpl() {
    // Default methods are public non-abstract instance methods declared in an interface.
    return ((getDfs().getModifiers() & (java.lang.reflect.Modifier.ABSTRACT | java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC)) ==
            java.lang.reflect.Modifier.PUBLIC) && getOwnersType().isInterface();
  }

  public IParameterInfo[] getParameters()
  {
    if( _params != null )
    {
      return _params;
    }
    return _params = makeParamDescriptors( getDfs() );
  }

  public IGenericTypeVariable[] getTypeVariables()
  {
    FunctionType funcType = (FunctionType)getDfs().getType();
    return funcType.getGenericTypeVariables();
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOwnersType(), getOwnersType() );
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }
    return TypeLord.getActualType( ((FunctionType)getDfs().getType()).getReturnType(), actualParamByVarName, true );
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    return getParameterizedParameterTypes2( null, typeParams );
  }
  public IType[] getParameterizedParameterTypes2( IType ownersType, IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( ownersType, ownersType );
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      if( actualParamByVarName.isEmpty() )
      {
        actualParamByVarName = new TypeVarToTypeMap();
      }
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }

    IType[] genParamTypes = ((FunctionType)getDfs().getType()).getParameterTypes();
    IType[] paramTypes = new IType[genParamTypes.length];
    for( int j = 0; j < genParamTypes.length; j++ )
    {
      paramTypes[j] = TypeLord.getActualType( genParamTypes[j], actualParamByVarName, true );
    }
    return paramTypes;
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    return inferTypeParametersFromArgumentTypes2( null, argTypes );
  }
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IType owningParameterizedType, IType... argTypes )
  {
    FunctionType funcType = (FunctionType)getDfs().getType();
    IType[] genParamTypes = funcType.getParameterTypes();
    IType ownersType = owningParameterizedType == null ? getOwnersType() : owningParameterizedType;
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
      if( genParamTypes.length > i )
      {
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamTypes[i], argTypes[i], map );
        ensureInferredTypeAssignableToBoundingType( actualParamByVarName, map );
      }
    }
    return map;
  }

  public static void ensureInferredTypeAssignableToBoundingType( TypeVarToTypeMap actualParamByVarName, TypeVarToTypeMap map )
  {
    for( ITypeVariableType s : new HashSet<>( map.keySet() ) )
    {
      IType inferredType = map.get( s );
      IType boundingType = actualParamByVarName.get( s );
      if( boundingType != null )
      {
        boundingType = TypeLord.getActualType( boundingType, actualParamByVarName, true );
        if( boundingType.isParameterizedType() && TypeLord.isRecursiveType( boundingType ) )
        {
          boundingType = TypeLord.getPureGenericType( boundingType );
        }
        if( !isBoundingTypeAssignableFromInferredType( inferredType, boundingType, s, map ) )
        {
          map.put( s, boundingType );
        }
      }
    }
  }

  private static boolean isBoundingTypeAssignableFromInferredType( IType inferredType, IType boundingType, ITypeVariableType tv, TypeVarToTypeMap map )
  {
    if( boundingType.isAssignableFrom( inferredType ) )
    {
      if( TypeLord.isRecursiveType( tv ) )
      {
        IType actualBoundingType = TypeLord.getActualType( tv.getBoundingType(), map );
        if( !actualBoundingType.isAssignableFrom( inferredType ) )
        {
          // If we are dealing with a structure, the types may still be structurally assignable...
          if( !(boundingType instanceof IGosuClass) || !((IGosuClass)boundingType).isStructure() ||
              !StandardCoercionManager.isStructurallyAssignable( actualBoundingType, inferredType ) )
          {
            // Bounding type not assignable from inferred type derived from its methods' signatures,
            map.put( tv, null );
          }
        }
      }
      return true;
    }

    if( boundingType instanceof IGosuClass && ((IGosuClass)boundingType).isStructure() )
    {
      if( StandardCoercionManager.isStructurallyAssignable( boundingType, inferredType ) )
      {
        if( TypeLord.isRecursiveType( tv ) )
        {
          // A recursive type var e.g., T extends Comparable<T> (if Comparable were implemented as a structure)
          // Ensure inferred type of T is assignable to Comparable<T> e.g., if T is inferred as Foo, ensure Foo is structurally assignable to concretet type, Comparable<Foo>
          // Essentially this means Foo must implement Comparable<T> contravariant with compareTo( f: Foo ), e.g., not compareTo( f: String )

          IType actualBoundingType = TypeLord.getActualType( tv.getBoundingType(), map );
          if( !StandardCoercionManager.isStructurallyAssignable( actualBoundingType, inferredType ) )
          {
            // Structural type is not assignable from inferred type derived from its methods' signatures,
            map.put( tv, null );
          }
        }
        return true;
      }
    }
    return false;
  }

  public List<IExceptionInfo> getExceptions() {
    if (_exceptions == null) {
      _exceptions = Collections.emptyList();
      List<IAnnotationInfo> annotations = getAnnotationsOfType(TypeSystem.getByFullName("gw.lang.Throws"));
      if (annotations != null) {
        for (IAnnotationInfo annotation : annotations) {
          Throws throwsInstance = (Throws) annotation.getInstance();
          if (throwsInstance != null) {
            if( _exceptions.isEmpty() ) {
              _exceptions = new ArrayList<IExceptionInfo>( 2 );
            }
            _exceptions.add(new GosuExceptionInfo(this, throwsInstance.ExceptionType().getName(), throwsInstance.ExceptionDescription()));
          }
        }
      }
    }
    return _exceptions;
  }

  IGosuClassInternal getGosuClass()
  {
    return getOwnersType();
  }

  public ReducedDynamicFunctionSymbol getDfs()
  {
    return _dfs;
  }

  private GosuMethodParamInfo[] makeParamDescriptors( ReducedDynamicFunctionSymbol dfs )
  {
    if( dfs.getArgs() == null || dfs.getArgs().isEmpty() )
    {
      return EMPTY_PARAMS;
    }

    List<IReducedSymbol> params = dfs.getArgs();
    GosuMethodParamInfo[] pd = new GosuMethodParamInfo[params.size()];
    for( int i = 0; i < pd.length; i++ )
    {
      IReducedSymbol argument = params.get(i);
      pd[i] = new GosuMethodParamInfo( this, argument, ((IFunctionType)dfs.getType()).getParameterTypes()[i] );
    }
    return pd;
  }

  @Override
  protected List<IGosuAnnotation> getGosuAnnotations()
  {
    IModifierInfo modifierInfo = ((GosuClassTypeInfo)getGosuClass().getTypeInfo()).getModifierInfo( this );
    return modifierInfo != null ? modifierInfo.getAnnotations() : Collections.<IGosuAnnotation>emptyList();
  }

  public List<IReducedSymbol> getArgs() {
    return getDfs().getArgs();
  }
}
