package gw.util.time
uses java.time.Month
uses java.time.chrono.JapaneseEra
uses java.time.ZoneId

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
  
  var Z: ZoneId = ZoneId.of( "Z" )
  var UTC: ZoneId = Z
  var GMT: ZoneId = Z
  property get LOCAL(): ZoneId { return TimeZone.getDefault().toZoneId() }
  var EST: ZoneId = ZoneId.of( "-05:00" )
  var MST: ZoneId = ZoneId.of( "-07:00" )
  var HST: ZoneId = ZoneId.of( "-10:00" )
  var ACT: ZoneId = ZoneId.of( "Australia/Darwin" )
  var AET: ZoneId = ZoneId.of( "Australia/Sydney" )
  var AGT: ZoneId = ZoneId.of( "America/Argentina/Buenos_Aires" )
  var ART: ZoneId = ZoneId.of( "Africa/Cairo" )
  var AST: ZoneId = ZoneId.of( "America/Anchorage" )
  var BET: ZoneId = ZoneId.of( "America/Sao_Paulo" )
  var BST: ZoneId = ZoneId.of( "Asia/Dhaka" )
  var CAT: ZoneId = ZoneId.of( "Africa/Harare" )
  var CNT: ZoneId = ZoneId.of( "America/St_Johns" )
  var CST: ZoneId = ZoneId.of( "America/Chicago" )
  var CTT: ZoneId = ZoneId.of( "Asia/Shanghai" )
  var EAT: ZoneId = ZoneId.of( "Africa/Addis_Ababa" )
  var ECT: ZoneId = ZoneId.of( "Europe/Paris" )
  var IET: ZoneId = ZoneId.of( "America/Indiana/Indianapolis" )
  var IST: ZoneId = ZoneId.of( "Asia/Kolkata" )
  var JST: ZoneId = ZoneId.of( "Asia/Tokyo" )
  var MIT: ZoneId = ZoneId.of( "Pacific/Apia" )
  var NET: ZoneId = ZoneId.of( "Asia/Yerevan" )
  var NST: ZoneId = ZoneId.of( "Pacific/Auckland" )
  var PLT: ZoneId = ZoneId.of( "Asia/Karachi" )
  var PNT: ZoneId = ZoneId.of( "America/Phoenix" )
  var PRT: ZoneId = ZoneId.of( "America/Puerto_Rico" )
  var PST: ZoneId = ZoneId.of( "America/Los_Angeles" )
  var SST: ZoneId = ZoneId.of( "Pacific/Guadalcanal" )
  var VST: ZoneId = ZoneId.of( "Asia/Ho_Chi_Minh" )
  
  var Meiji: JapaneseEra = JapaneseEra.MEIJI
  var Taisho: JapaneseEra = JapaneseEra.TAISHO
  var Showa: JapaneseEra = JapaneseEra.SHOWA
  var Heisei: JapaneseEra = JapaneseEra.HEISEI
}
