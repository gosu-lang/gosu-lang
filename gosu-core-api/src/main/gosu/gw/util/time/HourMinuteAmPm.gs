package gw.util.time

class HourMinuteAmPm implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _min: Integer as Min
  final var _amPm: AmPm as AmPm

  construct( amPm: AmPm, hour: Integer, min: Integer ) {
    _amPm = amPm
    _hour = hour
    _min = min
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteSecondAmPm {
    return new( _amPm, comp, _hour, _min )
  }
}