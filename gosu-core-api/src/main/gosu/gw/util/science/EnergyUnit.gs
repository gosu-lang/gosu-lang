package gw.util.science
uses gw.util.Rational
uses gw.util.concurrent.Cache
uses MetricScaleUnit#k

final class EnergyUnit extends AbstractProductUnit<ForceUnit, LengthUnit, Energy, EnergyUnit> {
  final static var CACHE: UnitCache<EnergyUnit> = new UnitCache()

  public static var J: EnergyUnit = get( ForceUnit.BASE, LengthUnit.BASE, null, "Joule", "J" )
  public static var kJ: EnergyUnit = get( ForceUnit.BASE, LengthUnit.BASE, 1k, "Kilojoule", "kJ" )
  public static var cal: EnergyUnit = get( ForceUnit.BASE, LengthUnit.BASE, 4.184, "Calorie", "cal" )
  public static var kcal: EnergyUnit = get( ForceUnit.BASE, LengthUnit.BASE, 4184, "Kilocalorie", "kcal" )
  public static var eV: EnergyUnit = get( ForceUnit.BASE, LengthUnit.BASE, 1.60217733e-19, "Electronvolt", "eV" )

  public static var BASE: EnergyUnit = J

  static function get( forceUnit: ForceUnit, lengthUnit: LengthUnit, factor: Rational = null, name: String = null, symbol: String = null ) : EnergyUnit {
    var unit = new EnergyUnit( forceUnit, lengthUnit, factor, name, symbol )
    return CACHE.get( unit )
  }
  
  private construct( forceUnit: ForceUnit, lengthUnit: LengthUnit, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( forceUnit, lengthUnit, factor, name, symbol )
  }

  property get ForceUnit() : ForceUnit {
    return LeftUnit 
  }
  property get LengthUnit() : LengthUnit {
    return RightUnit 
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

  function divide( i: CurrentUnit ) : MagneticFluxUnit {
    return MagneticFluxUnit.get( this, i )
  }
}
