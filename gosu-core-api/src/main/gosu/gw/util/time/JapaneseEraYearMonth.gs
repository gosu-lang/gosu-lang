package gw.util.time
uses java.time.Month
uses java.time.chrono.JapaneseDate

class JapaneseEraYearMonth {
  final var _month  : Month as Month
  final var _eraYear: JapaneseEraYear as EraYear
  
  construct( eraYear: JapaneseEraYear, month: Month ) {
    _eraYear = eraYear
    _month = month
  }
  
  function prefixBind( day: Integer ) : JapaneseDate {
    return JapaneseDate.of( EraYear.Era, EraYear.Year.Value, Month.Value, day )
  }
}