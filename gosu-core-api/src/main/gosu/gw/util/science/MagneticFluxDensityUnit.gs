package gw.util.science
uses gw.util.Rational

final class MagneticFluxDensityUnit extends AbstractQuotientUnit<MagneticFluxUnit, AreaUnit, MagneticFluxDensity, MagneticFluxDensityUnit> {
  final static var CACHE: UnitCache<MagneticFluxDensityUnit> = new UnitCache()

  public static var T: MagneticFluxDensityUnit = get( MagneticFluxUnit.BASE, AreaUnit.BASE, 1, "Tesla", "T" )

  public static var BASE: MagneticFluxDensityUnit = T

  static function get( magneticfluxUnit: MagneticFluxUnit, areaUnit: AreaUnit, factor: Rational = null, name: String = null, symbol: String = null ) : MagneticFluxDensityUnit {
    var unit = new MagneticFluxDensityUnit( magneticfluxUnit, areaUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( magneticfluxUnit: MagneticFluxUnit, areaUnit: AreaUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( magneticfluxUnit, areaUnit, factor, name, symbol )
  }

  property get MagneticFluxUnit() : MagneticFluxUnit {
    return LeftUnit 
  }
  property get AreaUnit() : AreaUnit {
    return RightUnit 
  }
}
