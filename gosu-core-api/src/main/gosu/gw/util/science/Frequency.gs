package gw.util.science
uses java.math.BigDecimal

final class Frequency extends AbstractMeasure<FrequencyUnit, Frequency> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Hz (cycles/sec)
   * @param displayUnit Unit in which to display this Frequency
   */
  construct( value: BigDecimal, unit: FrequencyUnit, displayUnit: FrequencyUnit ) {
    super( value, unit, displayUnit, FrequencyUnit.BASE )
  }
  construct( value : BigDecimal, unit: FrequencyUnit ) {
    this( value, unit, unit )
  }
}
