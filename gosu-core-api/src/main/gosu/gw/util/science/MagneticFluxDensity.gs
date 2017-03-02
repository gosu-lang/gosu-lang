package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class MagneticFluxDensity extends AbstractMeasure<MagneticFluxDensityUnit, MagneticFluxDensity> {
  construct( value : Rational, unit: MagneticFluxDensityUnit, displayUnit: MagneticFluxDensityUnit ) {
    super( value, unit, displayUnit, MagneticFluxDensityUnit.BASE )
  }
  construct( value : Rational, unit: MagneticFluxDensityUnit ) {
    this( value, unit, unit )
  }

  function multiply( area: Area ) : MagneticFlux {
    return new MagneticFlux( toBaseNumber() * area.toBaseNumber(), MagneticFluxUnit.BASE, Unit.MagneticFluxUnit )
  }
}
