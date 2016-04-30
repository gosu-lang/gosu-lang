package gw.util.science
uses gw.util.Rational

final class PressureUnit extends AbstractQuotientUnit<MassUnit, AreaUnit, Pressure, PressureUnit> {
  final static var CACHE: UnitCache<PressureUnit> = new UnitCache()

  public static var Pa: PressureUnit = get( Kilogram, AreaUnit.BASE, 1, "Pascal", "Pa" )
  public static var bar: PressureUnit = get( Kilogram, AreaUnit.BASE, 1.0e5, "Bar", "bar" )
  public static var at: PressureUnit = get( Kilogram, AreaUnit.BASE, 9.80665e4, "TechnicalAtm", "at" )
  public static var atm: PressureUnit = get( Kilogram, AreaUnit.BASE, 1.01325e5, "StandardAtm", "atm" )
  public static var Torr: PressureUnit = get( Kilogram, AreaUnit.BASE, 133.3224, "Torr", "Torr" )
  public static var psi: PressureUnit = get( Pound, AreaUnit.get( Inch ), 1, "Psi", "psi" )
  
  public static var BASE: PressureUnit = Pa
  
  static function get( massUnit: MassUnit, areaUnit: AreaUnit, factor: Rational = null, name: String = null, symbol: String = null ) : PressureUnit {
    var unit = new PressureUnit( massUnit, areaUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( massUnit: MassUnit, areaUnit: AreaUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( massUnit, areaUnit, factor, name, symbol )
  }  

  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get AreaUnit() : AreaUnit {
    return RightUnit 
  }
}