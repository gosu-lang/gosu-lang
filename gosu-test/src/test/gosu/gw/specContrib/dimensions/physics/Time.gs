package gw.specContrib.dimensions.physics

uses java.math.BigDecimal

final class Time extends AbstractDim<TimeUnit, Time> {
  /**
   * @param value Time period in specified units
   * @param unit Unit of time vor value
   * @param displayUnit Unit of time in which to display this Time
   */
  construct( value : BigDecimal, unit: TimeUnit, displayUnit: TimeUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, TimeUnit.Nanos )
  }
  construct( value : BigDecimal, unit: TimeUnit ) {
    this( value, unit, unit )
  }

  override function fromNumber( p0: BigDecimal ) : Time {
    return new Time( p0, Nanos, Unit )
  }
    
  function multiply( r: Rate ) : Length {
    return new Length( toNumber() * r.toNumber(), Micros, r.Unit.LengthUnit )
  }
}
