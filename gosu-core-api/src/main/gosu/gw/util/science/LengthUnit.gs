package gw.util.science
uses java.math.BigDecimal

enum LengthUnit implements IUnit<BigDecimal, Length, LengthUnit> {
  // Planck length
  Planck( 1.61605e-35, "Planck-length", "ℓP" ),
  
  // Metric
  Femto( .000000000000001bd, "Femtometer", "fm" ),
  Pico( .000000000001bd, "Picometer", "pm" ),
  Angstrom( 1e-10, "Angstrom", "Å" ),
  Nano( .000000001bd, "Nanometer", "nm" ),
  Micro( .000001bd, "Micrometre", "µm" ),
  Milli( .001bd, "Millimeter", "mm" ),
  Meter( 1bd, "Meter", "m" ),
  Kilometer( 1000bd, "Kilometer", "km" ),
  Megameter( 1000000bd, "Megameter", "Mm" ),
  Gigameter( 1000000000bd, "Gigameter", "Gm" ),
  Terameter( 1000000000000bd, "Terameter", "Tm" ),
  
  // UK
  Cubit( 0.4572bd, "Cubit", "cbt" ),
  
  // US Standard
  Caliber( .000254bd, "Caliber", "cal."),
  Inch( 0.0254bd, "Inch", "in" ),
  Foot( 12*0.0254bd, "Foot", "ft" ),
  Yard( 3*12*0.0254bd, "Yard", "yd" ),
  Rod( 5.0292bd, "Rod", "rd" ),
  Chain( 20.1168bd, "Chain", "ch" ),
  Furlong( 201.168bd, "Furlong", "fur" ),  
  Mile( 1609.344bd, "Mile", "mi" ),  
  
  // International
  NauticalMile( 1852bd, "NauticalMile", "n.m." ), 
   
  // Very large units
  IAU( 149597870700bd, "IAU-length", "au" ),  
  LightYear( 9.460730473e+15, "LightYear", "ly" ),  
  
  
  final var _meters: BigDecimal as Meters
  final var _name: String
  final var _symbol: String
  
  static property get BaseUnit() : LengthUnit {
    return Meter
  }
  
  private construct( meters: BigDecimal, name: String, symbol: String ) {
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
 
  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return Meters * myUnits
  }

  override function toNumber() : BigDecimal {
    return Meters
  }
    
  override function from( len: Length ) : BigDecimal {
    return len.toNumber() / Meters
  }
 
  function postfixBind( f: ForceUnit ) : WorkUnit {
    return multiply( f )
  }

  function divide( t: TimeUnit ) : VelocityUnit {
    return new VelocityUnit( this, t )
  }
   
  function divide( v: VelocityUnit ) : TimeUnit {
    return v.TimeUnit
  }
  
  function multiply( len: LengthUnit ) : AreaUnit {
    return new AreaUnit( this, len )
  }
  
  function multiply( area: AreaUnit ) : VolumeUnit {
    return new VolumeUnit( area, this )
  }  
  
  function multiply( f: ForceUnit ) : WorkUnit {
    return new( f, this )
  }  
}
