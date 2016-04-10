package gw.util.time
uses java.time.LocalTime
uses gw.util.science.Time
uses gw.util.science.UnitConstants

enum AmPm implements UnitConstants {
  AM, PM
  
  function postfixBind( time: Time ) : LocalTime {
    var is12 = time >= 12hr && time < 13hr
    if( this === PM ) {
      if( !is12 ) {
        time += 12hr
      }
    }
    else if( is12 ) {
      time -= 12hr
    }
    return LocalTime.ofNanoOfDay( time.toNumber( Nano ).longValue() )
  }
  
  function postfixBind( hour: Integer ) : HourAmPm {
    return new( this, hour )
  }
  
//  function postfixBind( timeComp: Integer ) : LocalTime {
//    var hour: int
//    var minute = 0
//    var second = 0
//    if( timeComp < 100 ) {
//      // Assume only hour is given e.g., 9 PM
//      hour = timeComp
//    }
//    else if( timeComp < 10000 ) {
//      // Assume hour + min, no seconds e.g., 0930 PM (9:30 PM)
//      hour = timeComp/100
//      minute = timeComp%100
//    }
//    else if( timeComp < 1000000 ) {
//      // Assume hour + min + sec, no millis e.g., 093045 PM (9:30:45 PM)
//      hour = timeComp/10000
//      minute = (timeComp%10000)/100
//      second = timeComp%100
//    }
//    
//    if( this === AM ) { 
//      hour = hour == 12 ? 0 : hour
//    }
//    else {
//      hour += 12 
//    }
//    
//    return LocalTime.of( hour, minute, second )
//  }  
}