package gw.util.science

uses gw.util.Rational

final class AreaUnit extends AbstractProductUnit<LengthUnit, LengthUnit, Area, AreaUnit> {
  final static var CACHE: UnitCache<AreaUnit> = new UnitCache()

  public static var BASE: AreaUnit = get( Meter, Meter )

  static function get( widthUnit: LengthUnit, lengthUnit: LengthUnit = null, factor: Rational = null, name: String = null, symbol: String = null ) : AreaUnit {
    var unit = new AreaUnit( widthUnit, lengthUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
   
  private construct( widthUnit: LengthUnit, lengthUnit: LengthUnit = null, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( widthUnit, lengthUnit ?: widthUnit, factor, name, symbol )
  }

  override property get FullName() : String {
    return LengthUnit == null
           ? LengthUnit + "\u00B2"
           : WidthUnit.FullName + "\u00D7" + LengthUnit.FullName  
  }
  
  override property get FullSymbol() : String {
    return LengthUnit == null
           ? LengthUnit + "\u00B2"
           : WidthUnit.FullSymbol + "\u00D7" + LengthUnit.FullSymbol  
  }
  
  property get WidthUnit() : LengthUnit {
    return LeftUnit
  }
  property get LengthUnit() : LengthUnit {
    return RightUnit
  }

  property get IsSquare() : boolean {
    return WidthUnit === LengthUnit
  }

  function multiply( lu: LengthUnit ) : VolumeUnit {
    return VolumeUnit.get( lu, this )
  }
}
