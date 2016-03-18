package gw.util.science
uses java.math.BigDecimal

final class Charge extends AbstractMeasure<ChargeUnit, Charge> {
  /**
   * @param value Length in specified units
   * @param unit Length unit for value, default is Coulomb
   * @param displayUnit Unit in which to display this Charge
   */
  construct( value: BigDecimal, unit: ChargeUnit, displayUnit: ChargeUnit ) {
    super( value, unit, displayUnit, ChargeUnit.Coulomb )
  }
  construct( value : BigDecimal, unit: ChargeUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Current {
    return new Current( toNumber() / time.toNumber(), CurrentUnit.BASE, new( Unit, time.Unit ) )
  } 
}
