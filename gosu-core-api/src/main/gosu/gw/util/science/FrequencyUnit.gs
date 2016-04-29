package gw.util.science

uses gw.util.Rational

final class FrequencyUnit extends AbstractQuotientUnit<AngleUnit, TimeUnit, Frequency, FrequencyUnit> {
  final static var CACHE: UnitCache<FrequencyUnit> = new UnitCache()

  public static var BASE: FrequencyUnit = get( Radian, Second )
  public static var Hertz: FrequencyUnit = get( Turn, Second, 1, "Hertz", "Hz" )
  public static var RPM: FrequencyUnit = get( Turn, Minute, 1, "RPM", "rpm" )

  static function get( angleUnit: AngleUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : FrequencyUnit {
    var unit = new FrequencyUnit( angleUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }

  private construct( angleUnit: AngleUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( angleUnit, timeUnit, factor, name, symbol )
  }
  
  property get AngleUnit() : AngleUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  } 
}
