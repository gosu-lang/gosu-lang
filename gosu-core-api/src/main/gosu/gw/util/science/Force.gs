package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Force extends AbstractMeasure<ForceUnit, Force> {
  construct( value: Rational, unit: ForceUnit, displayUnit: ForceUnit ) {
    super( value, unit, displayUnit, ForceUnit.BASE )
  }
  construct( value: Rational, unit: ForceUnit ) {
    this( value, unit, unit )
  }

  function multiply( v: Velocity ) : Power {
    return new Power( toBaseNumber() * v.toBaseNumber(), PowerUnit.BASE, Unit * v.Unit.LengthUnit / v.Unit.TimeUnit )
  }
  
  function multiply( len: Length ) : Energy {
    return new Energy( toBaseNumber() * len.toBaseNumber(), EnergyUnit.BASE, Unit * len.Unit )
  }

  function multiply( t: Time ) : Momentum {
    return new Momentum( toBaseNumber() * t.toBaseNumber(), MomentumUnit.BASE, Unit * t.Unit )
  }

  function divide( w: Mass ) : Acceleration {
    return new Acceleration( toBaseNumber() / w.toBaseNumber(), AccelerationUnit.BASE, Unit.AccUnit )
  }
  function divide( acc: Acceleration ) : Mass {
    return new Mass( toBaseNumber() / acc.toBaseNumber(), MassUnit.BASE, Unit.MassUnit )
  }
}