package gw.specContrib.dimensions.physics

uses java.math.BigDecimal

enum WeightUnit implements IUnit<BigDecimal, Weight, WeightUnit> {
  AMUs( 1.6605402e-18, "AMUs", "amu" ),
  Micros( 1bd, "Micrograms", "µg" ),
  Millis( 1000bd, "Milligrams", "mg" ),
  Grams( 1000000bd, "Grams", "g" ),
  Kilograms( 1000000000bd, "Kilograms", "kg" ),
  Carats( 200000bd, "Carats", "ct" ),
  Drams( 1771845.195312bd, "Drams", "dr" ),
  Grains( 64798.91bd, "Grains", "gr" ),
  Newtons( 101971621.2978bd, "Newtons", "N" ),
  Ounces( 28349523.125bd, "Ounces", "oz" ),
  TroyOunces( 31103476.8bd, "Troy Ounces", "ozt" ),
  Pounds( 453592370bd, "Pounds", "lb" ),
  Stones( 6350293180bd, "Stones", "st" ),
  Tons( 907184740000bd, "Tons (US, short)", "sht" ),
  TonsUK( 1016046908800bd, "Tons (UK, long)", "lt" ),
  Tonnes( 1000000000000bd, "Tonnes", "tonne" ),
  Solars( 1.9889200011445836e39, "Solar Masses", "M☉" )
  
  var _micros: BigDecimal as Micrograms
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
    return Micrograms
  }
      
  function multiply( rate: RateUnit ) : MomentumUnit {
    return new( this, rate )
  }
  
  function multiply( acc: AccelerationUnit ) : ForceUnit {
    return new( this, acc )
  }
}