package gw.util.science

uses java.math.BigDecimal

enum MassUnit implements IUnit<BigDecimal, Mass, MassUnit> {
  AtomicMass( 1.6605402e-27, "AMU", "amu" ),
  Micro( .000000001bd, "Microgram", "µg" ),
  Milli( .000001bd, "Milligram", "mg" ),
  Gram( .001bd, "Gram", "g" ),
  Kilogram( 1bd, "Kilogram", "kg" ),
  Carat( .0002bd, "Carat", "ct" ),
  Dram( .001771845195312bd, "Dram", "dr" ),
  Grain( 6.47989e-5, "Grain", "gr" ),
  Newton( 0.101971621bd, "Newton", "N" ),
  Ounce( 0.0283495bd, "Ounce", "oz" ),
  TroyOunce( 0.0311035bd, "Troy Ounce", "ozt" ),
  Pound( 0.453592bd, "Pound", "lb" ),
  Stone( 6.35029bd, "Stone", "st" ),
  Ton( 907.185bd, "Ton (US, short)", "sht" ),
  TonUK( 1016.05bd, "Ton (UK, long)", "lt" ),
  Tonne( 1000bd, "Tonne", "tonne" ),
  Solar( 1.9889200011445836e30, "Solar Masses", "M☉" )
  
  var _kilograms: BigDecimal as Kilograms
  var _name: String
  var _symbol: String
  
  private construct( kilograms: BigDecimal, name: String, symbol: String ) {
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
  
  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return Kilograms * myUnits
  }

  override function toNumber() : BigDecimal {
    return Kilograms
  }
  
  override function from( w: Mass ) : BigDecimal {
    return w.toNumber() / Kilograms
  }   
      
  function multiply( velocity: VelocityUnit ) : MomentumUnit {
    return new( this, velocity )
  }
  
  function multiply( acc: AccelerationUnit ) : ForceUnit {
    return new( this, acc )
  }
  
  function divide( area: AreaUnit ) : PressureUnit {
    return new PressureUnit( this, area )
  } 

  function divide( volume: VolumeUnit ) : DensityUnit {
    return new DensityUnit( this, volume )
  }   
}
