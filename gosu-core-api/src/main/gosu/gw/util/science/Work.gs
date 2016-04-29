package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Work extends AbstractMeasure<WorkUnit, Work> {
  construct( value : Rational, unit: WorkUnit, displayUnit: WorkUnit ) {
    super( value, unit, displayUnit, WorkUnit.BASE )
  }
  construct( value : Rational, unit: WorkUnit ) {
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
    return new Time( toNumber() / power.toNumber(), TimeUnit.BaseUnit, power.Unit.TimeUnit )
  }
  
  function divide( temperature: Temperature ) : HeatCapacity {
    return new HeatCapacity( toNumber() / temperature.toNumber(), HeatCapacityUnit.BASE, Unit / temperature.Unit ) 
  }
  
  function divide( c: HeatCapacity ) : Temperature {
    return new Temperature( toNumber() / c.toNumber(), TemperatureUnit.BaseUnit, c.Unit.TemperatureUnit ) 
  }
}
