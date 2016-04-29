package gw.util.science
uses gw.util.Rational

final class ConductanceUnit extends AbstractQuotientUnit<CurrentUnit, PotentialUnit, Conductance, ConductanceUnit> {
  final static var CACHE: UnitCache<ConductanceUnit> = new UnitCache()

  public static var S: ConductanceUnit = get( CurrentUnit.BASE, PotentialUnit.BASE, 1, "Siemens", "S" )

  public static var BASE: ConductanceUnit = S

  static function get( currentUnit: CurrentUnit, potentialUnit: PotentialUnit, factor: Rational = null, name: String = null, symbol: String = null ) : ConductanceUnit {
    var unit = new ConductanceUnit( currentUnit, potentialUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( currentUnit: CurrentUnit, potentialUnit: PotentialUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( currentUnit, potentialUnit, factor, name, symbol )
  }

  property get CurrentUnit() : CurrentUnit {
    return LeftUnit 
  }
  property get PotentialUnit() : PotentialUnit {
    return RightUnit 
  }
  
  function multiply( w: PotentialUnit ) : CurrentUnit {
    return CurrentUnit
  }
}
