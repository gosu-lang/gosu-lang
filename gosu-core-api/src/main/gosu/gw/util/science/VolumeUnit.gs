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
    super( lengthUnit, areaUnit ?: AreaUnit.get( lengthUnit ), factor,
           name != null
           ? name
           : areaUnit == null || areaUnit.IsSquare && areaUnit.WidthUnit === lengthUnit
             ? ("Cubic " + lengthUnit.UnitName)
             : (areaUnit.UnitName + "\u00D7" + lengthUnit.UnitName),
           symbol != null
           ? symbol
           : areaUnit == null || areaUnit.IsSquare && areaUnit.WidthUnit === lengthUnit
             ? (lengthUnit.UnitSymbol + "\u00B3")
             : (areaUnit.UnitSymbol + "\u00D7" + lengthUnit.UnitSymbol) )
  }

  property get AreaUnit() : AreaUnit {
    return RightUnit
  }
  property get LengthUnit() : LengthUnit {
    return LeftUnit
  }

  property get IsCubic() : boolean {
    return AreaUnit.IsSquare && AreaUnit.WidthUnit === LengthUnit
  }

  function divide( len: LengthUnit ) : AreaUnit {
    return AreaUnit
  }

  function divide( area: AreaUnit ) : LengthUnit {
    return LengthUnit
  }

  function multiply( density: DensityUnit ) : MassUnit {
    return density.MassUnit
  }
}
