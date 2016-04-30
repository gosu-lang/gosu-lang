package gw.util.science

uses gw.util.Rational

final class ForceUnit extends AbstractProductUnit<MassUnit, AccelerationUnit, Force, ForceUnit> {
  final static var CACHE: UnitCache<ForceUnit> = new UnitCache()

  public static var N: ForceUnit = get( Kilogram, AccelerationUnit.BASE, 1, "Newton", "N" )

  public static var BASE: ForceUnit = N

  static function get( massUnit: MassUnit, accUnit: AccelerationUnit, factor: Rational = null, name: String = null, symbol: String = null ) : ForceUnit {
    var unit = new ForceUnit( massUnit, accUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
    
  private construct( massUnit: MassUnit, accUnit: AccelerationUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( massUnit, accUnit, factor, name, symbol )
  }

  property get MassUnit() : MassUnit {
    return LeftUnit
  }
  property get AccUnit() : AccelerationUnit {
    return RightUnit 
  }
        
  function multiply( v: VelocityUnit ) : PowerUnit {
    return PowerUnit.get( this * v.LengthUnit, v.TimeUnit )
  }
    
  function multiply( len: LengthUnit ) : EnergyUnit {
    return EnergyUnit.get( this, len )
  }
  
  function multiply( t: TimeUnit ) : MomentumUnit {
    return MomentumUnit.get( MassUnit, VelocityUnit.get( AccUnit.VelocityUnit.LengthUnit, t ) )
  }
  
  function divide( acc: AccelerationUnit ) : MassUnit {
    return MassUnit 
  }
}
