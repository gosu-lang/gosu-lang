package gw.util.science

uses java.math.BigDecimal 

final class Angle extends AbstractMeasure<AngleUnit, Angle> {
  /**
   * @param value Angle in specified units
   * @param unit Angle unit for value, default is Radian
   * @param displayUnit Unit in which to display this Angle
   */
  construct( value: BigDecimal, unit: AngleUnit, displayUnit: AngleUnit ) {
    super( value, unit, displayUnit, AngleUnit.Radian )
  }
  construct( value: BigDecimal, unit: AngleUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Frequency {
    return new Frequency( toNumber() / time.toNumber(), FrequencyUnit.BASE, new( Unit, time.Unit ) )
  }
}
