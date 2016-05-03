package gw.util.science
uses gw.util.Rational

final class Charge extends AbstractMeasure<ChargeUnit, Charge> {
  construct( value: Rational, unit: ChargeUnit, displayUnit: ChargeUnit ) {
    super( value, unit, displayUnit, ChargeUnit.Coulomb )
  }
  construct( value : Rational, unit: ChargeUnit ) {
    this( value, unit, unit )
  }
  
  function divide( time: Time ) : Current {
    return new Current( toNumber() / time.toNumber(), CurrentUnit.BASE, CurrentUnit.get( Unit, time.Unit ) )
  } 

  function divide( i: Current ) : Time {
    return new Time( toNumber() / i.toNumber(), TimeUnit.BASE, i.Unit.TimeUnit )
  }

  function divide( p: Potential ) : Capacitance {
    return new Capacitance( toNumber() / p.toNumber(), CapacitanceUnit.BASE, CapacitanceUnit.get( Unit, p.Unit ) )
  }
  function divide( cap: Capacitance ) : Potential {
    return new Potential( toNumber() / cap.toNumber(), PotentialUnit.BASE, cap.Unit.PotentialUnit )
  }
}
