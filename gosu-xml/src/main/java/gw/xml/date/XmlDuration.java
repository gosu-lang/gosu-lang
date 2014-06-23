/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.math.BigInteger;

public class XmlDuration
{

  public static final XmlDuration ZERO = new XmlDuration( "PT0S" );

  private boolean _negative = false;
  private BigInteger _years = BigInteger.ZERO;
  private BigInteger _months = BigInteger.ZERO;
  private BigInteger _days = BigInteger.ZERO;
  private BigInteger _hours = BigInteger.ZERO;
  private BigInteger _minutes = BigInteger.ZERO;
  private BigDecimal _seconds = new BigDecimal( "0" );

  public XmlDuration()
  {

  }

  public XmlDuration( String s )
  {
    boolean gotField = false;
    boolean properlyTerminated = false;
    RuntimeException ex = new RuntimeException();
    try {
      StringTokenizer st = new StringTokenizer( s, "PTZ-YMDHS", true );
      String token = st.nextToken();
      if ( token.equals( "-" ) )
      {
        _negative = true;
        token = st.nextToken();
      }
      if ( ! token.equals( "P" ) )
      {
        throw ex;
      }
      properlyTerminated = true;
      token = st.nextToken();
      while ( ! token.equals( "T" ) )
      {
        properlyTerminated = false;
        String tmp = token;
        token = st.nextToken();
        properlyTerminated = true;
        gotField = true;
        if ( token.length() != 1 ) {
          throw ex;
        }
        switch ( token.charAt( 0 ) )
        {
          case 'Y':
            _years = new BigInteger(tmp);
            break;
          case 'M':
            _months = new BigInteger(tmp);
            break;
          case 'D':
            _days = new BigInteger(tmp);
            break;
          default:
            throw ex;
        }
        token = st.nextToken();
      }
      properlyTerminated = true;
      token = st.nextToken();
      //noinspection InfiniteLoopStatement
      while ( true )
      {
        properlyTerminated = false;
        String tmp = token;
        token = st.nextToken();
        properlyTerminated = true;
        gotField = true;
        if ( token.length() != 1 ) {
          throw ex;
        }
        switch ( token.charAt( 0 ) )
        {
          case 'H':
            _hours = new BigInteger(tmp);
            break;
          case 'M':
            _minutes = new BigInteger(tmp);
            break;
          case 'S':
            _seconds = new BigDecimal( tmp );
            break;
          default:
            throw ex;
        }
        token = st.nextToken();
      }
    }
    catch ( NoSuchElementException e )
    {
      if ( ! properlyTerminated || ! gotField )
      {
        throw new RuntimeException( "Could not parse duration: " + s );
      }
    }
    catch ( Exception e )
    {
      //noinspection ObjectEquality
      throw new RuntimeException( "Could not parse duration: " + s, e == ex ? null : e );
    }
  }

  public BigInteger getDays() {
    return _days;
  }

  public void setDays( BigInteger days ) {
    _days = days;
  }

  public BigInteger getHours() {
    return _hours;
  }

  public void setHours( BigInteger hours ) {
    _hours = hours;
  }

  public BigInteger getMinutes() {
    return _minutes;
  }

  public void setMinutes( BigInteger minutes ) {
    _minutes = minutes;
  }

  public BigInteger getMonths() {
    return _months;
  }

  public void setMonths( BigInteger months ) {
    _months = months;
  }

  public boolean isNegative() {
    return _negative;
  }

  public void setNegative( boolean negative ) {
    _negative = negative;
  }

  public BigDecimal getSeconds() {
    return _seconds;
  }

  public void setSeconds( BigDecimal seconds ) {
    _seconds = seconds;
  }

  public BigInteger getYears() {
    return _years;
  }

  public void setYears( BigInteger years ) {
    _years = years;
  }

  @Override
  public String toString()
  {
    if ( isZero() )
    {
      // at least one field is required - we'll use "zero seconds"
      if ( _negative )
      {
        return "-PT0S";
      }
      else
      {
        return "PT0S";
      }
    }
    StringBuilder sb = new StringBuilder();
    if ( _negative )
    {
      sb.append( "-" );
    }

    sb.append( "P" );
    if ( ! _years.equals( BigInteger.ZERO ) )
    {
      sb.append( getYears() );
      sb.append( "Y" );
    }
    if ( ! _months.equals( BigInteger.ZERO ) )
    {
      sb.append( getMonths() );
      sb.append( "M" );
    }
    if ( ! _days.equals( BigInteger.ZERO ) )
    {
      sb.append( getDays() );
      sb.append( "D" );
    }
    if ( ! ( _hours.equals( BigInteger.ZERO ) && _minutes.equals( BigInteger.ZERO ) && _seconds.equals( BigDecimal.ZERO ) ) )
    {
      sb.append( "T" );
      if ( ! _hours.equals( BigInteger.ZERO ) )
      {
        sb.append( getHours() );
        sb.append( "H" );
      }
      if ( ! _minutes.equals( BigInteger.ZERO ) )
      {
        sb.append( getMinutes() );
        sb.append( "M" );
      }
      if ( ! _seconds.equals( BigDecimal.ZERO ) )
      {
        sb.append( getSeconds() );
        sb.append( "S" );
      }
    }
    return sb.toString();
  }

  public boolean isZero()
  {
    return _years.equals( BigInteger.ZERO ) && _months.equals( BigInteger.ZERO ) && _days.equals( BigInteger.ZERO ) && _hours.equals( BigInteger.ZERO ) && _minutes.equals( BigInteger.ZERO ) && _seconds.equals( BigDecimal.ZERO );
  }

}
