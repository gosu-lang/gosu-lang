package gw.util.science

uses gw.util.Rational

final class ForceUnit extends AbstractProductUnit<MassUnit, AccelerationUnit, Force, ForceUnit> {
  public static var BASE: ForceUnit = new ( Kilogram, AccelerationUnit.BASE )
    
  construct( massUnit: MassUnit, accUnit: AccelerationUnit ) {
    super( massUnit, accUnit )
  }

  property get MassUnit() : MassUnit {
    return LeftUnit
  }
  property get AccUnit() : AccelerationUnit {
    return RightUnit 
  }
        
  function multiply( v: VelocityUnit ) : PowerUnit {
    return new( this * v.LengthUnit, v.TimeUnit )
  }
    
  function multiply( len: LengthUnit ) : WorkUnit {
    return new( this, len )
  }
  
  function divide( w: MassUnit ) : AccelerationUnit {
    return AccUnit
  }  
}
