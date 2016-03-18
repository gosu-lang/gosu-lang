package gw.util.science

uses java.math.BigDecimal

final class AreaUnit extends AbstractProductUnit<LengthUnit, LengthUnit, Area, AreaUnit> {
  public static var BASE: AreaUnit = new( Meter, Meter )
  
  construct( widthUnit: LengthUnit, lengthUnit: LengthUnit ) {
    super( widthUnit, lengthUnit )
  }
  construct( unit: LengthUnit ) {
    this( unit, unit )
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

  override property get UnitName() : String {
    return IsSquare
           ? ("Square " + WidthUnit.UnitName)
           : (WidthUnit.UnitName + "\u00D7" + LengthUnit.UnitName)
  }

  override property get UnitSymbol() : String {
    return IsSquare
           ? (WidthUnit.UnitSymbol + "\u00B2")
           : (WidthUnit.UnitSymbol + "\u00D7" + LengthUnit.UnitSymbol)
  }
 
  function multiply( lu: LengthUnit ) : VolumeUnit {
    return new( this, lu )
  }  
  
  function divide( lu: LengthUnit ) : LengthUnit {
    return lu
  }   
}
