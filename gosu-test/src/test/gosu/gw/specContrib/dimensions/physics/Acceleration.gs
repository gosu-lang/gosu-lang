package gw.specContrib.dimensions.physics

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Acceleration extends AbstractDim<AccelerationUnit, Acceleration> {
  /** 
   * @param value Acceleration in specified units
   * @param unit Acceleration unit, default is millis / second
   * @param displayUnit Unit in which to display this rate
   */
  construct( value : BigDecimal, unit: AccelerationUnit, displayUnit: AccelerationUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, AccelerationUnit.BASE )
  }
  construct( value: BigDecimal, unit: AccelerationUnit ) {
    this( value, unit, unit )
  }
     
  function multiply( t: Weight ) : Force {
    return new Force( toNumber() * t.toNumber(), ForceUnit.BASE, new( t.Unit, Unit ) )
  }
}