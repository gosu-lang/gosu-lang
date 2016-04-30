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
    super( velocityUnit, timeUnit, factor, name, symbol )
  }

  override property get FullName() : String {
    return VelocityUnit.TimeUnit === TimeUnit
           ? VelocityUnit.LengthUnit.FullName + "/" + TimeUnit.FullName + "\u00B2"
           : VelocityUnit.FullName + "/" + TimeUnit.FullName
  }

  override property get FullSymbol() : String {
    return VelocityUnit.TimeUnit === TimeUnit
           ? VelocityUnit.LengthUnit.FullSymbol + "/" + TimeUnit.FullSymbol + "\u00B2"
           : VelocityUnit.FullSymbol + "/" + TimeUnit.FullSymbol
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
