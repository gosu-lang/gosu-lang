package gw.util.science

uses gw.util.Rational

final class VolumeUnit extends AbstractProductUnit<LengthUnit, AreaUnit, Volume, VolumeUnit> {
  final static var CACHE: UnitCache<VolumeUnit> = new UnitCache()

  public static var BASE: VolumeUnit = get( Meter, AreaUnit.get( Meter ) )

  public static var LITER: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 1000, "Litre", "L" )
  public static var MILLI_LITER: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 1, "Millilitre", "mL" )
  public static var FLUID_OZ: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 29.5735295625r, "Fluid Ounce", "fl oz." )
  public static var GALLON: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 3785.411784r, "Gallon", "gal." )
  public static var QUART: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 946.352946r, "Quart", "qt." )
  public static var PINT: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 473.176473r, "Pint", "pt." )
  public static var CUP: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 236.5882365r, "Cup", "c." )
  public static var TABLE_SPOON: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 14.78676478125r, "Tablespoon", "tbsp" )
  public static var TEA_SPOON: VolumeUnit = get( Centi, AreaUnit.get( Centi ), 4.92892159375r, "Teaspoon", "tsp" )

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
