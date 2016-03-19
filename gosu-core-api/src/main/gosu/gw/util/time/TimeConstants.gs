package gw.util.time
uses java.time.Month
uses java.time.chrono.JapaneseEra

interface TimeConstants {
  var Jan: Month = JANUARY
  var Feb: Month = FEBRUARY
  var Mar: Month = MARCH
  var Apr: Month = APRIL
  var May: Month = MAY
  var June: Month = JUNE
  var July: Month = JULY
  var Aug: Month = AUGUST
  var Sept: Month = SEPTEMBER
  var Oct: Month = OCTOBER
  var Nov: Month = NOVEMBER
  var Dec: Month = DECEMBER
  
  var AM: AmPm = AM
  var PM: AmPm = PM
  
  var Meiji: JapaneseEra = JapaneseEra.MEIJI
  var Taisho: JapaneseEra = JapaneseEra.TAISHO
  var Showa: JapaneseEra = JapaneseEra.SHOWA
  var Heisei: JapaneseEra = JapaneseEra.HEISEI
}
