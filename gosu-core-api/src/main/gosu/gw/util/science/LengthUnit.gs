package gw.util.science
uses gw.util.Rational

enum LengthUnit implements IUnit<Rational, Length, LengthUnit> {
  // Planck length
  Planck( 1.61605e-35, "Planck-length", "ℓP" ),
  
  // Metric
  Femto( .000000000000001, "Femtometer", "fm" ),
  Pico( .000000000001, "Picometer", "pm" ),
  Angstrom( 1e-10, "Angstrom", "Å" ),
  Nano( .000000001, "Nanometer", "nm" ),
  Micro( .000001, "Micrometre", "µm" ),
  Milli( .001, "Millimeter", "mm" ),
  Centi( .01, "Centimeter", "cm" ),
  Deci( .1, "Decimeter", "dm" ),
  Meter( 1, "Meter", "m" ),
  Kilometer( 1000, "Kilometer", "km" ),
  Megameter( 1000000, "Megameter", "Mm" ),
  Gigameter( 1000000000, "Gigameter", "Gm" ),
  Terameter( 1000000000000, "Terameter", "Tm" ),
  
  // UK
  Cubit( 0.4572, "Cubit", "cbt" ),
  
  // US Standard
  Caliber( .000254, "Caliber", "cal."),
  Inch( 0.0254, "Inch", "in" ),
  Foot( 12*0.0254, "Foot", "ft" ),
  Yard( 3*12*0.0254, "Yard", "yd" ),
  Rod( 5.0292, "Rod", "rd" ),
  Chain( 20.1168, "Chain", "ch" ),
  Furlong( 201.168, "Furlong", "fur" ),  
  Mile( 1609.344, "Mile", "mi" ),  
  
  // International
  NauticalMile( 1852, "NauticalMile", "n.m." ), 
   
  // Very large units
  IAU( 149597870700, "IAU-length", "au" ),  
  LightYear( 9.460730473e+15, "LightYear", "ly" ),  
  
  
  final var _meters: Rational as Meters
  final var _name: String
  final var _symbol: String
  
  static property get BaseUnit() : LengthUnit {
    return Meter
  }
  
  private construct( meters: Rational, name: String, symbol: String ) {
    _meters = meters
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
    return Meters * myUnits
  }

  override function toNumber() : Rational {
    return Meters
  }
    
  override function from( len: Length ) : Rational {
    return len.toNumber() / Meters
  }
 
  function postfixBind( f: ForceUnit ) : WorkUnit {
    return multiply( f )
  }

  function divide( t: TimeUnit ) : VelocityUnit {
    return VelocityUnit.get( this, t )
  }
   
  function divide( v: VelocityUnit ) : TimeUnit {
    return v.TimeUnit
  }
  
  function multiply( len: LengthUnit ) : AreaUnit {
    return AreaUnit.get( this, len )
  }
  
  function multiply( area: AreaUnit ) : VolumeUnit {
    return VolumeUnit.get( this, area )
  }  
  
  function multiply( f: ForceUnit ) : WorkUnit {
    return WorkUnit.get( f, this )
  }  
}
