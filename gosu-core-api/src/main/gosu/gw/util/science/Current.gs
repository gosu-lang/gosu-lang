package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Current extends AbstractMeasure<CurrentUnit, Current> {
  construct( value : Rational, unit: CurrentUnit, displayUnit: CurrentUnit ) {
    super( value, unit, displayUnit, CurrentUnit.BASE )
  }
  construct( value : Rational, unit: CurrentUnit ) {
    this( value, unit, unit )
  }

  function multiply( time: Time ) : Charge {
    return new Charge( toNumber() * time.toNumber(), ChargeUnit.Coulomb )
  }

  function divide( p: Potential ) : Conductance {
    return new Conductance( toNumber() / p.toNumber(), ConductanceUnit.BASE, ConductanceUnit.get( Unit, p.Unit ) )
  }
}
