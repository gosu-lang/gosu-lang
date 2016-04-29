package gw.util.science
uses gw.util.Rational

final class VelocityUnit extends AbstractQuotientUnit<LengthUnit, TimeUnit, Velocity, VelocityUnit> {
  final static var CACHE: UnitCache<VelocityUnit> = new UnitCache()

  public static var BASE: VelocityUnit = get( Meter, Second )
  public static var mph: VelocityUnit = get( Mile, Hour, 1, "MPH", "mph" )
  
  static function get( lengthUnit: LengthUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : VelocityUnit {
    var unit = new VelocityUnit( lengthUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( lengthUnit: LengthUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( lengthUnit, timeUnit, factor, name, symbol )
  }  

  property get LengthUnit() : LengthUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
     return RightUnit 
  }

  function postfixBind( mass: MassUnit ) : MomentumUnit {
    return multiply( mass )
  }
        
  function multiply( t: TimeUnit ) : LengthUnit {
    return LengthUnit
  }
  
  function multiply( t: MassUnit ) : MomentumUnit {
    return MomentumUnit.get( t, this )
  }

  function multiply( force: ForceUnit ) : PowerUnit {
    return force * LengthUnit / TimeUnit
  }
      
  function divide( t: TimeUnit ) : AccelerationUnit {
    return AccelerationUnit.get( this, t )
  }
}
