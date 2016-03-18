package gw.util.science
uses java.math.BigDecimal

final class VelocityUnit extends AbstractQuotientUnit<LengthUnit, TimeUnit, Velocity, VelocityUnit> {
  public static var BASE: VelocityUnit = new( Meter, Second )
  
  construct( lengthUnit: LengthUnit, timeUnit: TimeUnit ) {
    super( lengthUnit, timeUnit )
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
    return new MomentumUnit( t, this )
  }
    
  function divide( t: TimeUnit ) : AccelerationUnit {
    return new AccelerationUnit( this, t )
  }
}
