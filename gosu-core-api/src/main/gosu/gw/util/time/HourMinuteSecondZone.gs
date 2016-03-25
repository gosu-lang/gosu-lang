package gw.util.time
uses java.time.ZoneId

class HourMinuteSecondZone implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _sec: Integer as Sec
  final var _zoneId: ZoneId as ZoneId

  construct( zoneId: ZoneId, hour: Integer, min: Integer, sec: Integer ) {
    _zoneId = zoneId
    _hour = hour
    _min = min
    _sec = sec
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteSecondMilliZone {
    return new( _zoneId, comp, _hour, _min, _sec )
  }
}