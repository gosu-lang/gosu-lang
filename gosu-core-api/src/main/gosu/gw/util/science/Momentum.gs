package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Momentum extends AbstractMeasure<MomentumUnit, Momentum> {
  /** 
   * @param value Momentum in specified units
   * @param unit Momentum unit, default is millis / second
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : BigDecimal, unit: MomentumUnit, displayUnit: MomentumUnit ) {
    super( value, unit, displayUnit, MomentumUnit.BASE )
  }
  construct( value : BigDecimal, unit: MomentumUnit ) {
    this( value, unit, unit )
  }
 
  function divide( w: Mass ) : Velocity {
    return new Velocity( toNumber() / w.toNumber(), VelocityUnit.BASE, Unit.VelocityUnit )
  }
}