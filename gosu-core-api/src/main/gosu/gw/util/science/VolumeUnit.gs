package gw.util.science

uses java.math.BigDecimal

final class VolumeUnit extends AbstractProductUnit<AreaUnit, LengthUnit, Volume, VolumeUnit> {
  public static var BASE: VolumeUnit = new( new( Meter ), Meter )
  
  construct( areaUnit: AreaUnit, lengthUnit: LengthUnit ) {
    super( areaUnit, lengthUnit )
  }
  construct( unit: LengthUnit ) {
    this( new( unit ), unit )
  }

  property get AreaUnit() : AreaUnit {
    return LeftUnit 
  }
  property get LengthUnit() : LengthUnit {
    return RightUnit 
  }

  property get IsCubic() : boolean {
    return AreaUnit.IsSquare && AreaUnit.WidthUnit === LengthUnit
  }

  override property get UnitName() : String {
    return IsCubic
           ? ("Cubic " + AreaUnit.WidthUnit.UnitName)
           : (AreaUnit.UnitName + "\u00D7" + LengthUnit.UnitName)
  }

  override property get UnitSymbol() : String {
    return IsCubic
           ? (AreaUnit.WidthUnit.UnitSymbol + "\u00B3")
           : (AreaUnit.UnitSymbol + "\u00D7" + LengthUnit.UnitSymbol)
  }   
}
