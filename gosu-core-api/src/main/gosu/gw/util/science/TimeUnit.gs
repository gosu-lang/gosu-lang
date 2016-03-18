package gw.util.science
uses java.math.BigDecimal

enum TimeUnit implements IUnit<BigDecimal, Time, TimeUnit> {
  Nanos( .000000001bd, "Nanosecond", "ns" ),
  Micro( .000001bd, "Microsecond", "µs" ),
  Milli( .001bd, "Millisecond", "ms" ),
  Second( 1bd, "Second", "s" ),
  Minute( 60bd, "Minute", "min"  ),
  Hour( 60*60bd, "Hour", "hr" ),
  Day( 24*60*60bd, "Day", "day" ),
  Week( 7*24*60*60bd, "Week", "wk" ),
  Year( 31556925.9936bd, "Year", "yr" ) // tropical year
  
  var _sec: BigDecimal as Seconds
  var _name: String
  var _symbol: String
  
  private construct( sec: BigDecimal, name: String, symbol: String ) {
    _sec = sec
    _name = name
    _symbol = symbol
  }
  
  override property get UnitName() : String {
    return _name
  }

  override property get UnitSymbol() : String {
    return _symbol
  }

  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return Seconds * myUnits
  }
  
  override function toNumber() : BigDecimal {
    return Seconds
  }
    
  override function from( t: Time ) : BigDecimal {
    return t.toNumber() / Seconds
  }   
}
