package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class Area extends AbstractMeasure<AreaUnit, Area> {
  construct( value : Rational, unit: AreaUnit, displayUnit: AreaUnit ) {
    super( value, unit, displayUnit, AreaUnit.BASE )
  }
  construct( value : Rational, unit: AreaUnit ) {
    this( value, unit, unit )
  }

  function multiply( t: Length ) : Volume {
    return new Volume( toBaseNumber() * t.toBaseNumber(), VolumeUnit.BASE, VolumeUnit.get( t.Unit, Unit ) )
  }
  
  function divide( t: Length ) : Length {
    return new Length( toBaseNumber() / t.toBaseNumber(), LengthUnit.BASE, t.Unit )
  }
}
