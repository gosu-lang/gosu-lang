package gw.util.science
uses gw.util.Rational

final class ResistanceUnit extends AbstractQuotientUnit<PotentialUnit, CurrentUnit, Resistance, ResistanceUnit> {
  final static var CACHE: UnitCache<ResistanceUnit> = new UnitCache()

  public static var ohm: ResistanceUnit = get( PotentialUnit.BASE, CurrentUnit.BASE, 1, "Ohm", "Ω" )

  public static var BASE: ResistanceUnit = ohm

  static function get( potentialUnit: PotentialUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) : ResistanceUnit {
    var unit = new ResistanceUnit( potentialUnit, currentUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( potentialUnit: PotentialUnit, currentUnit: CurrentUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( potentialUnit, currentUnit, factor, name, symbol )
  }

  property get PotentialUnit() : PotentialUnit {
    return LeftUnit 
  }
  property get CurrentUnit() : CurrentUnit {
    return RightUnit 
  }

  function multiply( t: TimeUnit ) : InductanceUnit {
    return InductanceUnit.get( this, t )
  }
}
