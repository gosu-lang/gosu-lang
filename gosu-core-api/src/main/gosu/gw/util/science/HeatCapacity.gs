package gw.util.science
uses gw.util.Rational

final class HeatCapacity extends AbstractMeasure<HeatCapacityUnit, HeatCapacity> {
  construct( value : Rational, unit: HeatCapacityUnit, displayUnit: HeatCapacityUnit ) {
    super( value, unit, displayUnit, HeatCapacityUnit.BASE )
  }
  construct( value : Rational, unit: HeatCapacityUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( temperature: Temperature ) : Work {
    return new Work( toNumber() * temperature.toNumber(), WorkUnit.BASE, Unit * temperature.Unit )  
  }
}