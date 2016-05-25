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
    return new Area( toBaseNumber() / t.toBaseNumber(), AreaUnit.BASE, AreaUnit.get( t.Unit ) )
  }
  
  function divide( area: Area ) : Length {
    return new Length( toBaseNumber() / area.toBaseNumber(), LengthUnit.BASE, area.Unit.WidthUnit )
  }
  
  function multiply( density: Density ) : Mass {
    return new Mass( toBaseNumber() * density.toBaseNumber(), MassUnit.BASE, density.Unit.MassUnit )
  }
}
