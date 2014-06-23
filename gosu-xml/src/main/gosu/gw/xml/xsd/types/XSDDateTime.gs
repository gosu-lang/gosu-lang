package gw.xml.xsd.types

uses java.util.Calendar
uses java.math.BigDecimal

// TODO dlank - deprecate in favor of gw.xml.date.*
/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
class XSDDateTime extends AbstractXSDDateType
{

  public var _year : int as Year
  public var _month : int as Month
  public var _day : int as Day
  public var _hour : int as Hour
  public var _minute : int as Minute
  public var _second : BigDecimal as Second

  private construct()
  {
    super( true, true, true, true )
  }

  construct( s : String )
  {
    this()
    parseString( s )
  }

  construct( cal : Calendar, useTimeZone : boolean )
  {
    this()
    getCalendarFields( cal, useTimeZone )
  }

}

