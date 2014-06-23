/*
 * Copyright 2014. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.parser.ICoercer;
import gw.lang.parser.IExpression;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.TypeSystemAwareCache;
import gw.lang.parser.coercers.BasePrimitiveCoercer;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Pair;
import gw.util.concurrent.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MethodScorer {
  private static volatile MethodScorer INSTANCE = null;

  private final TypeSystemAwareCache<Pair<IType, IType>, Integer> _typeScoreCache =
    TypeSystemAwareCache.make( "Type Score Cache", 1000,
                               new Cache.MissHandler<Pair<IType, IType>, Integer>() {
                                 public final Integer load( Pair<IType, IType> key ) {
                                   return _addToScoreForTypes( Collections.<IType>emptyList(), key.getFirst(), key.getSecond() );
                                 }
                               } );

  private final MethodScoreCache _methodScoreCache = new MethodScoreCache();


  public static MethodScorer instance() {
    if( INSTANCE == null ) {
      synchronized( MethodScorer.class ){
        if( INSTANCE == null ) {
          INSTANCE = new MethodScorer();
        }
      }
    }
    return INSTANCE;
  }

  private MethodScorer() {
  }

  public List<MethodScore> scoreMethods( List<IInvocableType> funcTypes, List<IType> argTypes, List<IType> inferringTypes ) {
    List<MethodScore> scores = new ArrayList<MethodScore>();
    for( IInvocableType funcType : funcTypes ) {
      scores.add( scoreMethod( funcType, Collections.<IInvocableType>emptyList(), argTypes, inferringTypes, funcTypes.size() == 1 ) );
    }
    Collections.sort( scores );
    return scores;
  }

  public MethodScore scoreMethod( IInvocableType funcType, List<? extends IInvocableType> listFunctionTypes, List<IType> argTypes, List<IType> inferringTypes, boolean bSkipScoring ) {
    MethodScore score = new MethodScore();
    score.setValid( true );
    if( !bSkipScoring ) {
      IInvocableType cachedFuncType = getCachedMethodScore( funcType, argTypes );
      cachedFuncType = matchInOverloads( listFunctionTypes, cachedFuncType );
      if( cachedFuncType != null ) {
        // Found cached function type, no need for further scoring
        score.setRawFunctionType( cachedFuncType );
        score.setScore( 0 );
      }
      else {
        // Perform method scoring
        score.setRawFunctionType( funcType );
        score.incScore( scoreMethod( funcType, argTypes, inferringTypes ) );
      }
    }
    else {
      // Skip method scoring
      score.setRawFunctionType( funcType );
    }
    return score;
  }

  private IInvocableType matchInOverloads( List<? extends IInvocableType> listOverloads, IInvocableType cachedFuncType ) {
    if( cachedFuncType == null ) {
      return null;
    }
    for( IInvocableType csr: listOverloads ) {
      if( csr.equals( cachedFuncType ) ) {
        return csr;
      }
    }
    return null;
  }

  public int scoreMethod( IInvocableType funcType, List<IType> argTypes, List<IType> inferringTypes ) {
    IType[] paramTypes = funcType.getParameterTypes();
    int iScore = 0;
    for( int i = 0; i < argTypes.size(); i++ ) {
      if( paramTypes.length <= i ) {
        // Extra argument  +Max+1
        iScore += Byte.MAX_VALUE + 1;
        continue;
      }
      IType argType = argTypes.get( i );
      // Argument  +(0..Max)
      iScore += addToScoreForTypes( inferringTypes, paramTypes[i], argType );
    }
    for( int i = argTypes.size(); i < paramTypes.length; i++ ) {
      // Missing argument  +Max
      iScore += Byte.MAX_VALUE;
    }
    return iScore;
  }

  public int addToScoreForTypes( List<IType> inferringTypes, IType paramType, IType argType ) {
    if( inferringTypes.isEmpty() || !(paramType instanceof IInvocableType) ) {
      paramType = inferringTypes.isEmpty() ? paramType : TypeSystem.boundTypes( paramType, inferringTypes );
      return _typeScoreCache.get( new Pair<IType, IType>( paramType, argType ) );
    }
    return _addToScoreForTypes( inferringTypes, paramType, argType );
  }

  public int _addToScoreForTypes( List<IType> inferringTypes, IType paramType, IType argType ) {
    int iScore;
    paramType = TypeSystem.boundTypes( paramType, inferringTypes );
    if( paramType.equals( argType ) ) {
      // Same types  +0
      iScore = 0;
    }
    else if( !paramType.isPrimitive() && argType == JavaTypes.pVOID() ) {
      // null  +1
      iScore = 1;
    }
    else if( StandardCoercionManager.arePrimitiveTypesAssignable( paramType, argType ) ) {
      // Primitive coercion  +(2..9)
      iScore = BasePrimitiveCoercer.getPriorityOf( paramType, argType );
    }
    else if( TypeSystem.isBoxedTypeFor( paramType, argType ) ||
             TypeSystem.isBoxedTypeFor( argType, paramType ) ) {
      // Boxed coercion  +10
      iScore = 10;
    }
    else if( paramType instanceof IInvocableType && argType instanceof IInvocableType ) {
      // Assignable function types  +10 + average-degress-of-separation-of-sum-of-params-and-return-type
      int iDegrees = addDegreesOfSeparation( paramType, argType, inferringTypes );
      iScore = Math.min( Byte.MAX_VALUE - 10,
                         10 + iDegrees / (Math.max( ((IInvocableType)paramType).getParameterTypes().length,
                                                   ((IInvocableType)argType).getParameterTypes().length ) + 1) );
    }
    else {
      if( paramType.isAssignableFrom( argType ) ) {
        if( !(argType instanceof IInvocableType) ) {
          // Assignable types  +10 + degress-of-separation
          iScore = 10 + addDegreesOfSeparation( paramType, argType, inferringTypes );
        }
        else {
          // Crooked assignable types involving function type and non-function type  +Max - 2
          iScore = Byte.MAX_VALUE - 2;
        }
      }
      else {
        ICoercer iCoercer = CommonServices.getCoercionManager().findCoercer( paramType, argType, false );
        if( iCoercer != null ) {
          if( iCoercer instanceof BasePrimitiveCoercer ) {
            // Coercible (non-standard primitive)  +24 + primitive-coercion-score  (0 is best score for pimitivie coercer)
            iScore = 24 + iCoercer.getPriority( paramType, argType );
          }
          else {
            // Coercible  +Max - priority - 1
            iScore = Byte.MAX_VALUE - iCoercer.getPriority( paramType, argType ) - 1;
          }
        }
        else {
          // Type not compatible  +Max-1
          iScore = Byte.MAX_VALUE - 1;
        }
      }
    }
    return iScore;
  }

  public int addDegreesOfSeparation( IType parameterType, IType exprType, List<IType> inferringTypes ) {
    return addDegreesOfSeparation( parameterType, exprType instanceof IInvocableType ? Collections.singleton( exprType ) : exprType.getAllTypesInHierarchy(), inferringTypes );
  }

  public int addDegreesOfSeparation( IType parameterType, Iterable<? extends IType> types, List<IType> inferringTypes ) {
    int iScore = 0;

    if( parameterType.isParameterizedType() ) {
      parameterType = getGenericType( parameterType );
    }
    for( IType type : types ) {
      if( type.isParameterizedType() ) {
        type = getGenericType( type );
      }

      if( parameterType instanceof IInvocableType && type instanceof IInvocableType ) {
        // Recursively apply over params and return type
        iScore += scoreMethod( (IInvocableType)parameterType, Arrays.asList( ((IInvocableType)type).getParameterTypes() ), inferringTypes );
        if( parameterType instanceof IFunctionType && type instanceof IFunctionType ) {
          iScore += addToScoreForTypes( inferringTypes, ((IFunctionType)parameterType).getReturnType(), ((IFunctionType)type).getReturnType() );
        }
      }
      else if( parameterType.isAssignableFrom( type ) ) {
        iScore += 1;
      }
    }
    return iScore;
  }

  public <E extends IType> E getGenericType( E type ) {
    if( type == null || TypeSystem.isDeleted( type ) ) {
      return null;
    }

    if( type.isArray() ) {
      //noinspection unchecked
      return (E)getGenericType( type.getComponentType() ).getArrayType();
    }

    while( type.isParameterizedType() ) {
      //noinspection unchecked
      type = (E)type.getGenericType();
    }
    return type;
  }

  private static class MethodScoreCache extends HashMap<MethodScoreKey, IInvocableType> {
    MethodScoreCache() {
      //super( 2000 );
      TypeSystem.addTypeLoaderListenerAsWeakRef( new CacheClearer() );
    }

    private class CacheClearer extends AbstractTypeSystemListener {
      @Override
      public void refreshed() {
        clear();
      }

      @Override
      public void refreshedTypes( RefreshRequest request ) {
        clear();
      }
    }
  }

  public IInvocableType getCachedMethodScore( IInvocableType funcType, List<IType> argTypes ) {
    return _methodScoreCache.get( new MethodScoreKey( argTypes, funcType ) );
  }
  public void putCachedMethodScore( MethodScore score ) {
    score.setScore( 0 );
    List<IExpression> argExpressions = score.getArguments();
    List<IType> argTypes = new ArrayList<IType>( argExpressions.size() );
    for( IExpression argExpression : argExpressions ) {
      argTypes.add( argExpression.getType() );
    }
    _methodScoreCache.put( new MethodScoreKey( argTypes, score.getRawFunctionType() ), score.getRawFunctionType() );
  }

  private class MethodScoreKey {
    private String _methodName;
    private IType _enclosingType;
    private List<IType> _argTypes;

    private MethodScoreKey( List<IType> argTypes, IInvocableType funcType ) {
      _argTypes = argTypes;
      if( funcType instanceof IConstructorType ) {
        _methodName = "construct";
        _enclosingType = ((IConstructorType)funcType).getDeclaringType();
      }
      else {
        _methodName = funcType.getDisplayName();
        _enclosingType = funcType.getEnclosingType();
      }
    }

    @Override
    public boolean equals( Object o ) {
      if( this == o ) {
        return true;
      }
      if( o == null || getClass() != o.getClass() ) {
        return false;
      }

      MethodScoreKey that = (MethodScoreKey)o;

      if( !_argTypes.equals( that._argTypes ) ) {
        return false;
      }
      if( _enclosingType != null ? !_enclosingType.equals( that._enclosingType ) : that._enclosingType != null ) {
        return false;
      }
      if( !_methodName.equals( that._methodName ) ) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int result = _methodName.hashCode();
      result = 31 * result + (_enclosingType != null ? _enclosingType.hashCode() : 0);
      result = 31 * result + _argTypes.hashCode();
      return result;
    }
  }
}
