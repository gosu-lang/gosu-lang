package gw.specContrib.dimensions.physics

uses java.math.BigDecimal

final class MomentumUnit implements IUnit<BigDecimal, Momentum, MomentumUnit> {
  public static var BASE: MomentumUnit = new ( Micros, RateUnit.BASE )
  
  var _weightUnit: WeightUnit as readonly WeightUnit
  var _rateUnit: RateUnit as readonly RateUnit
  
  construct( weightUnit: WeightUnit, rateUnit: RateUnit ) {
    _weightUnit = weightUnit
    _rateUnit = rateUnit
  }
  
  override property get UnitName() : String {
    return _weightUnit.UnitName + " " + _rateUnit.UnitName
  }

  override property get UnitSymbol() : String {
    return _weightUnit.UnitSymbol + " " + _rateUnit.UnitSymbol
  }
  
  override property get BaseUnitFactor() : BigDecimal {
    return _weightUnit.BaseUnitFactor * _rateUnit.BaseUnitFactor 
  }
}