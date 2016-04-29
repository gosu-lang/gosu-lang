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
 
  function multiply( v: Velocity ) : Work {
    return new Work( toNumber() * v.toNumber(), WorkUnit.BASE, Unit * v.Unit )  
  }
  
  function divide( w: Mass ) : Velocity {
    return new Velocity( toNumber() / w.toNumber(), VelocityUnit.BASE, Unit.VelocityUnit )
  }
}