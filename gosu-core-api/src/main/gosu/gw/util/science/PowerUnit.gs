package gw.util.science
uses java.math.BigDecimal

final class PowerUnit extends AbstractProductUnit<WorkUnit, TimeUnit, Power, PowerUnit> {
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
  
  function divide( w: WorkUnit ) : TimeUnit {
    return TimeUnit
  }

  function divide( w: TimeUnit ) : WorkUnit {
    return WorkUnit
  }
}
