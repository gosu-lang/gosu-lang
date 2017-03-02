package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Acceleration extends AbstractMeasure<AccelerationUnit, Acceleration> {
  construct( value : Rational, unit: AccelerationUnit, displayUnit: AccelerationUnit ) {
    super( value, unit, displayUnit, AccelerationUnit.BASE )
  }
  construct( value: Rational, unit: AccelerationUnit ) {
    this( value, unit, unit )
  }
     
  function multiply( mass: Mass ) : Force {
    return new Force( toBaseNumber() * mass.toBaseNumber(), ForceUnit.BASE, ForceUnit.get( mass.Unit, Unit ) )
  }

  function multiply( time: Time ) : Velocity {
    return new Velocity( toBaseNumber() * time.toBaseNumber(), VelocityUnit.BASE, Unit.VelocityUnit )
  }
}