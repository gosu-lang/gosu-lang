package gw.xml.xsd.types

uses java.util.TimeZone
uses java.util.Calendar
uses java.text.SimpleDateFormat
uses java.util.GregorianCalendar
uses java.lang.Exception
uses java.util.StringTokenizer
uses java.lang.Integer
uses java.lang.StringBuilder
uses java.lang.IllegalStateException
uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.text.ParseException

// TODO dlank - deprecate in favor of gw.xml.date.*
/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
abstract class AbstractXSDDateType
{

  public var TimeZone : TimeZone

  var includeYear : boolean
  var includeMonth : boolean
  var includeDay : boolean
  var includeTime : boolean

  internal property get Year() : int
  {
    return 0
  }

  internal property set Year( value : int )
  {
    // ignore
  }

  internal property get Month() : int
  {
    return 1
  }

  internal property set Month( value : int )
  {
    // ignore
  }

  internal property get Day() : int
  {
    return 1
  }

  internal property set Day( value : int )
  {
    // ignore
  }

  internal property get Hour() : int
  {
    return 0
  }

  internal property set Hour( value : int )
  {
    // ignore
  }

  internal property get Minute() : int
  {
    return 0
  }

  internal property set Minute( value : int )
  {
    // ignore
  }


  internal property get Second() : BigDecimal
  {
    return 0
  }

  internal property set Second( value : BigDecimal )
  {
    // ignore
  }

  construct( y : boolean, m : boolean, d : boolean, time : boolean )
  {
    includeYear = y
    includeMonth = m
    includeDay = d
    includeTime = time
  }

  final override function toString() : String
  {
    var sb = new StringBuilder()
    if ( includeYear or includeMonth or includeDay )
    {
      if ( includeYear )
      {
        sb.append( pad( Year as java.lang.String, 4 ) )
      }
      else
      {
        sb.append( "-" )
      }
      if ( includeMonth or includeDay )
      {
        sb.append( "-" )
        if ( includeMonth )
        {
          sb.append( pad( Month as java.lang.String, 2 ) )
        }
        if ( includeDay )
        {
          sb.append( "-" )
          sb.append( pad( Day as java.lang.String, 2 ) )
          if ( includeTime )
          {
            sb.append( "T" )
          }
        }
      }
    }
    if ( includeTime )
    {
      var s = Second as String
      var idx = s.indexOf( "." )
      if (idx < 0)
      {
        idx = s.length
      }
      while ( idx < 2 )
      {
        s = "0" + s
        idx++
      }
      sb.append(pad( Hour as String, 2 ))
        .append(":").append(pad( Minute as String, 2 ) )
        .append(":").append( s )
    }
    if ( TimeZone != null )
    {
      if ( TimeZone == java.util.TimeZone.GMT )
      {
        sb.append( "Z" )
      }
      else
      {
        var f = new SimpleDateFormat( "Z" )
        f.TimeZone = TimeZone
        var cal = toCalendar()
        var tz = f.format( cal.Time )
        sb.append(tz.substring( 0, 3 )).append(":").append(tz.substring( 3 ))
      }
    }
    return sb.toString()
  }

  private function pad( s : String, count : int ) : String
  {
    while ( s.length < count )
    {
      s = "0" + s
    }
    return s
  }

  final function toCalendar() : Calendar
  {
    if (TimeZone == null)
    {
      throw new IllegalStateException( "TimeZone must be set" )
    }
    return toCalendarInternal( TimeZone )
  }

  final function toCalendar( tzDefault : TimeZone ) : Calendar
  {
    return toCalendarInternal( TimeZone == null ? tzDefault : TimeZone )
  }

  private function toCalendarInternal( tzDefault : TimeZone ) : Calendar
  {
    if ( tzDefault == null )
    {
      tzDefault = java.util.TimeZone.GMT
    }
    var cal = new GregorianCalendar()
    cal.clear()
    cal.TimeZone = tzDefault
    if ( includeYear )
    {
      cal.CalendarYear = Year
    }
    if ( includeMonth )
    {
      cal.CalendarMonth = Month - 1
    }
    if ( includeDay )
    {
      cal.CalendarDay = Day
    }
    if ( includeTime )
    {
      cal.CalendarHourOfDay = Hour
      cal.CalendarMinute = Minute
      cal.CalendarSecond = Second as int
      var tmp = Second.remainder( 1 ).multiply( 1000 ).divideAndRemainder( 1 )
      cal.CalendarMillisecond = tmp[0] as int
      if ( tmp[1] >= 0.5 )
      {
        cal.CalendarMillisecond++
      }
    }
    return cal
  }

