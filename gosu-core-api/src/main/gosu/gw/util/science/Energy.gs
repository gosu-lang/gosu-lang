package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Energy extends AbstractMeasure<EnergyUnit, Energy> {
  construct( value : Rational, unit: EnergyUnit, displayUnit: EnergyUnit ) {
    super( value, unit, displayUnit, EnergyUnit.BASE )
  }
  construct( value : Rational, unit: EnergyUnit ) {
    this( value, unit, unit )
  }
 
  function divide( f: Force ) : Length {
    return new Length( toNumber() / f.toNumber(), Meter, Unit.LengthUnit )
  }
  
  function divide( len: Length ) : Force {
    return new Force( toNumber() / len.toNumber(), ForceUnit.BASE, Unit.ForceUnit )
  }
  
  function divide( t: Time ) : Power {
    return new Power( toNumber() / t.toNumber(), PowerUnit.BASE, PowerUnit.get( Unit, t.Unit ) )
  }

  function divide( power: Power ) : Time {
    return new Time( toNumber() / power.toNumber(), TimeUnit.BASE, power.Unit.TimeUnit )
  }
  
  function divide( temperature: Temperature ) : HeatCapacity {
    return new HeatCapacity( toNumber() / temperature.toNumber(), HeatCapacityUnit.BASE, Unit / temperature.Unit ) 
  }
  
  function divide( c: HeatCapacity ) : Temperature {
    return new Temperature( toNumber() / c.toNumber(), TemperatureUnit.BASE, c.Unit.TemperatureUnit )
  }

  function divide( i: Current ) : MagneticFlux {
    return new MagneticFlux( toNumber() / i.toNumber(), MagneticFluxUnit.BASE, MagneticFluxUnit.get( Unit, i.Unit ) )
  }

  function divide( mf: MagneticFlux ) : Current {
    return new Current( toNumber() / mf.toNumber(), CurrentUnit.BASE, mf.Unit.CurrentUnit )
  }
}
