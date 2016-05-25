package gw.util.science

uses gw.util.Rational

final class Time extends AbstractMeasure<TimeUnit, Time> {
  construct( value : Rational, unit: TimeUnit, displayUnit: TimeUnit ) {
    super( value, unit, displayUnit, TimeUnit.Second )
  }
  construct( value : Rational, unit: TimeUnit ) {
    this( value, unit, unit )
  }

  static property get Now() : Time {
    return new( Rational.get( System.nanoTime() ), Nano )
  }

  function multiply( r: Velocity ) : Length {
    return new Length( toBaseNumber() * r.toBaseNumber(), LengthUnit.BASE, r.Unit.LengthUnit )
  }

  function multiply( acc: Acceleration ) : Velocity {
    return new Velocity( toBaseNumber() * acc.toBaseNumber(), VelocityUnit.BASE, acc.Unit.VelocityUnit )
  }

  function multiply( current: Current ) : Charge {
    return new Charge( toBaseNumber() * current.toBaseNumber(), ChargeUnit.BASE, current.Unit.ChargeUnit )
  }

  function multiply( frequency: Frequency ) : Angle {
    return new Angle( toBaseNumber() * frequency.toBaseNumber(), AngleUnit.BASE, frequency.Unit.AngleUnit )
  }

  function multiply( power: Power ) : Energy {
    return new Energy( toBaseNumber() * power.toBaseNumber(), EnergyUnit.BASE, power.Unit.EnergyUnit )
  }

  function multiply( force: Force ) : Momentum {
    return new Momentum( toBaseNumber() * force.toBaseNumber(), MomentumUnit.BASE, Unit * force.Unit )
  }
}
