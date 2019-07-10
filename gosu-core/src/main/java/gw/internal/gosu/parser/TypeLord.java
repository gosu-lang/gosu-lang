/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.lang.reflect.IDynamicType;
import gw.internal.gosu.parser.expressions.BlockType;
import gw.internal.gosu.parser.expressions.TypeVariableDefinition;
import gw.internal.gosu.parser.expressions.TypeVariableDefinitionImpl;
import gw.lang.Gosu;
import gw.lang.parser.AsmTypeVarMatcher;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IExpression;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.RawTypeVarMatcher;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.TypeSystemAwareCache;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.Variance;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeVariableArrayType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.java.asm.AsmPrimitiveType;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.AsmWildcardType;
import gw.lang.reflect.java.asm.IAsmType;
import gw.lang.reflect.module.IModule;
import gw.util.GosuObjectUtil;
import gw.util.Pair;
import gw.util.concurrent.Cache;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Predicate;

/**
 */
public class TypeLord
{
  // LRUish cache of assignability results (recent tests indicate 99% hit rates)
  private static final TypeSystemAwareCache<Pair<IType, IType>, Boolean> ASSIGNABILITY_CACHE =
    TypeSystemAwareCache.make( "Assignability Cache", getAssignabilityCacheSize(),
                               new Cache.MissHandler<Pair<IType, IType>, Boolean>()
                               {
                                 public final Boolean load( Pair<IType, IType> key )
                                 {
                                   return areGenericOrParameterizedTypesAssignableInternal( key.getFirst(), key.getSecond() );
                                 }
                               } );

  private static final int DEFAULT_ASSIGNABILITY_CACHE_SIZE = 1000;

  private static int getAssignabilityCacheSize()
  {
    try
    {
      String assignabilityCacheSize = System.getProperty("assignabilityCacheSize");
      if (assignabilityCacheSize != null)
      {
        return Math.max(Integer.valueOf(assignabilityCacheSize), DEFAULT_ASSIGNABILITY_CACHE_SIZE);
      }
    }
    catch (Exception e)
    {
      new RuntimeException("Unable to set value of assignability cache due to an exception, the default value will be used", e).printStackTrace();
    }
    return DEFAULT_ASSIGNABILITY_CACHE_SIZE;
  }

