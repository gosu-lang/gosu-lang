package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Potential extends AbstractMeasure<PotentialUnit, Potential> {
  construct( value : Rational, unit: PotentialUnit, displayUnit: PotentialUnit ) {
    super( value, unit, displayUnit, PotentialUnit.BASE )
  }
  construct( value : Rational, unit: PotentialUnit ) {
    this( value, unit, unit )
  }

  function multiply( current: Current ) : Power {
    return new Power( toNumber() * current.toNumber(), PowerUnit.BASE, Unit.PowerUnit )
  }
}
