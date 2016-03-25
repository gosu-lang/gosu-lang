package gw.util.time
uses java.time.ZoneId

class HourMinuteSecondMilliZone implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _sec: Integer as Sec
  final var _milli: Integer as Milli
  final var _zoneId: ZoneId as ZoneId

  construct( zoneId: ZoneId, hour: Integer, min: Integer, sec: Integer, milli: Integer ) {
    _zoneId = zoneId
    _hour = hour
    _min = min
    _sec = sec
    _milli = milli
  }
}