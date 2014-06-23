package gw.xml.xsd.types

uses java.math.BigDecimal
uses java.lang.Exception
uses java.util.StringTokenizer
uses java.lang.RuntimeException
uses java.util.NoSuchElementException
uses java.lang.StringBuilder
uses java.math.BigInteger

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
class XSDDuration
{

  static property get Zero() : XSDDuration
  {
    return new XSDDuration( "PT0S" )
  }

  public var Negative : boolean = false
  public var Years : BigInteger = 0
  public var Months : BigInteger = 0
  public var Days : BigInteger = 0
  public var Hours : BigInteger = 0
  public var Minutes : BigInteger = 0
  public var Seconds : BigDecimal = 0.0

  construct()
  {
  }

  construct( s : String )
  {
    var gotField = false
    var properlyTerminated = false
    var ex = new Exception()
    try {
      var st = new StringTokenizer( s, "PTZ-YMDHS", true )
      var token = st.nextToken()
      if ( token == "-" )
      {
        Negative = true
        token = st.nextToken()
      }
      if ( token != "P" )
      {
        throw ex
      }
      properlyTerminated = true
      token = st.nextToken()
      while ( token != "T" )
      {
        properlyTerminated = false
        var tmp = token
        token = st.nextToken()
        properlyTerminated = true
        gotField = true
        switch ( token )
        {
          case "Y":
            Years = new BigInteger(tmp)
            break
          case "M":
            Months = new BigInteger(tmp)
            break
          case "D":
            Days = new BigInteger(tmp)
            break
          default:
            throw ex
        }
        token = st.nextToken()
      }
      properlyTerminated = true
      token = st.nextToken()
      while ( true )
      {
        properlyTerminated = false
        var tmp = token
        token = st.nextToken()
        properlyTerminated = true
        gotField = true
        switch ( token )
        {
          case "H":
            Hours = new BigInteger(tmp)
            break
          case "M":
            Minutes = new BigInteger(tmp)
            break
          case "S":
            Seconds = new BigDecimal( tmp )
            break
          default:
            throw ex
        }
        token = st.nextToken()
      }
    }
    catch ( e : NoSuchElementException )
    {
      if ( not properlyTerminated or not gotField )
      {
        throw new RuntimeException( "Could not parse duration: " + s )
      }
    }
    catch ( e : Exception )
    {
      throw new RuntimeException( "Could not parse duration: " + s, e == ex ? null : e )
    }
  }

  override function toString() : String
  {
    if ( IsZero )
    {
      // at least one field is required - we'll use "zero seconds"
      if ( Negative )
      {
        return "-PT0S"
      }
      else
      {
        return "PT0S"
      }
    }
    var sb = new StringBuilder()
    if ( Negative )
    {
      sb.append( "-" )
    }

    sb.append( "P" )
    if ( ! Years.IsZero )
    {
      sb.append( "${ Years }Y" )
    }
    if ( ! Months.IsZero )
    {
      sb.append( "${ Months }M" )
    }
    if ( ! Days.IsZero )
    {
      sb.append( "${ Days }D" )
    }
    if ( ! ( Hours.IsZero and Minutes.IsZero and Seconds.IsZero ) )
    {
      sb.append( "T" )
      if ( ! Hours.IsZero )
      {
        sb.append( "${ Hours }H" )
      }
      if ( ! Minutes.IsZero )
      {
        sb.append( "${ Minutes }M" )
      }
      if ( ! Seconds.IsZero )
      {
        sb.append( "${ Seconds }S" )
      }
    }
    return sb.toString()
  }

  property get IsZero() : boolean
  {
    return Years.IsZero and Months.IsZero and Days.IsZero and Hours.IsZero and Minutes.IsZero and Seconds.IsZero
  }

}
