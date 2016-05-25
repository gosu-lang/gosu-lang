package gw.util.science

uses gw.util.Rational

final class Temperature extends AbstractMeasure<TemperatureUnit, Temperature> {
  construct( value: Rational, unit: TemperatureUnit, displayUnit: TemperatureUnit ) {
    super( value, unit, displayUnit, TemperatureUnit.Kelvin )
  }
  construct( value: Rational, unit: TemperatureUnit ) {
    this( value, unit, unit )
  }
  
  function multiply( c: HeatCapacity ) : Energy {
    return new Energy( toBaseNumber() * c.toBaseNumber(), EnergyUnit.BASE, c.Unit.EnergyUnit )
  }  
}
