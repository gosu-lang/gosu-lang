package gw.util.science

uses gw.util.Rational

final class VolumeUnit extends AbstractProductUnit<LengthUnit, AreaUnit, Volume, VolumeUnit> {
  final static var CACHE: UnitCache<VolumeUnit> = new UnitCache()

  public static var BASE: VolumeUnit = get( Meter, AreaUnit.get( Meter ) )

  static function get( lengthUnit: LengthUnit, areaUnit: AreaUnit = null, factor: Rational = null, name: String = null, symbol: String = null ) : VolumeUnit {
    var unit = new VolumeUnit( lengthUnit, areaUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( lengthUnit: LengthUnit, areaUnit: AreaUnit = null, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( lengthUnit, areaUnit ?: AreaUnit.get( lengthUnit ), factor, name, symbol )
  }
  
  property get AreaUnit() : AreaUnit {
    return RightUnit
  }
  property get LengthUnit() : LengthUnit {
    return LeftUnit
  }

  override property get FullName() : String {
    return AreaUnit.IsSquare && AreaUnit.WidthUnit === LengthUnit
             ? LengthUnit.FullName + "\u00B3"
             : AreaUnit.FullName + "\u00D7" + LengthUnit.FullName  
  }
  
  override property get FullSymbol() : String {
    return AreaUnit.IsSquare && AreaUnit.WidthUnit === LengthUnit
             ? LengthUnit.FullSymbol + "\u00B3"
             : AreaUnit.FullSymbol + "\u00D7" + LengthUnit.FullSymbol 
  }

  property get IsCubic() : boolean {
    return AreaUnit.IsSquare && AreaUnit.WidthUnit === LengthUnit
  }

  function divide( len: AreaUnit ) : LengthUnit {
    return LengthUnit
  }

  function multiply( density: DensityUnit ) : MassUnit {
    return density.MassUnit
  }
}
