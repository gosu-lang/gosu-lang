/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.lang.reflect.interval.BigDecimalInterval;
import gw.lang.reflect.interval.BigIntegerInterval;
import gw.lang.reflect.interval.DateInterval;
import gw.lang.reflect.interval.ISequenceable;
import gw.lang.reflect.interval.IntegerInterval;
import gw.lang.reflect.interval.LongInterval;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.IntervalExpression;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.interval.ComparableInterval;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.interval.SequenceableInterval;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class IntervalExpressionTransformer extends AbstractExpressionTransformer<IntervalExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, IntervalExpression expr )
  {
    IntervalExpressionTransformer gen = new IntervalExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private IntervalExpressionTransformer( TopLevelTransformationContext cc, IntervalExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType type = _expr().getType();
    if( type == JavaTypes.INTEGER_INTERVAL() )
    {
      return makeIntegerInterval();
    }
    else if( type == JavaTypes.LONG_INTERVAL() )
    {
      return makeLongInterval();
    }
    else if( type == JavaTypes.BIG_INTEGER_INTERVAL() )
    {
      return makeBigIntegerInterval();
    }
    else if( type == JavaTypes.BIG_DECIMAL_INTERVAL() )
    {
      return makeBigDecimalInterval();
    }
    else if( type == JavaTypes.DATE_INTERVAL() )
    {
      return makeDateInterval();
    }
    else if( type.getGenericType() == JavaTypes.SEQUENCEABLE_INTERVAL() )
    {
      return makeSequenceableInterval();
    }
    if( type.getGenericType() == JavaTypes.COMPARABLE_INTERVAL() )
    {
      return makeComparableInterval();
    }
    throw new IllegalStateException( "Unknown interval type: " + type.getName() );
  }

  private IRExpression makeIntegerInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );
    args.add( boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );
    args.add( boxValue( JavaTypes.pINT(), pushConstant( 1 ) ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeIntegerInterval",
                             new Class[]{Object.class, Object.class, Integer.class, boolean.class, boolean.class},
                             args );
  }

  private IRExpression makeLongInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );
    args.add( boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );
    args.add( boxValue( JavaTypes.pLONG(), pushConstant( 1L ) ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeLongInterval",
                             new Class[]{Long.class, Long.class, Long.class, boolean.class, boolean.class},
                             args );
  }

  private IRExpression makeBigIntegerInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    args.add( ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    args.add( getStaticField( JavaTypes.BIG_INTEGER(), "ONE", getDescriptor( JavaTypes.BIG_INTEGER() ), IRelativeTypeInfo.Accessibility.PUBLIC ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeBigIntegerInterval",
                             new Class[]{BigInteger.class, BigInteger.class, BigInteger.class, boolean.class, boolean.class},
                             args );
  }

  private IRExpression makeBigDecimalInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    args.add( ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    args.add( getStaticField( JavaTypes.BIG_DECIMAL(), "ONE", getDescriptor( JavaTypes.BIG_DECIMAL() ), IRelativeTypeInfo.Accessibility.PUBLIC ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeBigDecimalInterval",
                             new Class[]{Number.class, Number.class, BigDecimal.class, boolean.class, boolean.class},
                             args );
  }

  private IRExpression makeDateInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    args.add( ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    args.add( pushNull() );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeDateInterval",
                             new Class[]{Date.class, Date.class, Integer.class, boolean.class, boolean.class},
                             args );
  }

  private IRExpression makeSequenceableInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    args.add( ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeSequenceableInterval",
                             new Class[]{ISequenceable.class, ISequenceable.class, boolean.class, boolean.class},
                             args );
  }
  
  private IRExpression makeComparableInterval()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    args.add( ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    args.add( ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    args.add( pushConstant( _expr().isLeftClosed() ) );
    args.add( pushConstant( _expr().isRightClosed() ) );

    return callStaticMethod( IntervalExpressionTransformer.class, "_makeComparableInterval",
                             new Class[]{Comparable.class, Comparable.class, boolean.class, boolean.class},
                             args );
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static IntegerInterval _makeIntegerInterval( Object lhs, Object rhs, Integer step, boolean bLeftClosed, boolean bRightClosed )
  {
    if( lhs instanceof Character )
    {
      lhs = Integer.valueOf( (Character)lhs );
    }
    if( rhs instanceof Character )
    {
      rhs = Integer.valueOf( (Character)rhs );
    }

    int iLhs = ((Number)lhs).intValue();
    int iRhs = ((Number)rhs).intValue();
    if( iRhs < iLhs )
    {
      return new IntegerInterval( iRhs, iLhs, step, bLeftClosed, bRightClosed, true );
    }
    return new IntegerInterval( iLhs, iRhs, step, bLeftClosed, bRightClosed, false );
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static LongInterval _makeLongInterval( Long lhs, Long rhs, Long step, boolean bLeftClosed, boolean bRightClosed )
  {
    if( rhs.longValue() < lhs.longValue() )
    {
      return new LongInterval( rhs, lhs, step, bLeftClosed, bRightClosed, true );
    }
    return new LongInterval( lhs, rhs, step, bLeftClosed, bRightClosed, false );
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static BigIntegerInterval _makeBigIntegerInterval( BigInteger lhs, BigInteger rhs, BigInteger step, boolean bLeftClosed, boolean bRightClosed )
  {
    if( rhs.compareTo( lhs ) < 0 )
    {
      return new BigIntegerInterval( rhs, lhs, step, bLeftClosed, bRightClosed, true );
    }
    return new BigIntegerInterval( lhs, rhs, step, bLeftClosed, bRightClosed, false );
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static BigDecimalInterval _makeBigDecimalInterval( Number lhs, Number rhs, BigDecimal step, boolean bLeftClosed, boolean bRightClosed )
  {
    BigDecimal biglhs = lhs instanceof BigDecimal ? (BigDecimal)lhs : new BigDecimal( lhs.toString() );
    BigDecimal bigrhs = rhs instanceof BigDecimal ? (BigDecimal)rhs : new BigDecimal( rhs.doubleValue() == Double.POSITIVE_INFINITY ? "1E+1000" : rhs.toString() );
    if( bigrhs.compareTo( biglhs ) < 0 )
    {
      return new BigDecimalInterval( bigrhs, biglhs, step, bLeftClosed, bRightClosed, true );
    }
    return new BigDecimalInterval( biglhs, bigrhs, step, bLeftClosed, bRightClosed, false );
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static SequenceableInterval _makeSequenceableInterval( ISequenceable lhs, ISequenceable rhs, boolean bLeftClosed, boolean bRightClosed )
  {
    if( ((Comparable)rhs).compareTo( lhs ) < 0 )
    {
      return new SequenceableInterval( rhs, lhs, null, null, bLeftClosed, bRightClosed, true );
    }
    return new SequenceableInterval( lhs, rhs, null, null, bLeftClosed, bRightClosed, false );
  }
  
  @SuppressWarnings({"UnusedDeclaration"})
  public static ComparableInterval _makeComparableInterval( Comparable lhs, Comparable rhs, boolean bLeftClosed, boolean bRightClosed )
  {
    if( rhs.compareTo( lhs ) < 0 )
    {
      return new ComparableInterval( rhs, lhs, bLeftClosed, bRightClosed, true );
    }
    return new ComparableInterval( lhs, rhs, bLeftClosed, bRightClosed, false );
  }
  
  @SuppressWarnings({"UnusedDeclaration"})
  public static DateInterval _makeDateInterval( Date lhs, Date rhs, Integer step, boolean bLeftClosed, boolean bRightClosed )
  {
    if( step == null )
    {
      step = 1;
    }
    
    if( rhs.compareTo( lhs ) < 0 )
    {
      return new DateInterval( rhs, lhs, step, null, bLeftClosed, bRightClosed, true );
    }
    return new DateInterval( lhs, rhs, step, null, bLeftClosed, bRightClosed, false );
  }
}
