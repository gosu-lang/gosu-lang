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
    super( widthUnit, lengthUnit ?: widthUnit, factor,
           name != null
           ? name
           : lengthUnit == null
             ? ("Square " + lengthUnit)
             : (widthUnit.UnitName + "\u00D7" + lengthUnit.UnitName),
           symbol != null
           ? symbol
           : lengthUnit == null
             ? (lengthUnit + "\u00B2")
             : (widthUnit.UnitSymbol + "\u00D7" + lengthUnit.UnitSymbol) )
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
  
  function divide( lu: LengthUnit ) : LengthUnit {
    return lu
  }   
}
