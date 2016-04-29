package gw.util.science
uses gw.util.Rational
uses gw.util.concurrent.Cache

final class WorkUnit extends AbstractProductUnit<ForceUnit, LengthUnit, Work, WorkUnit> {
  final static var CACHE: UnitCache<WorkUnit> = new UnitCache()

  public static var BASE: WorkUnit = get( ForceUnit.BASE, LengthUnit.BaseUnit, null, "Joule", "J" )
  public static var KJ: WorkUnit = get( ForceUnit.BASE, LengthUnit.BaseUnit, 1000, "Kilojoule", "kJ" )
  public static var CAL: WorkUnit = get( ForceUnit.BASE, LengthUnit.BaseUnit, 4.184, "Calorie", "cal" )
  public static var KCAL: WorkUnit = get( ForceUnit.BASE, LengthUnit.BaseUnit, 4184, "Kilocalorie", "kcal" )

  static function get( forceUnit: ForceUnit, lengthUnit: LengthUnit, factor: Rational = null, name: String = null, symbol: String = null ) : WorkUnit {
    var unit = new WorkUnit( forceUnit, lengthUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( forceUnit: ForceUnit, lengthUnit: LengthUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( forceUnit, lengthUnit, factor, name, symbol )
    var factory : IBinaryUnitFactory = WorkUnit
  }

  property get ForceUnit() : ForceUnit {
    return LeftUnit 
  }
  property get LengthUnit() : LengthUnit {
    return RightUnit 
  }
  
  function divide( w: ForceUnit ) : LengthUnit {
    return LengthUnit
  }

  function divide( w: LengthUnit ) : ForceUnit {
    return ForceUnit
  }
  
  function divide( time: TimeUnit ) : PowerUnit {
    return PowerUnit.get( this, time )
  }

  function divide( power: PowerUnit ) : TimeUnit {
    return power.TimeUnit
  }
  
  function divide( temperature: TemperatureUnit ) : HeatCapacityUnit {
    return HeatCapacityUnit.get( this, temperature ) 
  }
  
  function divide( c: HeatCapacityUnit ) : TemperatureUnit {
    return c.TemperatureUnit
  }
}
