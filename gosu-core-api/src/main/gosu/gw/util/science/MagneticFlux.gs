package gw.util.science

uses gw.util.Rational
uses java.math.RoundingMode
uses java.math.MathContext

final class MagneticFlux extends AbstractMeasure<MagneticFluxUnit, MagneticFlux> {
  construct( value : Rational, unit: MagneticFluxUnit, displayUnit: MagneticFluxUnit ) {
    super( value, unit, displayUnit, MagneticFluxUnit.BASE )
  }
  construct( value : Rational, unit: MagneticFluxUnit ) {
    this( value, unit, unit )
  }

  function multipy( current: Current ) : Energy {
    return new Energy( toNumber() * current.toNumber(), EnergyUnit.BASE, Unit.EnergyUnit )
  }

  function divide( area: Area ) : MagneticFluxDensity {
    return new MagneticFluxDensity( toNumber() / area.toNumber(), MagneticFluxDensityUnit.BASE, MagneticFluxDensityUnit.get( Unit, area.Unit ) )
  }
}
