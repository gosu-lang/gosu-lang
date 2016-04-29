package gw.util.science

uses gw.util.Rational

final class AccelerationUnit extends AbstractQuotientUnit<VelocityUnit, TimeUnit, Acceleration, AccelerationUnit> {
  final static var CACHE: UnitCache<AccelerationUnit> = new UnitCache()

  public static var BASE: AccelerationUnit = get( VelocityUnit.BASE, VelocityUnit.BASE.TimeUnit )

  static function get( velocityUnit: VelocityUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : AccelerationUnit {
    var unit = new AccelerationUnit( velocityUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
 
  private construct( velocityUnit: VelocityUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( velocityUnit, timeUnit, factor,
           name != null
           ? name
           : velocityUnit.TimeUnit === timeUnit
             ? velocityUnit.LengthUnit.UnitName + "/" + timeUnit.UnitName + "\u00B2"
             : velocityUnit.UnitName + "/" + timeUnit.UnitName,
           symbol != null
           ? symbol
           : velocityUnit.TimeUnit === timeUnit
             ? velocityUnit.LengthUnit.UnitSymbol + "/" + timeUnit.UnitSymbol + "\u00B2"
             : velocityUnit.UnitSymbol + "/" + timeUnit.UnitSymbol )
  }
  
  property get VelocityUnit() : VelocityUnit {
    return LeftUnit
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit
  }  
  
  function postfixBind( mass: MassUnit ) : ForceUnit {
    return multiply( mass )
  }
  
  function multiply( t: MassUnit ) : ForceUnit {
    return ForceUnit.get( t, this )
  }  
}
