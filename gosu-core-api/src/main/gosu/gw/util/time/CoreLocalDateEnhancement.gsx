package gw.util.time
uses java.time.LocalDate
uses java.time.chrono.Era
uses java.time.chrono.JapaneseEra
uses java.time.chrono.JapaneseDate
uses java.time.chrono.IsoEra
uses java.time.LocalDateTime
uses java.time.LocalTime
uses gw.util.science.Time
uses java.time.ZoneId
uses java.time.ZonedDateTime

enhancement CoreLocalDateEnhancement : LocalDate
{
  function prefixBind( era: JapaneseEra ) : JapaneseDate {
    return JapaneseDate.of( era, this.Year, this.MonthValue, this.DayOfMonth )
  }

  function prefixBind( zoneId: ZoneId ) : ZonedDateTime {
    return ZonedDateTime.of( this, LocalTime.MIDNIGHT, zoneId )
  }

  function prefixBind( time: Time ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.ofSecondOfDay( time.toBaseNumber().intValue() ) )
  }

  // 24 hour time
  function prefixBind( timeComp: Integer ) : LocalDateTime {
    var hour: int
    var minute = 0
    var second = 0
    if( timeComp < 100 ) {
      // Assume only hour is given e.g., 9 PM
      hour = timeComp
    }
    else if( timeComp < 10000 ) {
      // Assume hour + min, no seconds e.g., 0930 PM (9:30 PM)
      hour = timeComp/100
      minute = timeComp%100
    }
    else if( timeComp < 1000000 ) {
      // Assume hour + min + sec, no millis e.g., 093045 PM (9:30:45 PM)
      hour = timeComp/10000
      minute = (timeComp%10000)/100
      second = timeComp%100
    }
    
    return LocalDateTime.of( this, LocalTime.of( hour, minute, second ) )
  }  
  
  // 24 hour time + millis
  function prefixBind( time: Double ) : LocalDateTime {
    var timeComp = time as int
    var hour = timeComp/10000
    var minute = (timeComp%10000)/100
    var second = timeComp%100
    var millis = (Math.rint( (time - timeComp) * 1000 ) as int) * 1000000
    return LocalDateTime.of( this, LocalTime.of( hour, minute, second, millis ) )  
  }

  function prefixBind( time: HourZone ) : ZonedDateTime {
    return ZonedDateTime.of( this, LocalTime.of( time.Hour, 0 ), time.ZoneId )
  }
  function prefixBind( time: HourMinuteZone ) : ZonedDateTime {
    return ZonedDateTime.of( this, LocalTime.of( time.Hour, time.Min ), time.ZoneId )
  }
  function prefixBind( time: HourMinuteSecondZone ) : ZonedDateTime {
    return ZonedDateTime.of( this, LocalTime.of( time.Hour, time.Min, time.Sec ), time.ZoneId )
  }
  function prefixBind( time: HourMinuteSecondMilliZone ) : ZonedDateTime {
    return ZonedDateTime.of( this, LocalTime.of( time.Hour, time.Min, time.Sec, time.Milli * 1000000 ), time.ZoneId )
  }

  function prefixBind( time: HourAmPm ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.of( getHour( time ), 0 ) )
  }
  function prefixBind( time: HourMinuteAmPm ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.of( getHour( time ), time.Min ) )
  }
  function prefixBind( time: HourMinuteSecondAmPm ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.of( getHour( time ), time.Min, time.Sec ) )
  }
  function prefixBind( time: HourMinuteSecondMilliAmPm ) : LocalDateTime {
    return LocalDateTime.of( this, LocalTime.of( getHour( time ), time.Min, time.Sec, time.Milli * 1000000 ) )
  }
  
  private function getHour( time: ITimeOfDay ) : int {
    var hour = time.Hour
    if( time.AmPm === PM ) {
      if( time.Hour != 12 ) {
        hour += 12
      }
    }
    else if( time.AmPm === AM && time.Hour == 12 ) {
      hour -= 12
    }
    return hour
  }
}