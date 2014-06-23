/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.date;

import java.util.TimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.lang.Exception;
import java.util.StringTokenizer;
import java.lang.Integer;
import java.lang.StringBuilder;
import java.lang.IllegalStateException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.DateFormat;

public abstract class AbstractXmlDateType
{

  private TimeZone _timeZone;
  private final boolean _includeYear;
  private final boolean _includeMonth;
  private final boolean _includeDay;
  private final boolean _includeTime;

  public AbstractXmlDateType( boolean y, boolean m, boolean d, boolean time )
  {
    _includeYear = y;
    _includeMonth = m;
    _includeDay = d;
    _includeTime = time;
  }

  public TimeZone getTimeZone() {
    return _timeZone;
  }

  public void setTimeZone( TimeZone timeZone ) {
    _timeZone = timeZone;
  }


  protected int getYear() {
    return 0;
  }

  protected void setYear( int year )
  {
    // ignore
  }

  protected int getMonth() {
    return 1;
  }

  protected void setMonth( int month )
  {
    // ignore
  }

  protected int getDay() {
    return 1;
  }

  protected void setDay( int day )
  {
    // ignore
  }

  protected int getHour() {
    return 0;
  }

  protected void setHour( int hour )
  {
    // ignore
  }

  protected int getMinute() {
    return 0;
  }

  protected void setMinute( int minute )
  {
    // ignore
  }

  protected BigDecimal getSecond() {
    return BigDecimal.ZERO;
  }

  protected void setSecond( BigDecimal second )
  {
    // ignore
  }


  @Override
  public final String toString()
  {
    StringBuilder sb = new StringBuilder();
    if ( _includeYear || _includeMonth || _includeDay )
    {
      if ( _includeYear )
      {
        sb.append( pad( String.valueOf( getYear() ), 4 ) );
      }
      else
      {
        sb.append( "-" );
      }
      if ( _includeMonth || _includeDay )
      {
        sb.append( "-" );
        if ( _includeMonth )
        {
          sb.append( pad( String.valueOf( getMonth() ), 2 ) );
        }
        if ( _includeDay )
        {
          sb.append( "-" );
          sb.append( pad( String.valueOf( getDay() ), 2 ) );
          if ( _includeTime )
          {
            sb.append( "T" );
          }
        }
      }
    }
    if ( _includeTime )
    {
      String s = String.valueOf( getSecond() );
      int idx = s.indexOf( '.' );
      if (idx < 0)
      {
        idx = s.length();
      }
      while ( idx < 2 )
      {
        s = "0" + s;
        idx++;
      }
      sb.append( pad( String.valueOf( getHour() ), 2 ) );
      sb.append( ':' );
      sb.append( pad( String.valueOf( getMinute() ), 2 ) );
      sb.append( ':' );
      sb.append( s );
    }
    if ( getTimeZone() != null )
    {
      if ( getTimeZone().equals( java.util.TimeZone.getTimeZone( "GMT" ) ) )
      {
        sb.append( "Z" );
      }
      else
      {
        DateFormat f = new SimpleDateFormat( "Z" );
        f.setTimeZone( getTimeZone() );
        Calendar cal = toCalendar();
        String tz = f.format( cal.getTime() );
        tz = tz.substring( 0, 3 ) + ':' + tz.substring( 3 );
        sb.append(tz);
      }
    }
    return sb.toString();
  }

  private String pad( String s, int count )
  {
    while ( s.length() < count )
    {
      s = "0" + s;
    }
    return s;
  }

  public final Calendar toCalendar()
  {
    if (getTimeZone() == null)
    {
      throw new IllegalStateException( "TimeZone must be set" );
    }
    return toCalendarInternal( getTimeZone() );
  }

  public final Calendar toCalendar( TimeZone tzDefault )
  {
    return toCalendarInternal( getTimeZone() == null ? tzDefault : getTimeZone() );
  }

