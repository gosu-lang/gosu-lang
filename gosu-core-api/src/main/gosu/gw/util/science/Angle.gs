package gw.util.science

uses gw.util.Rational

final class Angle extends AbstractMeasure<AngleUnit, Angle> {
  construct( value: Rational, unit: AngleUnit, displayUnit: AngleUnit ) {
    super( value, unit, displayUnit, AngleUnit.BASE )
  }
  construct( value: Rational, unit: AngleUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Frequency {
    return new Frequency( toBaseNumber() / time.toBaseNumber(), FrequencyUnit.BASE, FrequencyUnit.get( Unit, time.Unit ) )
  }

  function divide( freq: Frequency ) : Time {
    return new Time( toBaseNumber() / freq.toBaseNumber(), TimeUnit.BASE, freq.Unit.TimeUnit )
  }
}