  public static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IJavaClassInfo cls )
  {
    Set<IJavaClassInfo> classSet = new HashSet<IJavaClassInfo>();
    addAllClassesInClassHierarchy( cls, classSet );

    Set<IType> intrinsicTypeSet = new HashSet<IType>();
    intrinsicTypeSet.add(JavaTypes.OBJECT());
    for(IJavaClassInfo classInfo : classSet) {
      intrinsicTypeSet.add( classInfo.getJavaType() );
    }

    return intrinsicTypeSet;
  }

  public static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IType type )
  {
    HashSet<IType> typeSet = new HashSet<IType>();
    addAllClassesInClassHierarchy( type, typeSet );
    return typeSet;
  }

  public static boolean encloses( IType type, IType inner )
  {
    return inner != null && (inner.getEnclosingType() == type || encloses( type, inner.getEnclosingType() ));
  }

  public static boolean enclosingTypeInstanceInScope( IType type, IGosuClassInternal inner )
  {
    return inner != null && !inner.isStatic() &&
           ((type != null && type.isAssignableFrom( inner.getEnclosingType() )) ||
            enclosingTypeInstanceInScope( type, (IGosuClassInternal)inner.getEnclosingType() ));
  }                                

  public static Set<IType> getArrayVersionsOfEachType( Set componentTypes )
  {
    Set<IType> allTypes = new HashSet<IType>();
    allTypes.add( JavaTypes.OBJECT() );
    Iterator it = componentTypes.iterator();
    while( it.hasNext() )
    {
      IType type = (IType)it.next();
      allTypes.add( type.getArrayType() );
    }

    return allTypes;
  }

  public static IType getActualType( Type type, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualType( type, actualParamByVarName, false );
  }

  public static IType getActualType( Type type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return getActualType( type, actualParamByVarName, bKeepTypeVars, new LinkedHashSet<Type>() );
  }
  public static IType getActualType( Type type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, LinkedHashSet<Type> recursiveTypes )
  {
    IType retType;
    if( type instanceof Class )
    {
      retType = TypeSystem.get( (Class)type );
    }
    else if( type instanceof TypeVariable )
    {
      retType = actualParamByVarName.getByMatcher( (TypeVariable)type, RawTypeVarMatcher.instance() );
      if( retType == null )
      {
        // the type must come from the map, otherwise it comes from a context where there is no argument for the type var, hence the error type
        return ErrorType.getInstance( ((TypeVariable)type).getName() );
      }
      if( bKeepTypeVars && retType instanceof ITypeVariableType && ((TypeVariable)type).getName().equals( retType.getName() ) && ((ITypeVariableType)retType).getBoundingType() != null )
      {
        IType boundingType = ((ITypeVariableType)retType).getBoundingType();
        IType actualBoundingType = getActualType( boundingType, actualParamByVarName, bKeepTypeVars );
        if( !actualBoundingType.getName().equals( boundingType.toString() ) )
        {
          ITypeVariableDefinition typeVarDef = ((ITypeVariableType)retType).getTypeVarDef();
          if( typeVarDef instanceof TypeVariableDefinition )
          {
            typeVarDef = ((TypeVariableDefinition)typeVarDef).getTypeVarDef();
          }
          TypeVariableDefinitionImpl tvd = ((TypeVariableDefinitionImpl)typeVarDef).clone( actualBoundingType );
          retType = new TypeVariableType( tvd, ((ITypeVariableType)retType).getTypeVarDef().getEnclosingType() instanceof IFunctionType );
        }
      }
      else if( !bKeepTypeVars )
      {
        retType = getDefaultParameterizedTypeWithTypeVars( retType );
      }
    }
    else if( type instanceof WildcardType )
    {
      Type bound = ((WildcardType)type).getUpperBounds()[0];
      Type lowerBound = maybeGetLowerBound( (WildcardType)type, actualParamByVarName, bKeepTypeVars, recursiveTypes );
      if( lowerBound != null )
      {
        bound = lowerBound;
      }
      retType = getActualType( bound, actualParamByVarName, bKeepTypeVars, recursiveTypes );
      if( retType instanceof TypeVariableType )
      {
        ITypeVariableDefinition tvd = ((ITypeVariableType)retType).getTypeVarDef().clone();
        retType = new TypeVariableType( tvd, ((ITypeVariableType)retType).isFunctionStatement() );
        ((TypeVariableType)retType).getTypeVarDef().setVariance( ((WildcardType)type).getLowerBounds().length == 0 ? Variance.WILD_COVARIANT : Variance.WILD_CONTRAVARIANT );
      }
    }
    else if( type instanceof ParameterizedType )
    {
      recursiveTypes.add( type );
      try
      {
        IType genType = getActualType( ((ParameterizedType)type).getRawType(), actualParamByVarName, bKeepTypeVars, recursiveTypes );
        Type[] typeArgs = ((ParameterizedType)type).getActualTypeArguments();
        if( typeArgs == null || typeArgs.length == 0 )
        {
          retType = genType;
        }
        else
        {
          IType[] types = new IType[typeArgs.length];
          for( int i = 0; i < types.length; i++ )
          {
            Type typeArg = typeArgs[i];
            if( !bKeepTypeVars && typeArg instanceof TypeVariable )
            {
              Type bound = ((TypeVariable)typeArg).getBounds()[0];
              if( !recursiveTypes.contains( bound ) )
              {
                types[i] = getActualType( bound, actualParamByVarName, bKeepTypeVars, recursiveTypes );
              }
              else if( bound instanceof ParameterizedType )
              {
                types[i] = getActualType( ((ParameterizedType)bound).getRawType(), actualParamByVarName, bKeepTypeVars, recursiveTypes );
              }
              else
              {
                throw new IllegalStateException( "Expecting bound to be a ParameterizedType here" );
              }
            }
            else
            {
              if( typeArg instanceof WildcardType && (((WildcardType)typeArg).getUpperBounds()[0].equals( Object.class ) ||
                                                      ((WildcardType)typeArg).getLowerBounds().length > 0) )
              {
                Type lowerBound = maybeGetLowerBound( (WildcardType)typeArg, actualParamByVarName, bKeepTypeVars, recursiveTypes );
                if( lowerBound == null )
                {
                  // Object is the default type for the naked <?> wildcard, so we have to get the actual bound, if different, from the corresponding type var
                  Type[] boundingTypes = ((Class)((ParameterizedType)type).getRawType()).getTypeParameters()[i].getBounds();
                  Type boundingType = boundingTypes.length == 0 ? null : boundingTypes[0];
                  if( boundingType != null )
                  {
                    typeArg = boundingType;
                  }
                }
              }

              types[i] = getActualType( typeArg, actualParamByVarName, bKeepTypeVars, recursiveTypes );
            }
          }
          retType = genType.getParameterizedType( types );
        }
      }
      finally
      {
        recursiveTypes.remove( type );
      }
    }
    else if( type instanceof GenericArrayType )
    {
      retType = getActualType( ((GenericArrayType)type).getGenericComponentType(), actualParamByVarName, bKeepTypeVars, recursiveTypes ).getArrayType();
    }
    else
    {
      //retType = parseType( normalizeJavaTypeName( type ), actualParamByVarName, bKeepTypeVars, null );
      throw new IllegalStateException();
    }
    return retType;
  }

  private static Type maybeGetLowerBound( WildcardType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, LinkedHashSet<Type> recursiveTypes )
  {
    Type[] lowers = type.getLowerBounds();
    if( lowers != null && lowers.length > 0 && recursiveTypes.size() > 0 )
    {
      // This is a "super" (contravariant) wildcard

      LinkedList<Type> list = new LinkedList<>( recursiveTypes );
      Type enclType = list.getLast();
      if( enclType instanceof ParameterizedType )
      {
        IType genType = getActualType( ((ParameterizedType)enclType).getRawType(), actualParamByVarName, bKeepTypeVars, recursiveTypes );
        if( genType instanceof IJavaType )
        {
          if( FunctionToInterfaceCoercer.getSingleMethodFromJavaInterface( (IJavaType)genType ) != null )
          {
            // For functional interfaces we keep the lower bound as an upper bound so that blocks maintain contravariance wrt the single method's parameters
            return lowers[0];
          }
        }
      }
    }
    return null;
  }

  public static IType getActualType( IAsmType type, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualType( type, actualParamByVarName, false, new LinkedHashSet<IAsmType>() );
  }
  public static IType getActualType( IAsmType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, LinkedHashSet<IAsmType> recursiveTypes )
  {
    if( type instanceof AsmClass )
    {
      return TypeSystem.getByFullNameIfValid( type.getName() );
    }
    if( type instanceof AsmPrimitiveType )
    {
      return JavaType.getPrimitiveType( type.getName() );
    }
    if( type.isArray() )
    {
      return getActualType( type.getComponentType(), actualParamByVarName, bKeepTypeVars, recursiveTypes ).getArrayType();
    }
    if( type.isTypeVariable() )
    {
      IType retType = actualParamByVarName.getByMatcher( type, AsmTypeVarMatcher.instance() );
      if( retType == null )
      {
        // the type must come from the map, otherwise it comes from a context where there is no argument for the type var, hence the error type
        retType = ErrorType.getInstance( type.getName() );
      }
      if( bKeepTypeVars && retType instanceof ITypeVariableType && type.getName().equals( retType.getName() ) && ((ITypeVariableType)retType).getBoundingType() != null )
      {
        IType boundingType = ((ITypeVariableType)retType).getBoundingType();
        IType actualBoundingType = getActualType( boundingType, actualParamByVarName, bKeepTypeVars );
        if( !actualBoundingType.getName().equals( boundingType.getName() ) )
        {
          TypeVariableDefinitionImpl tvd = ((TypeVariableDefinitionImpl)((ITypeVariableType)retType).getTypeVarDef()).clone( actualBoundingType );
          retType = new TypeVariableType( tvd, type instanceof AsmType && ((AsmType)type).isFunctionTypeVariable() );
        }
      }
      else if( !bKeepTypeVars )
      {
        retType = getDefaultParameterizedTypeWithTypeVars( retType );
      }

      return retType;
    }
    else if( type instanceof AsmWildcardType )
    {
      AsmType bound = ((AsmWildcardType)type).getBound();
      if( bound != null && !((AsmWildcardType)type).isCovariant() && recursiveTypes.size() > 0 )
      {
        LinkedList<IAsmType> list = new LinkedList<>( recursiveTypes );
        IAsmType enclType = list.getLast();
        List<AsmType> typeArgs = enclType.getTypeParameters();
        if( typeArgs != null && typeArgs.size() > 0 )
        {
          IType genType = TypeSystem.getByFullNameIfValid( enclType.getRawType().getName() );
          if( genType instanceof IJavaType )
          {
            // For functional interfaces we keep the lower bound as an upper bound so that blocks maintain contravariance wrt the single method's parameters
            if( FunctionToInterfaceCoercer.getSingleMethodFromJavaInterface( (IJavaType)genType ) == null )
            {
              bound = null;
            }
          }
        }
      }
      if( bound == null )
      {
        return JavaTypes.OBJECT();
      }
      IType actualType = getActualType( bound, actualParamByVarName, bKeepTypeVars, recursiveTypes );
      if( actualType instanceof TypeVariableType )
      {
        ITypeVariableDefinition tvd = ((ITypeVariableType)actualType).getTypeVarDef().clone();
        actualType = new TypeVariableType( tvd, bound.isFunctionTypeVariable() );
        ((TypeVariableType)actualType).getTypeVarDef().setVariance( ((AsmWildcardType)type).isCovariant() ? Variance.WILD_COVARIANT : Variance.WILD_CONTRAVARIANT );
      }
      return actualType;
    }
    else if( type instanceof AsmType )
    {
      List<AsmType> typeArgs = type.getTypeParameters();
      if( typeArgs == null || typeArgs.size() == 0 )
      {
        return TypeSystem.getByFullNameIfValid( type.getName() );
      }
      else
      {
        recursiveTypes.add( type );
        try
        {
          List<IType> types = new ArrayList<IType>( typeArgs.size() );
          for( int i = 0; i < typeArgs.size(); i++ )
          {
            AsmType typeArg = typeArgs.get( i );
            IType typeParam = null;
            if( !bKeepTypeVars && typeArg.isTypeVariable() )
            {
              if( !recursiveTypes.contains( typeArg ) )
              {
                IType t = getActualType( typeArg, actualParamByVarName, bKeepTypeVars, recursiveTypes );
                if( !(t instanceof ErrorType) )
                {
                  typeParam = t;
                }
              }

              if( typeParam == null )
              {
                List<AsmType> typeParameters = typeArg.getTypeParameters();
                if( typeParameters.isEmpty() )
                {
                  typeParam = JavaTypes.OBJECT();
                }
                else
                {
                  AsmType bound = typeParameters.get( 0 );
                  if( !recursiveTypes.contains( bound ) )
                  {
                    typeParam = getActualType( bound, actualParamByVarName, bKeepTypeVars, recursiveTypes );
                  }
                  else if( bound.isParameterized() )
                  {
                    typeParam = getActualType( bound.getRawType(), actualParamByVarName, bKeepTypeVars, recursiveTypes );
                  }
                  else
                  {
                    throw new IllegalStateException( "Expecting bound to be a parameterized here" );
                  }
                }
              }
            }
            else
            {
              if( typeArg instanceof AsmWildcardType &&
                  (((AsmWildcardType)typeArg).getBound() == null || !((AsmWildcardType)typeArg).isCovariant() ) )
              {
                // Get the bounding type from the corresponding type var
                IJavaClassInfo classInfo = TypeSystem.getDefaultTypeLoader().getJavaClassInfo( type.getRawType().getName() );
                if( classInfo != null && !isContravariantWildcardOnFunctionalInterface( (AsmWildcardType)typeArg, classInfo.getName() ) )
                {
                  List<AsmType> boundingTypes = ((AsmTypeJavaClassType)classInfo.getTypeParameters()[i]).getType().getTypeParameters();
                  AsmType boundingType = boundingTypes.isEmpty() ? null : boundingTypes.get( 0 );
                  if( boundingType != null )
                  {
                    typeArg = boundingType;
                  }
                }
              }
              typeParam = getActualType( typeArg, actualParamByVarName, bKeepTypeVars, recursiveTypes );
            }
            types.add( typeParam );
          }
          String rawTypeName = type.getRawType().getName();
          IType genType = TypeSystem.getByFullNameIfValid( rawTypeName );//getActualType( type.getRawType(), actualParamByVarName, bKeepTypeVars, recursiveTypes );
          if( genType == null )
          {
            return ErrorType.getInstance( rawTypeName );
            //throw new TypeNotPresentException( rawTypeName, null );
          }
          return genType.getParameterizedType( types.toArray( new IType[types.size()] ) );
        }
        finally
        {
          recursiveTypes.remove( type );
        }
      }
    }
    throw new IllegalStateException();
    //return parseType( type.getFqn(), actualParamByVarName, bKeepTypeVars, null );
  }

  private static boolean isContravariantWildcardOnFunctionalInterface( AsmWildcardType typeArg, String fqn )
  {
    IType genType = TypeSystem.getByFullNameIfValid( fqn );
    return !typeArg.isCovariant() &&
           genType instanceof IJavaType &&
           FunctionToInterfaceCoercer.getSingleMethodFromJavaInterface( (IJavaType)genType ) != null;
  }

  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualType( type, actualParamByVarName, false );
  }
  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return getActualType( type, actualParamByVarName, bKeepTypeVars, new HashSet<IType>() );
  }
  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, Set<IType> visited )
  {
    int iArrayDims = 0;
    if( type != null && type.isArray() )
    {
      for( iArrayDims = 0; type.isArray(); iArrayDims++ )
      {
        type = type.getComponentType();
      }
    }

    if( visited.contains( type ) )
    {
      return type;
    }
    visited.add( type );

    if( type instanceof TypeVariableType )
    {
      TypeVariableType saveType = (TypeVariableType)type;
      type = actualParamByVarName.get( (ITypeVariableType)type );
      if( type == null )
      {
        if( bKeepTypeVars )
        {
          type = saveType;
        }
      }
      else
      {
        if( bKeepTypeVars && type.equals( saveType ) && ((ITypeVariableType)type).getBoundingType() != null )
        {
          IType boundingType = ((ITypeVariableType)type).getBoundingType();
          IType actualBoundingType = getActualType( boundingType, actualParamByVarName, bKeepTypeVars, visited );
          visited.remove( boundingType );
          if( actualBoundingType != boundingType )
          {
            TypeVariableDefinitionImpl tvd = ((TypeVariableDefinitionImpl)((ITypeVariableType)type).getTypeVarDef()).clone( actualBoundingType );
            type = new TypeVariableType( tvd, ((ITypeVariableType)type).getTypeVarDef().getEnclosingType() instanceof IFunctionType );
          }
        }
        else if( !bKeepTypeVars && !isParameterizedWith( type, saveType ) )
        {
          type = getActualType( type, actualParamByVarName, bKeepTypeVars, visited );
          visited.remove( type );
        }
        else if( !bKeepTypeVars )
        {
          type = getDefaultParameterizedTypeWithTypeVars( type );
        }
      }
    }
    else if( type instanceof FunctionType )
    {
      if( !(type instanceof ErrorTypeInfo.UniversalFunctionType) )
      {
        type = ((FunctionType)type).parameterize( (FunctionType)type, actualParamByVarName, bKeepTypeVars );
      }
    }
    else if( type != null && type.isParameterizedType() )
    {
      IType[] typeParams = type.getTypeParameters();
      IType[] actualParamTypes = new IType[typeParams.length];
      boolean bDifferent = false;
      for( int i = 0; i < typeParams.length; i++ )
      {
        boolean bAlreadyVisiting = visited.contains( typeParams[i] );
        IType actualType = getActualType( typeParams[i], actualParamByVarName, bKeepTypeVars, visited );
        if( !bAlreadyVisiting )
        {
          visited.remove( typeParams[i] );
        }
        if( actualType == null )
        {
          actualType = JavaTypes.OBJECT();
        }
        actualParamTypes[i] = actualType;
        if( actualType != typeParams[i] )
        {
          bDifferent = true;
        }
      }
      visited.remove( type );
      type = bDifferent ? TypeLord.getPureGenericType( type ).getParameterizedType( actualParamTypes ) : type;
    }

    if( iArrayDims > 0 && type != null )
    {
      for( int j = 0; j < iArrayDims; j++ )
      {
        type = type.getArrayType();
      }
    }

    return type;
  }

  public static boolean isParameterizedWith( IType type, TypeVariableType typeVar )
  {
    type = getCoreType( type );

    if( type.equals( typeVar ) )
    {
      return true;
    }

    if( type instanceof FunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] types = funType.getParameterTypes();
      for( IType param : types )
      {
        if( isParameterizedWith( param, typeVar ) )
        {
          return true;
        }
      }
      return isParameterizedWith( funType.getReturnType(), typeVar );
    }
    else if( type.isParameterizedType() )
    {
      for( IType typeParam : type.getTypeParameters() )
      {
        if( isParameterizedWith( typeParam, typeVar ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  public static IType parseType( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName )
  {
    return parseType( strParameterizedTypeName, actualParamByVarName, null );
  }
  public static IType parseType( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap )
  {
    try
    {
      ITypeLiteralExpression expression = parseTypeLiteral( strParameterizedTypeName, actualParamByVarName, typeUsesMap );
      return expression.getType().getType();
    }
    catch( ParseResultsException e )
    {
      throw new RuntimeException( "Unable to parse the type " + strParameterizedTypeName + ".  When attempting to parse this " +
                                  "as a type literal, a parse error occured.", e );
    }
  }

  public static ITypeLiteralExpression parseTypeLiteral( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap ) throws ParseResultsException
  {
    StringTokenizer tokenizer = new StringTokenizer( strParameterizedTypeName, " <>[]?:(),", true );
    StringBuilder sbType = new StringBuilder();
    while( tokenizer.hasMoreTokens() )
    {
      String strToken = tokenizer.nextToken();
      IType type = actualParamByVarName.getByString( strToken );

      String resolvedTypeName;
      if( type != null )
      {
        if( type.isParameterizedType() )
        {
          type = resolveParameterizedType( type, actualParamByVarName );
        }
        if( type instanceof TypeVariableType )
        {
          type = ((TypeVariableType)type).getBoundingType();
        }
        resolvedTypeName = type instanceof TypeVariableType ? type.getRelativeName() : type.getName();
      }
      else
      {
        resolvedTypeName = strToken;
      }

      boolean bFirstToken = sbType.length() == 0;
      if( bFirstToken )
      {
        if( "?".equals( resolvedTypeName ) )
        {
          resolvedTypeName = JavaTypes.OBJECT().getName();
        }
      }
      sbType.append( resolvedTypeName );
    }

    String strNormalizedType = sbType.toString().replace( "$", "." );
    GosuParser parser = (GosuParser)GosuParserFactory.createParser( strNormalizedType, new StandardSymbolTable(), ScriptabilityModifiers.SCRIPTABLE );
    parser.setAllowWildcards( true );
    if( typeUsesMap != null )
    {
      parser.setTypeUsesMap(typeUsesMap);
    }
    parser.pushIgnoreTypeDeprecation();
    try
    {
      return parser.parseTypeLiteral( (IScriptPartId)null );
    }
    finally
    {
      parser.popIgnoreTypeDeprecation();
    }
  }

  private static IType resolveParameterizedType( IType parameterizedType, TypeVarToTypeMap actualParamByVarName )
  {
    List<IType> resolvedParams = new ArrayList<IType>();
    for( IType paramType : parameterizedType.getTypeParameters() )
    {
      if( paramType instanceof TypeVariableType && actualParamByVarName.containsKey( (ITypeVariableType)paramType ) )
      {
        resolvedParams.add( actualParamByVarName.get( (ITypeVariableType)paramType ) );
      }
      else
      {
        if( paramType.isParameterizedType() )
        {
          resolvedParams.add( resolveParameterizedType( paramType, actualParamByVarName ) );
        }
        else
        {
          resolvedParams.add( paramType );
        }
      }
    }
    return parameterizedType.getGenericType().getParameterizedType( resolvedParams.toArray( new IType[resolvedParams.size()] ) );
  }

  public static TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType )
  {
    TypeVarToTypeMap actualParamByVarName;
    ownersType = findActualDeclaringType( ownersType, declaringType );
    if( ownersType != null && ownersType.isParameterizedType() )
    {
      actualParamByVarName = mapActualTypeByVarName( ownersType );
    }
    else
    {
      actualParamByVarName = mapGenericTypeByVarName( ownersType );
      if( ownersType != null )
      {
        while( ownersType.getEnclosingType() != null )
        {
          ownersType = ownersType.getEnclosingType();
          TypeVarToTypeMap vars = mapGenericTypeByVarName( ownersType );
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = vars;
          }
          else
          {
            actualParamByVarName.putAll( vars );
          }
        }
      }
    }
    return actualParamByVarName;
  }

  public static IType makeParameteredType( IType genType, TypeVarToTypeMap inferenceMap )
  {
    IGenericTypeVariable[] gtvs = getPureGenericType( genType ).getGenericTypeVariables();
    IType[] typeParams = new IType[gtvs.length];
    int i = 0;
    for( IGenericTypeVariable gtv: gtvs )
    {
      typeParams[i] = inferenceMap.get( gtv.getTypeVariableDefinition().getType() );
      if( typeParams[i] == null )
      {
        if( genType.isParameterizedType() )
        {
          typeParams[i] = genType.getTypeParameters()[i];
        }
        else
        {
          return null;
        }
      }
      i++;
    }
    return genType.getParameterizedType( typeParams );
  }

  // If the declaring type is generic and the owning type is parameterized, we need to
  // find the corresponding parameterized type of the declaring type e.g.
  //
  // class Base<T> {
  //   function blah() : Bar<T> {}
  // }
  // class Foo<T> extends Base<T> {}
  //
  // new Foo<String>().blah() // infer return type as Bar<String>
  //
  // The declaring class of blah() is generic class Base<T> (not a parameterized one),
  // while the owner's type is Foo<String>, thus in order to resolve the actual return
  // type for blah() we must walk the ancestry of Foo<String> until find the corresponding
  // parameterized type for Base<T>.
  private static IType findActualDeclaringType( IType ownersType, IType declaringType )
  {
    if( ownersType == null )
    {
      return null;
    }

    if( declaringType.isParameterizedType() && !declaringType.isGenericType() )
    {
      return declaringType;
    }

    if( ownersType == declaringType )
    {
      return declaringType;
    }

    if( ownersType.getGenericType() == declaringType )
    {
      return ownersType;
    }

    IType actualDeclaringType = findActualDeclaringType( ownersType.getSupertype(), declaringType );
    if( actualDeclaringType != null && actualDeclaringType != declaringType )
    {
      return actualDeclaringType;
    }

    for( IType iface : ownersType.getInterfaces() )
    {
      actualDeclaringType = findActualDeclaringType( iface, declaringType );
      if( actualDeclaringType != null && actualDeclaringType != declaringType )
      {
        return actualDeclaringType;
      }
    }

    return declaringType;
  }

  private static TypeVarToTypeMap mapActualTypeByVarName( IType ownersType )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    IGenericTypeVariable[] vars = ownersType.getGenericType().getGenericTypeVariables();
    if (vars != null) {
      IType[] paramArgs = ownersType.getTypeParameters();
      for( int i = 0; i < vars.length; i++ )
      {
        IGenericTypeVariable typeVar = vars[i];
        if( paramArgs.length > i )
        {
          actualParamByVarName.put( typeVar.getTypeVariableDefinition().getType(), paramArgs[i] );
        }
      }
    }
    return actualParamByVarName;
  }

  private static TypeVarToTypeMap mapGenericTypeByVarName( IType ownersType )
  {
    TypeVarToTypeMap genericParamByVarName = TypeVarToTypeMap.EMPTY_MAP;
    IType genType = null;
    if( ownersType != null )
    {
      genType = ownersType.getGenericType();
    }
    if( genType != null )
    {
      genericParamByVarName = new TypeVarToTypeMap();
      IGenericTypeVariable[] vars = genType.getGenericTypeVariables();
      if( vars != null )
      {
        for( int i = 0; i < vars.length; i++ )
        {
          IGenericTypeVariable typeVar = vars[i];
          ITypeVariableType type = typeVar.getTypeVariableDefinition().getType();
          if( !genericParamByVarName.containsKey( type ) )
          {
            genericParamByVarName.put( type, new TypeVariableType( ownersType, typeVar ) );
          }
        }
      }
    }
    return genericParamByVarName;
  }

  public static String getNameWithQualifiedTypeVariables( IType type, boolean includeModules )
  {
    if( type.isArray() )
    {
      return getNameWithQualifiedTypeVariables( type.getComponentType(), includeModules ) + "[]";
    }
    else if( type.isParameterizedType() )
    {
      String strParams = getNameOfParams( type.getTypeParameters(), false, true, includeModules );
      return getPureGenericType( type ).getName() + strParams;
    }
    else if( type instanceof TypeVariableType )
    {
      if( type.getEnclosingType() != null )
      {
        return  ((TypeVariableType)type).getNameWithEnclosingType();
      }
    }
    return type.getName();
  }

  public static String getNameWithBoundQualifiedTypeVariables( IType type, boolean includeModules )
  {
    if( type.isArray() )
    {
      return getNameWithQualifiedTypeVariables( type.getComponentType(), includeModules ) + "[]";
    }
    else if( type.isParameterizedType() )
    {
      String strParams = getNameOfParams( type.getTypeParameters(), false, true, includeModules );
      return getPureGenericType( type ).getName() + strParams;
    }
    else if( type instanceof TypeVariableType )
    {
      if( type.getEnclosingType() != null )
      {
        return  ((TypeVariableType)type).getNameWithBoundingType();
      }
    }
    return type.getName();
  }

  public static String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType )
  {
    return getNameOfParams( paramTypes, bRelative, bWithEnclosingType, false );
  }
  public static String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType, boolean bIncludeModule )
  {
    return _getNameOfParams( paramTypes, bRelative, bWithEnclosingType, bIncludeModule, new HashSet<>() );
  }
  private static String _getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType, boolean bIncludeModule, Set<IType> visited )
  {
    StringBuilder sb = new StringBuilder( "<" );
    for( int i = 0; i < paramTypes.length; i++ )
    {
      IType paramType = paramTypes[i];
      if( bRelative )
      {
        sb.append( paramType.getRelativeName() );
      }
      else
      {
        appendTypeName( bWithEnclosingType, bIncludeModule, sb, paramType, visited );
      }
      if( i < paramTypes.length - 1 )
      {
        sb.append( ", " );
      }
    }
    sb.append( '>' );
    return sb.toString();
  }

  private static StringBuilder appendTypeName( boolean bWithEnclosingType, boolean bIncludeModule, StringBuilder sb, IType paramType, Set<IType> visited )
  {
    if( paramType.isArray() )
    {
      appendTypeName( bWithEnclosingType, bIncludeModule, sb, paramType.getComponentType(), visited ).append( "[]" );
    }
    else if( bWithEnclosingType && paramType instanceof TypeVariableType && !visited.contains( paramType ) )
    {
      visited.add( paramType );
      try
      {
        TypeVariableType type = (TypeVariableType)paramType;
        if( type.getEnclosingType() != null )
        {
          if( bIncludeModule && !(type.getEnclosingType() instanceof INonLoadableType) )
          {
            sb.append( type.getEnclosingType().getTypeLoader().getModule().getName() ).append( "." );
          }
          sb.append( type.getNameWithEnclosingType() );
          ITypeVariableDefinition typeVarDef = type.getTypeVarDef();
          if( typeVarDef != null )
          {
            sb.append( typeVarDef.getVariance().getSymbol() );
            IType boundingType = typeVarDef.getBoundingType();
            if( type.isFunctionStatement() && boundingType != null && boundingType != JavaTypes.OBJECT() )
            {
              sb.append( '-' );
              appendTypeName( bWithEnclosingType, bIncludeModule, sb, boundingType, visited );
            }
          }
        }
        else
        {
          sb.append( type.getName() );
        }
      }
      finally
      {
        visited.remove( paramType );
      }
    }
    else if( bWithEnclosingType && paramType.isParameterizedType() )
    {
      sb.append( paramType.getGenericType().getName() );
      sb.append( _getNameOfParams( paramType.getTypeParameters(), false, bWithEnclosingType, bIncludeModule, visited ) );
    }
    else
    {
      if( bIncludeModule && !(paramType instanceof INonLoadableType) )
      {
        ITypeLoader typeLoader = paramType.getTypeLoader();
        if( typeLoader != null )
        {
          IModule oldModule = typeLoader.getModule();
          if( oldModule != null )
          {
            sb.append( oldModule.getName() ).append( "." );
          }
        }
      }
      sb.append( paramType.getName() );
    }
    return sb;
  }

  public static boolean isDelegatableInterface( IType declaringType, IType iface ) {
    if( iface.isAssignableFrom( declaringType ) ) {
      return true;
    }
    IType superClass = declaringType.getSupertype();
    if( superClass != null && isDelegatableInterface( superClass, iface ) ) {
      return true;
    }
    IType[] interfaces = declaringType.getInterfaces();
    if( interfaces != null ) {
      for( IType t : interfaces ) {
        if( isDelegatableInterface( t, iface ) ) {
          return true;
        }
      }
    }
    return false;
  }

  public static IType findParameterizedStructureType( IType structureType, IType from )
  {
    TypeVarToTypeMap inferenceMap = new TypeVarToTypeMap();
    if( !StandardCoercionManager.isStructurallyAssignable_Laxed( structureType, from, inferenceMap ) )
    {
      return null;
    }
    return TypeLord.makeParameteredType( structureType, inferenceMap );
  }

  public static IType getFunctionalInterface( IFunctionType funcType )
  {
    IType iface = IRTypeResolver.getDescriptor( funcType ).getType();
    iface = TypeLord.getPureGenericType( iface );
    if( iface.isGenericType() )
    {
      IGenericTypeVariable[] gtvs = iface.getGenericTypeVariables();
      IType[] typeParams = new IType[gtvs.length];
      IType returnType = funcType.getReturnType();
      boolean hasReturn = returnType != JavaTypes.pVOID();
      for( int i = 0; i < gtvs.length; i++ )
      {
        if( i == 0 && hasReturn )
        {
          if( returnType.isPrimitive() )
          {
            returnType = TypeSystem.getBoxType( returnType );
          }
          typeParams[i] = returnType;
        }
        else if( funcType.getParameterTypes().length > 0 )
        {
          IType paramType = funcType.getParameterTypes()[i - (hasReturn ? 1 : 0)];
          if( paramType.isPrimitive() )
          {
            paramType = TypeSystem.getBoxType( paramType );
          }
          typeParams[i] = paramType;
        }
        else if( i == 0 )
        {
          iface = TypeLord.getDefaultParameterizedType( iface );
        }
      }
      iface = iface.getParameterizedType( typeParams );
    }
    return iface;
  }

  /**
   * Finds a parameterized type in the ancestry of a given type. For instance,
   * given the type for ArrayList&lt;Person&gt; as the sourceType and List as
   * the rawGenericType, returns List&lt;Person&gt;.
   *
   * @param sourceType     The type to search in.
   * @param rawGenericType The raw generic type of the parameterized type to
   *                       search for e.g., List is the raw generic type of List&lt;String&gt;.
   *
   * @return A parameterization of rawGenericType corresponding with the type
   *         params of sourceType.
   */
             // List<Foo>                    ArrayList<Foo>    List
  public static IType findParameterizedType( IType sourceType, IType rawGenericType )
  {
    return findParameterizedType( sourceType, rawGenericType, false );
  }
  public static IType findParameterizedType( IType sourceType, IType rawGenericType, boolean bForAssignability )
  {
    if( sourceType == null )
    {
      return null;
    }

    rawGenericType = getPureGenericType( rawGenericType );

    final IType srcRawType = sourceType.getGenericType();
    if( srcRawType == rawGenericType ||
        !bForAssignability && srcRawType instanceof IMetaType && rawGenericType == JavaTypes.CLASS() )
    {
      return sourceType;
    }

    IType parameterizedType = findParameterizedType( sourceType.getSupertype(), rawGenericType, bForAssignability );
    if( parameterizedType != null )
    {
      return parameterizedType;
    }

    IType[] interfaces = sourceType.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      IType iface = interfaces[i];
      parameterizedType = findParameterizedType( iface, rawGenericType, bForAssignability );
      if( parameterizedType != null )
      {
        return parameterizedType;
      }
    }

    return null;
  }
             // ArrayList<Foo>                       List<Foo>         ArrayList<Z>
  public static IType findParameterizedType_Reverse( IType sourceType, IType targetType )
  {
    if( sourceType == null || targetType == null )
    {
      return null;
    }

    if( !sourceType.isParameterizedType() )
    {
      return null;
    }

    // List<Z>
    IType sourceTypeInHier = findParameterizedType( targetType, getPureGenericType( sourceType ) );

    if( sourceTypeInHier == null || !sourceTypeInHier.isParameterizedType() )
    {
      return null;
    }

    TypeVarToTypeMap map = new TypeVarToTypeMap();
    IType[] params = sourceTypeInHier.getTypeParameters();
    for( int iPos = 0; iPos < params.length; iPos++  )
    {
      if( params[iPos] instanceof ITypeVariableType )
      {
        map.put( (ITypeVariableType)params[iPos], sourceType.getTypeParameters()[iPos] );
      }
    }
    // ArrayList<Foo>
    return getActualType( targetType, map, true );
  }

  // Todo: the above method is nearly identical to this one. lets see about combining them
  public static IType findParameterizedTypeInHierarchy( IType sourceType, IType rawGenericType )
  {
    if( sourceType == null )
    {
      return null;
    }

    if( sourceType.isParameterizedType() && sourceType.getGenericType().equals( rawGenericType ) )
    {
      return sourceType;
    }

    IType[] list = sourceType.getInterfaces();
    for( int i = 0; i < list.length; i++ )
    {
      IType returnType = findParameterizedTypeInHierarchy( list[i], rawGenericType );
      if( returnType != null )
      {
        return returnType;
      }
    }

    return findParameterizedTypeInHierarchy( sourceType.getSupertype(), rawGenericType );
  }

  public static void addAllClassesInClassHierarchy( Class entityClass, Set<Class> set )
  {
    if( !set.add( entityClass ) )
    {
      return;
    }
    
    Class[] interfaces = entityClass.getInterfaces();
    for( int i = 0; i < interfaces.length; i++ )
    {
      addAllClassesInClassHierarchy( interfaces[i], set );
    }
    
    if( entityClass.getSuperclass() != null )
    {
      addAllClassesInClassHierarchy( entityClass.getSuperclass(), set );
    }
  }
  
  private static void addAllClassesInClassHierarchy( IJavaClassInfo entityClass, Set<IJavaClassInfo> set )
  {
    if( !set.add( entityClass ) )
    {
      return;
    }

    IJavaClassInfo[] interfaces = entityClass.getInterfaces();
    for( int i = 0; i < interfaces.length; i++ )
    {
      addAllClassesInClassHierarchy( interfaces[i], set );
    }

    if( entityClass.getSuperclass() != null )
    {
      addAllClassesInClassHierarchy( entityClass.getSuperclass(), set );
    }
  }

  public static void addAllClassesInClassHierarchy( IType type, Set<IType> set )
  {
    addAllClassesInClassHierarchy( type, set, false );
  }

  public static void addAllClassesInClassHierarchy( IType type, Set<IType> set, boolean bForce )
  {
    if( !set.add( type  ) && !bForce )
    {
      return;
    }

    boolean bFromStructure = type instanceof IGosuClass && ((IGosuClass)type).isStructure();
    for( IType iface : type.getInterfaces() )
    {
      if( !bFromStructure || !(iface instanceof IGosuClass) || ((IGosuClass)iface).isStructure() )
      {
        addAllClassesInClassHierarchy( iface, set );
      }
    }

    if( type.getSupertype() != null )
    {
      addAllClassesInClassHierarchy( type.getSupertype(), set );
    }

    if( type.isParameterizedType() )
    {
      set.add( type.getGenericType() );
    }
  }

  public static <E extends IType> E getPureGenericType( E type )
  {
    if ( type == null || TypeSystem.isDeleted(type)) {
      return null;
    }

    if( type.isArray() ) {
      return (E)getPureGenericType( type.getComponentType() ).getArrayType();
    }
//## todo: write a test to verify whether or not this is correct
//    if( type instanceof CompoundType ) {
//      Set<IType> types = ((CompoundType)type).getTypes();
//      IType[] genTypes = new IType[types.size()];
//      int i = 0;
//      for( IType comp: types ) {
//        genTypes[i++] = getPureGenericType( comp );
//      }
//      return (E)CompoundType.get( genTypes );
//    }

    while( type.isParameterizedType() )
    {
      //noinspection unchecked
      type = (E)type.getGenericType();
    }
    return type;
  }

  public static IType deriveParameterizedTypeFromContext( IType type, IType contextType )
  {
    if( !type.isGenericType() || type.isParameterizedType() )
    {
      return type;
    }

    if( type instanceof MetaType )
    {
      return MetaType.DEFAULT_TYPE_TYPE.get();
    }

    if( contextType == null || !contextType.isParameterizedType() )
    {
      return makeDefaultParameterizedType( type );
    }

    IType genType = TypeLord.getPureGenericType( contextType );
    if( !genType.isGenericType() || !genType.isAssignableFrom( type ) )
    {
      return makeDefaultParameterizedType( type );
    }

    IType parameterizedWithTypeVars = type.getParameterizedType( Arrays.stream( type.getGenericTypeVariables() ).map( gtv -> gtv.getTypeVariableDefinition().getType() ).toArray( IType[]::new ) );
    IType result = findParameterizedType_Reverse( contextType, parameterizedWithTypeVars );
    if( result == null )
    {
      result = makeDefaultParameterizedType( type );
    }
    return result;
  }

  public static IType makeDefaultParameterizedType( IType type )
  {
    if( type != null && !(type instanceof IGosuEnhancementInternal) &&
        !type.isParameterizedType() && type.isGenericType() )
    {
      if( type instanceof MetaType )
      {
        return MetaType.DEFAULT_TYPE_TYPE.get();
      }

      IType[] boundingTypes = new IType[type.getGenericTypeVariables().length];
      for( int i = 0; i < boundingTypes.length; i++ )
      {
        boundingTypes[i] = type.getGenericTypeVariables()[i].getBoundingType();

        IGenericTypeVariable typeVar = type.getGenericTypeVariables()[i];
        if( TypeLord.isRecursiveType( typeVar.getTypeVariableDefinition().getType(), typeVar.getBoundingType() ) )
        {
          return type;
        }
      }

      if( boundingTypes.length == 0 )
      {
        return type;
      }

      type = type.getParameterizedType( boundingTypes );
    }
    return type;
  }

  public static IType replaceTypeVariableTypeParametersWithBoundingTypes( IType type )
  {
    return replaceTypeVariableTypeParametersWithBoundingTypes( type, null );
  }
  public static IType replaceTypeVariableTypeParametersWithBoundingTypes( IType type, IType enclType )
  {
    if( type instanceof ITypeVariableType )
    {
      if( isRecursiveType( (ITypeVariableType)type, ((ITypeVariableType)type).getBoundingType() ) )
      {
        // short-circuit recursive typevar
        return TypeLord.getPureGenericType( ((ITypeVariableType)type).getBoundingType() );
      }

      if( enclType != null && enclType.isParameterizedType() )
      {
        TypeVarToTypeMap map = mapTypeByVarName( enclType, enclType );
        return replaceTypeVariableTypeParametersWithBoundingTypes( getActualType( ((ITypeVariableType)type).getBoundingType(), map, true ) );
      }

      return replaceTypeVariableTypeParametersWithBoundingTypes( ((ITypeVariableType)type).getBoundingType(), enclType );
    }

    if( type.isArray() )
    {
      return replaceTypeVariableTypeParametersWithBoundingTypes( type.getComponentType(), enclType ).getArrayType();
    }

    if( type instanceof CompoundType )
    {
      Set<IType> types = ((CompoundType)type).getTypes();
      Set<IType> newTypes = new HashSet<>();
      for( IType t: types )
      {
        newTypes.add( replaceTypeVariableTypeParametersWithBoundingTypes( t ) );
      }
      if( newTypes.size() == 1 )
      {
        return newTypes.iterator().next();
      }
      return CompoundType.get( newTypes );
    }

    if( type.isParameterizedType() )
    {
      IType[] typeParams = type.getTypeParameters();
      IType[] concreteParams = new IType[typeParams.length];
      for( int i = 0; i < typeParams.length; i++ )
      {
        concreteParams[i] = replaceTypeVariableTypeParametersWithBoundingTypes( typeParams[i], enclType );
      }
      type = type.getParameterizedType( concreteParams );
    }
    else
    {
      if( type.isGenericType() )
      {
        IType[] boundingTypes = new IType[type.getGenericTypeVariables().length];
        for( int i = 0; i < boundingTypes.length; i++ )
        {
          boundingTypes[i] = type.getGenericTypeVariables()[i].getBoundingType();

          if( TypeLord.isRecursiveType( type.getGenericTypeVariables()[i].getTypeVariableDefinition().getType(), boundingTypes[i] ) )
          {
            return type;
          }
        }
        for( int i = 0; i < boundingTypes.length; i++ )
        {
          boundingTypes[i] = replaceTypeVariableTypeParametersWithBoundingTypes( boundingTypes[i], enclType );
        }
        type = type.getParameterizedType( boundingTypes );
      }
    }
    return type;
  }

  public static IType replaceRawGenericTypesWithDefaultParameterizedTypes( IType type )
  {
    if( type == null )
    {
      return null;
    }

    if( type.isArray() )
    {
      IType compType = replaceRawGenericTypesWithDefaultParameterizedTypes( type.getComponentType() );
      if( compType != type.getComponentType() )
      {
        return compType.getArrayType();
      }
      return type;
    }

    if( type.isParameterizedType() )
    {
      final IType[] typeParameters = type.getTypeParameters();
      IType[] typeParams = new IType[typeParameters.length];
      boolean bReplaced = false;
      for( int i = 0; i < typeParameters.length; i++ )
      {
        IType typeParam = typeParameters[i];
        typeParams[i] = replaceRawGenericTypesWithDefaultParameterizedTypes( typeParam );
        if( typeParam != typeParams[i] )
        {
          bReplaced = true;
        }
      }
      return bReplaced ? type.getParameterizedType( typeParams ) : type;
    }

    if( type.isGenericType() )
    {
      return getDefaultParameterizedType( type );
    }

    return type;
  }

  public static IType getDefaultParameterizedType( IType type )
  {
    if( type.isArray() )
    {
      IType defType = getDefaultParameterizedType( type.getComponentType() );
      if( defType != type )
      {
        return defType.getArrayType();
      }
      return type;
    }
    if( type instanceof CompoundType )
    {
      return makeDefaultParameterizedTypeForCompoundType( type );
    }
    if( !type.isGenericType() && !type.isParameterizedType() )
    {
      return type;
    }
    type = getPureGenericType( type );
    return makeDefaultParameterizedType( type );
  }

  private static IType makeDefaultParameterizedTypeForCompoundType( IType type )
  {
    IType[] defCompTypes = new IType[type.getCompoundTypeComponents().size()];
    int i = 0;
    boolean bDifferent = false;
    for( IType compType: type.getCompoundTypeComponents() )
    {
      defCompTypes[i++] = getDefaultParameterizedType( compType );
      bDifferent = bDifferent || defCompTypes[i] != compType;
    }
    if( bDifferent )
    {
      type = CompoundType.get( defCompTypes );
    }
    return type;
  }

  public static IType getDefaultParameterizedTypeWithTypeVars( IType type )
  {
    return getDefaultParameterizedTypeWithTypeVars(type, null, new HashSet<IType>());
  }
  public static IType getDefaultParameterizedTypeWithTypeVars( IType type, TypeVarToTypeMap map )
  {
    return getDefaultParameterizedTypeWithTypeVars( type, map, new HashSet<IType>() );
  }
  public static IType getDefaultParameterizedTypeWithTypeVars( IType type, TypeVarToTypeMap map, Set<IType> visited )
  {
    if( type.isArray() )
    {
      return getDefaultParameterizedTypeWithTypeVars( type.getComponentType(), map, visited ).getArrayType();
    }

    if( type instanceof ITypeVariableType )
    {
      if( map != null )
      {
        final IType assignedType = map.get( (ITypeVariableType)type );
        if( assignedType != null )
        {
          return assignedType;
        }
      }
      return getDefaultParameterizedTypeWithTypeVars(((ITypeVariableType) type).getBoundingType(), map, visited);
    }

    if( type instanceof ITypeVariableArrayType )
    {
      return getDefaultParameterizedTypeWithTypeVars( ((ITypeVariableType)type.getComponentType()).getBoundingType(), map, visited ).getArrayType();
    }

    if( !type.isGenericType() && !type.isParameterizedType() )
    {
      return type;
    }

    if( !visited.contains( type ) )
    {
      visited.add( type );
      if( isParameterizedType( type ) && isRecursiveType( type ) )
      {
        IType[] typeParameters = type.getTypeParameters();
        IType[] typeParams = new IType[typeParameters.length];
        int i = 0;
        for( IType param: typeParameters )
        {
          typeParams[i++] = getDefaultParameterizedTypeWithTypeVars( param, map, visited );
        }
        return getPureGenericType( type ).getParameterizedType( typeParams );
      }
      else if( type.isGenericType() && !type.isParameterizedType() && isRecursiveTypeFromBase( type ) )
      {
        final IGenericTypeVariable[] gvs = type.getGenericTypeVariables();
        IType[] typeParams = new IType[gvs.length];
        int i = 0;
        for( IGenericTypeVariable param: gvs )
        {
          final ITypeVariableDefinition typeDef = param.getTypeVariableDefinition();
          if( typeDef != null )
          {
            ITypeVariableType typeVarType = typeDef.getType();
            if( isRecursiveType( typeVarType, typeVarType.getBoundingType() ) )
            {
              // short-circuit recursive typevar
              typeParams[i++] = TypeLord.getPureGenericType( typeVarType.getBoundingType() );
            }
            else
            {
              typeParams[i++] = getDefaultParameterizedTypeWithTypeVars( typeVarType, map, visited );
            }
          }
          else
          {
            typeParams[i++] = getDefaultParameterizedTypeWithTypeVars( typeDef.getBoundingType(), map, visited );
          }
        }
        return getPureGenericType( type ).getParameterizedType( typeParams );
      }
    }

    type = getPureGenericType( type );
    return makeDefaultParameterizedType( type );
  }

  public static boolean isRecursiveTypeFromBase( IType rootType )
  {
    if( !rootType.isGenericType() && !rootType.isParameterizedType() )
    {
      return false;
    }

    IType genType = TypeLord.getPureGenericType( rootType );
    if( genType != TypeLord.getDefaultParameterizedType( genType ) )
    {
      return false;
    }

    if( rootType.isGenericType() && !rootType.isParameterizedType() )
    {
      if( rootType == TypeLord.getDefaultParameterizedType( rootType ) )
      {
        return true;
      }
    }
    else if( rootType.isParameterizedType() )
    {
      for( IType typeParam : rootType.getTypeParameters() )
      {
        if( isRecursiveTypeFromBase( typeParam ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isRecursiveType( IType declaringClass )
  {
    return _isRecursiveType( declaringClass, new HashSet<IType>() );
  }
  private static boolean _isRecursiveType( IType declaringClass, Set<IType> visited )
  {
    if( declaringClass instanceof ITypeVariableType )
    {
      if( visited.contains( declaringClass ) )
      {
        return true;
      }
      visited.add( declaringClass );
      try
      {
        return _isRecursiveType( ((ITypeVariableType)declaringClass).getBoundingType(), visited );
      }
      finally
      {
        visited.remove( declaringClass );
      }
    }

    if( declaringClass.isArray() )
    {
      return _isRecursiveType( declaringClass.getComponentType(), visited );
    }

    if( !declaringClass.isGenericType() && !declaringClass.isParameterizedType() )
    {
      return false;
    }

    if( declaringClass.isGenericType() && !declaringClass.isParameterizedType() )
    {
      for( IGenericTypeVariable gtv: declaringClass.getGenericTypeVariables() )
      {
        ITypeVariableDefinition tvd = gtv.getTypeVariableDefinition();
        if( tvd != null )
        {
          if( _isRecursiveType( tvd.getType(), visited ) )
          {
            return true;
          }
        }
      }
    }
    else if( declaringClass.isParameterizedType() )
    {
      for( IType typeParam : declaringClass.getTypeParameters() )
      {
        if( _isRecursiveType( typeParam, visited ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isSubtype( IType subtype, IType supertype )
  {
    if( subtype == null )
    {
      return false;
    }

    // Make sure we're dealing with pure types before doing any checks
    subtype = getPureGenericType( subtype );
    supertype = getPureGenericType( supertype );
    if( supertype instanceof IGosuClassInternal )
    {
      if( ((IGosuClassInternal)supertype).isSubClass( subtype ) )
      {
        return true;
      }
    }

    IType st = getPureGenericType( subtype.getSupertype() );
    return st == supertype || isSubtype( st, supertype );
  }

  static String fixSunInnerClassBug( String type )
  {

    int i = type.indexOf( "$", 0 );

    if( i < 0 )
    {
      return type;
    }
    else
    {

      int n = 0;
      i = 0;
      StringBuilder sb = new StringBuilder( type );

      while( i >= 0 )
      {
        i = findNthPositionOfString( n, sb, "$" );
        removeDuplicateClassName( sb, i );
        n++;
      }

      return sb.toString();
    }
  }

  private static int findNthPositionOfString( int n, StringBuilder sb, String str )
  {
    int count = 0;
    int i = 0;
    while( count <= n )
    {
      i = sb.indexOf( str, i + 1 );
      count++;
    }
    return i;
  }


  static void removeDuplicateClassName( StringBuilder sb, int dollarSignPosition )
  {
    StringBuilder foundBuffer = new StringBuilder();
    boolean chopped = false;
    int start = dollarSignPosition - 1;

    while( start >= 0 )
    {
      foundBuffer.append( sb.charAt( start ) );
      if( repeatsWithDot( foundBuffer ) && !chopped )
      {
        foundBuffer.setLength( foundBuffer.length() / 2 );
        chopped = true;
        break;
      }
      start--;
    }

    if( chopped )
    {
      sb.replace( start, dollarSignPosition, foundBuffer.reverse().toString() );
    }
  }

  static boolean repeatsWithDot( StringBuilder sb )
  {
    if( sb == null || sb.length() % 2 == 0 )
    {
      return false;
    }

    int halfPoint = sb.length() / 2;

    if( sb.charAt( halfPoint ) != '.' )
    {
      return false;
    }

    for( int i = 0; i < halfPoint; i++ )
    {
      if( sb.charAt( i ) != sb.charAt( i + halfPoint + 1 ) )
      {
        return false;
      }
    }
    return true;
  }

  public static boolean areGenericOrParameterizedTypesAssignable( IType to, IType from ) {
    return ASSIGNABILITY_CACHE.get(Pair.make(to, from));
  }

  private static boolean areGenericOrParameterizedTypesAssignableInternal( IType to, IType from )
  {
    if( from instanceof CompoundType )
    {
      Set<IType> types = ((CompoundType)from).getTypes();
      for( IType type : types )
      {
        if( areGenericOrParameterizedTypesAssignable( to, type ) )
        {
          return true;
        }
      }
      return false;
    }
    else if( to.isParameterizedType() || to.isGenericType() )
    {
      if( from.isGenericType() && !from.isParameterizedType() && to.isParameterizedType() &&
          TypeLord.getPureGenericType( to ).isAssignableFrom( from ) )
      {
        // Raw generic type is assignable to any parameterized version of it
        return true;
      }

      IType relatedParameterizedType = findParameterizedType( from, to.getGenericType(), true );
      if( relatedParameterizedType == null )
      {
        if( from == to.getGenericType() )
        {
          // Handle case List<Object> assignable from List
          relatedParameterizedType = from;
        }
        else
        {
          return from instanceof TypeVariableType &&
                 ((TypeVariableType)from).getBoundingType() == GosuParser.PENDING_BOUNDING_TYPE;
        }
      }

      if( from.isGenericType() &&
          to.isParameterizedType() &&
          sameAsDefaultProxiedType( to, from ) )
      {
        return true;
      }

      IType[] typeParams = to.getTypeParameters();
      if( typeParams != null )
      {
        IType[] paramTypesFrom = relatedParameterizedType.getTypeParameters();
        for( int i = 0; i < typeParams.length; i++ )
        {
          IType paramType = typeParams[i];

//## Leaving this commented out instead of deleting it to prevent it from coming back.
//        if( paramType instanceof TypeVariableType )
//        {
//          paramType = ((TypeVariableType)paramType).getTypeVarDef().getTypeVar().getBoundingTypes()[0];
//        }

          Boolean bDeclarationSiteResult = compareWithDeclarationSiteVariance( to, relatedParameterizedType, i );
          if( bDeclarationSiteResult != null )
          {
            if( !bDeclarationSiteResult )
            {
              return false;
            }
            continue;
          }

          if( paramType != JavaTypes.OBJECT() &&
              (paramTypesFrom == null ||
               paramTypesFrom.length <= i ||
               (!paramType.isAssignableFrom( paramTypesFrom[i] ) &&
                !StandardCoercionManager.isStructurallyAssignable( paramType, paramTypesFrom[i] ) &&
                !(JavaTypes.CLASS().isAssignableFrom( to ) &&
                  StandardCoercionManager.isStructurallyAssignable( paramType, MetaType.getLiteral( paramTypesFrom[i] ) )))) )
          {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }

  private static Boolean compareWithDeclarationSiteVariance( IType to, IType from, int iIndex )
  {
    IType toGenType = getPureGenericType( to );
    IType fromGenType = getPureGenericType( from );

    if( toGenType != fromGenType )
    {
      return null;
    }

    IGenericTypeVariable[] toGtvs = toGenType.getGenericTypeVariables();
    if( toGtvs == null || iIndex >= toGtvs.length )
    {
      return null;
    }
    IGenericTypeVariable[] fromGtvs = fromGenType.getGenericTypeVariables();
    if( fromGtvs == null || iIndex >= fromGtvs.length )
    {
      return null;
    }
    if( toGtvs.length != fromGtvs.length )
    {
      return null;
    }

    IType[] toTypeParams = to.getTypeParameters();
    if( toTypeParams == null || iIndex >= toTypeParams.length )
    {
      return null;
    }
    IType[] fromTypeParams = from.getTypeParameters();
    if( fromTypeParams == null || iIndex >= fromTypeParams.length )
    {
      return null;
    }

    ITypeVariableDefinition typeVarDef = toGtvs[iIndex].getTypeVariableDefinition();
    if( typeVarDef == null )
    {
      return null;
    }

    IType toTypeParam = toTypeParams[iIndex];
    IType fromTypeParam = fromTypeParams[iIndex];

    switch( typeVarDef.getVariance() )
    {
      case COVARIANT:
        return toTypeParam.isAssignableFrom( fromTypeParam );
      case CONTRAVARIANT:
        return fromTypeParam.isAssignableFrom( toTypeParam );
      case INVARIANT:
        return toTypeParam.equals( fromTypeParam );
    }
    return null;
  }

  private static boolean sameAsDefaultProxiedType( IType to, IType from )
  {
    if( to instanceof IJavaTypeInternal &&
        from instanceof IGosuClassInternal &&
        ((IGosuClassInternal)from).isProxy() )
    {
      IJavaType proxiedType = ((IGosuClassInternal)from).getJavaType();
      return proxiedType != null &&
             (TypeLord.makeDefaultParameterizedType( proxiedType ) == to ||
              TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( proxiedType ) == to);
    }
    return false;
  }

  public static Set<String> getNamespacesFromTypeNames( Set<? extends CharSequence> typeNames, Set<String> namespaces )
  {
    for( CharSequence typeName : typeNames )
    {
      String strName = typeName.toString();
      int iIndex = strName.lastIndexOf( '.' );
      if( iIndex > 0 )
      {
        String strPossibleEnclosingTypeName = strName.substring( 0, iIndex );
        if( typeNames.contains( strPossibleEnclosingTypeName ) )
        {
          // Don't add the enclosing type of an inner class as a namespace
          continue;
        }
      }
      addNamespace( namespaces, strName );
    }
    namespaces.add( Gosu.NOPACKAGE );
    return namespaces;
  }

  public static void addNamespace( Set<String> namespaces, String strType )
  {
    int iIndex = strType.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      String strPackage = strType.substring( 0, iIndex );
      if( namespaces.add( strPackage ) )
      {
        // Add parent namespaces i.e. a namespace may not have a class
        // e.g. the java namespace has no direct classes, so we have to
        // add it here.
        addNamespace( namespaces, strPackage );
      }
    }
  }

  public static IType getRootType(IType type) {
    IType result = type;
    while (result.getSupertype() != null || result.getSupertype() != JavaTypes.OBJECT()) {
      result = result.getSupertype();
    }
    return result;
  }

  public static IType findGreatestLowerBound( IType t1, IType t2 )
  {
    if( t1.equals( t2 ) )
    {
      return t1;
    }
    if( t1.isAssignableFrom( t2 ) )
    {
      return t2;
    }
    if( t2.isAssignableFrom( t1 ) )
    {
      return t1;
    }
    return t1; //## todo: return JavaTypes.VOID() or return null or Object?
  }

  public static IType findLeastUpperBound( List<? extends IType> types )
  {
    return findLeastUpperBoundImpl( types, new HashSet<IType>() );
  }

  private static IType findLeastUpperBoundImpl( List<? extends IType> types, Set<IType> resolvingTypes )
  {
    // Optimization 1: if there are no types, return void
    if( types.size() == 0 )
    {
      return JavaTypes.pVOID();
    }
    // Optimization 2: if there is one type, return that type
    if( types.size() == 1 )
    {
      return types.get( 0 );
    }

    for( IType type: types )
    {
      if( type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder() )
      {
        // Dynamic type trumps all
        return type.getName().equals( IDynamicType.QNAME ) ? type : IDynamicType.instance();
      }
    }

    IType lubType = null;
    boolean disJointTypes = false;
    boolean foundOnlyNullTypes = true;
    for( Iterator<? extends IType> it = types.iterator(); it.hasNext(); )
    {
      IType type = it.next();
      //nuke null types, which don't contribute to the type
      if( type.equals( GosuParserTypes.NULL_TYPE() ) )
      {
        continue;
      }
      foundOnlyNullTypes = false;
      if( lubType == null )
      {
        lubType = type;
      }
      if( !lubType.equals( type ) &&
          !BeanAccess.isBoxedTypeFor( lubType, type ) &&
          !BeanAccess.isBoxedTypeFor( type, lubType ) )
      {
        disJointTypes = true;
        break;
      }
    }
    if( foundOnlyNullTypes )
    {
      return GosuParserTypes.NULL_TYPE();
    }
    if( !disJointTypes )
    {
      return lubType;
    }

    // Short circuit recursive LUBs
    for( IType iType : types )
    {
      if( resolvingTypes.contains( iType ) )
      {
        return JavaTypes.OBJECT();
      }
    }
    resolvingTypes.addAll( types );

    // OK, we have disjoint types, so we need to do the full-monty LUB analysis
    IType seedType = types.get( 0 );

    Set<IType> lubSet;

    if( areAllTypesBlocks( types ) )
    {
      lubSet = findLubForBlockTypes( (List<IBlockType>)types, resolvingTypes );
    }
    else
    {
      lubSet = new HashSet<IType>( seedType.getAllTypesInHierarchy() );
      for( int i = 1; i < types.size(); i++ )
      {
        IType iIntrinsicType = types.get( i );
        lubSet.retainAll( iIntrinsicType.getAllTypesInHierarchy() );
      }
    }

    pruneNonLUBs( lubSet );

    if( lubSet.size() == 0 )
    {
      /* If there is no common types, return Object */
      return JavaTypes.OBJECT();
    }

    lubSet = findParameterizationLUBS( types, lubSet, resolvingTypes );

    if( lubSet.size() == 1 )
    {
      /* If there is a single, unabiguous LUB type, return that */
      return lubSet.iterator().next();
    }
    else
    {
      return CompoundType.get( lubSet );
    }
  }

  private static Set<IType> findLubForBlockTypes( List<? extends IBlockType> types, Set<IType> resolvingTypes )
  {
    IBlockType lowerBound = types.get( 0 );
    for( int i = 1; i < types.size(); i++ )
    {
      IBlockType csr = types.get( i );
      if( lowerBound.isAssignableFrom( csr ) )
      {
        continue;
      }
      IType[] contraTypes = FunctionType.findContravariantParams( lowerBound.getParameterTypes(), csr.getParameterTypes() );
      if( contraTypes == null )
      {
        return Collections.singleton( JavaTypes.IBLOCK() );
      }
      IType returnType = findLeastUpperBoundImpl( Arrays.asList( lowerBound.getReturnType(), csr.getReturnType() ), resolvingTypes );
      lowerBound = new BlockType( returnType, contraTypes, Arrays.asList( lowerBound.getParameterNames() ), Collections.<IExpression>emptyList() );
    }
    return Collections.singleton( lowerBound );
  }

  private static boolean areAllTypesBlocks( List<? extends IType> types )
  {
    for( IType t: types ) {
      if( !(t instanceof IBlockType) ) {
        return false;
      }
    }
    return true;
  }

  public static IType getLeastUpperBoundForPrimitiveTypes(IType t0, IType t1) {
    if( t0 == null || t1 == null )
    {
      return null;
    }
    if( t0 == t1 )
    {
      return t0;
    }

    boolean toT0FromT1 = isAWideningConversion(t0, t1);
    boolean toT1FromT0 = isAWideningConversion(t1, t0);

    if( toT0FromT1 ) {
      return t0;
    } else if( toT1FromT0 ){
      return t1;
    }
    return null;
  }

  private static int getIndex(IType type) {
    if( type == JavaTypes.pBOOLEAN() || type == JavaTypes.BOOLEAN() )
    {
      return 0;
    }
    else if( type == JavaTypes.pCHAR() || type == JavaTypes.CHARACTER() )
    {
      return 1;
    }
    else if( type == JavaTypes.pBYTE() || type == JavaTypes.BYTE() )
    {
      return 2;
    }
    else if( type == JavaTypes.pSHORT() || type == JavaTypes.SHORT() )
    {
      return 3;
    }
    else if( type == JavaTypes.pINT() || type == JavaTypes.INTEGER() )
    {
      return 4;
    }
    else if( type == JavaTypes.pLONG() || type == JavaTypes.LONG() )
    {
      return 5;
    }
    else if( type == JavaTypes.pFLOAT() || type == JavaTypes.FLOAT() )
    {
      return 6;
    }
    else if( type == JavaTypes.pDOUBLE() || type == JavaTypes.DOUBLE() )
    {
      return 7;
    }
    else if( type == JavaTypes.BIG_INTEGER() )
    {
      return 8;
    }
    else if( type == JavaTypes.BIG_DECIMAL() )
    {
      return 9;
    }
    return -1;
  }

  private static boolean isAWideningConversion(IType to, IType from) {
    boolean[][] tab =
                   {                                       //TO
                      //FROM           boolean  char    byte    short   int     long   float  double BI     BD
                      /*boolean    */  {true,   false,  false,  false,  false,  false, false, false, false, false},
                      /*char       */  {false,  true,   false,  false,  true,   true,  true,  true,  true,  true },
                      /*byte       */  {false,  false,  true,   true,   true,   true,  true,  true,  true,  true },
                      /*short      */  {false,  false,  false,  true,   true,   true,  true,  true,  true,  true },
                      /*int        */  {false,  false,  false,  false,  true,   true,  false, true,  true,  true },
                      /*long       */  {false,  false,  false,  false,  false,  true,  false, false, true,  true },
                      /*float      */  {false,  false,  false,  false,  false,  false, true,  true,  false, true },
                      /*double     */  {false,  false,  false,  false,  false,  false, false, true,  false, true },
                      /*BigInteger */  {false,  false,  false,  false,  false,  false, false, false, true,  true },
                      /*BigDecimal */  {false,  false,  false,  false,  false,  false, false, false, false, true }
                   };

    final int i = getIndex(from);
    final int j = getIndex(to);
    if(i == -1 || j == -1 )
    {
      return false;
    }
    return tab[i][j];
  }

  private static Set<IType> findParameterizationLUBS( List<? extends IType> currentTypes, Set<IType> lubSet, Set<IType> resolvingTypes )
  {
    Set<IType> returnSet = new HashSet<IType>();
    for( IType lub : lubSet )
    {
      if( lub.isGenericType() && !lub.isParameterizedType() && currentTypes.size() > 1 )
      {
        ArrayList<List<IType>> typeParametersByPosition = new ArrayList<List<IType>>( lub.getGenericTypeVariables().length );
        for( int i = 0; i < lub.getGenericTypeVariables().length; i++ )
        {
          typeParametersByPosition.add( new ArrayList<IType>() );
        }
        for( IType initialType : currentTypes )
        {
          IType parameterizedType = findParameterizedType( initialType, lub );
          if (parameterizedType == null) {
            System.out.println("*** ERROR: parameteredType is null");
            System.out.println("*** initialType is " + initialType);
            System.out.println("*** lub is " + lub);
            for (IType t : currentTypes) {
              System.out.println("*** currentTypes contains " + t);
            }
            for (IType t : lubSet) {
              System.out.println("*** lubSet contains " + t);
            }
          }
          if( !parameterizedType.isParameterizedType() )
          {
            parameterizedType = getDefaultParameterizedType( initialType );
          }
          if( parameterizedType.isParameterizedType() )
          {
            for( int i = 0; i < parameterizedType.getTypeParameters().length; i++ )
            {
              IType parameter = parameterizedType.getTypeParameters()[i];
              typeParametersByPosition.get( i ).add( parameter );
            }
          }
        }
        ArrayList<IType> paramLubs = new ArrayList<IType>();
        for( List<IType> paramterTypesAtPositionI : typeParametersByPosition )
        {
          IType leastUpperBound = findLeastUpperBoundImpl( paramterTypesAtPositionI, resolvingTypes );
          paramLubs.add( leastUpperBound );
        }
        IType finalType = lub.getParameterizedType( paramLubs.toArray( new IType[paramLubs.size()] ) );
        returnSet.add( finalType );
      }
      else
      {
        returnSet.add( lub );
      }
    }
    return returnSet;
  }

  private static void pruneNonLUBs( Set<IType> typeSet )
  {
    for( Iterator<IType> outerIterator = typeSet.iterator(); outerIterator.hasNext(); )
    {
      IType typeToPossiblyRemove = outerIterator.next();
      for( IType otherType : typeSet )
      {
        //noinspection SuspiciousMethodCalls
        if( otherType.getAllTypesInHierarchy().contains( typeToPossiblyRemove ) &&
            !typeToPossiblyRemove.getAllTypesInHierarchy().contains( otherType ) )
        {
          outerIterator.remove();
          break;
        }
      }
    }
  }

  public static boolean isRecursiveType( IJavaType javaType )
  {
    return getDefaultParameterizedType( javaType ).isGenericType();
  }

  public static boolean isRecursiveType( ITypeVariableType subject, IType... types )
  {
    return _isRecursiveType( subject, new HashSet<>(), types );
  }
  private static boolean _isRecursiveType( ITypeVariableType subject, Set<IType> visited, IType... types )
  {
    visited.add( subject );

    for( IType csr : types )
    {
      for( IType subj : visited )
      {
        if( getPureGenericType( csr ).equals( getPureGenericType( subj ) ) )
        {
          // Short-circuit recursive type parameterization e.g., class Foo<T extends Foo<T>>
          return true;
        }
      }
      if( csr instanceof CompoundType )
      {
        Set<IType> compoundTypeComponents = csr.getCompoundTypeComponents();
        IType[] typesArr = compoundTypeComponents.toArray( new IType[compoundTypeComponents.size()] );
        if( _isRecursiveType( subject, visited, typesArr ) )
        {
          return true;
        }
      }
      else if( csr.isParameterizedType() )
      {
        if( _isRecursiveType( subject, visited, csr.getTypeParameters() ) )
        {
          return true;
        }
      }
      else if( csr.isGenericType() )
      {
        for( IGenericTypeVariable gtv: csr.getGenericTypeVariables() )
        {
          ITypeVariableDefinition tvd = gtv.getTypeVariableDefinition();
          if( tvd != null && tvd.getType() != null && _isRecursiveType( subject, visited, tvd.getType() ) )
          {
            return true;
          }
        }
      }
      else if( csr instanceof TypeVariableType )
      {
        if( !visited.contains( csr ) && _isRecursiveType( (ITypeVariableType)csr, visited, ((TypeVariableType)csr).getBoundingType() ) )
        {
          return true;
        }
        visited.remove( csr );
        if( _isRecursiveType( subject, visited, ((TypeVariableType)csr).getBoundingType() ) )
        {
          return true;
        }
      }
      else if( csr.isArray() )
      {
        if( _isRecursiveType( subject, visited, csr.getComponentType() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static IJavaClassInfo getOuterMostEnclosingClass(IJavaClassInfo innerClass) {
    IJavaClassInfo outerMost = innerClass;
    while( outerMost.getEnclosingType() != null )
    {
      outerMost = outerMost.getEnclosingClass();
    }
    return outerMost;
  }

  public static IType getOuterMostEnclosingClass( IType innerClass )
  {
    IType outerMost = innerClass;
    while( outerMost.getEnclosingType() != null && !isEvalProgram( outerMost ) )
    {
      outerMost = outerMost.getEnclosingType();
      if (TypeSystem.isDeleted(outerMost)) {
        return null;
      }
    }
    return outerMost;
  }

  public static boolean isParameterizedType( IType type )
  {
    if( type.isParameterizedType() )
    {
      return true;
    }

    if( type instanceof CompoundType )
    {
      for( IType compType : type.getCompoundTypeComponents() )
      {
        if( isParameterizedType( compType ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean isEvalProgram( IType type )
  {
    return (type instanceof IGosuProgram) && ((IGosuProgram)type).isAnonymous();
  }

  public static void addReferencedTypeVarsThatAreNotInMap( IType type, TypeVarToTypeMap map )
  {
    if( type instanceof TypeVariableType )
    {
      IType existingType = map.get( (TypeVariableType)type );
      if( existingType == null )
      {
        map.put( (ITypeVariableType)type, type );
      }
    }
    else if( type.isParameterizedType() )
    {
      for( IType typeParam : type.getTypeParameters() )
      {
        addReferencedTypeVarsThatAreNotInMap( typeParam, map );
      }
    }
    else if( type.isArray() )
    {
      addReferencedTypeVarsThatAreNotInMap( type.getComponentType(), map );
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] types = funType.getParameterTypes();
      for( IType iType : types )
      {
        addReferencedTypeVarsThatAreNotInMap( iType, map );
      }
      addReferencedTypeVarsThatAreNotInMap( funType.getReturnType(), map );
    }
  }

  public static boolean hasTypeVariable( IType type )
  {
    return getTypeVariables( type, tv -> true );
  }

  public static List<ITypeVariableType> getTypeVariables( IType type )
  {
    final List<ITypeVariableType> tvs = new ArrayList<>();
    getTypeVariables( type, tv -> {tvs.add( tv ); return false;} );
    return tvs;
  }

  public static boolean getTypeVariables( IType type, Predicate<ITypeVariableType> cb )
  {
    boolean bRet = false;
    if( type == null )
    {
      bRet = false;
    }
    else if( type instanceof TypeVariableType )
    {
      bRet = cb.test( (ITypeVariableType)type );
    }
    else if( type instanceof TypeVariableArrayType )
    {
      bRet = getTypeVariables( type.getComponentType(), cb );
    }
    else if( type instanceof CompoundType )
    {
      for( IType t: ((CompoundType)type).getTypes() )
      {
        if( getTypeVariables( t, cb ) )
        {
          bRet = true;
          break;
        }
      }
    }
    else if( type.isParameterizedType() )
    {
      IType[] compileTimeTypeParams = type.getTypeParameters();
      for( int i = 0; i < compileTimeTypeParams.length; i++ )
      {
        IType ctTypeParam = compileTimeTypeParams[i];
        if( getTypeVariables( ctTypeParam, cb ) )
        {
          bRet = true;
          break;
        }
      }
    }


    return bRet;
  }

  public static boolean isExpandable( IType type )
  {
    return getExpandableComponentType( type ) != null;
  }

  public static IType getExpandableComponentType( IType type )
  {
    return getExpandableComponentType( type, true );
  }
  public static IType getExpandableComponentType( IType type, boolean bCore )
  {
    Set<IType> visited = new HashSet<IType>();
    while( true )
    {
      if( !visited.add( type ) )
      {
        // short-circuit recursive types
        return type;
      }

      if( type.isArray() )
      {
        type = type.getComponentType();
      }
      else if( JavaTypes.INTEGER_INTERVAL().isAssignableFrom( type ) )
      {
        return JavaTypes.pINT();
      }
      else if( JavaTypes.LONG_INTERVAL().isAssignableFrom( type ) )
      {
        return JavaTypes.pLONG();
      }
      else if( type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder() )
      {
        return type.getComponentType();
      }
      else
      {
        IType parameterized = TypeLord.findParameterizedType( type, JavaTypes.ITERABLE() );
        if( parameterized != null && parameterized.isParameterizedType() )
        {
          type = parameterized.getTypeParameters()[0];
        }
        else
        {
          parameterized = TypeLord.findParameterizedType( type, JavaTypes.ITERATOR() );
          if( parameterized != null && parameterized.isParameterizedType() )
          {
            type = parameterized.getTypeParameters()[0];
          }
          else
          {
            return type;
          }
        }
      }
      if( !bCore )
      {
        return type;
      }
    }
  }

  /**
   */
  public static void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap inferenceMap )
  {
    inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamType, argType, inferenceMap, new HashSet<ITypeVariableType>(), false );
  }
  public static void inferTypeVariableTypesFromGenParamTypeAndConcreteType_Reverse( IType genParamType, IType argType, TypeVarToTypeMap inferenceMap )
  {
    inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamType, argType, inferenceMap, new HashSet<ITypeVariableType>(), true );
  }
  public static void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap inferenceMap, HashSet<ITypeVariableType> inferredInCallStack, boolean bReverse )
  {
    if( argType == GosuParserTypes.NULL_TYPE() ||
        argType instanceof IErrorType ||
        (argType instanceof IMetaType && ((IMetaType)argType).getType() instanceof IErrorType) )
    {
      return;
    }

    if( argType.isPrimitive() )
    {
      argType = getBoxedTypeFromPrimitiveType(argType);
    }

    if( genParamType.isArray() )
    {
      if( !argType.isArray() )
      {
        return;
      }
      //## todo: DON'T allow a null component type here; we do it now as a hack that enables gosu arrays to be compatible with java arrays
      //## todo: same as JavaMethodInfo.inferTypeVariableTypesFromGenParamTypeAndConcreteType()
      if( argType.getComponentType() == null || !argType.getComponentType().isPrimitive() )
      {
        inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamType.getComponentType(), argType.getComponentType(), inferenceMap, inferredInCallStack, bReverse );
      }
    }
    else if( genParamType.isParameterizedType() )
    {
      if( argType instanceof FunctionType )
      {
        IFunctionType funcType = genParamType.getFunctionalInterface();
        if( funcType != null )
        {
          inferTypeVariableTypesFromGenParamTypeAndConcreteType( funcType, argType, inferenceMap, inferredInCallStack, bReverse );
          return;
        }
      }
      else if( genParamType instanceof IGosuClass && ((IGosuClass)genParamType).isStructure() )
      {
        if( StandardCoercionManager.isStructurallyAssignable_Laxed( genParamType, argType, inferenceMap ) )
        {
          return;
        }
      }

      IType argTypeInTermsOfParamType = bReverse ? findParameterizedType_Reverse( argType, genParamType ) : findParameterizedType( argType, genParamType.getGenericType() );
      if( argTypeInTermsOfParamType == null )
      {
        argTypeInTermsOfParamType = !bReverse ? findParameterizedType_Reverse( argType, genParamType ) : findParameterizedType( argType, genParamType.getGenericType() );
        if( argTypeInTermsOfParamType == null )
        {
          return;
        }
      }
      IType[] concreteTypeParams = argTypeInTermsOfParamType.getTypeParameters();
      if( concreteTypeParams != null && concreteTypeParams.length > 0 )
      {
        int i = 0;
        IType[] genTypeParams = genParamType.getTypeParameters();
        if( concreteTypeParams.length >= genTypeParams.length )
        {
          for( IType typeArg : genTypeParams )
          {
            inferTypeVariableTypesFromGenParamTypeAndConcreteType( typeArg, concreteTypeParams[i++], inferenceMap, inferredInCallStack, bReverse );
          }
        }
      }
    }
    else if( genParamType instanceof ITypeVariableType &&
             argType != GosuParserTypes.NULL_TYPE() )
    {
      ITypeVariableType tvType = ((ITypeVariableType)genParamType).getTypeVarDef().getType();
      Pair<IType, Boolean> pair = inferenceMap.getPair( tvType );
      IType type = pair == null ? null : pair.getFirst();
      boolean pairReverse = pair != null && pair.getSecond();

      if( type == null || type instanceof ITypeVariableType )
      {
        // Infer the type
        inferenceMap.put( tvType, getActualType( argType, inferenceMap, true ), bReverse );
        inferredInCallStack.add( tvType );
        if( type != null && type.equals( argType ) )
        {
          return;
        }
      }
      else if( type != null )
      {
        IType combinedType = solveType( genParamType, argType, inferenceMap, bReverse || pairReverse, tvType, type );
        inferenceMap.put( tvType, combinedType, bReverse );
      }
      IType boundingType = ((ITypeVariableType)genParamType).getBoundingType();
      if( !isRecursiveType( (ITypeVariableType)genParamType, boundingType ) )
      {
        inferTypeVariableTypesFromGenParamTypeAndConcreteType( boundingType, argType, inferenceMap, inferredInCallStack, bReverse );
      }
    }
    else if( genParamType instanceof FunctionType )
    {
      FunctionType genBlockType = (FunctionType)genParamType;
      if( !(argType instanceof FunctionType) )
      {
        if( argType.isParameterizedType() )
        {
          argType = argType.getFunctionalInterface();
        }

        if( !(argType instanceof FunctionType) )
        {
          return;
        }
      }

      inferTypeVariableTypesFromGenParamTypeAndConcreteType(
        genBlockType.getReturnType(), ((FunctionType)argType).getReturnType(), inferenceMap, inferredInCallStack, bReverse );

      IType[] genBlockParamTypes = genBlockType.getParameterTypes();
      if( genBlockParamTypes != null )
      {
        IType[] argTypeParamTypes = ((FunctionType)argType).getParameterTypes();
        if( argTypeParamTypes.length == genBlockParamTypes.length )
        {
          for( int i = 0; i < genBlockParamTypes.length; i++ )
          {
            // Infer param types in reverse
            inferTypeVariableTypesFromGenParamTypeAndConcreteType(
              genBlockParamTypes[i], ((FunctionType)argType).getParameterTypes()[i], inferenceMap, inferredInCallStack, true );
          }
        }
      }
    }
  }

  private static IType solveType( IType genParamType, IType argType, TypeVarToTypeMap inferenceMap, boolean bReverse, ITypeVariableType tvType, IType type )
  {
    // Solve the type.  Either LUB or GLB.
    //
    // Infer the type as the intersection of the existing inferred type and this one.  This is most relevant for
    // case where we infer a given type var from more than one type context e.g., a method call:
    // var l : String
    // var s : StringBuilder
    // var r = foo( l, s ) // here we must use the LUB of String and StringBuilder, which is CharSequence & Serializable
    // function foo<T>( t1: T, t2: T ) {}
    //
    // Also handle inferring a type from a structure type's methods:
    //

    IType lubType;
    if( bReverse )
    {
      // Contravariant
      lubType = TypeLord.findGreatestLowerBound( type, argType );
    }
    else
    {
      if( inferenceMap.isInferredForCovariance( tvType ) )
      {
        // Covariant
        lubType = argType.equals( genParamType ) ? type : TypeLord.findLeastUpperBound( Arrays.asList( type, argType ) );
      }
      else
      {
        // Contravariant

        // This is the first type encountered in a return/covariant position, the prior type[s] are in contravariant positions,
        // therefore we can apply contravariance to maintain the return type's more specific type i.e., since the other type[s]
        // are all param types and are contravariant with tvType, we should keep the more specific type between them.  Note if
        // the param type is more specific, tvType's variance is broken either way (both lub and glb produce a type that is not
        // call-compatible).
        lubType = TypeLord.findGreatestLowerBound( type, argType );
      }

      // We have inferred tvType from a covariant position, so we infer using covariance in subsequent positions
      inferenceMap.setInferredForCovariance( tvType );
    }
    return lubType;
  }

  public static IType getConcreteType( IType type )
  {
    if( type.isGenericType() && !type.isParameterizedType() )
    {
      IGenericTypeVariable[] genTypeVars = type.getGenericTypeVariables();
      IType[] typeVarTypes = new IType[genTypeVars.length];
      for( int i = 0; i < typeVarTypes.length; i++ )
      {
        typeVarTypes[i] = genTypeVars[i].getTypeVariableDefinition().getType();
      }
      type = type.getParameterizedType( typeVarTypes );
    }
    return type;
  }

  public static IType getCoreType( IType type )
  {
    if (TypeSystem.isDeleted(type)) {
      return null;
    }
    if( type.isArray() )
    {
      return getCoreType( type.getComponentType() );
    }
    return type;
  }

  public static IType getBoxedTypeFromPrimitiveType( IType primitiveType )
  {
    IType boxedType;

    if( primitiveType == JavaTypes.pBOOLEAN() )
    {
      boxedType = JavaTypes.BOOLEAN();
    }
    else if( primitiveType == JavaTypes.pBYTE() )
    {
      boxedType = JavaTypes.BYTE();
    }
    else if( primitiveType == JavaTypes.pCHAR() )
    {
      boxedType = JavaTypes.CHARACTER();
    }
    else if( primitiveType == JavaTypes.pSHORT() )
    {
      boxedType = JavaTypes.SHORT();
    }
    else if( primitiveType == JavaTypes.pINT() )
    {
      boxedType = JavaTypes.INTEGER();
    }
    else if( primitiveType == JavaTypes.pLONG() )
    {
      boxedType = JavaTypes.LONG();
    }
    else if( primitiveType == JavaTypes.pFLOAT() )
    {
      boxedType = JavaTypes.FLOAT();
    }
    else if( primitiveType == JavaTypes.pDOUBLE() )
    {
      boxedType = JavaTypes.DOUBLE();
    }
    else if( primitiveType == JavaTypes.pVOID() )
    {
      boxedType = JavaTypes.VOID();
    }
    else
    {
      throw new IllegalArgumentException( "Unhandled type " + primitiveType );
    }
    return boxedType;
  }

  public static IType boundTypes( IType type, List<IType> typesToBound )
  {
    return boundTypes( type, typesToBound, false );
  }
  public static IType boundTypes( IType type, List<IType> typesToBound, boolean bKeepTypeVars )
  {
    IType inferringType;
    if( type == null )
    {
      return null;
    }
    else if( type instanceof ITypeVariableType && (inferringType = inferringType( type, typesToBound, bKeepTypeVars )) != null )
    {
      // inferringType removes type from the list, to prevent stack overflow.
      IType boundType;
      if( bKeepTypeVars )
      {
        boundType = inferringType;
      }
      else if( isRecursiveType( (ITypeVariableType)type, ((ITypeVariableType)type).getBoundingType() ) &&
               !(((ITypeVariableType)type).getBoundingType() instanceof ITypeVariableType) )
      {
        // short-circuit recursive typevar
        boundType = TypeLord.getPureGenericType( ((ITypeVariableType)type).getBoundingType() );
      }
      else
      {
        boundType = boundTypes( ((ITypeVariableType)type).getBoundingType(), typesToBound, bKeepTypeVars );
      }
      typesToBound.add(inferringType); // add it back
      return boundType;
    }
    else if( type instanceof ITypeVariableArrayType )
    {
      IType componentType = type.getComponentType();
      return boundTypes( componentType, typesToBound, bKeepTypeVars ).getArrayType();
    }
    else if( type.isParameterizedType() )
    {
      IType[] typeParameters = type.getTypeParameters();
      IType[] parameters = new IType[typeParameters.length];
      System.arraycopy( typeParameters, 0, parameters, 0, typeParameters.length );
      for( int i = 0; i < parameters.length; i++ )
      {
        parameters[i] = boundTypes( parameters[i], typesToBound, bKeepTypeVars );
      }
      return type.getGenericType().getParameterizedType( parameters );
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] parameterTypes = funType.getParameterTypes();
      IType[] paramTypes = new IType[parameterTypes.length];
      System.arraycopy( parameterTypes, 0, paramTypes, 0, paramTypes.length );
      for( int i = 0; i < paramTypes.length; i++ )
      {
        paramTypes[i] = boundTypes( paramTypes[i], typesToBound, bKeepTypeVars );
      }
      IType returnType = boundTypes( funType.getReturnType(), typesToBound, bKeepTypeVars );
      return funType.newInstance( paramTypes, returnType );
    }
    else
    {
      return type;
    }
  }

  private static IType inferringType(IType type, List<IType> currentlyInferringTypes, boolean bKeepTypeVars)
  {
    if( type instanceof TypeVariableType )
    {
      TypeVariableType typeVarType = (TypeVariableType)type;
      for( IType currentlyInferringType : currentlyInferringTypes )
      {
        TypeVariableType inferringTypeVarType = (TypeVariableType)currentlyInferringType;
        if( areTypeVariablesEquivalent( typeVarType, inferringTypeVarType ) )
        {
          if( !bKeepTypeVars )
          {
            currentlyInferringTypes.remove(inferringTypeVarType);
          }
          return inferringTypeVarType;
        }
      }
    }
    return null;
  }

  private static boolean areTypeVariablesEquivalent( TypeVariableType possible, TypeVariableType inferred )
  {
    boolean match = false;
    if( GosuObjectUtil.equals( possible.getName(), inferred.getName() ) )
    {
      IType enclosingType1 = possible.getEnclosingType();
      IType enclosingType2 = inferred.getEnclosingType();
      if( enclosingType1 instanceof IFunctionType && enclosingType2 instanceof IFunctionType )
      {
        IFunctionType funType1 = (IFunctionType)enclosingType1;
        IFunctionType funType2 = (IFunctionType)enclosingType2;
        IScriptPartId id1 = funType1.getScriptPart();
        IScriptPartId id2 = funType2.getScriptPart();
        String typeName1 = id1 == null ? null : id1.getContainingTypeName();
        String typeName2 = id2 == null ? null : id2.getContainingTypeName();
        if( GosuObjectUtil.equals( typeName1, typeName2 ) &&
            GosuObjectUtil.equals( funType1.getParamSignature(), funType2.getParamSignature() ) )
        {
          match = true;
        }
      }
      else if( !(enclosingType1 instanceof IFunctionType) && !(enclosingType2 instanceof IFunctionType) )
      {
        match = enclosingType1 == enclosingType2;
      }
    }
    return match;
  }

  public static IType getTopLevelType(IType type) {
    IType topType = getCoreType(type);
    topType = getPureGenericType(topType);
    topType = getOuterMostEnclosingClass(topType);
    return topType;
  }
}
