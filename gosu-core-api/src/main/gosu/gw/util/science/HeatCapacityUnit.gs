package gw.util.science

uses gw.util.Rational

final class HeatCapacityUnit extends AbstractQuotientUnit<WorkUnit, TemperatureUnit, HeatCapacity, HeatCapacityUnit> {
  final static var CACHE: UnitCache<HeatCapacityUnit> = new UnitCache()

  public static var BASE: HeatCapacityUnit = get( WorkUnit.BASE, TemperatureUnit.BaseUnit )

  static function get( workUnit: WorkUnit, temperatureUnit: TemperatureUnit, factor: Rational = null, name: String = null, symbol: String = null ) : HeatCapacityUnit {
    var unit = new HeatCapacityUnit( workUnit, temperatureUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( workUnit: WorkUnit, temperatureUnit: TemperatureUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( workUnit, temperatureUnit, factor, name, symbol )
  }
  
  property get WorkUnit() : WorkUnit {
    return LeftUnit 
  }
  property get TemperatureUnit() : TemperatureUnit {
    return RightUnit 
  }
  
  function multiply( v: TemperatureUnit ) : WorkUnit {
    return WorkUnit
  }
}
