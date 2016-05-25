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
    return new Power( toBaseNumber() * current.toBaseNumber(), PowerUnit.BASE, Unit.PowerUnit )
  }

  function divide( i: Current ) : Resistance {
    return new Resistance( toBaseNumber() / i.toBaseNumber(), ResistanceUnit.BASE, ResistanceUnit.get( Unit, i.Unit ) )
  }
  function divide( r: Resistance ) : Current {
    return new Current( toBaseNumber() / r.toBaseNumber(), CurrentUnit.BASE, r.Unit.CurrentUnit )
  }
}
