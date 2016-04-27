package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Power extends AbstractMeasure<PowerUnit, Power> {
  construct( value : Rational, unit: PowerUnit, displayUnit: PowerUnit ) {
    super( value, unit, displayUnit, PowerUnit.BASE )
  }
  construct( value : Rational, unit: PowerUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( time: Time ) : Work {
    return new Work( toNumber() * time.toNumber(), WorkUnit.BASE, Unit.WorkUnit )
  }

  function divide( v: Velocity ) : Force {
    return new Force( toNumber() / v.toNumber(), ForceUnit.BASE, Unit.WorkUnit.ForceUnit )
  }

  function divide( force: Force ) : Velocity {
    return new Velocity( toNumber() / force.toNumber(), VelocityUnit.BASE, Unit.WorkUnit.ForceUnit.AccUnit.VelocityUnit )
  }
}
