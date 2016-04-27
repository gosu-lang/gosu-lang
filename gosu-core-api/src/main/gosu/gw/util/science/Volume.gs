package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Volume extends AbstractMeasure<VolumeUnit, Volume> {
  construct( value : Rational, unit: VolumeUnit, displayUnit: VolumeUnit ) {
    super( value, unit, displayUnit, VolumeUnit.BASE )
  }
  construct( value : Rational, unit: VolumeUnit ) {
    this( value, unit, unit )
  }

  function divide( t: Length ) : Area {
    return new Area( toNumber() / t.toNumber(), new( Meter ), new( t.Unit ) )
  }
  
  function divide( area: Area ) : Length {
    return new Length( toNumber() / area.toNumber(), Meter, area.Unit.WidthUnit )
  }
  
  function multiply( density: Density ) : Mass {
    return new Mass( toNumber() * density.toNumber(), Kilogram, density.Unit.MassUnit )
  }
}
