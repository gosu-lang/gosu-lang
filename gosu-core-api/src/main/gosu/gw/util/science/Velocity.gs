package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Velocity extends AbstractMeasure<VelocityUnit, Velocity> {
  construct( value : Rational, unit: VelocityUnit, displayUnit: VelocityUnit ) {
    super( value, unit, displayUnit, VelocityUnit.BASE )
  }
  construct( value : Rational, unit: VelocityUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Time ) : Length {
    return new Length( toNumber() * t.toNumber(), Meter, Unit.LengthUnit )
  }

  function multiply( mass: Mass ) : Momentum {
    return new Momentum( toNumber() * mass.toNumber(), MomentumUnit.BASE, new( mass.Unit, Unit ) )
  }

  function multiply( force: Force ) : Power {
    return new Power( toNumber() * force.toNumber(), PowerUnit.BASE, Unit * force.Unit )
  }
  
  function divide( t: Time ) : Acceleration {
    return new Acceleration( toNumber() / t.toNumber(), AccelerationUnit.BASE, new( Unit, t.Unit ) )
  }  
}
