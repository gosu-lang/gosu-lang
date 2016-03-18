package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

final class ForceUnit implements IUnit<BigDecimal, Force, ForceUnit>, UnitConstants {
  public static var BASE: ForceUnit = new ( Micros, AccelerationUnit.BASE )
  public static var NEWTONS: ForceUnit = kg*(m/s/s)
  
  var _weightUnit: WeightUnit as readonly WeightUnit
  var _accUnit: AccelerationUnit as readonly AccUnit
  
  construct( weightUnit: WeightUnit, accUnit: AccelerationUnit ) {
    _weightUnit = weightUnit
    _accUnit = accUnit
  }
  
  override property get UnitName() : String {
    return _weightUnit.UnitName + " " + _accUnit.UnitName
  }
  
  override property get UnitSymbol() : String {
    return _weightUnit.UnitSymbol + " " + _accUnit.UnitSymbol
  }

  override property get BaseUnitFactor() : BigDecimal {
    return _weightUnit.BaseUnitFactor * _accUnit.BaseUnitFactor 
  }
}
