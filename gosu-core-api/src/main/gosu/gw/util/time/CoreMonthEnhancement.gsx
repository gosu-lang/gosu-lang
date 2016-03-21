package gw.util.time

uses java.time.Month
uses java.time.MonthDay
uses java.time.Year
uses java.time.YearMonth

enhancement CoreMonthEnhancement : Month
{
  function prefixBind( day: Integer ) : MonthDay {
    return MonthDay.of( this, day )
  }   
  
  function postfixBind( year: Integer ) : YearMonth {
    return YearMonth.of( year, this )
  }
  
  function postfixBind( jer: JapaneseEraYear ) : JapaneseEraYearMonth {
    return new JapaneseEraYearMonth( jer, this )
  }
}
