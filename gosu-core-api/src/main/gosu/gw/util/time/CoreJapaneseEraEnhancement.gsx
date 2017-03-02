package gw.util.time
uses java.time.chrono.JapaneseEra
uses java.time.Year
uses java.time.LocalDate
uses java.time.chrono.JapaneseDate

enhancement CoreJapaneseEraEnhancement : JapaneseEra {
  function prefixBind( year: Integer ) : JapaneseEraYear {
    return new JapaneseEraYear( this, Year.of( year ) )
  }
}