package gw.util.time
uses java.time.LocalTime
uses java.time.LocalDate
uses java.time.LocalDateTime

enhancement CoreLocalTimeEnhancement : LocalTime
{
  function postfixBind( date: LocalDate ) : LocalDateTime {
    return LocalDateTime.of( date, this )
  }
}