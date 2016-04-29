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
 
  function divide( w: Mass ) : Acceleration {
    return new Acceleration( toNumber() / w.toNumber(), AccelerationUnit.BASE, Unit.AccUnit )
  }
  
  function multiply( v: Velocity ) : Power {
    return new Power( toNumber() * v.toNumber(), PowerUnit.BASE, Unit * v.Unit.LengthUnit / v.Unit.TimeUnit )
  }
  
  function multiply( len: Length ) : Energy {
    return new Energy( toNumber() * len.toNumber(), EnergyUnit.BASE, Unit * len.Unit )
  }

  function multiply( t: Time ) : Momentum {
    return new Momentum( toNumber() * t.toNumber(), MomentumUnit.BASE, Unit * t.Unit )
  }
}