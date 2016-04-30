package gw.util.science
uses gw.util.Rational

final class PotentialUnit extends AbstractQuotientUnit<PowerUnit, CurrentUnit, Potential, PotentialUnit> {
  final static var CACHE: UnitCache<PotentialUnit> = new UnitCache()

  public static var V: PotentialUnit = get( PowerUnit.BASE, CurrentUnit.BASE, 1, "Volt", "V" )

  public static var BASE: PotentialUnit = V

  static function get( powerUnit: PowerUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) : PotentialUnit {
    var unit = new PotentialUnit( powerUnit, currentUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( powerUnit: PowerUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( powerUnit, currentUnit, factor, name, symbol )
  }

  property get PowerUnit() : PowerUnit {
    return LeftUnit 
  }
  property get CurrentUnit() : CurrentUnit {
    return RightUnit 
  }

  function divide( current: CurrentUnit ) : ResistanceUnit {
    return ResistanceUnit.get( this, current )
  }
}
