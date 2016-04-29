package gw.util.science

uses gw.util.Rational

final class HeatCapacityUnit extends AbstractQuotientUnit<EnergyUnit, TemperatureUnit, HeatCapacity, HeatCapacityUnit> {
  final static var CACHE: UnitCache<HeatCapacityUnit> = new UnitCache()

  public static var BASE: HeatCapacityUnit = get( EnergyUnit.BASE, TemperatureUnit.BaseUnit )

  static function get( energyUnit: EnergyUnit, temperatureUnit: TemperatureUnit, factor: Rational = null, name: String = null, symbol: String = null ) : HeatCapacityUnit {
    var unit = new HeatCapacityUnit( energyUnit, temperatureUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( energyUnit: EnergyUnit, temperatureUnit: TemperatureUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( energyUnit, temperatureUnit, factor, name, symbol )
  }
  
  property get EnergyUnit() : EnergyUnit {
    return LeftUnit 
  }
  property get TemperatureUnit() : TemperatureUnit {
    return RightUnit 
  }
  
  function multiply( v: TemperatureUnit ) : EnergyUnit {
    return EnergyUnit
  }
}
