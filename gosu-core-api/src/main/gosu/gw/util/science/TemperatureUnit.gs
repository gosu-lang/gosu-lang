package gw.util.science
uses gw.util.Rational

enum TemperatureUnit implements IUnit<Rational, Temperature, TemperatureUnit> {
  Kelvin( \degK -> degK, \degK -> degK, "Kelvin", "K" ),
  Celcius( \degC -> degC + 273.15, \degK -> degK - 273.15, "Celcius", "°C" ),
  Fahrenheit( \degF -> (degF + 459.67) * 5/9, \degK -> degK * 9/5 - 459.67, "Fahrenheit", "°F" ),
  Rankine( \degR -> degR * (5/9), \degK -> degK * 9/5, "Rankine", "°R" ),
  Delisle( \De -> 373.15 - De * (2/3), \degK -> (373.15 - degK) * 3/2, "Delisle", "°De" ),
  Newton( \degN -> degN * (100/33) + 273.15, \degK -> (degK - 273.15) * 33/100, "Newton", "°N" )

  var _toK(deg:Rational): Rational as ToKelvin
  var _fromK(deg:Rational): Rational as FromKelvin
  var _name: String
  var _symbol: String

  static property get BASE() : TemperatureUnit {
    return Kelvin
  }

  private construct( toK(deg:Rational): Rational, fromK(deg:Rational): Rational, name: String, symbol: String ) {
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

  override function toBaseUnits( myUnits: Rational ) : Rational {
    return ToKelvin( myUnits )
  }
  
  override function toNumber() : Rational {
    return ToKelvin( 1 )
  }
    
  override function from( t: Temperature ) : Rational {
    return FromKelvin( t.toNumber() )
  }   
  
  function multiply( c: HeatCapacityUnit ) : EnergyUnit {
    return c.EnergyUnit
  }
}
