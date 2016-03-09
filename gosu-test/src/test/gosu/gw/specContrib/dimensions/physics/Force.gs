package gw.specContrib.dimensions.physics

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Force extends AbstractDim<ForceUnit, Force> {
  /** 
   * @param value Force in specified units
   * @param unit Force unit, default is millis / second
   * @param displayUnit Unit in which to display this rate
   */
  construct( value : BigDecimal, unit: ForceUnit, displayUnit: ForceUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, ForceUnit.BASE )
  }
  construct( value : BigDecimal, unit: ForceUnit ) {
    this( value, unit, unit )
  }
 
  function divide( w: Weight ) : Acceleration {
    return new Acceleration( toNumber() / w.toNumber(), AccelerationUnit.BASE, Unit.AccUnit )
  }
}