package gw.util.science
uses gw.util.Rational

final class PowerUnit extends AbstractQuotientUnit<EnergyUnit, TimeUnit, Power, PowerUnit> {
  final static var CACHE: UnitCache<PowerUnit> = new UnitCache()

  public static var pW: PowerUnit = get( EnergyUnit.BASE, Second, 1/1000000000000, "Picowatt", "pW" )
  public static var nW: PowerUnit = get( EnergyUnit.BASE, Second, 1/1000000000, "Nanowatt", "nW" )
  public static var uW: PowerUnit = get( EnergyUnit.BASE, Second, 1/1000000, "Microwatt", "μW" )
  public static var mW: PowerUnit = get( EnergyUnit.BASE, Second, 1/1000, "Milliwatt", "mW" )
  public static var W: PowerUnit = get( EnergyUnit.BASE, Second, 1, "Watt", "W" )
  public static var kW: PowerUnit = get( EnergyUnit.BASE, Second, 1000, "Kilowatt", "kW" )
  public static var MW: PowerUnit = get( EnergyUnit.BASE, Second, 1000000, "Megawatt", "MW" )
  public static var GW: PowerUnit = get( EnergyUnit.BASE, Second, 1000000000, "Gigawatt", "GW" )

  public static var BASE: PowerUnit = W
  
  static function get( energyUnit: EnergyUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) : PowerUnit {
    var unit = new PowerUnit( energyUnit, timeUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
    
  private construct( energyUnit: EnergyUnit, timeUnit: TimeUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( energyUnit, timeUnit, factor, name, symbol )
  }

  property get EnergyUnit() : EnergyUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  }
  
  function multiply( w: TimeUnit ) : EnergyUnit {
    return EnergyUnit
  }
  
  function divide( v: VelocityUnit ) : ForceUnit {
    return EnergyUnit.ForceUnit
  }

  function divide( force: ForceUnit ) : VelocityUnit {
    return EnergyUnit.ForceUnit.AccUnit.VelocityUnit
  }

  function divide( potential: PotentialUnit ) : CurrentUnit {
    return potential.CurrentUnit
  }

  function divide( current: CurrentUnit ) : PotentialUnit {
    return PotentialUnit.get( this, current )
  }
}
