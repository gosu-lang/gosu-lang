package gw.util.science
uses gw.util.Rational

final class Charge extends AbstractMeasure<ChargeUnit, gw.util.science.Charge> { //to prevent Type Parameter naming conflict with Guidewire's entity.Charge type
  construct( value: Rational, unit: ChargeUnit, displayUnit: ChargeUnit ) {
    super( value, unit, displayUnit, ChargeUnit.Coulomb )
  }
  construct( value : Rational, unit: ChargeUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Current {
    return new Current( toBaseNumber() / time.toBaseNumber(), CurrentUnit.BASE, CurrentUnit.get( Unit, time.Unit ) )
  } 

  function divide( i: Current ) : Time {
    return new Time( toBaseNumber() / i.toBaseNumber(), TimeUnit.BASE, i.Unit.TimeUnit )
  }

  function divide( p: Potential ) : Capacitance {
    return new Capacitance( toBaseNumber() / p.toBaseNumber(), CapacitanceUnit.BASE, CapacitanceUnit.get( Unit, p.Unit ) )
  }
  function divide( cap: Capacitance ) : Potential {
    return new Potential( toBaseNumber() / cap.toBaseNumber(), PotentialUnit.BASE, cap.Unit.PotentialUnit )
  }
}
