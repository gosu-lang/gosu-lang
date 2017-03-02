package gw.util.science

uses gw.util.Rational

final class MomentumUnit extends AbstractProductUnit<MassUnit, VelocityUnit, Momentum, MomentumUnit> {
  final static var CACHE: UnitCache<MomentumUnit> = new UnitCache()

  public static var BASE: MomentumUnit = get( Kilogram, VelocityUnit.BASE )

  static function get( massUnit: MassUnit, velocityUnit: VelocityUnit, factor: Rational = null, name: String = null, symbol: String = null ) : MomentumUnit {
    var unit = new MomentumUnit( massUnit, velocityUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( massUnit: MassUnit, velocityUnit: VelocityUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( massUnit, velocityUnit, factor, name, symbol )
  }
  
  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get VelocityUnit() : VelocityUnit {
    return RightUnit 
  }
  
  function multiply( v: VelocityUnit ) : EnergyUnit {
    return EnergyUnit.get( MassUnit * (VelocityUnit / v.TimeUnit), v.LengthUnit )
  }
    
  function divide( w: VelocityUnit ) : MassUnit {
    return MassUnit
  }  
}
