package gw.util.science
uses gw.util.Rational

final class Frequency extends AbstractMeasure<FrequencyUnit, Frequency> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Hz (cycles/sec)
   * @param displayUnit Unit in which to display this Frequency
   */
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
