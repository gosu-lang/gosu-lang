package gw.lang.enhancements

uses java.util.TimeZone

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreTimeZoneEnhancement : TimeZone
{

  static property get GMT() : TimeZone
  {
    return TimeZone.getTimeZone( "GMT" ) 
  }
  
}