  private Calendar toCalendarInternal( TimeZone tzDefault )
  {
    if ( tzDefault == null )
    {
      tzDefault = java.util.TimeZone.getTimeZone( "GMT" );
    }
    Calendar cal = new GregorianCalendar();
    cal.clear();
    cal.setTimeZone( tzDefault );
    if ( _includeYear )
    {
      cal.set( Calendar.YEAR, getYear() );
    }
    if ( _includeMonth )
    {
      cal.set( Calendar.MONTH, getMonth() - 1 );
    }
    if ( _includeDay )
    {
      cal.set( Calendar.DAY_OF_MONTH, getDay() );
    }
    if ( _includeTime )
    {
      cal.set( Calendar.HOUR_OF_DAY, getHour() );
      cal.set( Calendar.MINUTE, getMinute() );
      cal.set( Calendar.SECOND, getSecond().intValue() );
      BigDecimal[] tmp = getSecond().remainder( BigDecimal.ONE ).multiply( new BigDecimal( "1000" ) ).divideAndRemainder( BigDecimal.ONE );
      if ( tmp[1].compareTo( new BigDecimal( "0.5" ) ) >= 0 )
      {
        cal.set( Calendar.MILLISECOND, tmp[0].intValue() + 1 );
      }
      else
      {
        cal.set( Calendar.MILLISECOND, tmp[0].intValue() );
      }
    }
    return cal;
  }

  protected final void parseString( String s ) throws ParseException {
    RuntimeException ex = new RuntimeException();
    try 
    {
      StringTokenizer st = new StringTokenizer(s, "TZ+-:", true);
      if ( _includeYear || _includeMonth || _includeDay )
      {
        if (_includeYear)
        {
          String token = st.nextToken();
          int yearSign = 1;
          if ( token.equals( "-" ) ) {
              yearSign = -1;
              token = st.nextToken();
          }
          setYear( Integer.parseInt(token) * yearSign );
        }
        else
        {
          checkToken( st, "-", ex );
        }
        if ( _includeMonth || _includeDay ) {
          checkToken(st, "-", ex);
          if (_includeMonth)
          {
            setMonth( Integer.parseInt( st.nextToken() ) );
            if ( getMonth() > 12 || getMonth() <= 0 )
            {
              throw ex;
            }
          }
          if (_includeDay)
          {
            checkToken(st, "-", ex);
            setDay( Integer.parseInt( st.nextToken() ) );
            int maxDays = 31;
            if ( getMonth() == 9 || getMonth() == 4 || getMonth() == 6 || getMonth() == 11 )
            {
              maxDays = 30;
            }
            else if ( getMonth() == 2 )
            {
              maxDays = 28;
              if ( getYear() % 4 == 0 )
              {
                maxDays = 29;
                if ( getYear() % 100 == 0 )
                {
                  maxDays = 28;
                  if ( getYear() % 400 == 0 )
                  {
                    maxDays = 29;
                  }
                }
              }
            }
            if ( getDay() > maxDays || getDay() <= 0 )
            {
              throw ex;
            }
            if ( _includeTime )
            {
              checkToken(st, "T", ex);
            }
          }
        }
      }
      if (_includeTime)
      {
        setHour( Integer.parseInt( st.nextToken() ) );
        if ( getHour() >= 24 )
        {
          throw ex;
        }
        checkToken(st, ":", ex);
        setMinute( Integer.parseInt( st.nextToken() ) );
        if ( getMinute() >= 60 )
        {
          throw ex;
        }
        checkToken(st, ":", ex);
        String secondString = st.nextToken();
        setSecond( new BigDecimal( secondString ) );
        if ( getSecond().compareTo( new BigDecimal( "60" ) ) >= 0 )
        {
          throw ex;
        }
      }

      if (st.hasMoreTokens())
      {
        String token = st.nextToken();
        if (token.equals( "Z" ) ) {
            token = "GMT";
        } else {
            token = "GMT" + token;
        }
        StringBuilder tzString = new StringBuilder(token);
        while (st.hasMoreTokens()) {
            tzString.append(st.nextToken());
        }
        String tzs = tzString.toString();
        if ( ! tzs.matches( "GMT([+-]\\d\\d:\\d\\d)?" ) )
        {
          throw ex;
        }
        setTimeZone( TimeZone.getTimeZone( tzs ) );
        if ( ! getTimeZone().getID().equals( tzs ) )
        {
          throw ex;
        }
      }

      if (st.hasMoreTokens()) {
          throw ex;
      }

    } catch ( Exception e ) {
        throw new ParseException("Could not parse date: " + s, -1);
    }
  }

