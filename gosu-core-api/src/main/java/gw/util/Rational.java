package gw.util;

import gw.lang.reflect.interval.ISequenceable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;


/**
 */
final public class Rational extends Number implements ISequenceable<Rational, Rational, Void>, Serializable
{
  public static final Rational ZERO = new Rational( BigInteger.ZERO, BigInteger.ONE, true );
  public static final Rational ONE = new Rational( BigInteger.ONE, BigInteger.ONE, true );

  private static final int VERSION_1 = 1;

  private final BigInteger _numerator;
  private final BigInteger _denominator;
  private boolean _reduced;

  public static Rational get( int numerator )
  {
    return get( BigInteger.valueOf( numerator ), BigInteger.ONE );
  }
  public static Rational get( int numerator, int denominator )
  {
    return get( BigInteger.valueOf( numerator ), BigInteger.valueOf( denominator ) );
  }
  public static Rational get( long numerator )
  {
    return get( BigInteger.valueOf( numerator ), BigInteger.ONE );
  }
  public static Rational get( long numerator, long denominator )
  {
    return get( BigInteger.valueOf( numerator ), BigInteger.valueOf( denominator ) );
  }
  public static Rational get( float f )
  {
    return get( Float.toString( f ) );
  }
  public static Rational get( double d )
  {
    return get( Double.toString( d ) );
  }
  public static Rational get( BigInteger numerator )
  {
    return get( numerator, BigInteger.ONE );
  }
  public static Rational get( String decimal )
  {
    int iDiv = decimal.indexOf( "/" );
    if( iDiv > 0 )
    {
      String numerator = decimal.substring( 0, iDiv ).trim();
      String denominator = decimal.substring( iDiv+1 ).trim();
      boolean numeratorIsDecimal = isDecimalString( numerator );
      boolean denominatorIsDecimal = isDecimalString( denominator );
      if( numeratorIsDecimal )
      {
        if( denominatorIsDecimal )
        {
          return get( new BigDecimal( numerator ) ).divide( get( new BigDecimal( denominator ) ) );
        }
        return get( new BigDecimal( numerator ) ).divide( new BigInteger( denominator ) );
      }
      else if( denominatorIsDecimal )
      {
        return get( new BigInteger( numerator ) ).divide( get( new BigDecimal( denominator ) ) );
      }
      return get( new BigInteger( numerator ), new BigInteger( denominator ) );
    }
    else
    {
      if( isDecimalString( decimal ) )
      {
        return get( new BigDecimal( decimal ) );
      }
      return get( new BigInteger( decimal ) );
    }
  }

  private static boolean isDecimalString( String decimal )
  {
    return decimal.indexOf( '.' ) >= 0 ||
           decimal.indexOf( 'e' ) > 0 ||
           decimal.indexOf( 'E' ) > 0;
  }

  public static Rational get( BigDecimal bd )
  {
    int scale = bd.scale();

    BigInteger numerator;
    BigInteger denominator;
    if( scale >= 0 )
    {
      numerator = bd.unscaledValue();
      denominator = BigInteger.TEN.pow( scale );
    }
    else
    {
      numerator = bd.unscaledValue().multiply( BigInteger.TEN.pow( -scale ) );
      denominator = BigInteger.ONE;
    }
    return get( numerator, denominator );
  }
  public static Rational get( BigInteger numerator, BigInteger denominator )
  {
    return get( numerator, denominator, false );
  }
  private static Rational get( BigInteger numerator, BigInteger denominator, boolean reduced )
  {
    if( numerator.equals( BigInteger.ZERO ) )
    {
      return ZERO;
    }
    if( numerator.equals( BigInteger.ONE ) && denominator.equals( BigInteger.ONE ) )
    {
      return ONE;
    }
    return new Rational( numerator, denominator, reduced );
  }

  private Rational( BigInteger numerator, BigInteger denominator, boolean reduced )
  {
    if( denominator.signum() == 0 )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    if( numerator.signum() == 0 )
    {
      _numerator = BigInteger.ZERO;
      _denominator = BigInteger.ONE;
    }
    else
    {
      if( denominator.signum() == -1 )
      {
        numerator = numerator.negate();
        denominator = denominator.negate();
      }

      _numerator = numerator;
      _denominator = denominator;
    }
    _reduced = reduced;
  }

