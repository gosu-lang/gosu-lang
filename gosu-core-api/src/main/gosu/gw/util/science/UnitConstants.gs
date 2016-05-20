package gw.util.science
uses gw.util.Rational

interface UnitConstants {
  var mum: LengthUnit = Micro
  var mm: LengthUnit = Milli
  var cm: LengthUnit = Centi
  var m: LengthUnit = Meter
  var km: LengthUnit = Kilometer
  var inch: LengthUnit = Inch
  var ft: LengthUnit = Foot
  var yd: LengthUnit = Yard
  var mi: LengthUnit = Mile
  
  var ns: TimeUnit = Nano
  var mus: TimeUnit = Micro
  var ms: TimeUnit = Milli
  var s: TimeUnit = Second
  var min: TimeUnit = Minute
  var hr: TimeUnit = Hour
  var day: TimeUnit = Day
  var wk: TimeUnit =  Week
  var mo: TimeUnit =  Month
  var yr: TimeUnit = Year
  var tmo: TimeUnit =  TrMonth
  var tyr: TimeUnit = TrYear
  
  var amu: MassUnit = AtomicMass
  var mug: MassUnit = Micro
  var mg: MassUnit = Milli
  var g: MassUnit = Gram
  var kg: MassUnit = Kilogram
  var ct: MassUnit = Carat
  var dr: MassUnit = Dram
  var gr: MassUnit = Grain
  var Nt: MassUnit = Newton
  var oz: MassUnit = Ounce
  var ozt: MassUnit = TroyOunce
  var lb: MassUnit = Pound
  var st: MassUnit = Stone
  var sht: MassUnit = Ton
  var lt: MassUnit = TonUK
  var tonne: MassUnit = Tonne
  var Mo: MassUnit = Solar

  var L: VolumeUnit = VolumeUnit.LITER
  var mL: VolumeUnit = VolumeUnit.MILLI_LITER
  var fl_oz: VolumeUnit = VolumeUnit.FLUID_OZ
  var gal: VolumeUnit = VolumeUnit.GALLON
  var qt: VolumeUnit = VolumeUnit.QUART
  var pt: VolumeUnit = VolumeUnit.PINT
  var cup: VolumeUnit = VolumeUnit.CUP
  var tsp: VolumeUnit = VolumeUnit.TABLE_SPOON
  var tbsp: VolumeUnit = VolumeUnit.TEA_SPOON

  var cyc: AngleUnit = Turn
  var rad: AngleUnit = Radian
  var mrad: AngleUnit = Milli
  var nrad: AngleUnit = Nano
  var arcsec: AngleUnit = ArcSecond
  var mas: AngleUnit = MilliArcSecond
  var grad: AngleUnit = Gradian
  var quad: AngleUnit = Quadrant
  var moa: AngleUnit = MOA
  var deg: AngleUnit = Degree
  
  var dK: TemperatureUnit = Kelvin
  var dC: TemperatureUnit = Celcius
  var dF: TemperatureUnit = Fahrenheit   
  
  var mph: VelocityUnit = mi/hr
  
  var Ns: MomentumUnit = kg m/s
  
  var N: ForceUnit = kg m/s/s
  var dyn: ForceUnit = g cm/s/s
  
  var joule: EnergyUnit = N m
  var J: EnergyUnit = joule
  var erg: EnergyUnit = dyn cm
  var kcal: EnergyUnit = EnergyUnit.kcal
  
  var watt: PowerUnit = J/s
  var W: PowerUnit = watt
  
  var C: HeatCapacityUnit = J/dK
  
  var Hz: FrequencyUnit = cyc/s
  var kHz: FrequencyUnit = cyc/ms
  var MHz: FrequencyUnit = cyc/mus
  var GHz: FrequencyUnit = cyc/ns
  var rpm: FrequencyUnit = cyc/min
    
  var coulomb: ChargeUnit = Coulomb
  var amp: CurrentUnit = coulomb/s
}
