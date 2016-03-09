package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

final class RateUnit implements IUnit<BigDecimal, Rate, RateUnit> {
  public static var BASE: RateUnit = new( LengthUnit.Micros, TimeUnit.Nanos )
  
  var _lengthUnit: LengthUnit as readonly LengthUnit
  var _timeUnit: TimeUnit as readonly TimeUnit
  
  construct( lengthUnit: LengthUnit, timeUnit: TimeUnit ) {
    _lengthUnit = lengthUnit
    _timeUnit = timeUnit
  }
  
  override property get UnitName() : String {
    return _lengthUnit.UnitName + "/" + _timeUnit.UnitName
  }

  override property get UnitSymbol() : String {
    return _lengthUnit.UnitSymbol + "/" + _timeUnit.UnitSymbol
  }
  
  override property get BaseUnitFactor() : BigDecimal {
    return _lengthUnit.BaseUnitFactor / _timeUnit.BaseUnitFactor 
  }
  
  function divide( t: TimeUnit ) : AccelerationUnit {
    return new AccelerationUnit( this, t )
  }
}