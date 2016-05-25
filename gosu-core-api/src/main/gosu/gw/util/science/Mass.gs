package gw.util.science
uses gw.util.Rational

final class Mass extends AbstractMeasure<MassUnit, Mass> {
  construct( value: Rational, unit: MassUnit, displayUnit: MassUnit ) {
    super( value, unit, displayUnit, Kilogram )
  }
  construct( value : Rational, unit: MassUnit ) {
    this( value, unit, unit )
  }
  
  function multiply( a: Acceleration ) : Force {
    return new Force( toBaseNumber() * a.toBaseNumber(), ForceUnit.BASE, ForceUnit.get( Unit, a.Unit ) )
  } 
  
  function multiply( v: Velocity ) : Momentum {
    return new Momentum( toBaseNumber() * v.toBaseNumber(), MomentumUnit.BASE, MomentumUnit.get( Unit, v.Unit ) )
  } 
  
  function divide( area: Area ) : Pressure {
    return new Pressure( toBaseNumber() / area.toBaseNumber(), PressureUnit.BASE, PressureUnit.get( Unit, area.Unit ) )
  } 
  function divide( p: Pressure ) : Area {
    return new Area( toBaseNumber() / p.toBaseNumber(), AreaUnit.BASE, p.Unit.AreaUnit )
  }

  function divide( volume: Volume ) : Density {
    return new Density( toBaseNumber() / volume.toBaseNumber(), DensityUnit.BASE, DensityUnit.get( Unit, volume.Unit ) )
  }
  function divide( d: Density ) : Volume {
    return new Volume( toBaseNumber() / d.toBaseNumber(), VolumeUnit.BASE, d.Unit.VolumeUnit )
  }
}
