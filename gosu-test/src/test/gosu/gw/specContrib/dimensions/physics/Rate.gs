package gw.specContrib.dimensions.physics

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Rate extends AbstractDim<RateUnit, Rate> {
  /** 
   * @param value Rate in specified units
   * @param unit Rate unit, default is millis / second
   * @param displayUnit Unit in which to display this rate
   */
  construct( value : BigDecimal, unit: RateUnit, displayUnit: RateUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, RateUnit.BASE )
  }
  construct( value : BigDecimal, unit: RateUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Time ) : Length {
    return new Length( toNumber() * t.toNumber(), Micros, Unit.LengthUnit )
  }
  
  function divice( t: Time ) : Acceleration {
    return new Acceleration( toNumber() / t.toNumber(), AccelerationUnit.BASE, new( Unit, t.Unit ) )
  }
}
