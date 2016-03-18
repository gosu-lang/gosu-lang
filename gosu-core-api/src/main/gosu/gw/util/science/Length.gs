package gw.util.science

uses java.math.BigDecimal 

final class Length extends AbstractMeasure<LengthUnit, Length> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Micromillimetres
   * @param displayUnit Unit in which to display this Length
   */
  construct( value: BigDecimal, unit: LengthUnit, displayUnit: LengthUnit ) {
    super( value, unit, displayUnit, Meter )
  }
  construct( value: BigDecimal, unit: LengthUnit ) {
    this( value, unit, unit )
  }

  function divide( t: Time ) : Velocity {
    return new Velocity( toNumber() / t.toNumber(), VelocityUnit.BASE, new( Unit, t.Unit ) )
  } 
  function divide( v: Velocity ) : Time {
    return new Time( toNumber() / v.toNumber(), Second, v.Unit.TimeUnit )
  }
  
  function multiply( len: Length ) : Area {
    return new Area( toNumber() * len.toNumber(), AreaUnit.BASE, new( Unit, len.Unit ) )
  }
  
  function multiply( area: Area ) : Volume {
    return new Volume( toNumber() * area.toNumber(), VolumeUnit.BASE, new( area.Unit, Unit ) )
  }
  
  function multiply( force: Force ) : Work {
    return new Work( toNumber() * force.toNumber(), WorkUnit.BASE, new( force.Unit, Unit ) ) 
  }
}
