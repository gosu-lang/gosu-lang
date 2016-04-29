package gw.util.science
uses gw.util.Rational

final class PowerUnit extends AbstractQuotientUnit<WorkUnit, TimeUnit, Power, PowerUnit> {
  final static var CACHE: UnitCache<PowerUnit> = new UnitCache()

  public static var pW: PowerUnit = get( WorkUnit.BASE, Second, 1/1000000000000, "Picowatt", "pW" )
  public static var nW: PowerUnit = get( WorkUnit.BASE, Second, 1/1000000000, "Nanowatt", "nW" )
  public static var uW: PowerUnit = get( WorkUnit.BASE, Second, 1/1000000, "Microwatt", "μW" )
  public static var mW: PowerUnit = get( WorkUnit.BASE, Second, 1/1000, "Milliwatt", "mW" )
  public static var W: PowerUnit = get( WorkUnit.BASE, Second, 1, "Watt", "W" )
  public static var kW: PowerUnit = get( WorkUnit.BASE, Second, 1000, "Kilowatt", "kW" )
  public static var MW: PowerUnit = get( WorkUnit.BASE, Second, 1000000, "Megawatt", "MW" )
  public static var GW: PowerUnit = get( WorkUnit.BASE, Second, 1000000000, "Gigawatt", "GW" )

  public static var BASE: PowerUnit = W
  
  static function get( workUnit: WorkUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : PowerUnit {
    var unit = new PowerUnit( workUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
    
  private construct( workUnit: WorkUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( workUnit, timeUnit, factor, name, symbol )
  }

  property get WorkUnit() : WorkUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  }
  
  function multiply( w: TimeUnit ) : WorkUnit {
    return WorkUnit
  }
  
  function divide( v: VelocityUnit ) : ForceUnit {
    return WorkUnit.ForceUnit
  }

  function divide( force: Force ) : VelocityUnit {
    return WorkUnit.ForceUnit.AccUnit.VelocityUnit
  }
}
