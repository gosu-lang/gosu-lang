package gw.util.science

uses java.math.BigDecimal 

final class Temperature extends AbstractMeasure<TemperatureUnit, Temperature> {
  /**
   * @param value Temperature in specified units
   * @param unit Temperature unit for value, default is Micromillimetres
   * @param displayUnit Unit in which to display this Temperature
   */
  construct( value: BigDecimal, unit: TemperatureUnit, displayUnit: TemperatureUnit ) {
    super( value, unit, displayUnit, TemperatureUnit.Kelvin )
  }
  construct( value: BigDecimal, unit: TemperatureUnit ) {
    this( value, unit, unit )
  }
}
