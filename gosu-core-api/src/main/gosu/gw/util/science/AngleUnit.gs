package gw.util.science
uses gw.util.Rational
uses DimensionlessConstants#pi

enum AngleUnit implements IUnit<Rational, Angle, AngleUnit> {
  Nano( .000000001, "Nanoradian", "nrad" ),
  Milli( .001, "Milliradian", "mrad" ),
  Radian( 1, "Radian", "rad" ),
  Degree( pi/180, "Degree", "deg" ),
  MOA( pi/10800, "MinuteOfArc", "moa" ),
  ArcSecond( pi/648000, "ArcSecond", "arcsec" ),
  MilliArcSecond( pi/648000000, "MilliArcSecond", "mas" ),
  Turn( 2*pi, "Turn", "cyc" ),
  Gradian( pi/200, "Gradian", "grad" ),
  Quadrant( pi/2, "Quadrant", "quad" )

  var _rads: Rational as Rads
  var _name: String
  var _symbol: String

  static property get BaseUnit() : AngleUnit {
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
  
  override function from( len: Angle ) : Rational {
    return len.toNumber() / Rads
  }
  
  function divide( time: TimeUnit ) : FrequencyUnit {
    return FrequencyUnit.get( this, time )
  }
}