  private static void checkToken( StringTokenizer st, String token, RuntimeException ex ) {
    String nextToken = st.nextToken();
    if ( ! token.equals( nextToken ) ) {
      throw ex;
    }
  }

  protected final void getCalendarFields( Calendar cal, boolean useTimeZone )
  {
    if ( _includeYear )
    {
      setYear( cal.get( Calendar.YEAR ) );
    }
    if ( _includeMonth )
    {
      setMonth( cal.get( Calendar.MONTH ) + 1 );
    }
    if ( _includeDay )
    {
      setDay( cal.get( Calendar.DAY_OF_MONTH ) );
    }
    if ( _includeTime )
    {
      setHour( cal.get( Calendar.HOUR_OF_DAY ) );
      setMinute( cal.get( Calendar.MINUTE ) );
      setSecond( new BigDecimal( String.valueOf( cal.get( Calendar.SECOND ) ) ) );
      setSecond( getSecond().add( new BigDecimal( String.valueOf( cal.get( Calendar.MILLISECOND ) ) ).divide( new BigDecimal( "1000" ) ) ) );
    }
    if ( useTimeZone )
    {
      setTimeZone( cal.getTimeZone() );
    }
    else
    {
      setTimeZone( null );
    }
  }

  // This algorithm is specified in the following specification:
  // http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes

  public void add( XmlDuration d )
  {
    BigDecimal sMonth = new BigDecimal( String.valueOf( getMonth() ) );
    BigDecimal sDay = new BigDecimal( String.valueOf( getDay() ) );
    BigDecimal sYear = new BigDecimal( String.valueOf( getYear() ) );
    BigDecimal sHour = new BigDecimal( String.valueOf( getHour() ) );
    BigDecimal sMinute = new BigDecimal( String.valueOf( getMinute()  ) );
    BigDecimal sSecond = getSecond();

    // months (may be modified additionally below)
    BigDecimal temp = sMonth.add( new BigDecimal( d.isNegative() ? d.getMonths().negate() : d.getMonths() ) );
    BigDecimal eMonth = modulo( temp, BigDecimal.ONE, new BigDecimal( "13" ) );
    BigDecimal carry = fQuotient( temp, BigDecimal.ONE, new BigDecimal( "13" ) );

    // Years (may be modified additionally below)
    BigDecimal eYear = sYear.add( new BigDecimal( d.isNegative() ? d.getYears().negate() : d.getYears() ) ).add( carry );

    // Zone
    //var eZone = sZone

    // Seconds
    temp = sSecond.add( d.isNegative() ? d.getSeconds().negate() : d.getSeconds() );
    BigDecimal eSecond = modulo( temp, new BigDecimal( "60" ) );
    carry = fQuotient( temp, new BigDecimal( "60" ) );

    // Minutes
    temp = sMinute.add( new BigDecimal( d.isNegative() ? d.getMinutes().negate() : d.getMinutes() ) ).add( carry );
    BigDecimal eMinute = modulo( temp, new BigDecimal( "60" ) );
    carry = fQuotient( temp, new BigDecimal( "60" ) );

    // Hours
    temp = sHour.add( new BigDecimal( d.isNegative() ? d.getHours().negate() : d.getHours() ) ).add( carry );
    BigDecimal eHour = modulo( temp, new BigDecimal( "24" ) );
    carry = fQuotient( temp, new BigDecimal( "24" ) );

    // Days
    BigDecimal tempDays;
    if ( sDay.compareTo( maximumDayInMonthFor( eYear, eMonth ) ) > 0 )
    {
      tempDays = maximumDayInMonthFor( eYear, eMonth );
    }
    else if ( sDay.compareTo( BigDecimal.ONE ) < 0 )
    {
      tempDays = BigDecimal.ONE;
    }
    else
    {
      tempDays = sDay;
    }
    BigDecimal eDay = tempDays.add( new BigDecimal( d.isNegative() ? d.getDays().negate() : d.getDays() ) ).add( carry );
    while ( true )
    {
      if ( eDay.compareTo( BigDecimal.ONE ) < 0 )
      {
        eDay = eDay.add( maximumDayInMonthFor( eYear, eMonth.subtract( BigDecimal.ONE ) ) );
        carry = new BigDecimal( "-1" );
      }
      else if ( eDay.compareTo( maximumDayInMonthFor( eYear, eMonth ) ) > 0 )
      {
        eDay = eDay.subtract( maximumDayInMonthFor( eYear, eMonth ) );
        carry = BigDecimal.ONE;
      }
      else
      {
        break;
      }
      temp = eMonth.add( carry );
      eMonth = modulo( temp, BigDecimal.ONE, new BigDecimal( "13" ) );
      eYear = eYear.add( fQuotient( temp, BigDecimal.ONE, new BigDecimal( "13" ) ) );
    }

    setMonth( eMonth.intValue() );
    setDay( eDay.intValue() );
    setYear( eYear.intValue() );
    setHour( eHour.intValue() );
    setMinute( eMinute.intValue() );
    setSecond( eSecond );
  }

