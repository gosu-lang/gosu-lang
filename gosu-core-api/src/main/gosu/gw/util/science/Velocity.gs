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
    return new Length( toBaseNumber() * t.toBaseNumber(), LengthUnit.BASE, Unit.LengthUnit )
  }

  function multiply( mass: Mass ) : Momentum {
    return new Momentum( toBaseNumber() * mass.toBaseNumber(), MomentumUnit.BASE, MomentumUnit.get( mass.Unit, Unit ) )
  }

  function multiply( force: Force ) : Power {
    return new Power( toBaseNumber() * force.toBaseNumber(), PowerUnit.BASE, Unit * force.Unit )
  }
  
  function divide( t: Time ) : Acceleration {
    return new Acceleration( toBaseNumber() / t.toBaseNumber(), AccelerationUnit.BASE, AccelerationUnit.get( Unit, t.Unit ) )
  }  
  function divide( acc: Acceleration ) : Time {
    return new Time( toBaseNumber() / acc.toBaseNumber(), TimeUnit.BASE, acc.Unit.TimeUnit )
  }
}
