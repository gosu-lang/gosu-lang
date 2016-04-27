package gw.util.science
uses gw.util.Rational

final class WorkUnit extends AbstractProductUnit<ForceUnit, LengthUnit, Work, WorkUnit> {
  public static var BASE: WorkUnit = new( ForceUnit.BASE, Meter )
  
  construct( forceUnit: ForceUnit, lengthUnit: LengthUnit ) {
    super( forceUnit, lengthUnit )
  }

  property get ForceUnit() : ForceUnit {
    return LeftUnit 
  }
  property get LengthUnit() : LengthUnit {
    return RightUnit 
  }
  
  function divide( w: ForceUnit ) : LengthUnit {
    return LengthUnit
  }

  function divide( w: LengthUnit ) : ForceUnit {
    return ForceUnit
  }
  
  function divide( time: TimeUnit ) : PowerUnit {
    return new( this, time )
  }

  function divide( power: PowerUnit ) : TimeUnit {
    return power.TimeUnit
  }
}