  protected final function parseString(s : String)
  {
    var ex = new Exception()
    try
    {
      var st = new StringTokenizer(s, "TZ+-:", true)
      if ( includeYear or includeMonth or includeDay )
      {
        if (includeYear)
        {
          var token = st.nextToken()
          var yearSign = 1
          if (token == "-") {
              yearSign = -1
              token = st.nextToken()
          }
          Year = Integer.parseInt(token) * yearSign
        }
        else
        {
          checkToken( st, "-", ex )
        }
        if ( includeMonth or includeDay ) {
          checkToken(st, "-", ex)
          if (includeMonth)
          {
            Month = Integer.parseInt(st.nextToken())
            if ( Month > 12 or Month <= 0 )
            {
              throw ex
            }
          }
          if (includeDay)
          {
            checkToken(st, "-", ex)
            Day = Integer.parseInt(st.nextToken())
            var maxDays = 31
            if ( Month == 9 or Month == 4 or Month == 6 or Month == 11 )
            {
              maxDays = 30
            }
            else if ( Month == 2 )
            {
              maxDays = 28
              if ( Year % 4 == 0 )
              {
                maxDays = 29
                if ( Year % 100 == 0 )
                {
                  maxDays = 28
                  if ( Year % 400 == 0 )
                  {
                    maxDays = 29
                  }
                }
              }
            }
            if ( Day > maxDays or Day <= 0 )
            {
              throw ex
            }
            if ( includeTime )
            {
              checkToken(st, "T", ex)
            }
          }
        }
      }
      if (includeTime)
      {
        Hour = Integer.parseInt(st.nextToken())
        if ( Hour >= 24 )
        {
          throw ex
        }
        checkToken(st, ":", ex)
        Minute = Integer.parseInt(st.nextToken())
        if ( Minute >= 60 )
        {
          throw ex
        }
        checkToken(st, ":", ex)
        var secondString = st.nextToken()
        Second = new BigDecimal( secondString )
        if ( Second >= 60 )
        {
          throw ex
        }
      }

      if (st.hasMoreTokens())
      {
        var token = st.nextToken()
        if (token == "Z") {
            token = "GMT"
        } else {
            token = "GMT" + token
        }
        var tzString = new StringBuilder(token)
        while (st.hasMoreTokens()) {
            tzString.append(st.nextToken())
        }
        var tzs = tzString.toString()
        if ( ! tzs.matches( "GMT([+-]\\d\\d:\\d\\d)?" ) )
        {
          throw ex
        }
        TimeZone = TimeZone.getTimeZone( tzs )
        if ( TimeZone.ID != tzs )
        {
          throw ex
        }
      }

      if (st.hasMoreTokens()) {
          throw ex
      }

    } catch (e : Exception) {
        throw new ParseException("Could not parse date: " + s, -1)
    }
  }

  private static function checkToken( st : StringTokenizer, token : String, ex : Exception )
  {
    var nextToken = st.nextToken()
    if ( token != nextToken ) {
        throw ex
    }
  }

  protected final function getCalendarFields( cal : Calendar, useTimeZone : boolean )
  {
    if ( includeYear )
    {
      Year = cal.CalendarYear
    }
    if ( includeMonth )
    {
      Month = cal.CalendarMonth + 1
    }
    if ( includeDay )
    {
      Day = cal.CalendarDay
    }
    if ( includeTime )
    {
      Hour = cal.CalendarHourOfDay
      Minute = cal.CalendarMinute
      Second = cal.CalendarSecond
      Second = Second.add( ( cal.CalendarMillisecond as BigDecimal ).divide( 1000 ) )
    }
    if ( useTimeZone )
    {
      TimeZone = cal.TimeZone
    }
    else
    {
      TimeZone = null
    }
  }

  // This algorithm is specified in the following specification:
  // http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes

