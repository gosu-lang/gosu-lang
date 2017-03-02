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
    return new Energy( toBaseNumber() * time.toBaseNumber(), EnergyUnit.BASE, Unit.EnergyUnit )
  }

  function divide( v: Velocity ) : Force {
    return new Force( toBaseNumber() / v.toBaseNumber(), ForceUnit.BASE, Unit.EnergyUnit.ForceUnit )
  }

  function divide( force: Force ) : Velocity {
    return new Velocity( toBaseNumber() / force.toBaseNumber(), VelocityUnit.BASE, Unit.EnergyUnit.ForceUnit.AccUnit.VelocityUnit )
  }

  function divide( potential: Potential ) : Current {
    return new Current( toBaseNumber() / potential.toBaseNumber(), CurrentUnit.BASE, potential.Unit.CurrentUnit )
  }

  function divide( current: Current ) : Potential {
    return new Potential( toBaseNumber() / current.toBaseNumber(), PotentialUnit.BASE, PotentialUnit.get( Unit, current.Unit ) )
  }
}
