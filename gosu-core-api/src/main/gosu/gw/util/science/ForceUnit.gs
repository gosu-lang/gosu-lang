package gw.util.science
uses java.math.BigDecimal

final class ForceUnit extends AbstractProductUnit<MassUnit, AccelerationUnit, Force, ForceUnit> {
  public static var BASE: ForceUnit = new ( Kilogram, AccelerationUnit.BASE )
  public static var NEWTONS: ForceUnit = kg m/s/s
    
  construct( massUnit: MassUnit, accUnit: AccelerationUnit ) {
    super( massUnit, accUnit )
  }

  property get MassUnit() : MassUnit {
    return LeftUnit
  }
  property get AccUnit() : AccelerationUnit {
    return RightUnit 
  }
  
  function divide( w: MassUnit ) : AccelerationUnit {
    return AccUnit
  }
  
  function multiply( len: LengthUnit ) : WorkUnit {
    return new( this, len )
  }
}
