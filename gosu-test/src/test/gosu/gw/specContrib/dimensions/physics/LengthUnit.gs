package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

enum LengthUnit implements IUnit<BigDecimal, Length, LengthUnit> {
  Micros( 1bd, "Micrometres", "µm" ),
  Millis( 1000bd, "Millimeters", "mm" ),
  Meters( 1000000bd, "Meters", "m" ),
  Kilometers( 1000000000bd, "Kilometers", "km" ),
  Inches( 25400bd, "Inches", "in" ),
  Feet( 12*25400bd, "Feet", "ft" ),
  Yards( 3*12*25400bd, "Yards", "yd" ),
  Miles( 1609344000bd, "Miles", "mi" )
  
  var _micros: BigDecimal as Micrometres
  var _name: String
  var _symbol: String
  
  private construct( micros: BigDecimal, name: String, symbol: String ) {
    _micros = micros
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
    return Micrometres 
  }
  
  function divide( t: TimeUnit ) : RateUnit {
    return new RateUnit( this, t )
  }
}