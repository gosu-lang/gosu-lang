package gw.util.science
uses gw.util.Rational

final class InductanceUnit extends AbstractProductUnit<ResistanceUnit, TimeUnit, Inductance, InductanceUnit> {
  final static var CACHE: UnitCache<InductanceUnit> = new UnitCache()

  public static var H: InductanceUnit = get( ResistanceUnit.BASE, TimeUnit.BASE, 1, "Henry", "H" )

  public static var BASE: InductanceUnit = H

  static function get( resistanceUnit: ResistanceUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : InductanceUnit {
    var unit = new InductanceUnit( resistanceUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( resistanceUnit: ResistanceUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( resistanceUnit, timeUnit, factor, name, symbol )
  }

  property get ResistanceUnit() : ResistanceUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  }
  
  function divide( w: TimeUnit ) : ResistanceUnit {
    return ResistanceUnit
  }
}
