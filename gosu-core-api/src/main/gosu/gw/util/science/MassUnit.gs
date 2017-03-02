package gw.util.science

uses gw.util.Rational
uses MetricScaleUnit#*

enum MassUnit implements IUnit<Rational, Mass, MassUnit> {
  AtomicMass( 1.6605402e-27, "AMU", "amu" ),
  Nano( 1p, "Nanogram", "µg" ),
  Micro( 1n, "Microgram", "µg" ),
  Milli( 1u, "Milligram", "mg" ),
  Gram( 1m, "Gram", "g" ),
  Kilogram( 1, "Kilogram", "kg" ),
  Tonne( 1k, "Metric Ton", "tonne" ),
  Carat( .0002, "Carat", "ct" ),
  Dram( .001771845195312, "Dram", "dr" ),
  Grain( 6.47989e-5, "Grain", "gr" ),
  Newton( 0.101971621, "Newton", "N" ),
  Ounce( 0.0283495, "Ounce", "oz" ),
  TroyOunce( 0.0311035, "Troy Ounce", "ozt" ),
  Pound( 0.453592, "Pound", "lb" ),
  Stone( 6.35029, "Stone", "st" ),
  Ton( 907.185, "Ton (US, short)", "sht" ),
  TonUK( 1016.05, "Ton (UK, long)", "lt" ),
  Solar( 1.9889200011445836e30, "Solar Masses", "M☉" )
  
  var _kilograms: Rational as Kilograms
  var _name: String
  var _symbol: String

  static property get BASE() : MassUnit {
    return Kilogram
  }

  private construct( kilograms: Rational, name: String, symbol: String ) {
    _kilograms = kilograms
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
    return Kilograms * myUnits
  }

  override function toNumber() : Rational {
    return Kilograms
  }
  
  override function from( w: Mass ) : Rational {
    return w.toBaseNumber() / Kilograms
  }   
      
  function multiply( velocity: VelocityUnit ) : MomentumUnit {
    return MomentumUnit.get( this, velocity )
  }
  
  function multiply( acc: AccelerationUnit ) : ForceUnit {
    return ForceUnit.get( this, acc )
  }
  
  function divide( area: AreaUnit ) : PressureUnit {
    return PressureUnit.get( this, area )
  }
  function divide( pressure: PressureUnit ) : AreaUnit {
    return pressure.AreaUnit
  }

  function divide( volume: VolumeUnit ) : DensityUnit {
    return DensityUnit.get( this, volume )
  }   
  function divide( d: DensityUnit ) : VolumeUnit {
    return d.VolumeUnit
  }
}
