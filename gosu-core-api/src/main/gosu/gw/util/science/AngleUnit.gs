package gw.util.science
uses java.math.BigDecimal
uses UnitConstants#PI

enum AngleUnit implements IUnit<BigDecimal, Angle, AngleUnit> {
  Nano( .000000001bd, "Nanoradian", "nrad" ),
  Milli( .001bd, "Milliradian", "mrad" ),
  Radian( 1bd, "Radian", "rad" ),
  Degree( PI/180bd, "Degree", "deg" ),
  MOA( PI/10800bd, "MinuteOfArc", "moa" ),
  ArcSecond( PI/648000bd, "ArcSecond", "arcsec" ),
  MilliArcSecond( PI/648000000bd, "MilliArcSecond", "mas" ),
  Turn( 2bd*PI, "Turn", "cyc" ),
  Gradian( PI/200bd, "Gradian", "grad" ),
  Quadrant( PI/2bd, "Quadrant", "quad" )

  var _rads: BigDecimal as Rads
  var _name: String
  var _symbol: String
  
  private construct( rads: BigDecimal, name: String, symbol: String ) {
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
 
  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return Rads * myUnits
  }
  
  override function toNumber() : BigDecimal {
    return Rads
  }
  
  override function from( len: Angle ) : BigDecimal {
    return len.toNumber() / Rads
  }
  
  function divide( time: TimeUnit ) : FrequencyUnit {
    return new( this, time )
  }
}
