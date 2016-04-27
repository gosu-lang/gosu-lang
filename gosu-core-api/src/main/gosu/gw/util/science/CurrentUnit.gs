package gw.util.science
uses gw.util.Rational

final class CurrentUnit extends AbstractQuotientUnit<ChargeUnit, TimeUnit, Current, CurrentUnit> {
  public static var BASE: CurrentUnit = new( Coulomb, Second ) // Amperes
  
  construct( chargeUnit: ChargeUnit, timeUnit: TimeUnit ) {
    super( chargeUnit, timeUnit )
  }

  property get ChargeUnit() : ChargeUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  }
  
  function multiply( w: TimeUnit ) : ChargeUnit {
    return ChargeUnit
  }
}
