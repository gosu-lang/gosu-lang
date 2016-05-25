package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Current extends AbstractMeasure<CurrentUnit, Current> {
  construct( value : Rational, unit: CurrentUnit, displayUnit: CurrentUnit ) {
    super( value, unit, displayUnit, CurrentUnit.BASE )
  }
  construct( value : Rational, unit: CurrentUnit ) {
    this( value, unit, unit )
  }

  function multiply( time: Time ) : Charge {
    return new Charge( toBaseNumber() * time.toBaseNumber(), ChargeUnit.Coulomb )
  }

  function divide( p: Potential ) : Conductance {
    return new Conductance( toBaseNumber() / p.toBaseNumber(), ConductanceUnit.BASE, ConductanceUnit.get( Unit, p.Unit ) )
  }
  function divide( c: Conductance ) : Potential {
    return new Potential( toBaseNumber() / c.toBaseNumber(), PotentialUnit.BASE, c.Unit.PotentialUnit )
  }
}
