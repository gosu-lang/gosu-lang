package gw.util.science
uses gw.util.Rational
uses MetricScaleUnit#*

enum TimeUnit implements IUnit<Rational, Time, TimeUnit> {
  // Ephemeris (SI) units
  Planck( 5.39056e-44, "Planck-time", "tP" ),
  Femto( 1fe, "Femtosecond", "fs" ),
  Pico( 1p, "Picosecond", "ps" ),
  Nano( 1n, "Nanosecond", "ns" ),
  Micro( 1u, "Microsecond", "µs" ),
  Milli( 1m, "Millisecond", "ms" ),
  Second( 1, "Second", "s" ),
  Minute( 60, "Minute", "min"  ),
  Hour( 60*60, "Hour", "hr" ),
  Day( 24*60*60, "Day", "day" ),
  Week( 7*24*60*60, "Week", "wk" ),
  
  // Mean Gregorian (ISO Calendar) units  
  Month( 31556952/12, "Month", "mo" ),
  Year( 31556952, "Year", "yr" ),
  Decade( 31556952 * 10, "Decade", "decade" ),
  Century( 31556952 * 100, "Century", "century" ),
  Millennium( 31556952 k, "Millennium", "millennium" ),
  Era( 31556952 G, "Era", "era" ),
  
  // Mean Tropical (Solar) units
  TrMonth( 31556925.445/12, "Tropical Month", "tmo" ),
  TrYear( 31556925.445, "Tropical Year", "tyr" ),
  
  final var _sec: Rational as Seconds
  final var _name: String
  final var _symbol: String

  static property get BASE() : TimeUnit {
    return Second
  }

  private construct( sec: Rational, name: String, symbol: String ) {
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

  override function toBaseUnits( myUnits: Rational ) : Rational {
    return Seconds * myUnits
  }
  
  override function toNumber() : Rational {
    return Seconds
  }
    
  override function from( t: Time ) : Rational {
    return t.toBaseNumber() / Seconds
  }

  function multiply( v: VelocityUnit ) : LengthUnit {
    return v.LengthUnit
  }

  function multiply( acc: AccelerationUnit ) : VelocityUnit {
    return acc.VelocityUnit
  }

  function multiply( current: CurrentUnit ) : ChargeUnit {
    return Coulomb
  }

  function multiply( frequency: FrequencyUnit ) : AngleUnit {
    return frequency.AngleUnit
  }

  function multiply( power: PowerUnit ) : EnergyUnit {
    return power.EnergyUnit
  }

  function multiply( force: ForceUnit ) : MomentumUnit {
    return force.MassUnit * (force.AccUnit.VelocityUnit.LengthUnit/this)
  }
}