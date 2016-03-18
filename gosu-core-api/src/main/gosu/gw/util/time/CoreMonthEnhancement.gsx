package gw.util.time

uses java.time.Month
uses java.time.MonthDay
uses java.time.Year
uses java.time.YearMonth

enhancement CoreMonthEnhancement : Month
{
  function postfixBind( day: Integer ) : MonthDay {
    return MonthDay.of( this, day )
  }   
  
  function prefixBind( year: Integer ) : YearMonth {
    return YearMonth.of( year, this )
  }
}
