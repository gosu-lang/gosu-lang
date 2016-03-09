package gw.specContrib.dimensions.physics

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Momentum extends AbstractDim<MomentumUnit, Momentum> {
  /** 
   * @param value Momentum in specified units
   * @param unit Momentum unit, default is millis / second
   * @param displayUnit Unit in which to display this rate
   */
  construct( value : BigDecimal, unit: MomentumUnit, displayUnit: MomentumUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, MomentumUnit.BASE )
  }
  construct( value : BigDecimal, unit: MomentumUnit ) {
    this( value, unit, unit )
  }
 
  function divide( w: Weight ) : Rate {
    return new Rate( toNumber() / w.toNumber(), RateUnit.BASE, Unit.RateUnit )
  }
}