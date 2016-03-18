package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Pressure extends AbstractMeasure<PressureUnit, Pressure> {
  /** 
   * @param value Pressure in specified units
   * @param unit Pressure unit, default is mg / sq micros
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : BigDecimal, unit: PressureUnit, displayUnit: PressureUnit ) {
    super( value, unit, displayUnit, PressureUnit.BASE )
  }
  construct( value : BigDecimal, unit: PressureUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( w: Area ) : Mass {
    return new Mass( toNumber() * w.toNumber(), Kilogram, Unit.MassUnit )
  }
}
