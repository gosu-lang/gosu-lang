package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Force extends AbstractMeasure<ForceUnit, Force> {
  /** 
   * @param value Force in specified units
   * @param unit Force unit, default is millis / second
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : BigDecimal, unit: ForceUnit, displayUnit: ForceUnit ) {
    super( value, unit, displayUnit, ForceUnit.BASE )
  }
  construct( value : BigDecimal, unit: ForceUnit ) {
    this( value, unit, unit )
  }
 
  function divide( w: Mass ) : Acceleration {
    return new Acceleration( toNumber() / w.toNumber(), AccelerationUnit.BASE, Unit.AccUnit )
  }
  
  function multiply( len: Length ) : Work {
    return new Work( toNumber() * len.toNumber(), WorkUnit.BASE, new( Unit, len.Unit ) ) 
  }
}