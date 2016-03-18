package gw.util.time

uses java.time.MonthDay
uses java.time.LocalDate

enhancement CoreMonthDayEnhancement : MonthDay
{
  function prefixBind( year: Integer ) : LocalDate {
    return LocalDate.of( year, this.Month, this.DayOfMonth )
  }
}
