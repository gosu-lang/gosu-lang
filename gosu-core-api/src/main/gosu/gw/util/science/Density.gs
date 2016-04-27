package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Density extends AbstractMeasure<DensityUnit, Density> {
  /** 
   * @param value Density in specified units
   * @param unit Density unit, default is mg / cubic micros
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : Rational, unit: DensityUnit, displayUnit: DensityUnit ) {
    super( value, unit, displayUnit, DensityUnit.BASE )
  }
  construct( value : Rational, unit: DensityUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( w: Volume ) : Mass {
    return new Mass( toNumber() * w.toNumber(), Kilogram, Unit.MassUnit )
  }
}
