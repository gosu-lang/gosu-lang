package gw.util.science

uses gw.util.Rational

final class Angle extends AbstractMeasure<AngleUnit, Angle> {
  /**
   * @param value Angle in specified units
   * @param unit Angle unit for value, default is Radian
   * @param displayUnit Unit in which to display this Angle
   */
  construct( value: Rational, unit: AngleUnit, displayUnit: AngleUnit ) {
    super( value, unit, displayUnit, AngleUnit.Radian )
  }
  construct( value: Rational, unit: AngleUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Frequency {
    return new Frequency( toNumber() / time.toNumber(), FrequencyUnit.BASE, FrequencyUnit.get( Unit, time.Unit ) )
  }
}
