package gw.util.time
uses java.time.ZoneId

class HourZone implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _zoneId: ZoneId as ZoneId

  construct( zoneId: ZoneId, hour: Integer ) {
    _zoneId = zoneId
    _hour = hour
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteZone {
    return new( _zoneId, comp, _hour )
  }
}