  function add( d : XSDDuration )
  {
    var sMonth = Month as BigDecimal
    var sDay = Day as BigDecimal
    var sYear = Year as BigDecimal
    var sHour = Hour as BigDecimal
    var sMinute = Minute as BigDecimal
    var sSecond = Second

    // months (may be modified additionally below)
    var temp = sMonth.add( d.Negative ? d.Months.negate() : d.Months )
    var eMonth = modulo( temp, 1, 13 )
    var carry = fQuotient( temp, 1, 13 )

    // Years (may be modified additionally below)
    var eYear = sYear.add( d.Negative ? d.Years.negate() : d.Years ).add( carry )

    // Zone
    //var eZone = sZone

    // Seconds
    temp = sSecond.add( d.Negative ? d.Seconds.negate() : d.Seconds )
    var eSecond = modulo( temp, 60 )
    carry = fQuotient( temp, 60 )

    // Minutes
    temp = sMinute.add( d.Negative ? d.Minutes.negate() : d.Minutes ).add( carry )
    var eMinute = modulo( temp, 60 )
    carry = fQuotient( temp, 60 )

    // Hours
    temp = sHour.add( d.Negative ? d.Hours.negate() : d.Hours ).add( carry )
    var eHour = modulo( temp, 24 )
    carry = fQuotient( temp, 24 )

    // Days
    var tempDays : BigDecimal
    if ( sDay > maximumDayInMonthFor( eYear as int, eMonth as int ) )
    {
      tempDays = maximumDayInMonthFor( eYear as int, eMonth as int )
    }
    else if ( sDay < 1 )
    {
      tempDays = 1
    }
    else
    {
      tempDays = sDay
    }
    var eDay = tempDays.add( d.Negative ? d.Days.negate() : d.Days ).add( carry )
    while ( true )
    {
      if ( eDay < 1 )
      {
        eDay = eDay.add( maximumDayInMonthFor( eYear as int, ( eMonth as int ) - 1 ) )
        carry = -1
      }
      else if ( eDay > maximumDayInMonthFor( eYear as int, eMonth as int ) )
      {
        eDay = eDay.subtract( maximumDayInMonthFor( eYear as int, eMonth as int ) )
        carry = 1
      }
      else
      {
        break
      }
      temp = eMonth.add( carry )
      eMonth = modulo( temp, 1, 13 )
      eYear = eYear.add( fQuotient( temp, 1, 13 ) )
    }

    Month = eMonth as int
    Day = eDay as int
    Year = eYear as int
    Hour = eHour as int
    Minute = eMinute as int
    Second = eSecond
  }

  private function fQuotient( a : BigDecimal, b : BigDecimal ) : BigDecimal
  {
    var result = a.divide( b, 0, RoundingMode.DOWN )
    if ( a.compareTo( BigDecimal.ZERO ) < 0 )
    {
      result = result.subtract( new BigDecimal( "1" ) )
    }
    return result
  }

  private function modulo( a : BigDecimal, b : BigDecimal ) : BigDecimal
  {
    var result = a.remainder( b )
    if ( result.compareTo( BigDecimal.ZERO ) < 0 )
    {
      result = result.add( b )
    }
    return result
  }

  private function fQuotient( a : BigDecimal, low : BigDecimal, high : BigDecimal ) : BigDecimal
  {
    if ( a.compareTo( BigDecimal.ZERO ) < 0 )
    {
      return fQuotient( a.add( low ), high.subtract( low ) )
    }
    else
    {
      return fQuotient( a.subtract( low ), high.subtract( low ) )
    }
  }

  private function modulo( a : BigDecimal, low : BigDecimal, high : BigDecimal ) : BigDecimal
  {
    return modulo( a.subtract( low ), high.subtract( low ) ).add( low )
  }

  private function maximumDayInMonthFor( yearValue : int, monthValue : int ) : int
  {
    var m = modulo( monthValue, 1, 13 )
    var y = yearValue + fQuotient( monthValue, 1, 13 )
    switch ( m )
    {
      case 9:  // september
      case 4:  // april
      case 6:  // june
      case 11: // november
               return 30
      case 2:  return ( modulo( y, 400 ) == 0 or ( modulo( y, 100 ) != 0 ) and modulo( y, 4 ) == 0 ) ? 29 : 28
      default: return 31
    }
  }

  final override function equals( o : Object ) : boolean
  {
    if ( typeof o == typeof this )
    {
      var other = o as AbstractXSDDateType
      if ( ( TimeZone == null ) == ( other.TimeZone == null ) )
      {
        // compare timestamps
        var thisCal = toCalendarInternal( TimeZone )
        var otherCal = other.toCalendarInternal( other.TimeZone )
        return thisCal.Time == otherCal.Time
      }
    }
    return false
  }

  final override function hashCode() : int
  {
    var result = toCalendarInternal( TimeZone ).Time.hashCode()
    if ( TimeZone == null )
    {
      result = -result
    }
    return result
  }

}
