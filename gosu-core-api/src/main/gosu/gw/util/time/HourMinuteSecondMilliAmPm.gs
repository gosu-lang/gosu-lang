package gw.util.time

class HourMinuteSecondMilliAmPm implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _sec: Integer as Sec
  final var _milli: Integer as Milli
  final var _amPm: AmPm as AmPm

  construct( amPm: AmPm, hour: Integer, min: Integer, sec: Integer, milli: Integer ) {
    _amPm = amPm
    _hour = hour
    _min = min
    _sec = sec
    _milli = milli
  }
}