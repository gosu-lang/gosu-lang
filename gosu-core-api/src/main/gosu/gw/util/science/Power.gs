package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Power extends AbstractMeasure<PowerUnit, Power> {
  /** 
   * @param value Power in specified units
   * @param unit Power unit, default is default Force units * default Length units
   * @param displayUnit Unit in which to display this power
   */
  construct( value : BigDecimal, unit: PowerUnit, displayUnit: PowerUnit ) {
    super( value, unit, displayUnit, PowerUnit.BASE )
  }
  construct( value : BigDecimal, unit: PowerUnit ) {
    this( value, unit, unit )
  }
 
  function divide( f: Work ) : Time {
    return new Time( toNumber() / f.toNumber(), Second, Unit.TimeUnit )
  }
  
  function divide( f: Time ) : Work {
    return new Work( toNumber() / f.toNumber(), WorkUnit.BASE, Unit.WorkUnit )
  }
}
