package gw.lang.parser.expressions;

import gw.internal.gosu.parser.IParameterizableType;
import gw.lang.parser.Keyword;
import gw.lang.parser.TypeSystemAwareCache;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IIntrinsicTypeReference;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public enum Variance
{
  COVARIANT( Keyword.KW_out.getName(), '+' ),
  CONTRAVARIANT( Keyword.KW_in.getName(), '-' ),
  INVARIANT( Keyword.KW_in.getName() + "/" + Keyword.KW_out.getName(), '=' ),
  DEFAULT( "default", '*' ),

  /** internal use: variance inference */
  PENDING( "pending", '?' ),

  /** internal use: Java interop */
  WILD_COVARIANT( "wout", '>' ),
  /** internal use: Java interop */
  WILD_CONTRAVARIANT( "win", '<' );


  private static final boolean SUPPORT_DEFAULT_VARIANCE_CHECKING = true;
  private static final TypeSystemAwareCache<IType, Map<String, Variance>> DEFAULT_VARIANCE_MAP =
    new TypeSystemAwareCache<>( "Default Variance Map", 10000, type -> new HashMap<>() );

  private final String _desc;
  private final char _sign;

  Variance( String desc, char sign )
  {
    _desc = desc;
    _sign = sign;
  }

  public String getDesc()
  {
    return _desc;
  }

  public char getSymbol()
  {
    return _sign;
  }

  public interface IVarianceVerifier
  {
    void verify( Variance ctxVariance, Variance typeVarVariance );
  }

  public static void verifyTypeVarVariance( Variance variance, IType enclosingType, IVarianceVerifier verifier, IType type )
  {
    if( type instanceof IErrorType )
    {
      return;
    }

    if( variance == Variance.DEFAULT )
    {
      return;
    }

    if( type instanceof ITypeVariableType )
    {
      Variance typeVarVariance = getVariance( (ITypeVariableType)type );
      if( typeVarVariance == WILD_COVARIANT )
      {
        typeVarVariance = COVARIANT;
      }
      else if( typeVarVariance == WILD_CONTRAVARIANT )
      {
        typeVarVariance = CONTRAVARIANT;
      }
      verifier.verify( variance, typeVarVariance );
    }
    else if( type.isArray() )
    {
      verifyTypeVarVariance( variance, enclosingType, verifier, type.getComponentType() );
    }
    else if( type.isParameterizedType() && !(type instanceof IMetaType) )
    {
      IType[] typeParameters = type.getTypeParameters();
      for( int i = 0; i < typeParameters.length; i++ )
      {
        IType typeParam = typeParameters[i];
        IGenericTypeVariable[] gtvs = type.getGenericType().getGenericTypeVariables();
        if( i < gtvs.length )
        {
          Variance tv = null;
          if( typeParam instanceof ITypeVariableType )
          {
            tv = getVariance( (ITypeVariableType)typeParam );
          }
          Variance targetTypeVarVariance;
          targetTypeVarVariance = getVariance( gtvs[i].getTypeVariableDefinition().getType() );
          //noinspection PointlessBooleanExpression
          if( SUPPORT_DEFAULT_VARIANCE_CHECKING &&
              (variance == COVARIANT || variance == CONTRAVARIANT) &&
              (targetTypeVarVariance == Variance.DEFAULT &&
               typeParam instanceof ITypeVariableType &&
               isTypeVarFromEnclosingType( enclosingType, (ITypeVariableType)typeParam ) &&
               type instanceof IParameterizableType) )
          {
            if( tv != DEFAULT )
            {
              targetTypeVarVariance = maybeInferVariance( type, gtvs[i] );
            }
          }
          Variance ctxVariance = variance == CONTRAVARIANT
                                 ? invertVariance( targetTypeVarVariance )
                                 : variance == COVARIANT
                                   ? targetTypeVarVariance
                                   : variance;
          if( variance == CONTRAVARIANT && (tv == WILD_COVARIANT || tv == WILD_CONTRAVARIANT) )
          {
            ctxVariance = invertVariance( ctxVariance );
          }
          verifyTypeVarVariance( ctxVariance, enclosingType, verifier, typeParam );
        }
      }
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funcType = (IFunctionType)type;
      Variance invertedVariance = invertVariance( variance );
      for( IType paramType : funcType.getParameterTypes() )
      {
        verifyTypeVarVariance( invertedVariance, enclosingType, verifier, paramType );
      }
      verifyTypeVarVariance( variance, enclosingType, verifier, funcType.getReturnType() );
    }
  }

  private static Variance getVariance( ITypeVariableType type )
  {
    Variance typeVarVariance = type.getTypeVarDef().getVariance();
    if( typeVarVariance == DEFAULT )
    {
      IType typeVarOwner = type.getEnclosingType();
      if( !(typeVarOwner instanceof IFunctionType) && (!(typeVarOwner instanceof IGosuClass) || !((IGosuClass)typeVarOwner).isCompilingDefinitions()) )
      {
        Map<String, Variance> map = DEFAULT_VARIANCE_MAP.get( typeVarOwner );
        typeVarVariance = map.get( type.getName() );
        if( typeVarVariance == null )
        {
          typeVarVariance = DEFAULT;
        }
      }
    }
    return typeVarVariance;
  }

  public static Variance maybeInferVariance( IType type, IGenericTypeVariable gtv )
  {
    Variance typeVarVariance = gtv.getTypeVariableDefinition().getVariance();
    if( typeVarVariance == DEFAULT )
    {
      Map<String, Variance> map = DEFAULT_VARIANCE_MAP.get( type.getGenericType() );
      typeVarVariance = map.get( gtv.getName() );
      if( typeVarVariance == null )
      {
        synchronized( map )
        {
          typeVarVariance = map.get( gtv.getName() );
          if( typeVarVariance == null )
          {
            inferVariance( type.getGenericType(), map );
            typeVarVariance = map.get( gtv.getName() );
          }
        }
      }
    }
    return typeVarVariance;
  }

  private static boolean isTypeVarFromEnclosingType( IType enclosingType, ITypeVariableType typeVar )
  {
    for( IGenericTypeVariable gtv: enclosingType.getGenericTypeVariables() )
    {
      if( gtv.getTypeVariableDefinition().getType().equals( typeVar ) &&
          (getVariance( typeVar ) == COVARIANT || getVariance( typeVar ) == CONTRAVARIANT) ||
          (getVariance( typeVar ) == WILD_COVARIANT || getVariance( typeVar ) == WILD_CONTRAVARIANT) )
      {
        return true;
      }
    }
    return false;
  }

  private static void inferVariance( IType genericType, Map<String, Variance> map )
  {
    // perform the following iteratively until the values in the map settle:
    // - test each of generic type's typevars for "in" and "out"
    // - unresolved typevars are initially marked "pending" during this analysis
    // - after analyzing a type var...
    // -- mark the type var "default" if both "in" and "out" succeeded, otherwise
    // -- mark the type var "in out" (invariant) if none succeeded, otherwise
    // -- mark the type var "in" or "out"
    // - reuse verifyTypeVarVariance(...) for the analysis...
    // -- because the map comes from DEFAULT_VARIANCE_MAP directly, the caller of
    //    this method must ensure concurrency of the map

    Map<String, Variance> copy;
    do
    {
      copy = new HashMap<>( map );

      IGenericTypeVariable[] gtvs = genericType.getGenericTypeVariables();
      for( int i = 0; i < gtvs.length; i++ )
      {
        for( int j = i+1; j < gtvs.length; j++ )
        {
          if( map.get( gtvs[j].getName() ) == null )
          {
            map.put( gtvs[j].getName(), PENDING );
          }
        }

        map.put( gtvs[i].getName(), COVARIANT );
        boolean bValidOut = verifyDefaultForVariance( genericType );

        map.put( gtvs[i].getName(), CONTRAVARIANT );
        boolean bValidIn = verifyDefaultForVariance( genericType );

        map.put( gtvs[i].getName(),
                 bValidOut
                 ? bValidIn
                   ? DEFAULT
                   : COVARIANT
                 : bValidIn
                   ? CONTRAVARIANT
                   : INVARIANT );
      }
    } while( !copy.equals( map ) );
  }

  private static boolean verifyDefaultForVariance( IType type )
  {
    try
    {
      List<? extends IMethodInfo> declaredMethods = ((IRelativeTypeInfo)type.getTypeInfo()).getDeclaredMethods();
      for( IMethodInfo mi : declaredMethods )
      {
        if( !mi.isStatic() )
        {
          verifyDefaultTypeVarVariance( type, COVARIANT, mi.getReturnType() );
          verifyDefaultTypeVarVariance( type, CONTRAVARIANT, Arrays.stream( mi.getParameters() ).map( IIntrinsicTypeReference::getFeatureType ).toArray( IType[]::new ) );
        }
      }
      List<? extends IPropertyInfo> declaredProperties = ((IRelativeTypeInfo)type.getTypeInfo()).getDeclaredProperties();
      for( IPropertyInfo mi : declaredProperties )
      {
        if( !mi.isStatic() )
        {
          if( mi.isReadable() )
          {
            verifyDefaultTypeVarVariance( type, COVARIANT, mi.getFeatureType() );
          }
          if( mi.isWritable( type ) )
          {
            verifyDefaultTypeVarVariance( type, CONTRAVARIANT, mi.getFeatureType() );
          }
        }
      }

      IType superType = type.getSupertype();
      if( superType != null )
      {
        verifyDefaultTypeVarVariance( type, COVARIANT, superType );
      }
      IType[] interfaces = type.getInterfaces();
      if( interfaces != null )
      {
        verifyDefaultTypeVarVariance( type, COVARIANT, interfaces );
      }

      return true;
    }
    catch( VarianceInvalidException e )
    {
      //## todo: should we record the source of the variance invalidation?
      // eat
    }
    return false;
  }

  public static void verifyDefaultTypeVarVariance( IType enclosingType, Variance ctxVariance, IType... types )
  {
    for( IType type : types )
    {
      Variance.verifyTypeVarVariance( ctxVariance,
                                      enclosingType,
                                      new DefaultVarianceVerifier(),
                                      type );
    }
  }

  private static class VarianceInvalidException extends RuntimeException
  {
    @Override
    public synchronized Throwable fillInStackTrace()
    {
      return this;
    }
  }

  private static Variance invertVariance( Variance variance )
  {
    return variance == COVARIANT
           ? CONTRAVARIANT
           : variance == CONTRAVARIANT
             ? COVARIANT
             : variance;
  }

  private static class DefaultVarianceVerifier implements IVarianceVerifier
  {
    @Override
    public void verify( Variance ctxV, Variance typeVarVariance )
    {
      if( typeVarVariance != ctxV && typeVarVariance != Variance.DEFAULT && typeVarVariance != Variance.INVARIANT && ctxV != PENDING && typeVarVariance != PENDING )
      {
        throw new VarianceInvalidException();
      }
    }
  }
}
