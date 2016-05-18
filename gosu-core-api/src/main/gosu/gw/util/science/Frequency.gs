package gw.util.science
uses gw.util.Rational

final class Frequency extends AbstractMeasure<FrequencyUnit, Frequency> {
  construct( value: Rational, unit: FrequencyUnit, displayUnit: FrequencyUnit ) {
    super( value, unit, displayUnit, FrequencyUnit.BASE )
  }
  construct( value : Rational, unit: FrequencyUnit ) {
    this( value, unit, unit )
  }

  function multiply( time: Time ) : Angle {
    return new Angle( toNumber() * time.toNumber(), AngleUnit.BASE, Unit.AngleUnit )
  }
}
