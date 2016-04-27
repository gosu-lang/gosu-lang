package gw.util.science

uses gw.util.Rational

final class AccelerationUnit extends AbstractQuotientUnit<VelocityUnit, TimeUnit, Acceleration, AccelerationUnit> {
  public static var BASE: AccelerationUnit = new( VelocityUnit.BASE, VelocityUnit.BASE.TimeUnit )
  
  construct( velocityUnit: VelocityUnit, timeUnit: TimeUnit ) {
    super( velocityUnit, timeUnit )
  }
  
  property get VelocityUnit() : VelocityUnit {
    return LeftUnit
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit
  }  
  
  override property get UnitName() : String {
    return VelocityUnit.TimeUnit === TimeUnit 
           ? VelocityUnit.LengthUnit.UnitName + "/" + TimeUnit.UnitName + "\u00B2"
           : VelocityUnit.UnitName + "/" + TimeUnit.UnitName
  }
  
  override property get UnitSymbol() : String {
    return VelocityUnit.TimeUnit === TimeUnit 
           ? VelocityUnit.LengthUnit.UnitSymbol + "/" + TimeUnit.UnitSymbol + "\u00B2"
           : VelocityUnit.UnitSymbol + "/" + TimeUnit.UnitSymbol
  }
    
  function postfixBind( mass: MassUnit ) : ForceUnit {
    return multiply( mass )
  }
  
  function multiply( t: MassUnit ) : ForceUnit {
    return new ForceUnit( t, this )
  }  
}
