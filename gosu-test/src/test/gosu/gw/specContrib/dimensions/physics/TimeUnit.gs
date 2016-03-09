package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

enum TimeUnit implements IUnit<BigDecimal, Time, TimeUnit> {
  Nanos( 1bd, "Nanoseconds", "ns" ),
  Micros( 1000bd, "Microseconds", "µs" ),
  Millis( 1000000bd, "Milliseconds", "ms" ),
  Seconds( 1000000000bd, "Seconds", "s" ),
  Minutes( 60*1000000000bd, "Minutes", "min"  ),
  Hours( 60*60*1000000000bd, "Hours", "h" ),
  Days( 24*60*60*1000000000bd, "Days", "d" ),
  Weeks( 7*24*60*60*1000000000bd, "Weeks", "wk" ),
  Years( 31556925993600000bd, "Years", "yr" ) // tropical year
  
  var _nanos: BigDecimal as Nanoseconds
  var _name: String
  var _symbol: String
  
  private construct( nanos: BigDecimal, name: String, symbol: String ) {
    _nanos = nanos
    _name = name
    _symbol = symbol
  }
  
  override property get UnitName() : String {
    return _name
  }

  override property get UnitSymbol() : String {
    return _symbol
  }
  
  override property get BaseUnitFactor() : BigDecimal {
    return Nanoseconds 
  }
}
