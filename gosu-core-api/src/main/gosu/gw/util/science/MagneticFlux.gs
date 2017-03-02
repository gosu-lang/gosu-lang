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

  function multiply( current: Current ) : Energy {
    return new Energy( toBaseNumber() * current.toBaseNumber(), EnergyUnit.BASE, Unit.EnergyUnit )
  }

  function divide( area: Area ) : MagneticFluxDensity {
    return new MagneticFluxDensity( toBaseNumber() / area.toBaseNumber(), MagneticFluxDensityUnit.BASE, MagneticFluxDensityUnit.get( Unit, area.Unit ) )
  }

  function divide( mf: MagneticFluxDensity ) : Area {
    return new Area( toBaseNumber() / mf.toBaseNumber(), AreaUnit.BASE, mf.Unit.AreaUnit )
  }
}
