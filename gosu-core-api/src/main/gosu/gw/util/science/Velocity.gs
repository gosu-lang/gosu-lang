package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Velocity extends AbstractMeasure<VelocityUnit, Velocity> {
  /** 
   * @param value Velocity in specified units
   * @param unit Velocity unit, default is millis / second
   * @param displayUnit Unit in which to display this velocity
   */
  construct( value : BigDecimal, unit: VelocityUnit, displayUnit: VelocityUnit ) {
    super( value, unit, displayUnit, VelocityUnit.BASE )
  }
  construct( value : BigDecimal, unit: VelocityUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Time ) : Length {
    return new Length( toNumber() * t.toNumber(), Meter, Unit.LengthUnit )
  }
  
  function divide( t: Time ) : Acceleration {
    return new Acceleration( toNumber() / t.toNumber(), AccelerationUnit.BASE, new( Unit, t.Unit ) )
  }
}