  public Rational reduce()
  {
    if( !_reduced )
    {
      BigInteger gcd = _numerator.gcd( _denominator );
      if( gcd.compareTo( BigInteger.ONE ) > 0 )
      {
        return get( _numerator.divide( gcd ), _denominator.divide( gcd ), true );
      }
      _reduced = true;
    }
    return this;
  }

  public BigInteger getNumerator()
  {
    return _numerator;
  }

  public BigInteger getDenominator()
  {
    return _denominator;
  }

  public BigInteger wholePart() {
    return _numerator.divide( _denominator );
  }

  public Rational fractionPart()
  {
    BigInteger remainder = _numerator.remainder( _denominator );
    if( remainder.signum() == 0 )
    {
      return ZERO;
    }
    return Rational.get( remainder, _denominator );
  }

  @Override
  public int intValue()
  {
    return _numerator.divide( _denominator ).intValue();
  }

  @Override
  public long longValue()
  {
    return _numerator.divide( _denominator ).longValue();
  }

  @Override
  public double doubleValue()
  {
    return toBigDecimal().doubleValue();
  }

  @Override
  public float floatValue()
  {
    return toBigDecimal().floatValue();
  }

  public BigInteger toBigInteger()
  {
    return toBigDecimal().toBigInteger();
  }

  public BigDecimal toBigDecimal()
  {
    return toBigDecimal( MathContext.DECIMAL128 );
  }

  public BigDecimal toBigDecimal( MathContext mc )
  {
    return equals( ZERO )
           ? BigDecimal.ZERO
           : new BigDecimal( _numerator ).divide( new BigDecimal( _denominator ), mc );
  }

  public boolean isInteger()
  {
    return _denominator.equals( BigInteger.ONE );
  }

  public Rational add( int i )
  {
    return i == 0 ? this : add( get( i ) );
  }

  public Rational add( long l )
  {
    return l == 0 ? this : add( get( l ) );
  }

  public Rational add( float f )
  {
    return f == 0 ? this : add( get( f ) );
  }

  public Rational add( double d )
  {
    return d == 0 ? this : add( get( d ) );
  }

  public Rational add( BigInteger bg )
  {
    if( signum() == 0 )
    {
      return get( bg );
    }
    if( bg.signum() == 0 )
    {
      return this;
    }

    return bg.equals( BigInteger.ZERO )
           ? this
           : get( _numerator.add( _denominator.multiply( bg ) ), _denominator );
  }

  public Rational add( BigDecimal bd )
  {
    return bd.signum() == 0 ? this : add( get( bd ) );
  }

  public Rational add( Rational rational )
  {
    if( rational.signum() == 0 )
    {
      return this;
    }
    if( signum() == 0 )
    {
      return rational;
    }

    BigInteger numerator;
    BigInteger denominator;

    if( _denominator.equals( rational._denominator ) )
    {
      numerator = _numerator.add( rational._numerator );
      denominator = _denominator;
    }
    else
    {
      numerator = (_numerator.multiply( rational._denominator )).add( (rational._numerator).multiply( _denominator ) );
      denominator = _denominator.multiply( rational._denominator );
    }

    return numerator.signum() == 0
           ? ZERO
           : get( numerator, denominator );
  }

  public Rational subtract( int i )
  {
    return subtract( BigInteger.valueOf( i ) );
  }

  public Rational subtract( long l )
  {
    return subtract( BigInteger.valueOf( l ) );
  }

  public Rational subtract( float f )
  {
    return subtract( get( f ) );
  }

  public Rational subtract( double d )
  {
    return subtract( get( d ) );
  }

  public Rational subtract( BigInteger bi )
  {
    if( bi.signum() == 0 )
    {
      return this;
    }
    if( signum() == 0 )
    {
      return get( bi.negate() );
    }
    return get( _numerator.subtract( _denominator.multiply( bi ) ), _denominator );
  }

  public Rational subtract( BigDecimal bd )
  {
    return bd.signum() == 0 ? this : subtract( get( bd ) );
  }

