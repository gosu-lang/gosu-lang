package gw.util.science
uses gw.util.Rational

final class CapacitanceUnit extends AbstractQuotientUnit<ChargeUnit, PotentialUnit, Capacitance, CapacitanceUnit> {
  final static var CACHE: UnitCache<CapacitanceUnit> = new UnitCache()

  public static var F: CapacitanceUnit = get( ChargeUnit.BASE, PotentialUnit.BASE, 1, "Farad", "F" )

  public static var BASE: CapacitanceUnit = F

  static function get( chargeUnit: ChargeUnit, potentialUnit: PotentialUnit, factor: Rational = null, name: String = null, symbol: String = null ) : CapacitanceUnit {
    var unit = new CapacitanceUnit( chargeUnit, potentialUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( chargeUnit: ChargeUnit, potentialUnit: PotentialUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( chargeUnit, potentialUnit, factor, name, symbol )
  }

  property get ChargeUnit() : ChargeUnit {
    return LeftUnit 
  }
  property get PotentialUnit() : PotentialUnit {
    return RightUnit 
  }
}
