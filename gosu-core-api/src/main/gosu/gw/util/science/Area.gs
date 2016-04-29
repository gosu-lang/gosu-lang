package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Area extends AbstractMeasure<AreaUnit, Area> {
  /** 
   * @param value Area in specified units
   * @param unit Area unit, default is Sq Micro
   * @param displayUnit Unit in which to display this area
   */
  construct( value : Rational, unit: AreaUnit, displayUnit: AreaUnit ) {
    super( value, unit, displayUnit, AreaUnit.BASE )
  }
  construct( value : Rational, unit: AreaUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Length ) : Volume {
    return new Volume( toNumber() * t.toNumber(), VolumeUnit.get( t.Unit, Unit ) )
  }
  
  function divide( t: Length ) : Length {
    return new Length( toNumber() / t.toNumber(), Meter, t.Unit )
  }
}
