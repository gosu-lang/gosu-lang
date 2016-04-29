package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Resistance extends AbstractMeasure<ResistanceUnit, Resistance> {
  construct( value : Rational, unit: ResistanceUnit, displayUnit: ResistanceUnit ) {
    super( value, unit, displayUnit, ResistanceUnit.BASE )
  }
  construct( value : Rational, unit: ResistanceUnit ) {
    this( value, unit, unit )
  }

  function multiply( current: Current ) : Potential {
    return new Potential( toNumber() * current.toNumber(), PotentialUnit.BASE, Unit.PotentialUnit )
  }
}
