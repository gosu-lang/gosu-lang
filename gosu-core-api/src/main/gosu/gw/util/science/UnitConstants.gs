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
  
  var joule: WorkUnit = N m
  var J: WorkUnit = joule
  var erg: WorkUnit = dyn cm
  var kcal: WorkUnit = WorkUnit.KCAL
  
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
