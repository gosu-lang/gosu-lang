package gw.util.time
uses java.time.Year
uses java.time.chrono.JapaneseEra

class JapaneseEraYear {
  final var _year: Year as Year
  final var _era: JapaneseEra as Era
  
  construct( era: JapaneseEra, year: Year ) {
    _era = era
    _year = year
  }
}