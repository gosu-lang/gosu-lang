package gw.util.science
uses gw.util.Rational

final class MagneticFluxUnit extends AbstractQuotientUnit<EnergyUnit, CurrentUnit, MagneticFlux, MagneticFluxUnit> {
  final static var CACHE: UnitCache<MagneticFluxUnit> = new UnitCache()

  public static var Wb: MagneticFluxUnit = get( EnergyUnit.BASE, CurrentUnit.BASE, 1, "Weber", "Wb" )

  public static var BASE: MagneticFluxUnit = Wb

  static function get( energyUnit: EnergyUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) : MagneticFluxUnit {
    var unit = new MagneticFluxUnit( energyUnit, currentUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( energyUnit: EnergyUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( energyUnit, currentUnit, factor, name, symbol )
  }

  property get EnergyUnit() : EnergyUnit {
    return LeftUnit 
  }
  property get CurrentUnit() : CurrentUnit {
    return RightUnit 
  }

  function divide( area: AreaUnit ) : MagneticFluxDensityUnit {
    return MagneticFluxDensityUnit.get( this, area )
  }
}
