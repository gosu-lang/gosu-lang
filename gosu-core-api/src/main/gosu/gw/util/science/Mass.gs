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
    return new Force( toNumber() * a.toNumber(), ForceUnit.BASE, new( Unit, a.Unit ) )
  } 
  
  function multiply( v: Velocity ) : Momentum {
    return new Momentum( toNumber() * v.toNumber(), MomentumUnit.BASE, new( Unit, v.Unit ) )
  } 
  
  function divide( area: Area ) : Pressure {
    return new Pressure( toNumber() / area.toNumber(), PressureUnit.BASE, new( Unit, area.Unit ) )
  } 

  function divide( volume: Volume ) : Density {
    return new Density( toNumber() / volume.toNumber(), DensityUnit.BASE, new( Unit, volume.Unit ) )
  } 
}
