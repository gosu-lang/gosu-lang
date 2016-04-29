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

  override function fromNumber( p0: Rational ) : Time {
    return new Time( p0, Second, Unit )
  }
    
  function multiply( r: Velocity ) : Length {
    return new Length( toNumber() * r.toNumber(), LengthUnit.BaseUnit, r.Unit.LengthUnit )
  }

  function multiply( acc: Acceleration ) : Velocity {
    return new Velocity( toNumber() * acc.toNumber(), VelocityUnit.BASE, acc.Unit.VelocityUnit )
  }

  function multiply( current: Current ) : Charge {
    return new Charge( toNumber() * current.toNumber(), ChargeUnit.Coulomb )
  }

  function multiply( frequency: Frequency ) : Angle {
    return new Angle( toNumber() * frequency.toNumber(), AngleUnit.BaseUnit, frequency.Unit.AngleUnit )
  }

  function multiply( power: Power ) : Work {
    return new Work( toNumber() * power.toNumber(), WorkUnit.BASE, power.Unit.WorkUnit )
  }

  function multiply( force: Force ) : Momentum {
    return new Momentum( toNumber() * force.toNumber(), MomentumUnit.BASE, Unit * force.Unit )
  }
}
