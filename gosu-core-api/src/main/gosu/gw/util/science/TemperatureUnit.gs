package gw.util.science
uses java.math.BigDecimal

enum TemperatureUnit implements IUnit<BigDecimal, Temperature, TemperatureUnit> {
  Kelvin( \degK -> degK, \degK -> degK, "Kelvin", "K" ),
  Celcius( \degC -> degC + 273.15bd, \degK -> degK - 273.15bd, "Celcius", "°C" ),
  Fahrenheit( \degF -> (degF + 459.67bd) * 5bd/9bd, \degK -> degK * 9bd/5bd - 459.67bd, "Fahrenheit", "°F" ),
  Rankine( \degR -> degR * (5bd/9bd), \degK -> degK * 9bd/5bd, "Rankine", "°R" ),
  Delisle( \De -> 373.15bd - De * (2bd/3bd), \degK -> (373.15bd - degK) * 3bd/2bd, "Delisle", "°De" ),
  Newton( \degN -> degN * (100bd/33bd) + 273.15, \degK -> (degK - 273.15bd) * 33bd/100bd, "Newton", "°N" )

  var _toK(deg:BigDecimal): BigDecimal as ToKelvin
  var _fromK(deg:BigDecimal): BigDecimal as FromKelvin
  var _name: String
  var _symbol: String
  
  private construct( toK(deg:BigDecimal):BigDecimal, fromK(deg:BigDecimal):BigDecimal, name: String, symbol: String ) {
    _toK = toK
    _fromK = fromK
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
    return ToKelvin( myUnits )
  }
  
  override function toNumber() : BigDecimal {
    return ToKelvin( 1 )
  }
    
  override function from( t: Temperature ) : BigDecimal {
    return FromKelvin( t.toNumber() )
  }   
}
