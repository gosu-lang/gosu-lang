package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Conductance extends AbstractMeasure<ConductanceUnit, Conductance> {
  construct( value : Rational, unit: ConductanceUnit, displayUnit: ConductanceUnit ) {
    super( value, unit, displayUnit, ConductanceUnit.BASE )
  }
  construct( value : Rational, unit: ConductanceUnit ) {
    this( value, unit, unit )
  }

  function multiply( potential: Potential ) : Current {
    return new Current( toBaseNumber() * potential.toBaseNumber(), CurrentUnit.BASE, Unit.CurrentUnit )
  }
}
