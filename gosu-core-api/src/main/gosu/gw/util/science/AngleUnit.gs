package gw.util.science
uses gw.util.Rational
uses DimensionlessConstants#pi
uses MetricScaleUnit#*

enum AngleUnit implements IUnit<Rational, Angle, AngleUnit> {
  Nano( 1n, "Nanoradian", "nrad" ),
  Milli( 1m, "Milliradian", "mrad" ),
  Radian( 1, "Radian", "rad" ),
  Degree( pi/180, "Degree", "deg" ),
  MOA( pi/10800, "MinuteOfArc", "moa" ),
  ArcSecond( pi/648k, "ArcSecond", "arcsec" ),
  MilliArcSecond( pi/648M, "MilliArcSecond", "mas" ),
  Turn( 2*pi, "Turn", "cyc" ),
  Gradian( pi/200, "Gradian", "grad" ),
  Quadrant( pi/2, "Quadrant", "quad" )

  var _rads: Rational as Rads
  var _name: String
  var _symbol: String

  static property get BASE() : AngleUnit {
    return Radian
  }

  private construct( rads: Rational, name: String, symbol: String ) {
    _rads = rads
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
    return Rads * myUnits
  }
  
  override function toNumber() : Rational {
    return Rads
  }
  
  override function from( angle: Angle ) : Rational {
    return angle.toBaseNumber() / Rads
  }
  
  function divide( time: TimeUnit ) : FrequencyUnit {
    return FrequencyUnit.get( this, time )
  }

  function divide( freq: FrequencyUnit ) : TimeUnit {
    return freq.TimeUnit
  }
}
