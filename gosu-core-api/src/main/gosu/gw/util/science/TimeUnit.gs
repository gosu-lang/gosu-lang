package gw.util.science
uses java.math.BigDecimal

enum TimeUnit implements IUnit<BigDecimal, Time, TimeUnit> {
  // Ephemeris (SI) units
  Planck( 5.39056e-44, "Planck-time", "tP" ),
  Femto( .000000000000001bd, "Femtosecond", "fs" ),
  Pico( .000000000001bd, "Picosecond", "ps" ),
  Nano( .000000001bd, "Nanosecond", "ns" ),
  Micro( .000001bd, "Microsecond", "µs" ),
  Milli( .001bd, "Millisecond", "ms" ),
  Second( 1bd, "Second", "s" ),
  Minute( 60bd, "Minute", "min"  ),
  Hour( 60*60bd, "Hour", "hr" ),
  Day( 24*60*60bd, "Day", "day" ),
  Week( 7*24*60*60bd, "Week", "wk" ),
  
  // Mean Gregorian (ISO Calendar) units  
  Month( 31556952bd/12, "Month", "mo" ),
  Year( 31556952bd, "Year", "yr" ),
  Decade( 31556952bd * 10, "Decade", "decade" ),
  Century( 31556952bd * 100, "Century", "century" ),
  Millennium( 31556952bd * 1000, "Millennium", "millennium" ),
  Era( 31556952bd * 1000000000, "Era", "era" ),
  
  // Mean Tropical (Solar) units
  TrMonth( 31556925.445bd/12, "Tropical Month", "tmo" ),
  TrYear( 31556925.445bd, "Tropical Year", "tyr" ),
  
  final var _sec: BigDecimal as Seconds
  final var _name: String
  final var _symbol: String
  
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
