package gw.util.science
uses java.math.BigDecimal

final class Mass extends AbstractMeasure<MassUnit, Mass> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Micromillimetres
   * @param displayUnit Unit in which to display this Length
   */
  construct( value: BigDecimal, unit: MassUnit, displayUnit: MassUnit ) {
    super( value, unit, displayUnit, Kilogram )
  }
  construct( value : BigDecimal, unit: MassUnit ) {
    this( value, unit, unit )
  }
  
  function multiply( a: Acceleration ) : Force {
    return new Force( toNumber() * a.toNumber(), ForceUnit.BASE, new( Unit, a.Unit ) )
  } 
  
  function multiply( r: Velocity ) : Momentum {
    return new Momentum( toNumber() * r.toNumber(), MomentumUnit.BASE, new( Unit, r.Unit ) )
  } 
  
  function divide( area: Area ) : Pressure {
    return new Pressure( toNumber() / area.toNumber(), PressureUnit.BASE, new( Unit, area.Unit ) )
  } 

  function divide( volume: Volume ) : Density {
    return new Density( toNumber() / volume.toNumber(), DensityUnit.BASE, new( Unit, volume.Unit ) )
  } 
}
