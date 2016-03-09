package gw.specContrib.dimensions.physics

uses java.math.BigDecimal 

final class Length extends AbstractDim<LengthUnit, Length> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Micromillimetres
   * @param displayUnit Unit in which to display this Length
   */
  construct( value: BigDecimal, unit: LengthUnit, displayUnit: LengthUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, LengthUnit.Micros )
  }
  construct( value: BigDecimal, unit: LengthUnit ) {
    this( value, unit, unit )
  }

  function divide( t: Time ) : Rate {
    return new Rate( toNumber() / t.toNumber(), RateUnit.BASE, new( Unit, t.Unit ) )
  } 
  function divide( r: Rate ) : Time {
    return new Time( toNumber() / r.toNumber(), Nanos, r.Unit.TimeUnit )
  }
}
