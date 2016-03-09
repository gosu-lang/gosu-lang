package gw.specContrib.dimensions.physics

uses java.math.BigDecimal

final class AccelerationUnit implements IUnit<BigDecimal, Acceleration, AccelerationUnit> {
  public static var BASE: AccelerationUnit = new( RateUnit.BASE, RateUnit.BASE.TimeUnit )
  
  var _rateUnit: RateUnit as readonly RateUnit
  var _timeUnit: TimeUnit as readonly TimeUnit
  
  construct( rateUnit: RateUnit, timeUnit: TimeUnit ) {
    _rateUnit = rateUnit
    _timeUnit = timeUnit
  }
  
  override property get UnitName() : String {
    return _rateUnit.UnitName + "/" + _timeUnit.UnitName
  }
  
  override property get UnitSymbol() : String {
    return _rateUnit.UnitSymbol + "/" + _timeUnit.UnitSymbol
  }
  
  override property get BaseUnitFactor() : BigDecimal {
    return _rateUnit.BaseUnitFactor * _timeUnit.BaseUnitFactor 
  }
}