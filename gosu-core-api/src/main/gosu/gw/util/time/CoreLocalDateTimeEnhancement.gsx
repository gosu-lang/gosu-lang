package gw.util.time

uses java.time.LocalDateTime
uses java.time.ZonedDateTime
uses java.time.ZoneId

enhancement CoreLocalDateTimeEnhancement : LocalDateTime
{
  function prefixBind( zoneId: ZoneId ) : ZonedDateTime {
    return ZonedDateTime.of( this, zoneId )
  }
}