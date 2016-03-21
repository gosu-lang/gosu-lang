package gw.util.time

uses java.time.MonthDay
uses java.time.LocalDate

enhancement CoreMonthDayEnhancement : MonthDay
{
  function postfixBind( year: Integer ) : LocalDate {
    return LocalDate.of( year, this.Month, this.DayOfMonth )
  }
}
