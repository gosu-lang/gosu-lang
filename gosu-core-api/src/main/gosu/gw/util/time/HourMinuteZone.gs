package gw.util.time
uses java.time.ZoneId

class HourMinuteZone implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _zoneId: ZoneId as ZoneId

  construct( zoneId: ZoneId, hour: Integer, min: Integer ) {
    _zoneId = zoneId
    _hour = hour
    _min = min
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteSecondZone {
    return new( _zoneId, comp, _hour, _min )
  }
}