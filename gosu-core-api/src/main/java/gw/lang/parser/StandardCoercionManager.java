/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.lang.GosuShop;
import gw.lang.IDimension;
import gw.lang.parser.coercers.*;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.*;
import gw.lang.reflect.features.FeatureReference;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuArrayClassInstance;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.IJavaArrayType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.concurrent.Cache;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StandardCoercionManager extends BaseService implements ICoercionManager
{
  private static final DecimalFormat BIG_DECIMAL_FORMAT = new DecimalFormat();
  static {
    BIG_DECIMAL_FORMAT.setParseBigDecimal( true );
  }

  public static final Object NO_DICE = new Object();

  // LRUish cache of coercers
  public final TypeSystemAwareCache<Pair<IType, IType>, ICoercer> _coercerCache =
      TypeSystemAwareCache.make( "Coercer Cache", 1000,
                                 new Cache.MissHandler<Pair<IType, IType>, ICoercer>()
                                 {
                                   public final ICoercer load( Pair<IType, IType> key )
                                   {
                                     ICoercer coercer = findCoercerImpl( key.getFirst(), key.getSecond(), false );
                                     if( coercer == null )
                                     {
                                       return NullSentinalCoercer.instance();
                                     }
                                     else
                                     {
                                       return coercer;
                                     }
                                   }
                                 } )/*.logEveryNSeconds( 10, new SystemOutLogger( SystemOutLogger.LoggingLevel.INFO ) )*/;

  public final boolean canCoerce( IType lhsType, IType rhsType )
  {
    ICoercer iCoercer = findCoercer( lhsType, rhsType, false );
    return iCoercer != null;
  }

  private Object coerce( IType intrType, IType runtimeType, Object value )
  {
    ICoercer coercer = findCoercer( intrType, runtimeType, true );
    if( coercer != null )
    {
      return coercer.coerceValue( intrType, value );
    }
    return null;
  }

  private boolean hasPotentialLossOfPrecisionOrScale( IType lhsType, IType rhsType )
  {
    if( lhsType == JavaTypes.pBYTE() || lhsType == JavaTypes.BYTE() )
    {
      return rhsType != JavaTypes.pBYTE() && rhsType != JavaTypes.BYTE();
    }
    else if( lhsType == JavaTypes.pCHAR() || lhsType == JavaTypes.CHARACTER() )
    {
      return rhsType != JavaTypes.pCHAR() && rhsType != JavaTypes.CHARACTER();
    }
    else if( lhsType == JavaTypes.pDOUBLE() || lhsType == JavaTypes.DOUBLE() )
    {
      return rhsType != JavaTypes.DOUBLE() && !rhsType.isPrimitive() &&
             (JavaTypes.BIG_DECIMAL().isAssignableFrom( rhsType ) || JavaTypes.BIG_INTEGER().isAssignableFrom( rhsType ));
    }
    else if( lhsType == JavaTypes.pFLOAT() || lhsType == JavaTypes.FLOAT() )
    {
      return rhsType == JavaTypes.pDOUBLE() || rhsType == JavaTypes.DOUBLE() ||
             JavaTypes.BIG_DECIMAL().isAssignableFrom( rhsType ) || JavaTypes.BIG_INTEGER().isAssignableFrom( rhsType );
    }
    else if( lhsType == JavaTypes.pINT() || lhsType == JavaTypes.INTEGER() )
    {
      return rhsType != JavaTypes.pINT() && rhsType != JavaTypes.INTEGER() &&
             rhsType != JavaTypes.pSHORT() && rhsType != JavaTypes.SHORT() &&
             rhsType != JavaTypes.pBYTE() && rhsType != JavaTypes.BYTE() &&
             rhsType != JavaTypes.pCHAR() && rhsType != JavaTypes.CHARACTER();
    }
    else if( lhsType == JavaTypes.pLONG() || lhsType == JavaTypes.LONG() )
    {
      return rhsType != JavaTypes.pLONG() && rhsType != JavaTypes.LONG() &&
             rhsType != JavaTypes.pINT() && rhsType != JavaTypes.INTEGER() &&
             rhsType != JavaTypes.pSHORT() && rhsType != JavaTypes.SHORT() &&
             rhsType != JavaTypes.pBYTE() && rhsType != JavaTypes.BYTE() &&
             rhsType != JavaTypes.pCHAR() && rhsType != JavaTypes.CHARACTER();
    }
    else if( lhsType == JavaTypes.pSHORT() || lhsType == JavaTypes.SHORT() )
    {
      return rhsType != JavaTypes.pSHORT() && rhsType != JavaTypes.SHORT() &&
             rhsType != JavaTypes.pBYTE() && rhsType != JavaTypes.BYTE();
    }
    else if( JavaTypes.BIG_INTEGER().isAssignableFrom( lhsType ) )
    {
      return rhsType != JavaTypes.BIG_INTEGER() && hasPotentialLossOfPrecisionOrScale( JavaTypes.LONG(), rhsType );
    }
    return false;
  }

  public final ICoercer findCoercer( IType lhsType, IType rhsType, boolean runtime )
  {
    if( runtime )
    {
      return findCoercerImpl( lhsType, rhsType, runtime );
    }
    else
    {
      @SuppressWarnings({"unchecked"})
      ICoercer iCoercer = _coercerCache.get( new Pair( lhsType, rhsType ) );
      if( iCoercer == NullSentinalCoercer.instance() )
      {
        return null;
      }
      else
      {
        return iCoercer;
      }
    }
  }

  private ICoercer findCoercerImpl( IType lhsType, IType rhsType, boolean runtime )
  {
    ICoercer coercer = getCoercerInternal( lhsType, rhsType, runtime );
    if( coercer != null )
    {
      return coercer;
    }

    //Look at interfaces on rhsType for coercions
    IType[] interfaces = rhsType.getInterfaces();
    for ( IType anInterface1 : interfaces ) {
      coercer = findCoercer(lhsType, anInterface1, runtime);
      if (coercer != null) {
        return coercer;
      }
    }

    //Recurse to the superclass of rhsType for coercions
    if( rhsType.getSupertype() == null || isPrimitiveOrBoxed( lhsType ) )
    {
      return null;
    }
    else
    {
      return findCoercer( lhsType, rhsType.getSupertype(), runtime );
    }
  }

  /**
   * Returns a coercer from values of rhsType to values of lhsType if one exists.
   * I tried to write a reasonable spec in the comments below that indicate exactly
   * what should coerce to what.
   *
   * @param lhsType the type to coerce to
   * @param rhsType the type to coerce from
   * @param runtime true if the coercion is happening at runtime rather than compile time
   *                (note: This param should go away as we store the coercions on the parsed elements, rather than calling into the
   *                coercion manager)
   *
   * @return a coercer from the lhsType to the rhsType, or null if no such coercer exists or is needed
   */
  protected ICoercer getCoercerInternal( IType lhsType, IType rhsType, boolean runtime )
  {
    //=============================================================================
    //  Anything can be coerced to a string
    //=============================================================================
    if( JavaTypes.STRING() == lhsType && !(rhsType instanceof IErrorType) )
    {
      if( JavaTypes.pCHAR().equals( rhsType ) || JavaTypes.CHARACTER().equals( rhsType ) )
      {
        return NonWarningStringCoercer.instance();
      }
      else
      {
        return StringCoercer.instance();
      }
    }

    //=============================================================================
    //  All primitives and boxed types inter-coerce, except null to true primitives
    //=============================================================================
    if( lhsType.isPrimitive() && rhsType.equals( JavaTypes.pVOID() ) )
    {
      return null;
    }
    if( isPrimitiveOrBoxed( lhsType ) && isPrimitiveOrBoxed( rhsType ) )
    {
      if( TypeSystem.isBoxedTypeFor( lhsType, rhsType ) ||
          TypeSystem.isBoxedTypeFor( rhsType, lhsType ) )
      {
        return getHighPriorityPrimitiveOrBoxedConverter( lhsType );
      }
      return getPrimitiveOrBoxedConverter( lhsType );
    }

    //=============================================================================
    //  Primitives coerce to things their boxed type is assignable to
    //=============================================================================
    if( rhsType.isPrimitive() )
    {
      if( lhsType.isAssignableFrom( TypeSystem.getBoxType( rhsType ) ) )
      {
        return getPrimitiveOrBoxedConverter( rhsType );
      }
    }

    //=============================================================================
    //  IMonitorLock supports java-style synchronization
    //=============================================================================
    if( !rhsType.isPrimitive() && GosuTypes.IMONITORLOCK_NAME.equals(lhsType.getName()) )
    {
      return IMonitorLockCoercer.instance();
    }

    //=============================================================================
    // Class<T> <- Meta<T' instanceof JavaType>
    //=============================================================================
    if( (JavaTypes.CLASS().equals( lhsType.getGenericType() ) &&
         (rhsType instanceof IMetaType &&
          (((IMetaType)rhsType).getType() instanceof IHasJavaClass ||
           ((IMetaType)rhsType).getType() instanceof IMetaType && ((IMetaType)((IMetaType)rhsType).getType()).getType() instanceof IHasJavaClass)))  )
    {
      if( !lhsType.isParameterizedType() ||
          lhsType.getTypeParameters()[0].isAssignableFrom( ((IMetaType)rhsType).getType() ) ||
          isStructurallyAssignable( lhsType.getTypeParameters()[0], rhsType ) ||
          isStructurallyAssignable( lhsType.getTypeParameters()[0], ((IMetaType)rhsType).getType() ) ||
          (((IMetaType)rhsType).getType().isPrimitive() && canCoerce( lhsType.getTypeParameters()[0], ((IMetaType)rhsType).getType() )) )
      {
        return MetaTypeToClassCoercer.instance();
      }
    }

    //=============================================================================
    // Numeric type unification
    //=============================================================================
    if( TypeSystem.isNumericType( lhsType ) && TypeSystem.isNumericType( rhsType ) )
    {
      //=============================================================================
      // All numeric values can be down-coerced to the primitives and boxed types
      //=============================================================================
      if( lhsType.isPrimitive() || isBoxed( lhsType ) )
      {
        return getPrimitiveOrBoxedConverter( lhsType );
      }

      //=============================================================================
      // All numeric values can be coerced to BigDecimal
      //=============================================================================
      if( lhsType.equals( JavaTypes.BIG_DECIMAL() ))
      {
        return BigDecimalCoercer.instance();
      }

      //=============================================================================
      // All numeric values can be coerced to BigInteger
      //=============================================================================
      if( lhsType.equals( JavaTypes.BIG_INTEGER() ))
      {
        if( hasPotentialLossOfPrecisionOrScale( lhsType, rhsType ) )
        {
          return BigIntegerCoercer.instance();
        }
        else
        {
          return BigIntegerCoercer.instance();
        }
      }
    }

    //=============================================================================
    // JavaType interface <- compatible block
    //=============================================================================
    if( rhsType instanceof IFunctionType && lhsType.isInterface() )
    {
      IFunctionType rhsFunctionType = (IFunctionType)rhsType;
      IFunctionType lhsFunctionType = FunctionToInterfaceCoercer.getRepresentativeFunctionType( lhsType );
      if( lhsFunctionType != null )
      {
        if( lhsFunctionType.isAssignableFrom( rhsFunctionType ) )
        {
          return FunctionToInterfaceCoercer.instance();
        }
        else
        {
          if( lhsFunctionType.areParamsCompatible( rhsFunctionType ) )
          {
            ICoercer coercer = findCoercer( lhsFunctionType.getReturnType(), rhsFunctionType.getReturnType(), runtime );
            if( coercer != null )
            {
              return FunctionToInterfaceCoercer.instance();
            }
          }
        }
      }
    }
    //=============================================================================
    // JavaType interface <- block class
    //=============================================================================
    if( rhsType instanceof IBlockClass && lhsType.isInterface() )
    {
      // Note this condition is only ever called at runtime e.g., block cast to Object cast to Interface
      return FunctionToInterfaceCoercer.instance();
    }

    //=============================================================================
    // block <- compatible block
    //=============================================================================
    if (lhsType instanceof IFunctionType &&
      TypeSystem.get(FeatureReference.class).getParameterizedType(TypeSystem.get(Object.class), lhsType).isAssignableFrom(rhsType)) {
      return FeatureReferenceToBlockCoercer.instance();
    }

    //=============================================================================
    // Coerce synthetic block classes to function types
    //=============================================================================
    if( lhsType instanceof IFunctionType && rhsType instanceof IBlockClass )
    {
      if( lhsType.isAssignableFrom( ((IBlockClass)rhsType).getBlockType() ) )
      {
        return IdentityCoercer.instance();
      }
    }
    
    //=============================================================================
    // compatible block <- JavaType interface
    //=============================================================================
    if( lhsType instanceof IFunctionType && rhsType.isInterface() &&
        FunctionFromInterfaceCoercer.areTypesCompatible( (IFunctionType)lhsType, rhsType ) )
    {
      return FunctionFromInterfaceCoercer.instance();
    }

    //=============================================================================
    // Coerce block types that are coercable in return values and contravariant in arg types
    //=============================================================================
    if( lhsType instanceof IBlockType && rhsType instanceof IBlockType )
    {
      IBlockType lBlock = (IBlockType)lhsType;
      IBlockType rBlock = (IBlockType)rhsType;
      if( lBlock.areParamsCompatible( rBlock ) )
      {
        IType leftType = lBlock.getReturnType();
        IType rightType = rBlock.getReturnType();
        if( rightType != JavaTypes.pVOID() )
        {
          ICoercer iCoercer = findCoercer( leftType, rightType, runtime );
          if( iCoercer != null && !coercionRequiresWarningIfImplicit( leftType, rightType ))
          {
            return BlockCoercer.instance();
          }
        }
      }
    }
    return null;
  }

  public boolean isPrimitiveOrBoxed( IType lhsType )
  {
    return lhsType.isPrimitive() || isBoxed( lhsType );
  }

  public static boolean isBoxed( IType lhsType )
  {
    return lhsType == JavaTypes.BOOLEAN()
           || lhsType == JavaTypes.BYTE()
           || lhsType == JavaTypes.CHARACTER()
           || lhsType == JavaTypes.DOUBLE()
           || lhsType == JavaTypes.FLOAT()
           || lhsType == JavaTypes.INTEGER()
           || lhsType == JavaTypes.LONG()
           || lhsType == JavaTypes.SHORT();
  }

  protected ICoercer getPrimitiveOrBoxedConverter( IType type )
  {
    if( type == JavaTypes.pBOOLEAN() )
    {
      return BasePrimitiveCoercer.BooleanPCoercer.get();
    }
    else if( type == JavaTypes.BOOLEAN() )
    {
      return BooleanCoercer.instance();
    }
    else if( type == JavaTypes.pBYTE() )
    {
      return BasePrimitiveCoercer.BytePCoercer.get();
    }
    else if( type == JavaTypes.BYTE() )
    {
      return ByteCoercer.instance();
    }
    else if( type == JavaTypes.pCHAR() )
    {
      return BasePrimitiveCoercer.CharPCoercer.get();
    }
    else if( type == JavaTypes.CHARACTER() )
    {
      return CharCoercer.instance();
    }
    else if( type == JavaTypes.pDOUBLE() )
    {
      return BasePrimitiveCoercer.DoublePCoercer.get();
    }
    else if( type == JavaTypes.DOUBLE() )
    {
      return DoubleCoercer.instance();
    }
    else if( type == JavaTypes.pFLOAT() )
    {
      return BasePrimitiveCoercer.FloatPCoercer.get();
    }
    else if( type == JavaTypes.FLOAT() )
    {
      return FloatCoercer.instance();
    }
    else if( type == JavaTypes.pINT() )
    {
      return BasePrimitiveCoercer.IntPCoercer.get();
    }
    else if( type == JavaTypes.INTEGER() )
    {
      return IntCoercer.instance();
    }
    else if( type == JavaTypes.pLONG() )
    {
      return BasePrimitiveCoercer.LongPCoercer.get();
    }
    else if( type == JavaTypes.LONG() )
    {
      return LongCoercer.instance();
    }
    else if( type == JavaTypes.pSHORT() )
    {
      return BasePrimitiveCoercer.ShortPCoercer.get();
    }
    else if( type == JavaTypes.SHORT() )
    {
      return ShortCoercer.instance();
    }
    else if( type == JavaTypes.pVOID() )
    {
      return IdentityCoercer.instance();
    }
    else
    {
      return null;
    }
  }

  protected ICoercer getHighPriorityPrimitiveOrBoxedConverter( IType type )
  {
    if( type == JavaTypes.pBOOLEAN() )
    {
      return BooleanPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.BOOLEAN() )
    {
      return BooleanHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pBYTE() )
    {
      return BytePHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.BYTE() )
    {
      return ByteHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pCHAR() )
    {
      return CharPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.CHARACTER() )
    {
      return CharHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pDOUBLE() )
    {
      return DoublePHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.DOUBLE() )
    {
      return DoubleHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pFLOAT() )
    {
      return FloatPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.FLOAT() )
    {
      return FloatHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pINT() )
    {
      return IntPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.INTEGER() )
    {
      return IntHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pLONG() )
    {
      return LongPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.LONG() )
    {
      return LongHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pSHORT() )
    {
      return ShortPHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.SHORT() )
    {
      return ShortHighPriorityCoercer.instance();
    }
    else if( type == JavaTypes.pVOID() )
    {
      return IdentityCoercer.instance();
    }
    else
    {
      return null;
    }
  }

  public IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional ) throws ParseException
  {
    return verifyTypesComparable( lhsType, rhsType, bBiDirectional, null );
  }

  public IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional, IFullParserState parserState ) throws ParseException
  {
    IType lhsT;
    IType rhsT;
    if( bBiDirectional )
    {
      // Bi-Directional indicates comparison as opposed to assignability, therefore for comparison
      // we need to test comparability between type variables' bounds
      lhsT = TypeSystem.getDefaultParameterizedTypeWithTypeVars( lhsType );
      rhsT = TypeSystem.getDefaultParameterizedTypeWithTypeVars( rhsType );
    }
    else
    {
      lhsT = lhsType;
      rhsT = rhsType;
    }
    
    //==================================================================================
    // Upcasting
    //==================================================================================
    if( lhsT == rhsT )
    {
      return lhsType;
    }
    if( lhsT.equals( rhsT ) )
    {
      return lhsType;
    }
    if( lhsT.isAssignableFrom( rhsT ) )
    {
      return lhsType;
    }

    //==================================================================================
    // null/void confusion (see http://jira/jira/browse/PL-12766)
    //==================================================================================
    if( JavaTypes.pVOID().equals( rhsT ) && !lhsT.isPrimitive() )
    {
      return lhsType;
    }
    if( JavaTypes.pVOID().equals( lhsT ) && !rhsT.isPrimitive() )
    {
      return rhsType;
    }

    //==================================================================================
    // Error type handling
    //==================================================================================
    if( lhsT instanceof IErrorType)
    {
      return lhsType;
    }
    if( rhsT instanceof IErrorType )
    {
      return rhsType;
    }

    //==================================================================================
    // IPlaceholderType type handling
    //==================================================================================
    if( (lhsT instanceof IPlaceholder && ((IPlaceholder)lhsT).isPlaceholder()) ||
        (rhsT instanceof IPlaceholder && ((IPlaceholder)rhsT).isPlaceholder()) )
    {
      return lhsType;
    }

    //==================================================================================
    //Covariant arrays
    //==================================================================================
    if( lhsT.isArray() && rhsT.isArray() )
    {
      // Note an array of primitives and an array of non-primitives are never assignable
      if( lhsT.getComponentType().isPrimitive() == rhsT.getComponentType().isPrimitive() &&
          lhsT.getComponentType().isAssignableFrom( rhsT.getComponentType() ) )
      {
        return lhsType;
      }
    }

    //==================================================================================
    // Downcasting
    //==================================================================================
    if( bBiDirectional )
    {
      if( rhsT.isAssignableFrom( lhsT ) )
      {
        return lhsType;
      }
      if( lhsT.isArray() && rhsT.isArray() )
      {
        if( rhsT.getComponentType().isAssignableFrom( lhsT.getComponentType() ) )
        {
          return lhsType;
        }
      }
    }

    //==================================================================================
    // Structurally suitable (static duck typing)
    //==================================================================================
    if( isStructurallyAssignable( lhsT, rhsT ) )
    {
      return lhsType;
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    if( canCoerce( lhsT, rhsT ) )
    {
      return lhsType;
    }

    if( bBiDirectional )
    {
      if( canCoerce( rhsT, lhsT ) )
      {
        return rhsType;
      }
    }

    String strLhs = TypeSystem.getNameWithQualifiedTypeVariables( lhsType );
    String strRhs = TypeSystem.getNameWithQualifiedTypeVariables( rhsType );
    throw new ParseException( parserState, lhsType, Res.MSG_TYPE_MISMATCH, strLhs, strRhs );
  }

  public static boolean isStructurallyAssignable( IType toType, IType fromType )
  {
    if( !(toType instanceof IGosuClass && ((IGosuClass)toType).isStructure()) )
    {
      return false;
    }
    return isStructurallyAssignable_Laxed( toType, fromType );
  }
  public static boolean isStructurallyAssignable_Laxed( IType toType, IType fromType )
  {
//    if( fromType instanceof IMetaType && ((IMetaType)fromType).getType() instanceof IGosuClass && ((IGosuClass)((IMetaType)fromType).getType()).isStructure() )
//    {
//      // So that:
//      // foo( MyStaticCharAtType )
//      // function foo( t: Type<CharAt> ) {
//      //   var c = (t as CharAt).charAt( 0 )
//      //   ...
//      // }
//      //
//      fromType = ((IMetaType)fromType).getType();
//    }
//    else if( JavaTypes.CLASS().isAssignableFrom( fromType ) && fromType.getTypeParameters()[0] instanceof IGosuClass && ((IGosuClass)((IMetaType)fromType).getType()).isStructure() )
//    {
//      // So that:
//      // foo( MyStaticCharAtType )
//      // function foo( t: Class<CharAt> ) {
//      //   var c = (t as CharAt).charAt( 0 )
//      //   ...
//      // }
//      //
//      fromType = fromType.getTypeParameters()[0];
//    }

    ITypeInfo fromTypeInfo = fromType.getTypeInfo();
    MethodList fromMethods = fromTypeInfo instanceof IRelativeTypeInfo
                             ? ((IRelativeTypeInfo)fromTypeInfo).getMethods( toType )
                             : fromTypeInfo.getMethods();
    ITypeInfo toTypeInfo = toType.getTypeInfo();
    MethodList toMethods = toTypeInfo instanceof IRelativeTypeInfo
                           ? ((IRelativeTypeInfo)toTypeInfo).getMethods( fromType )
                           : toTypeInfo.getMethods();
    for( IMethodInfo toMi : toMethods )
    {
      if( isObjectMethod( toMi ) ) {
        continue;
      }
      if( toMi.getOwnersType() instanceof IGosuEnhancement ) {
        continue;
      }
      IMethodInfo fromMi = fromMethods.findAssignableMethod( toMi, fromType instanceof IMetaType && (!(((IMetaType)fromType).getType() instanceof IGosuClass) || !((IGosuClass)((IMetaType)fromType).getType()).isStructure()) );
      if( fromMi == null ) {
        if( toMi.getDisplayName().startsWith( "@" ) ) {
          // Find matching property/field
          IPropertyInfo fromPi = fromTypeInfo.getProperty( toMi.getDisplayName().substring( 1 ) );
          if( fromPi != null ) {
            if( toMi.getParameters().length == 0 ) {
              boolean bAssignable = toMi.getReturnType().equals( fromPi.getFeatureType() ) ||
                                    arePrimitiveTypesAssignable( toMi.getReturnType(), fromPi.getFeatureType() );
              if( bAssignable ) {
                continue;
              }
            }
            else {
              boolean bAssignable = fromPi.isWritable( toType ) &&
                                    (fromPi.getFeatureType().equals( toMi.getParameters()[0].getFeatureType() ) ||
                                    arePrimitiveTypesAssignable( fromPi.getFeatureType(), toMi.getParameters()[0].getFeatureType() ));
              if( bAssignable ) {
                continue;
              }
            }
          }
        }
        return false;
      }
    }
    return true;
  }

  public static boolean arePrimitiveTypesAssignable( IType toType, IType fromType ) {
    if( toType == null || fromType == null || !toType.isPrimitive() || !fromType.isPrimitive() ) {
      return false;
    }
    if( toType == fromType )
    {
      return true;
    }

    if( toType == JavaTypes.pDOUBLE() ) {
      return fromType == JavaTypes.pFLOAT() ||
             fromType == JavaTypes.pINT() ||
             fromType == JavaTypes.pCHAR() ||
             fromType == JavaTypes.pSHORT() ||
             fromType == JavaTypes.pBYTE();
    }
    if( toType == JavaTypes.pFLOAT() ) {
      return fromType == JavaTypes.pCHAR() ||
             fromType == JavaTypes.pSHORT() ||
             fromType == JavaTypes.pBYTE();
    }
    if( toType == JavaTypes.pLONG() ) {
      return fromType == JavaTypes.pINT() ||
             fromType == JavaTypes.pCHAR() ||
             fromType == JavaTypes.pSHORT() ||
             fromType == JavaTypes.pBYTE();
    }
    if( toType == JavaTypes.pINT() ) {
      return fromType == JavaTypes.pSHORT() ||
             fromType == JavaTypes.pCHAR() ||
             fromType == JavaTypes.pBYTE();
    }
    if( toType == JavaTypes.pSHORT() ) {
      return fromType == JavaTypes.pBYTE();
    }

    return false;
  }

  public static boolean isObjectMethod( IMethodInfo mi )
  {
    IGosuClass gosuObjectType = GosuShop.getGosuClassFrom( JavaTypes.IGOSU_OBJECT() );
    if( mi.getOwnersType() == gosuObjectType || mi.getDisplayName().equals( "@itype" ) )
    {
      // A IGosuObject method
      return true;
    }

    IParameterInfo[] params = mi.getParameters();
    IType[] paramTypes = new IType[params.length];
    for( int i = 0; i < params.length; i++ )
    {
      paramTypes[i] = params[i].getFeatureType();
    }
    IRelativeTypeInfo ti = (IRelativeTypeInfo)JavaTypes.OBJECT().getTypeInfo();
    IMethodInfo objMethod = ti.getMethod( JavaTypes.OBJECT(), mi.getDisplayName(), paramTypes );
    return objMethod != null;
  }

  public boolean coercionRequiresWarningIfImplicit( IType lhsType, IType rhsType )
  {

    //==================================================================================
    // Upcasting
    //==================================================================================
    if( lhsType == rhsType )
    {
      return false;
    }
    if( lhsType.equals( rhsType ) )
    {
      return false;
    }
    if( lhsType.isAssignableFrom( rhsType ) )
    {
      return false;
    }
    if ( rhsType.isPrimitive() && lhsType.isAssignableFrom( TypeSystem.getBoxType( rhsType ) ) )
    {
      return false;
    }

    //==================================================================================
    // null/void confusion (see http://jira/jira/browse/PL-12766)
    //==================================================================================
    if( JavaTypes.pVOID().equals(rhsType) )
    {
      return false;
    }
    
    //==================================================================================
    // Error type handling
    //==================================================================================
    if( lhsType instanceof IErrorType )
    {
      return false;
    }
    if( rhsType instanceof IErrorType )
    {
      return false;
    }

    //==================================================================================
    // IPlaceholderType type handling
    //==================================================================================
    if( (lhsType instanceof IPlaceholder && ((IPlaceholder)lhsType).isPlaceholder()) ||
        (rhsType instanceof IPlaceholder && ((IPlaceholder)rhsType).isPlaceholder()) )
    {
      return false;
    }

    //==================================================================================
    //Covariant arrays (java semantics, which are wrong)
    //
    // (Five years later, let me totally disagree with my former self.  Java array semantics are not only right,
    //  we've decided to adopt them for generics.  Worse is better.)
    //==================================================================================
    if( lhsType.isArray() && rhsType.isArray() )
    {
      if( lhsType.getComponentType().isAssignableFrom( rhsType.getComponentType() ) )
      {
        return false;
      }
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    if( TypeSystem.isNumericType( lhsType ) &&
        TypeSystem.isNumericType( rhsType ) )
    {
      return hasPotentialLossOfPrecisionOrScale( lhsType, rhsType );
    }
    else
    {
      if( TypeSystem.isBoxedTypeFor( lhsType, rhsType ) ||
               TypeSystem.isBoxedTypeFor( rhsType, lhsType ) )
      {
        return false;
      }
      else
      {
        ICoercer iCoercer = findCoercer( lhsType, rhsType, false );
        return iCoercer != null && iCoercer.isExplicitCoercion();
      }
    }
  }

  /**
   * Given a value and a target Class, return a compatible value via the target Class.
   */
  public final Object convertValue(Object value, IType intrType)
  {
    //==================================================================================
    // Null handling
    //==================================================================================
    if( intrType == null )
    {
      return null;
    }

    //## todo: This is a horrible hack
    //## todo: The only known case where this is necessary is when we have an array of parameterized java type e.g., List<String>[]
    intrType = getBoundingTypeOfTypeVariable( intrType );

    if( value == null )
    {
      return intrType.isPrimitive() ? convertNullAsPrimitive( intrType, true ) : null;
    }

    IType runtimeType = TypeSystem.getFromObject( value );

    //==================================================================================
    // IPlaceholder type handling
    //==================================================================================
    if( (intrType instanceof IPlaceholder && ((IPlaceholder)intrType).isPlaceholder()) ||
        (runtimeType instanceof IPlaceholder && ((IPlaceholder)runtimeType).isPlaceholder()) )
    {
      return value;
    }

    //==================================================================================
    // Runtime polymorphism (with java array semantics)
    //==================================================================================
    if( intrType == runtimeType )
    {
      return value;
    }
    if( intrType.equals( runtimeType ) )
    {
      return value;
    }
    if( intrType.isAssignableFrom( runtimeType ) )
    {
      value = extractObjectArray( intrType, value );
      return value;
    }
    if( intrType.isArray() && runtimeType.isArray() )
    {
      if( intrType.getComponentType().isAssignableFrom( runtimeType.getComponentType() ) )
      {
        value = extractObjectArray( intrType, value );
        return value;
      }
      else if( intrType instanceof IGosuArrayClass &&
               value instanceof IGosuObject[] )
      {
        return value;
      }
    }
    // Proxy coercion. The proxy class generated for Java classes is not a super type of the Gosu class.
    // The following check allows coercion of the Gosu class to the Gosu proxy class needed for the super call.
    if( intrType instanceof IJavaType &&
        IGosuClass.ProxyUtil.isProxy( intrType ) &&
        runtimeType instanceof IGosuClass &&
        intrType.getSupertype() != null &&
        intrType.getSupertype().isAssignableFrom( runtimeType ) )
    {
      return value;
    }

    // Check Java world types
    //noinspection deprecation
    if( intrType instanceof IJavaType &&
        ((IJavaType)intrType).getIntrinsicClass().isAssignableFrom( value.getClass() ) )
    {
      return value;
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    Object convertedValue = coerce( intrType, runtimeType, value );
    if( convertedValue != null )
    {
      return convertedValue;
    }
    else
    {
      //If the null arose from an actual coercion, return it
      if( canCoerce( intrType, runtimeType ) )
      {
        return convertedValue;
      }
      else
      {
        //otherwise, return the value itself uncoerced (See comment above)
        if( !runtimeType.isArray() )
        {
          return NO_DICE;
        }
        return value;
      }
    }
  }

  private IType getBoundingTypeOfTypeVariable( IType intrType )
  {
    int i = 0;
    while( intrType instanceof ITypeVariableArrayType )
    {
      i++;
      intrType = intrType.getComponentType();
    }
    if( intrType instanceof ITypeVariableType )
    {
      intrType = ((ITypeVariableType)intrType).getBoundingType();
      while( i-- > 0 )
      {
        intrType = intrType.getArrayType();
      }
    }
    return intrType;
  }

  private Object extractObjectArray( IType intrType, Object value )
  {
    if( intrType.isArray() &&
        intrType instanceof IJavaArrayType &&
        value instanceof IGosuArrayClassInstance )
    {
      value = ((IGosuArrayClassInstance)value).getObjectArray();
    }
    return value;
  }

  public Object convertNullAsPrimitive( IType intrType, boolean isForBoxing )
  {
    if( intrType == null )
    {
      return null;
    }

    if( !intrType.isPrimitive() )
    {
      throw GosuShop.createEvaluationException( intrType.getName() + " is not a primitive type." );
    }

    if( intrType == JavaTypes.pBYTE() )
    {
      return (byte) 0;
    }
    if( intrType == JavaTypes.pCHAR() )
    {
      return '\0';
    }
    if( intrType == JavaTypes.pDOUBLE() )
    {
      return isForBoxing ? IGosuParser.NaN : (double) 0;
    }
    if( intrType == JavaTypes.pFLOAT() )
    {
      return isForBoxing ? Float.NaN : (float) 0;
    }
    if( intrType == JavaTypes.pINT() )
    {
      return 0;
    }
    if( intrType == JavaTypes.pLONG() )
    {
      return (long) 0;
    }
    if( intrType == JavaTypes.pSHORT() )
    {
      return (short) 0;
    }
    if( intrType == JavaTypes.pBOOLEAN() )
    {
      return Boolean.FALSE;
    }
    if( intrType == JavaTypes.pVOID() )
    {
      return null;
    }
    throw GosuShop.createEvaluationException( "Unexpected primitive type: " + intrType.getName() );
  }

  public ICoercer resolveCoercerStatically( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( typeToCoerceTo == null || typeToCoerceFrom == null )
    {
      return null;
    }
    else if( typeToCoerceTo == typeToCoerceFrom )
    {
      return null;
    }
    else if( typeToCoerceTo.equals( typeToCoerceFrom ) )
    {
      return null;
    }
    else if( typeToCoerceTo instanceof IErrorType || typeToCoerceFrom instanceof IErrorType )
    {
      return null;
    }
    else if( typeToCoerceTo instanceof ITypeVariableArrayType )
    {
      return RuntimeCoercer.instance();
    }
    else if( typeToCoerceTo instanceof ITypeVariableType )
    {
      return TypeVariableCoercer.instance();
    }
    else if( typeToCoerceTo.isAssignableFrom( typeToCoerceFrom ) )
    {
      return null;
    }
    else
    {
      ICoercer coercerInternal = findCoercerImpl( typeToCoerceTo, typeToCoerceFrom, false );
      if( coercerInternal == null )
      {
        if( typeToCoerceFrom.isAssignableFrom( typeToCoerceTo ) && !JavaTypes.pVOID().equals(typeToCoerceTo) )
        {
          if( areJavaClassesAndAreNotAssignable( typeToCoerceTo, typeToCoerceFrom ) )
          {
            return RuntimeCoercer.instance();
          }
          return identityOrRuntime( typeToCoerceTo, typeToCoerceFrom );
        }
        else if( (typeToCoerceFrom.isInterface() || typeToCoerceTo.isInterface()) &&
                 !typeToCoerceFrom.isPrimitive() && !typeToCoerceTo.isPrimitive() )
        {
          return identityOrRuntime( typeToCoerceTo, typeToCoerceFrom );
        }
      }
      return coercerInternal;
    }
  }

  private boolean areJavaClassesAndAreNotAssignable( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( typeToCoerceFrom instanceof IJavaType && typeToCoerceTo instanceof IJavaType )
    {
      if( !((IJavaType)typeToCoerceFrom).getBackingClassInfo().isAssignableFrom( ((IJavaType)typeToCoerceTo).getBackingClassInfo() ) )
      {
        return true;
      }
    }
    return false;
  }

  private ICoercer identityOrRuntime( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( TypeSystem.isBytecodeType( typeToCoerceFrom ) &&
        TypeSystem.isBytecodeType( typeToCoerceTo ) )
    {
      return IdentityCoercer.instance(); // (perf) class-to-class downcast can use checkcast bytecode
    }
    if( typeToCoerceTo instanceof IGosuClass && ((IGosuClass)typeToCoerceTo).isStructure() &&
        typeToCoerceFrom instanceof IMetaType )
    {
      return IdentityCoercer.instance();
    }
    return RuntimeCoercer.instance();
  }


  public Double makeDoubleFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension)
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Double )
    {
      return (Double)obj;
    }

    double d;

    if( obj instanceof Number )
    {
      d = ((Number)obj).doubleValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? IGosuParser.ONE : IGosuParser.ZERO;
    }
    else if( obj instanceof Date )
    {
      return (double)((Date)obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (double) ((Character) obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( JavaTypes.NUMBER(),
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, JavaTypes.NUMBER() );
      return num.doubleValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeDoubleFrom( parseNumber( strValue ) );
    }

    if( d >= 0D && d <= 9D )
    {
      int i = (int)d;

      if( ((double)i == d) && (i >= 0 && i <= 9) )
      {
        return IGosuParser.DOUBLE_DIGITS[i];
      }
    }

    return d;
  }

  public int makePrimitiveIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return 0;
    }
    else
    {
      return makeIntegerFrom( obj );
    }
  }
  public Integer makeIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Integer )
    {
      return (Integer)obj;
    }

    if( obj instanceof Number )
    {
      return ( (Number) obj ).intValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj
             ? IGosuParser.ONE.intValue()
             : IGosuParser.ZERO.intValue();
    }
    else if( obj instanceof Date )
    {
      return (int) ((Date) obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (int) ((Character) obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( JavaTypes.NUMBER(),
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, JavaTypes.NUMBER() );
      return num.intValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeIntegerFrom( parseNumber( strValue ) );
    }
  }

  public long makePrimitiveLongFrom( Object obj )
  {
    if( obj == null )
    {
      return 0;
    }
    else
    {
      return makeLongFrom( obj );
    }
  }

  public Long makeLongFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Long )
    {
      return (Long)obj;
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).longValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj
             ? IGosuParser.ONE.longValue()
             : IGosuParser.ZERO.longValue();
    }
    else if( obj instanceof Date )
    {
      return ((Date)obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (long)((Character)obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( JavaTypes.NUMBER(),
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj , JavaTypes.NUMBER() );
      return num.longValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeLongFrom( parseNumber( strValue ) );
    }
  }

  public BigDecimal makeBigDecimalFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof BigDecimal )
    {
      return (BigDecimal)obj;
    }

    if( obj instanceof String )
    {
      try
      {
        return (BigDecimal)BIG_DECIMAL_FORMAT.parse( obj.toString() );
      }
      catch( java.text.ParseException e )
      {
        throw GosuExceptionUtil.convertToRuntimeException( e );
      }
    }

    if( obj instanceof Integer )
    {
      return BigDecimal.valueOf( (Integer)obj );
    }
    else if( obj instanceof BigInteger )
    {
      return new BigDecimal( (BigInteger)obj );
    }
    else if( obj instanceof Long )
    {
      return BigDecimal.valueOf( (Long)obj );
    }
    else if( obj instanceof Short )
    {
      return BigDecimal.valueOf( (Short)obj );
    }
    else if( obj instanceof Byte )
    {
      return BigDecimal.valueOf( (Byte)obj );
    }
    else if( obj instanceof Character )
    {
      return BigDecimal.valueOf( (Character)obj );
    }
    else if (obj instanceof Float)
    {
      // Convert a float directly to a BigDecimal via the String value; don't
      // up-convert it to a double first, since converting a double can be lossy
      return new BigDecimal( obj.toString());
    }
    else if( obj instanceof Number )
    {
      // Make a double from any type of number that we haven't yet dealt with
      Double d = makeDoubleFrom( obj );
      return new BigDecimal( d.toString() );
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? BigDecimal.ONE : BigDecimal.ZERO;
    }
    else if( obj instanceof Date )
    {
      return new BigDecimal( ((Date)obj).getTime() );
    }
    else if( CommonServices.getCoercionManager().canCoerce( JavaTypes.NUMBER(), TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, JavaTypes.NUMBER() );
      return makeBigDecimalFrom( num );
    }
    else
    {
      // As a last resort, convert it to a double and then convert that to a big decimal
      Double d = makeDoubleFrom( obj );
      return new BigDecimal( d.toString() );
    }
  }

  public BigInteger makeBigIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof BigInteger )
    {
      return (BigInteger)obj;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof String )
    {
      String strValue = (String)obj;
      return makeBigIntegerFrom( parseNumber( strValue ) );
    }

    BigDecimal d = makeBigDecimalFrom( obj );
    return d.toBigInteger();
  }

  public double makePrimitiveDoubleFrom( Object obj )
  {
    if( obj == null )
    {
      return Double.NaN;
    }
    else
    {
      return makeDoubleFrom( obj );
    }
  }

  public Float makeFloatFrom( Object obj )
  {
    if( obj == null )
    {
      return Float.NaN;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).floatValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? 1f : 0f;
    }
    else if( obj instanceof Date )
    {
      return (float) ((Date) obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (float) ((Character) obj).charValue();
    }
    else
    {
      try
      {
        return Float.parseFloat( obj.toString() );
      }
      catch( Throwable t )
      {
        // Nonparsable floating point numbers have a NaN value (a la JavaScript)
        return Float.NaN;
      }
    }
  }

  public float makePrimitiveFloatFrom( Object obj )
  {
    if( obj == null )
    {
      return Float.NaN;
    }
    else
    {
      return makeFloatFrom( obj );
    }
  }

  public String makeStringFrom( Object obj )
  {
    return obj == null ? null : obj.toString();
  }

  /**
   * @return A Boolean for an arbitrary object.
   */
  public boolean makePrimitiveBooleanFrom( Object obj )
  {
    //noinspection SimplifiableIfStatement
    if( obj == null )
    {
      return false;
    }
    else
    {
      return Boolean.TRUE.equals( makeBooleanFrom( obj ) );
    }
  }

  public Boolean makeBooleanFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Boolean )
    {
      return (Boolean)obj;
    }

    if( obj instanceof String )
    {
      return Boolean.valueOf( (String)obj );
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).doubleValue() == 0D ? Boolean.FALSE : Boolean.TRUE;
    }

    return Boolean.valueOf( obj.toString() );
  }

  /**
   * Returns a new Date instance representing the object.
   */
  public Date makeDateFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Date )
    {
      return (Date)obj;
    }

    if( obj instanceof Number )
    {
      return new Date( ((Number)obj).longValue() );
    }

    if( obj instanceof Calendar)
    {
      return ((Calendar)obj).getTime();
    }

    if( !(obj instanceof String) )
    {
      obj = obj.toString();
    }

    try
    {
      return parseDateTime( (String)obj );
    }
    catch( Exception e )
    {
      //e.printStackTrace();
    }

    return null;
  }

  @Override
  public boolean isDateTime( String str ) throws java.text.ParseException
  {
    return parseDateTime( str ) != null;
  }

  /**
   * Produce a date from a string using standard DateFormat parsing.
   */
  public Date parseDateTime( String str ) throws java.text.ParseException
  {
    if( str == null )
    {
      return null;
    }

    return DateFormat.getDateInstance().parse(str);
  }

  /**
   * Convert a string to an array of specified type.
   * @param strValue the string to convert
   * @param intrType the array component type
   * @return the string converted to an array
   */
  public static Object makeArrayFromString( String strValue, IType intrType )
  {
    if( JavaTypes.pCHAR() == intrType )
    {
      return strValue.toCharArray();
    }

    if( JavaTypes.CHARACTER() == intrType )
    {
      Character[] characters = new Character[strValue.length()];
      for( int i = 0; i < characters.length; i++ )
      {
        characters[i] = strValue.charAt(i);
      }

      return characters;
    }

    if( JavaTypes.STRING() == intrType )
    {
      String[] strings = new String[strValue.length()];
      for( int i = 0; i < strings.length; i++ )
      {
        strings[i] = String.valueOf( strValue.charAt( i ) );
      }

      return strings;
    }

    throw GosuShop.createEvaluationException( "The type, " + intrType.getName() + ", is not supported as a coercible component type to a String array." );
  }

  public String formatDate( Date value, String strFormat )
  {
    DateFormat df = new SimpleDateFormat( strFormat );
    return df.format( value );
  }

  public String formatTime( Date value, String strFormat )
  {
    DateFormat df = new SimpleDateFormat( strFormat );
    return df.format( value );
  }

  public String formatNumber( Double value, String strFormat )
  {
    NumberFormat nf = new DecimalFormat( strFormat );
    return nf.format( value.doubleValue() );
  }

  public Number parseNumber( String strValue )
  {
    try
    {
      return Double.parseDouble( strValue );
    }
    catch( Exception e )
    {
      // Nonparsable floating point numbers have a NaN value (a la JavaScript)
      return IGosuParser.NaN;
    }
  }

  private static class NullSentinalCoercer extends StandardCoercer
  {
    private static final NullSentinalCoercer INSTANCE = new NullSentinalCoercer();
    @Override
    public Object coerceValue( IType typeToCoerceTo, Object value )
    {
      throw new IllegalStateException( "This is the null sentinal coercer, and is used only to " +
                                       "represent a miss in the coercer cache.  It should never " +
                                       "be returned for actual use" );
    }
    public static NullSentinalCoercer instance()
    {
      return INSTANCE;
    }
  }
}
