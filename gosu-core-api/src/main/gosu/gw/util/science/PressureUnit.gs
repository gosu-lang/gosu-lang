package gw.util.science
uses gw.util.Rational

final class PressureUnit extends AbstractQuotientUnit<MassUnit, AreaUnit, Pressure, PressureUnit> {
  public static var BASE: PressureUnit = new( Kilogram, AreaUnit.BASE )
  
  construct( massUnit: MassUnit, areaUnit: AreaUnit ) {
    super( massUnit, areaUnit )
  }  

  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get AreaUnit() : AreaUnit {
    return RightUnit 
  }

  function multiply( t: AreaUnit ) : MassUnit {
    return  MassUnit
  }
}
