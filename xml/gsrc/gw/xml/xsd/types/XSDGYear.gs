package gw.xml.xsd.types

uses java.util.Calendar

// TODO dlank - deprecate in favor of gw.xml.date.*
/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
class XSDGYear extends AbstractXSDDateType
{

  public var _year : int as Year

  private construct()
  {
    super( true, false, false, false )
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

