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
 
  function multiply( time: Time ) : Energy {
    return new Energy( toNumber() * time.toNumber(), EnergyUnit.BASE, Unit.EnergyUnit )
  }

  function divide( v: Velocity ) : Force {
    return new Force( toNumber() / v.toNumber(), ForceUnit.BASE, Unit.EnergyUnit.ForceUnit )
  }

  function divide( force: Force ) : Velocity {
    return new Velocity( toNumber() / force.toNumber(), VelocityUnit.BASE, Unit.EnergyUnit.ForceUnit.AccUnit.VelocityUnit )
  }

  function divide( potential: Potential ) : Current {
    return new Current( toNumber() / potential.toNumber(), CurrentUnit.BASE, potential.Unit.CurrentUnit )
  }

  function divide( current: Current ) : Potential {
    return new Potential( toNumber() / current.toNumber(), PotentialUnit.BASE, PotentialUnit.get( Unit, current.Unit ) )
  }
}