  public Rational subtract( Rational rational )
  {
    if( rational.signum() == 0 )
    {
      return this;
    }
    if( signum() == 0 )
    {
      return rational.negate();
    }

    BigInteger numerator;
    BigInteger denominator;
    if( _denominator.equals( rational._denominator ) )
    {
      numerator = _numerator.subtract( rational._numerator );
      denominator = _denominator;
    }
    else
    {
      numerator = (_numerator.multiply( rational._denominator )).subtract( (rational._numerator).multiply( _denominator ) );
      denominator = _denominator.multiply( rational._denominator );
    }
    return numerator.signum() == 0
           ? ZERO
           : get( numerator, denominator );
  }

  public Rational multiply( int i )
  {
    if( i == 0 || signum() == 0 )
    {
      return ZERO;
    }
    return multiply( BigInteger.valueOf( i ) );
  }

  public Rational multiply( long l )
  {
    if( l == 0 || signum() == 0 )
    {
      return ZERO;
    }
    return multiply( BigInteger.valueOf( l ) );
  }

  public Rational multiply( float f )
  {
    if( f == 0 || signum() == 0 )
    {
      return ZERO;
    }
    return multiply( get( f ) );
  }

  public Rational multiply( double d )
  {
    if( d == 0 || signum() == 0 )
    {
      return ZERO;
    }
    return multiply( get( d ) );
  }

  public Rational multiply( BigInteger bi )
  {
    if( signum() == 0 || bi.signum() == 0 )
    {
      return ZERO;
    }
    return get( bi.multiply( _numerator ), _denominator );
  }

  public Rational multiply( BigDecimal bd )
  {
    if( signum() == 0 || bd.signum() == 0 )
    {
      return ZERO;
    }
    return multiply( get( bd ) );
  }

  public Rational multiply( Rational rational )
  {
    if( signum() == 0 || rational.signum() == 0 )
    {
      return ZERO;
    }
    return get( _numerator.multiply( rational._numerator ),
                _denominator.multiply( rational._denominator ) );
  }

  public Rational divide( int i )
  {
    return divide( BigInteger.valueOf( i ) );
  }

  public Rational divide( long l )
  {
    return divide( BigInteger.valueOf( l ) );
  }

  public Rational divide( float f )
  {
    return divide( get( f ) );
  }

  public Rational divide( double d )
  {
    return divide( get( d ) );
  }

  public Rational divide( BigInteger bi )
  {
    if( bi.equals( BigInteger.ZERO ) )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    if( signum() == 0 )
    {
      return ZERO;
    }
    return get( _numerator, _denominator.multiply( bi ) );
  }

  public Rational divide( BigDecimal bd )
  {
    if( bd.signum() == 0 )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    if( signum() == 0 )
    {
      return ZERO;
    }
    return divide( get( bd ) );
  }

  public Rational divide( Rational rational )
  {
    if( rational.equals( ZERO ) )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    if( signum() == 0 )
    {
      return ZERO;
    }
    return multiply( rational.invert() );
  }

  public Rational modulo( int i )
  {
    return modulo( BigInteger.valueOf( i ) );
  }

  public Rational modulo( long l )
  {
    return modulo( BigInteger.valueOf( l ) );
  }

  public Rational modulo( float f )
  {
    return modulo( get( f ) );
  }

  public Rational modulo( double d )
  {
    return modulo( get( d ) );
  }

  public Rational modulo( BigInteger bi )
  {
    if( bi.equals( BigInteger.ZERO ) )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    return modulo( get( bi ) );
  }

  public Rational modulo( BigDecimal bd )
  {
    if( bd.signum() == 0 )
    {
      throw new ArithmeticException( "Divide by zero" );
    }
    return modulo( get( bd ) );
  }

  public Rational modulo( Rational rational )
  {
    Rational quotient = divide( rational );
    return subtract( rational.multiply( quotient.toBigInteger() ) ).abs();
  }

  public Rational negate()
  {
    return get( _numerator.negate(), _denominator );
  }

  public Rational invert()
  {
    return get( _denominator, _numerator );
  }

  public Rational abs()
  {
    return signum() >= 0 ? this : negate();
  }

  public Rational pow( int exponent )
  {
    if( signum() == 0 )
    {
      return exponent == 0 ? ONE : this;
    }
    return Rational.get( _numerator.pow( exponent ), _denominator.pow( exponent ) );
  }