  private BigDecimal fQuotient( BigDecimal a, BigDecimal b )
  {
    BigDecimal result = a.divide( b, 0, RoundingMode.DOWN );
    if ( a.compareTo( BigDecimal.ZERO ) < 0 )
    {
      result = result.subtract( BigDecimal.ONE );
    }
    return result;
  }

  private BigDecimal modulo( BigDecimal a, BigDecimal b )
  {
    BigDecimal result = a.remainder( b );
    if ( result.compareTo( BigDecimal.ZERO ) < 0 )
    {
      result = result.add( b );
    }
    return result;
  }

  private BigDecimal fQuotient( BigDecimal a, BigDecimal low, BigDecimal high )
  {
    if ( a.compareTo( BigDecimal.ZERO ) < 0 )
    {
      return fQuotient( a.add( low ), high.subtract( low ) );
    }
    else
    {
      return fQuotient( a.subtract( low ), high.subtract( low ) );
    }
  }

  private BigDecimal modulo( BigDecimal a, BigDecimal low, BigDecimal high )
  {
    return modulo( a.subtract( low ), high.subtract( low ) ).add( low );
  }

  private BigDecimal maximumDayInMonthFor( BigDecimal yearValue, BigDecimal monthValue )
  {
    BigDecimal m = modulo( monthValue, BigDecimal.ONE, new BigDecimal( "13" ) );
    BigDecimal y = yearValue.add( fQuotient( monthValue, BigDecimal.ONE, new BigDecimal( 13 ) ) );
    switch ( m.intValue() )
    {
      case 9:  // september
      case 4:  // april
      case 6:  // june
      case 11: // november
               return new BigDecimal( "30" );
      case 2:  return ( modulo( y, new BigDecimal( "400" ) ).intValue() == 0 || ( modulo( y, new BigDecimal( "100" ) ).intValue() != 0 ) && modulo( y, new BigDecimal( "4" ) ).intValue() == 0 ) ? new BigDecimal( "29" ) : new BigDecimal( "28" );
      default: return new BigDecimal( "31" );
    }
  }

  @Override
  public final boolean equals( Object o )
  {
    if ( o.getClass() == this.getClass() )
    {
      AbstractXmlDateType other = (AbstractXmlDateType) o;
      if ( ( getTimeZone() == null ) == ( other.getTimeZone() == null ) )
      {
        // compare timestamps
        Calendar thisCal = toCalendarInternal( getTimeZone() );
        Calendar otherCal = other.toCalendarInternal( other.getTimeZone() );
        return thisCal.getTime().getTime() == otherCal.getTime().getTime();
      }
    }
    return false;
  }

  @Override
  public final int hashCode()
  {
    int result = toCalendarInternal( getTimeZone() ).getTime().hashCode();
    if ( getTimeZone() == null )
    {
      result = -result;
    }
    return result;
  }

}
