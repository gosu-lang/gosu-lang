package gw.util.time

class HourAmPm implements ITimeOfDay {
  final var _hour: Integer as Hour
  final var _amPm: AmPm as AmPm

  construct( amPm: AmPm, hour: Integer ) {
    _amPm = amPm
    _hour = hour
  }
  
  @BinderSeparators( :required = {":"} )
  function postfixBind( comp: Integer ) : HourMinuteAmPm {
    return new( _amPm, comp, _hour )
  }
}