package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

final class Weight extends AbstractDim<WeightUnit, Weight> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Micromillimetres
   * @param displayUnit Unit in which to display this Length
   */
  construct( value: BigDecimal, unit: WeightUnit, displayUnit: WeightUnit ) {
    super( value * unit.BaseUnitFactor, displayUnit, WeightUnit.Micros )
  }
  construct( value : BigDecimal, unit: WeightUnit ) {
    this( value, unit, unit )
  }
  
  function multiply( a: Acceleration ) : Force {
    return new Force( toNumber() * a.toNumber(), ForceUnit.BASE, new( Unit, a.Unit ) )
  } 
  
  function multiply( r: Rate ) : Momentum {
    return new Momentum( toNumber() * r.toNumber(), MomentumUnit.BASE, new( Unit, r.Unit ) )
  } 
}
