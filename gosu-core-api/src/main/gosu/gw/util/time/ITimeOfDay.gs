package gw.util.time
uses java.time.ZoneId

interface ITimeOfDay {
  property get Hour() : Integer
  property get Min() : Integer { return 0 }
  property get Sec() : Integer { return 0 }
  property get Milli() : Integer { return 0 }
  property get AmPm() : AmPm { return null }
  property get ZoneId() : ZoneId { return null }
}