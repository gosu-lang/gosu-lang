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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MethodScorer {
  public static final int BOXED_COERCION_SCORE = 10;
  public static final int PRIMITIVE_COERCION_SCORE = 24;
  private static volatile MethodScorer INSTANCE = null;

  private final TypeSystemAwareCache<Pair<IType, IType>, Double> _typeScoreCache =
    TypeSystemAwareCache.make( "Type Score Cache", 1000,
                               key -> _addToScoreForTypes( Collections.<IType>emptyList(), key.getFirst(), key.getSecond() ) );

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

  public List<MethodScore> scoreMethods( List<IInvocableType> funcTypes, List<IType> argTypes) {
    List<MethodScore> scores = new ArrayList<>();
    for( IInvocableType funcType : funcTypes ) {
      scores.add( scoreMethod( null, null, funcType, Collections.<IInvocableType>emptyList(), argTypes,  Collections.<IType>emptyList(), funcTypes.size() == 1, true ) );
    }
    Collections.sort( scores );
    return scores;
  }

  public MethodScore scoreMethod( IType callsiteEnclosingType, IType rootType, IInvocableType funcType, List<? extends IInvocableType> listFunctionTypes, List<IType> argTypes, List<IType> inferringTypes, boolean bSkipScoring, boolean bLookInCache ) {
    MethodScore score = new MethodScore( rootType, callsiteEnclosingType );
    score.setValid( true );
    if( !bSkipScoring ) {
      IInvocableType cachedFuncType = bLookInCache ? getCachedMethodScore( funcType, callsiteEnclosingType, rootType, argTypes ) : null;
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

  public double scoreMethod( IInvocableType funcType, List<IType> argTypes, List<IType> inferringTypes ) {
    IType[] paramTypes = funcType.getParameterTypes();
    double score = 0;
    for( int i = 0; i < argTypes.size(); i++ ) {
      if( paramTypes.length <= i ) {
        // Extra argument  +Max+1
        score += Byte.MAX_VALUE + 1;
        continue;
      }
      IType argType = argTypes.get( i );
      // Argument  +(0..Max)
      if( funcType instanceof IBlockType ) {
        // block params are contravariant wrt assignability between block types
        score += addToScoreForTypes( inferringTypes, argType, paramTypes[i] );
      }
      else {
        // function params are covariant wrt assignability from a call site
        score += addToScoreForTypes( inferringTypes, paramTypes[i], argType );
      }
    }
    for( int i = argTypes.size(); i < paramTypes.length; i++ ) {
      // Missing argument  +Max
      score += Byte.MAX_VALUE;
    }

    return score;
  }

  public double addToScoreForTypes( List<IType> inferringTypes, IType paramType, IType argType ) {
    if( inferringTypes.isEmpty() || !(paramType instanceof IInvocableType) ) {
      paramType = inferringTypes.isEmpty() ? paramType : TypeSystem.boundTypes( paramType, inferringTypes );
      if( argType == paramType )
      {
        return 0;
      }
      return _typeScoreCache.get( new Pair<>( paramType, argType ) );
    }
    if( argType == paramType )
    {
      return 0;
    }
    return _addToScoreForTypes( inferringTypes, paramType, argType );
  }

  public double _addToScoreForTypes( List<IType> inferringTypes, IType paramType, IType argType ) {
    double score;
    IType primitiveArgType;
    IType primitiveParamType;
    paramType = TypeSystem.boundTypes( paramType, inferringTypes );
    if( paramType.equals( argType ) ) {
      // Same types  +0
      score = 0;
    }
    else if( !paramType.isPrimitive() && argType == JavaTypes.pVOID() ) {
      // null  +1
      score = 1;
    }
    else if( arePrimitiveTypesCompatible( paramType, argType ) ) {
      // Primitive coercion  +(2..9)
      score = BasePrimitiveCoercer.getPriorityOf( paramType, argType );
    }
    else if( TypeSystem.isBoxedTypeFor( paramType, argType ) ||
             TypeSystem.isBoxedTypeFor( argType, paramType ) ) {
      // Boxed coercion  +10
      score = BOXED_COERCION_SCORE;
    }
    else if( argType.isPrimitive() && StandardCoercionManager.isBoxed( paramType ) &&
             arePrimitiveTypesCompatible( primitiveParamType = TypeSystem.getPrimitiveType( paramType ), argType ) ) {
      // primitive -> Boxed coercion  +10 + Primitive coercion  +(12..19)
      score = BOXED_COERCION_SCORE + BasePrimitiveCoercer.getPriorityOf( primitiveParamType, argType );
    }
    else if( StandardCoercionManager.isBoxed( argType ) && paramType.isPrimitive() &&
             arePrimitiveTypesCompatible( paramType, primitiveArgType = TypeSystem.getPrimitiveType( argType ) ) ) {
      // Boxed -> primitive coercion  +10 + Primitive coercion  +(12..19)
      score = BOXED_COERCION_SCORE + BasePrimitiveCoercer.getPriorityOf( paramType, primitiveArgType );
    }
    else if( StandardCoercionManager.isBoxed( argType ) && StandardCoercionManager.isBoxed( paramType ) &&
             arePrimitiveTypesCompatible( primitiveParamType = TypeSystem.getPrimitiveType( paramType ), primitiveArgType = TypeSystem.getPrimitiveType( argType ) ) ) {
      // Boxed -> Boxed coercion  +10 + 10 + Primitive coercion  +(22..29)
      score = BOXED_COERCION_SCORE + BOXED_COERCION_SCORE + BasePrimitiveCoercer.getPriorityOf( primitiveParamType, primitiveArgType );
    }
    else {
      int iFunctionToInterfacePenalty = 0;
      if( paramType.isInterface() && argType instanceof IInvocableType ) {
        IFunctionType funcType = paramType.getFunctionalInterface();
        if( funcType != null ) {
          paramType = funcType;
          // Function type assignable to functional interface
          iFunctionToInterfacePenalty = 2;
        }
        // fall through...
      }
      if( paramType instanceof IInvocableType && argType instanceof IInvocableType ) {
        // Assignable function types  0 + average-degrees-of-separation-of-sum-of-params-and-return-type
        double degrees = addDegreesOfSeparation( paramType, argType, inferringTypes );
        int paramCountPlusReturn = Math.max( ((IInvocableType)paramType).getParameterTypes().length,
                                   ((IInvocableType)argType).getParameterTypes().length ) + 1;
        // round up to prevent false perfect scores
        score = Math.min( Byte.MAX_VALUE - 10, ((int)degrees + paramCountPlusReturn - 1) / paramCountPlusReturn );
        score += iFunctionToInterfacePenalty;
      }
      else {
        IType boxedArgType;
        if( argType.isPrimitive() && argType != JavaTypes.pVOID() && !paramType.isPrimitive() &&
            paramType.isAssignableFrom( boxedArgType = TypeSystem.getBoxType( argType ) ) ) {
          // Autobox type assignable  10 + Assignable degrees-of-separation
          score = BOXED_COERCION_SCORE + addDegreesOfSeparation( paramType, boxedArgType, inferringTypes );
        }
        else if( paramType.isAssignableFrom( argType ) ) {
          if( !(argType instanceof IInvocableType) ) {
            // Assignable types  0 + degrees-of-separation
            score = addDegreesOfSeparation( paramType, argType, inferringTypes );
          }
          else {
            // Crooked assignable types involving function type and non-function type  +Max - 2
            score = Byte.MAX_VALUE - 2;
          }
        }
        else if( StandardCoercionManager.isStructurallyAssignable( paramType, argType ) ) {
          // Structurally assignable types  +Max - 2
          score = Byte.MAX_VALUE - 2;
        }
        else {
          ICoercer iCoercer = CommonServices.getCoercionManager().findCoercer( paramType, argType, false );
          if( iCoercer != null ) {
            if( iCoercer instanceof BasePrimitiveCoercer ) {
              // Coercible (non-standard primitive)  +24 + primitive-coercion-score  (0 is best score for primitive coercer)
              score = PRIMITIVE_COERCION_SCORE + iCoercer.getPriority( paramType, argType );
            }
            else {
              // Coercible  +Max - priority - 1
              score = Byte.MAX_VALUE - iCoercer.getPriority( paramType, argType ) - 1;
            }
          }
          else {
            // Type not compatible  +Max-1
            score = Byte.MAX_VALUE - 1;
          }
        }
      }
    }
    return score;
  }

  private boolean arePrimitiveTypesCompatible( IType paramType, IType argType ) {
    return StandardCoercionManager.arePrimitiveTypesAssignable( paramType, argType ) ||
           (paramType.isPrimitive() && argType.isPrimitive() &&
            (paramType != JavaTypes.pBOOLEAN()) && (argType != JavaTypes.pBOOLEAN()) &&
            (paramType != JavaTypes.pCHAR()) && (argType != JavaTypes.pCHAR()) &&
            (paramType != JavaTypes.pVOID()) && (argType != JavaTypes.pVOID()) &&
            BasePrimitiveCoercer.losesInformation( argType, paramType ) <= 1);
  }

  public double addDegreesOfSeparation( IType parameterType, IType exprType, List<IType> inferringTypes ) {

    // first, calculate degrees on first-order type
    double degrees = addDegreesOfSeparation( parameterType,
      exprType instanceof IInvocableType
      ? Collections.singleton( exprType )
      : exprType.getAllTypesInHierarchy(), inferringTypes );

    // now, calculate degrees on type parameters, this is the fractional part of the score
    double paramDegrees = addDegreesOfSeparationFromParameterization( parameterType, exprType, inferringTypes );

    degrees += paramDegrees;

    return degrees;
  }

  private double addDegreesOfSeparationFromParameterization( IType parameterType, IType exprType, List<IType> inferringTypes )
  {
    double paramDegrees = 0;
    if( parameterType.isParameterizedType() )
    {
      IType mappedExprType = TypeSystem.findParameterizedType( exprType, getGenericType( parameterType ) );
      if( mappedExprType != null && mappedExprType.isParameterizedType() )
      {
        IType[] typeParameters = parameterType.getTypeParameters();
        IType[] mappedParams = mappedExprType.getTypeParameters();
        for( int i = 0; i < typeParameters.length; i++ )
        {
          IType paramType = typeParameters[i];
          IType mappedParam = mappedParams[i];

          paramDegrees += addDegreesOfSeparation( paramType,
            mappedParam instanceof IInvocableType
            ? Collections.singleton( mappedParam )
            : mappedParam.getAllTypesInHierarchy(), inferringTypes );
        }
      }
    }
    else if( exprType.isParameterizedType() )
    {
      // make score worse if passing parameterized arg type to non-parameterized param type
      // e.g.,
      // void foo(Object) vs. void foo(List<Object>) where arg is List<String>

      for( IType exprTypeParam: exprType.getTypeParameters() )
      {
        paramDegrees += addDegreesOfSeparation( JavaTypes.OBJECT(),
          exprTypeParam instanceof IInvocableType
            ? Collections.singleton( exprTypeParam )
            : exprTypeParam.getAllTypesInHierarchy(), inferringTypes );
      }
    }

    // weight of degrees decreases exponentially with depth of parameterization
    return paramDegrees/1000;
  }

  public double addDegreesOfSeparation( IType parameterType, Set<? extends IType> types, List<IType> inferringTypes ) {
    double degrees = 0;

    if( parameterType.isParameterizedType() ) {
      parameterType = getGenericType( parameterType );
    }
    for( IType type : types ) {
      IType componentType = type.isArray() ? type.getComponentType() : type;
      if( componentType.isParameterizedType() ) {
        type = getGenericType( componentType );
        if( types.contains( type ) ) {
          // don't double-count a generic type
          continue;
        }
      }
      if( parameterType == type ) {
        // don't include the same type in the hierarchy.  We are adding degrees because the arg type and param type are different, but assignable, which
        // means there must be at least one type in the hierarchy, if not the arg type itself, that is not the parameter type.
        continue;
      }
      if( parameterType instanceof IInvocableType && type instanceof IInvocableType ) {
        // Recursively apply over params and return type
        degrees += scoreMethod( (IInvocableType)parameterType, Arrays.asList( ((IInvocableType)type).getParameterTypes() ), inferringTypes );
        if( parameterType instanceof IFunctionType && type instanceof IFunctionType ) {
          degrees += addToScoreForTypes( inferringTypes, ((IFunctionType)parameterType).getReturnType(), ((IFunctionType)type).getReturnType() );
        }
      }
      else if( parameterType.isAssignableFrom( type ) ) {
        degrees += 1;
      }
    }
    return degrees;
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

  private static class MethodScoreCache extends HashMap<MethodScoreKey, IInvocableType> implements ITypeLoaderListener {
    MethodScoreCache() {
      TypeSystem.addTypeLoaderListenerAsWeakRef( this );
    }

    @Override
    public void refreshed() {
      clear();
    }

    @Override
    public void refreshedTypes( RefreshRequest request ) {
      clear();
    }
  }

  public IInvocableType getCachedMethodScore( IInvocableType funcType, IType callsiteEnclosingType, IType rootType, List<IType> argTypes ) {
    return _methodScoreCache.get( new MethodScoreKey( argTypes, funcType, callsiteEnclosingType, rootType ) );
  }
  public MethodScoreKey putCachedMethodScore( MethodScore score ) {
    score.setScore( 0 );
    List<IExpression> argExpressions = score.getArguments();
    List<IType> argTypes = new ArrayList<>( argExpressions.size() );
    for( IExpression argExpression : argExpressions ) {
      argTypes.add( argExpression.getType() );
    }
    MethodScoreKey key = new MethodScoreKey( argTypes, score );
    _methodScoreCache.put( key, score.getRawFunctionType() );
    return key;
  }
  public void removeCachedMethodScore( MethodScoreKey key )
  {
    _methodScoreCache.remove( key );
  }

  public static class MethodScoreKey {
    private String _methodName;
    private IType _declaringType;
    private IType _rootType;
    private IRelativeTypeInfo.Accessibility _acc;
    private List<IType> _argTypes;

    private MethodScoreKey( List<IType> argTypes, IInvocableType funcType, IType callsiteEnclosingType, IType rootType ) {
      _argTypes = argTypes;
      _rootType = rootType;
      if( rootType != null && callsiteEnclosingType != null )
      {
        if( rootType instanceof IMetaType )
        {
          rootType = ((IMetaType)rootType).getType();
        }
        _acc = FeatureManager.getAccessibilityForClass( rootType, callsiteEnclosingType );
      }
      else
      {
        _acc = IRelativeTypeInfo.Accessibility.NONE;
      }
      if( funcType instanceof IConstructorType ) {
        _methodName = "construct";
        _declaringType = ((IConstructorType)funcType).getDeclaringType();
      }
      else {
        _methodName = funcType.getDisplayName();
        _declaringType = funcType.getEnclosingType();
      }
    }

    public MethodScoreKey( List<IType> argTypes, MethodScore score )
    {
      _argTypes = argTypes;
      _rootType = score.getReceiverType();
      _acc = score.getAccessibility();

      IInvocableType funcType = score.getRawFunctionType();
      if( funcType instanceof IConstructorType ) {
        _methodName = "construct";
        _declaringType = ((IConstructorType)funcType).getDeclaringType();
      }
      else {
        _methodName = funcType.getDisplayName();
        _declaringType = funcType.getEnclosingType();
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
      if( _declaringType != null ? !_declaringType.equals( that._declaringType ) : that._declaringType != null ) {
        return false;
      }
      if( _rootType != null ? !_rootType.equals( that._rootType ) : that._rootType != null ) {
        return false;
      }
      if( _acc != null ? !_acc.equals( that._acc ) : that._acc != null ) {
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
      result = 31 * result + (_declaringType != null ? _declaringType.hashCode() : 0);
      result = 31 * result + (_rootType != null ? _rootType.hashCode() : 0);
      result = 31 * result + (_acc != null ? _acc.hashCode() : 0);
      result = 31 * result + _argTypes.hashCode();
      return result;
    }

//    @Override
//    public String toString() {
//      String ret = "_methodName " + _methodName + "\n"
//           + "_enclosingType " + _enclosingType.getName() + "\n"
//           + "_argTypes ";
//      for( IType a : _argTypes ) {
//        ret += " " + a.getName() + "\n";
//      }
//      return ret;
//    }
  }
}
