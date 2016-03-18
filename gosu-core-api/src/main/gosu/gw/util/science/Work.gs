package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Work extends AbstractMeasure<WorkUnit, Work> {
  /** 
   * @param value Work in specified units
   * @param unit Work unit, default is default Force units * default Length units
   * @param displayUnit Unit in which to display this work
   */
  construct( value : BigDecimal, unit: WorkUnit, displayUnit: WorkUnit ) {
    super( value, unit, displayUnit, WorkUnit.BASE )
  }
  construct( value : BigDecimal, unit: WorkUnit ) {
    this( value, unit, unit )
  }
 
  function divide( f: Force ) : Length {
    return new Length( toNumber() / f.toNumber(), Meter, Unit.LengthUnit )
  }
  
  function divide( f: Length ) : Force {
    return new Force( toNumber() / f.toNumber(), ForceUnit.BASE, Unit.ForceUnit )
  }
  
  function divide( t: Time ) : Power {
    return new Power( toNumber() / t.toNumber(), PowerUnit.BASE, new( Unit, t.Unit ) )
  }
}
