package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Current extends AbstractMeasure<CurrentUnit, Current> {
  /** 
   * @param value Current in specified units
   * @param unit Current unit, default is coulomb / second
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : Rational, unit: CurrentUnit, displayUnit: CurrentUnit ) {
    super( value, unit, displayUnit, CurrentUnit.BASE )
  }
  construct( value : Rational, unit: CurrentUnit ) {
    this( value, unit, unit )
  }

  function multiply( time: Time ) : Charge {
    return new Charge( toNumber() * time.toNumber(), ChargeUnit.Coulomb )
  }
}
