package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Acceleration extends AbstractMeasure<AccelerationUnit, Acceleration> {
  /** 
   * @param value Acceleration in specified units
   * @param unit Acceleration unit, default is millis / second
   * @param displayUnit Unit in which to display this acceleration
   */
  construct( value : Rational, unit: AccelerationUnit, displayUnit: AccelerationUnit ) {
    super( value, unit, displayUnit, AccelerationUnit.BASE )
  }
  construct( value: Rational, unit: AccelerationUnit ) {
    this( value, unit, unit )
  }
     
  function multiply( t: Mass ) : Force {
    return new Force( toNumber() * t.toNumber(), ForceUnit.BASE, new( t.Unit, Unit ) )
  }
}