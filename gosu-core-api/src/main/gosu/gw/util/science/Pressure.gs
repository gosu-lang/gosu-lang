package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Pressure extends AbstractMeasure<PressureUnit, Pressure> {
  construct( value : Rational, unit: PressureUnit, displayUnit: PressureUnit ) {
    super( value, unit, displayUnit, PressureUnit.BASE )
  }
  construct( value : Rational, unit: PressureUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( w: Area ) : Mass {
    return new Mass( toBaseNumber() * w.toBaseNumber(), MassUnit.BASE, Unit.MassUnit )
  }
}
