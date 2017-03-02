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
    return new Velocity( toBaseNumber() / t.toBaseNumber(), VelocityUnit.BASE, VelocityUnit.get( Unit, t.Unit ) )
  } 
  function divide( v: Velocity ) : Time {
    return new Time( toBaseNumber() / v.toBaseNumber(), TimeUnit.BASE, v.Unit.TimeUnit )
  }
  
  function multiply( len: Length ) : Area {
    return new Area( toBaseNumber() * len.toBaseNumber(), AreaUnit.BASE, AreaUnit.get( Unit, len.Unit ) )
  }
  
  function multiply( area: Area ) : Volume {
    return new Volume( toBaseNumber() * area.toBaseNumber(), VolumeUnit.BASE, VolumeUnit.get( Unit, area.Unit ) )
  }
  
  function multiply( force: Force ) : Energy {
    return new Energy( toBaseNumber() * force.toBaseNumber(), EnergyUnit.BASE, EnergyUnit.get( force.Unit, Unit ) )
  }
}
