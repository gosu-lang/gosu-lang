package gw.util.time

uses java.time.LocalDateTime
uses java.time.ZonedDateTime
uses java.time.ZoneId
uses gw.util.science.Time

enhancement CoreZoneIdEnhancement : ZoneId
{
  function postfixBind( time: Time ) : HourMinuteSecondMilliZone {
    var hour = time.toNumber( Hour ) as int
    var minute = (time - new Time( hour, Hour )).to( Minute ) as int
    var second = (time - new Time( hour, Hour ) - new Time( minute, Minute )).to( Second ) as int
    var milli = (time - new Time( hour, Hour ) - new Time( minute, Minute ) - new Time( second, Second )).to( Milli ) as int
    return new( this, hour, minute, second, milli )
  }

  function postfixBind( hour: Integer ) : HourZone {
    return new( this, hour )
  }
}