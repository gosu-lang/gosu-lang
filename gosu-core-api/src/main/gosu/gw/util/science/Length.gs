package gw.util.science

uses gw.util.Rational

final class Length extends AbstractMeasure<LengthUnit, Length> {
  construct( value: Rational, unit: LengthUnit, displayUnit: LengthUnit ) {
    super( value, unit, displayUnit, Meter )
  }
  construct( value: Rational, unit: LengthUnit ) {
    this( value, unit, unit )
  }

  function divide( t: Time ) : Velocity {
    return new Velocity( toNumber() / t.toNumber(), VelocityUnit.BASE, VelocityUnit.get( Unit, t.Unit ) )
  } 
  function divide( v: Velocity ) : Time {
    return new Time( toNumber() / v.toNumber(), Second, v.Unit.TimeUnit )
  }
  
  function multiply( len: Length ) : Area {
    return new Area( toNumber() * len.toNumber(), AreaUnit.BASE, AreaUnit.get( Unit, len.Unit ) )
  }
  
  function multiply( area: Area ) : Volume {
    return new Volume( toNumber() * area.toNumber(), VolumeUnit.BASE, VolumeUnit.get( Unit, area.Unit ) )
  }
  
  function multiply( force: Force ) : Work {
    return new Work( toNumber() * force.toNumber(), WorkUnit.BASE, WorkUnit.get( force.Unit, Unit ) )
  }
}
