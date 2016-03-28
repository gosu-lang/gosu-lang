package gw.util.science

uses java.math.BigDecimal

final class Time extends AbstractMeasure<TimeUnit, Time> {
  /**
   * @param value Time period in specified units
   * @param unit Unit of time vor value
   * @param displayUnit Unit of time in which to display this Time
   */
  construct( value : BigDecimal, unit: TimeUnit, displayUnit: TimeUnit ) {
    super( value, unit, displayUnit, TimeUnit.Second )
  }
  construct( value : BigDecimal, unit: TimeUnit ) {
    this( value, unit, unit )
  }

  static property get Now() : Time {
    return new( System.nanoTime(), Nanos )
  }
  
  override function fromNumber( p0: BigDecimal ) : Time {
    return new Time( p0, Second, Unit )
  }
    
  function multiply( r: Velocity ) : Length {
    return new Length( toNumber() * r.toNumber(), Meter, r.Unit.LengthUnit )
  }
}
