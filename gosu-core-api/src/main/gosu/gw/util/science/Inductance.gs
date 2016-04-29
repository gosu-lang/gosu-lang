package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Inductance extends AbstractMeasure<InductanceUnit, Inductance> {
  construct( value : Rational, unit: InductanceUnit, displayUnit: InductanceUnit ) {
    super( value, unit, displayUnit, InductanceUnit.BASE )
  }
  construct( value : Rational, unit: InductanceUnit ) {
    this( value, unit, unit )
  }

  function divide( time: Time ) : Resistance {
    return new Resistance( toNumber() / time.toNumber(), ResitanceUnit.BASE, Unit.ResistanceUnit )
  }
  function divide( resistance: Resistance ) : Time {
    return new Time( toNumber() / resistance.toNumber(), TimeUnit.BASE, Unit.TimeUnit )
  }
}
