package gw.xml.xsd.types

uses java.util.Calendar

// TODO dlank - deprecate in favor of gw.xml.date.*
/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
class XSDGMonthDay extends AbstractXSDDateType
{

  public var _month : int as Month
  public var _day : int as Day

  private construct()
  {
    super( false, true, true, false )
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
