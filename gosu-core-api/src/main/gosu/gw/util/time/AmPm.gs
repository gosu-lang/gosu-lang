package gw.util.time
uses java.time.LocalTime

enum AmPm {
  AM, PM
  
  function postfixBind( timeComp: Integer ) : LocalTime {
    return LocalTime.of( this == AM ? timeComp : 12 + timeComp, 0 ) 
  }  
}