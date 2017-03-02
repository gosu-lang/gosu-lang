package gw.util.time

class HourMinuteSecondAmPm implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _sec: Integer as Sec
  final var _amPm: AmPm as AmPm

  construct( amPm: AmPm, hour: Integer, min: Integer, sec: Integer ) {
    _amPm = amPm
    _hour = hour
    _min = min
    _sec = sec
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteSecondMilliAmPm {
    return new( _amPm, comp, _hour, _min, _sec )
  }
}