  public Rational root( int iRoot )
  {
    return root( iRoot, 10 );
  }
  public Rational root( int n, int scale )
  {
    if( signum() < 0 )
    {
      throw new IllegalArgumentException( "nth root can only be calculated for positive numbers" );
    }
    if( signum() == 0 )
    {
      return ZERO;
    }

    BigInteger biScale = BigDecimal.ONE.movePointRight( scale ).toBigInteger();
    Rational small = Rational.get( BigInteger.ONE, biScale );
    Rational rRoot = Rational.get( n );

    Rational prev = this;
    Rational x = divide( rRoot );
    while( x.subtract( prev ).abs().compareTo( small ) > 0 )
    {
      prev = x;
      x = Rational.get( n - 1 )
      .multiply( x )
      .add( divide( x.pow( n - 1 ) ) )
      .divide( rRoot );
    }
    return x;
  }

  public Rational sqrt()
  {
    return Rational.get( Math.sqrt( doubleValue() ) );
    //return root( 2 );
  }

  @Override
  public Rational nextInSequence( Rational step, Void unit )
  {
    step = step == null ? Rational.ONE : step;
    return add( step );
  }

  @Override
  public Rational nextNthInSequence( Rational step, Void unit, int iIndex )
  {
    step = step == null ? Rational.ONE : step;
    return add( step.multiply( iIndex ) );
  }

  @Override
  public Rational previousInSequence( Rational step, Void unit )
  {
    step = step == null ? Rational.ONE : step;
    return subtract( step );
  }

  @Override
  public Rational previousNthInSequence( Rational step, Void unit, int iIndex )
  {
    step = step == null ? Rational.ONE : step;
    return subtract( step.multiply( iIndex ) );
  }

  @Override
  public int compareTo( Rational that )
  {
    int thisSign = signum();
    int thatSign = that.signum();
    if( thisSign != thatSign || thisSign == 0 )
    {
      return thisSign - thatSign;
    }
    BigInteger crossNum = _numerator.multiply( that._denominator );
    BigInteger crossDen = _denominator.multiply( that._numerator );
    return crossNum.compareTo( crossDen );
  }

  public int signum()
  {
    return _numerator.signum();
  }

  @Override
  public boolean equals( Object that )
  {
    if( this == that )
    {
      return true;
    }
    if( that == null || getClass() != that.getClass() )
    {
      return false;
    }

    Rational rational = (Rational)that;
    if( !_denominator.equals( rational._denominator ) )
    {
      return false;
    }
    return _numerator.equals( rational._numerator );
  }

  @Override
  public int hashCode()
  {
    int result = _numerator.hashCode();
    result = 31 * result + _denominator.hashCode();
    return result;
  }

  public String toFractionString()
  {
    if( !_reduced )
    {
      return reduce().toFractionString();
    }
    return _numerator + "/" + _denominator;
  }

  public String toMixedString()
  {
    if( !_reduced )
    {
      return reduce().toMixedString();
    }

    if( _denominator.equals( BigInteger.ONE ) )
    {
      return _numerator.toString();
    }
    BigInteger whole = wholePart();
    if( whole.signum() == 0  )
    {
      return fractionPart().toFractionString();
    }
    return whole + " " + fractionPart().abs().toFractionString();
  }

  public String toDecimalString() {
    return toBigDecimal().toString();
  }

  public String toPlainDecimalString() {
    return toBigDecimal().toPlainString();
  }

  @Override
  public String toString()
  {
    return _numerator + " / " + _denominator;
  }

  private Object writeReplace()
  {
    return new Serializer( this );
  }

  private static class Serializer implements Externalizable
  {
    private Rational _rational;

    public Serializer()
    {
    }

    public Serializer( Rational rational )
    {
      _rational = rational;
    }

    @Override
    public void writeExternal( ObjectOutput out ) throws IOException
    {
      out.writeInt( VERSION_1 );
      out.writeObject( _rational._numerator );
      out.writeObject( _rational._denominator );
      out.writeBoolean( _rational._reduced );
    }

    @Override
    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException
    {
      int version = in.readInt();
      switch( version )
      {
        case VERSION_1:
          BigInteger numerator = (BigInteger)in.readObject();
          BigInteger denominator = (BigInteger)in.readObject();
          boolean reduced = in.readBoolean();
          _rational = get( numerator, denominator, reduced );
          break;

        default:
          throw new IllegalStateException( "Unsupported version: " + version );
      }
    }

    Object readResolve()
    {
      return _rational;
    }
  }
}