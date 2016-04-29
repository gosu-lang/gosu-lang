package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Capacitance extends AbstractMeasure<CapacitanceUnit, Capacitance> {
  construct( value : Rational, unit: CapacitanceUnit, displayUnit: CapacitanceUnit ) {
    super( value, unit, displayUnit, CapacitanceUnit.BASE )
  }
  construct( value : Rational, unit: CapacitanceUnit ) {
    this( value, unit, unit )
  }

  function multiply( potential: Potential ) : Charge {
    return new Charge( toNumber() * potential.toNumber(), ChargeUnit.BaseUnit, Unit.ChargeUnit )
  }
}
