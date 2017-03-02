package gw.util.science
uses gw.util.Rational

final class DensityUnit extends AbstractQuotientUnit<MassUnit, VolumeUnit, Density, DensityUnit> {
  final static var CACHE: UnitCache<DensityUnit> = new UnitCache()

  public static var BASE: DensityUnit = get( Kilogram, VolumeUnit.BASE )

  static function get( massUnit: MassUnit, volumeUnit: VolumeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : DensityUnit {
    var unit = new DensityUnit( massUnit, volumeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( massUnit: MassUnit, volumeUnit: VolumeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( massUnit, volumeUnit, factor, name, symbol )
  }  

  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get VolumeUnit() : VolumeUnit {
    return RightUnit 
  }
}
