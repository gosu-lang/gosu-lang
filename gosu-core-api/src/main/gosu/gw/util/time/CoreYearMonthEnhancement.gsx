package gw.util.time

uses java.time.YearMonth
uses java.time.LocalDate

enhancement CoreYearMonthEnhancement : YearMonth
{
  function postfixBind( dayOfMonth: Integer ) : LocalDate {
    return LocalDate.of( this.Year, this.Month, dayOfMonth )
  }
}
