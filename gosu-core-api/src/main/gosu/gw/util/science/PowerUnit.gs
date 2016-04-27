package gw.util.science
uses gw.util.Rational

final class PowerUnit extends AbstractQuotientUnit<WorkUnit, TimeUnit, Power, PowerUnit> {
  public static var BASE: PowerUnit = new( WorkUnit.BASE, Second )
    
  construct( workUnit: WorkUnit, timeUnit: TimeUnit ) {
    super( workUnit, timeUnit )
  }

  property get WorkUnit() : WorkUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  }
  
  function multiply( w: TimeUnit ) : WorkUnit {
    return WorkUnit
  }
  
  function divide( v: VelocityUnit ) : ForceUnit {
    return WorkUnit.ForceUnit
  }

  function divide( force: Force ) : VelocityUnit {
    return WorkUnit.ForceUnit.AccUnit.VelocityUnit
  }
}
