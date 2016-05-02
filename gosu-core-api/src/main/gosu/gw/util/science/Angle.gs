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
    return new Frequency( toNumber() / time.toNumber(), FrequencyUnit.BASE, FrequencyUnit.get( Unit, time.Unit ) )
  }
}
