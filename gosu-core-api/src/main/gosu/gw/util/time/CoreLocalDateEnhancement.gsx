package gw.util.time
uses java.time.LocalDate
uses java.time.chrono.Era
uses java.time.chrono.JapaneseEra
uses java.time.chrono.JapaneseDate
uses java.time.chrono.IsoEra
uses java.time.LocalDateTime
uses java.time.LocalTime
uses gw.util.science.Time

enhancement CoreLocalDateEnhancement : LocalDate
{
  function prefixBind( era: JapaneseEra ) : JapaneseDate {
    return JapaneseDate.of( era, this.Year, this.MonthValue, this.DayOfMonth )
  }
  
  function prefixBind( time: Time ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.ofSecondOfDay( time.toNumber().intValue() ) )
  }

  // 24 hour time
  function prefixBind( time: Integer ) : LocalDateTime {
    // hhmmss
    // hhmm
    return LocalDateTime.of( this, LocalTime.ofSecondOfDay( time ) )    
  }
  
  // 24 hour time + millis
  function prefixBind( time: Double ) : LocalDateTime {
    // hhmmss.SSS
    print( "double" )
    return LocalDateTime.of( this, LocalTime.ofSecondOfDay( time as int ) )    
  }
}