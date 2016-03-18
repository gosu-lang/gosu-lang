package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Area extends AbstractMeasure<AreaUnit, Area> {
  /** 
   * @param value Area in specified units
   * @param unit Area unit, default is Sq Micro
   * @param displayUnit Unit in which to display this area
   */
  construct( value : BigDecimal, unit: AreaUnit, displayUnit: AreaUnit ) {
    super( value, unit, displayUnit, AreaUnit.BASE )
  }
  construct( value : BigDecimal, unit: AreaUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Length ) : Volume {
    return new Volume( toNumber() * t.toNumber(), new( Unit, t.Unit ) )
  }
  
  function divide( t: Length ) : Length {
    return new Length( toNumber() / t.toNumber(), Meter, t.Unit )
  }
}
