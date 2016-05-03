package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Momentum extends AbstractMeasure<MomentumUnit, Momentum> {
  construct( value : Rational, unit: MomentumUnit, displayUnit: MomentumUnit ) {
    super( value, unit, displayUnit, MomentumUnit.BASE )
  }
  construct( value : Rational, unit: MomentumUnit ) {
    this( value, unit, unit )
  }
 
  function multiply( v: Velocity ) : Energy {
    return new Energy( toNumber() * v.toNumber(), EnergyUnit.BASE, Unit * v.Unit )
  }
  
  function divide( mass: Mass ) : Velocity {
    return new Velocity( toNumber() / mass.toNumber(), VelocityUnit.BASE, Unit.VelocityUnit )
  }
  function divide( v: Velocity ) : Mass {
    return new Mass( toNumber() / v.toNumber(), MassUnit.BASE, Unit.MassUnit )
  }
}