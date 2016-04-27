package gw.util.science

uses gw.util.Rational

final class MomentumUnit extends AbstractProductUnit<MassUnit, VelocityUnit, Momentum, MomentumUnit> {
  public static var BASE: MomentumUnit = new( Kilogram, VelocityUnit.BASE )
  
  construct( massUnit: MassUnit, velocityUnit: VelocityUnit ) {
    super( massUnit, velocityUnit )
  }
  
  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get VelocityUnit() : VelocityUnit {
    return RightUnit 
  }
  
  function divide( w: MassUnit ) : VelocityUnit {
    return VelocityUnit
  }  
}
