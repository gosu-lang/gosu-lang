package gw.specContrib.dimensions.physics

interface UnitConstants {
  var mum: LengthUnit = Micros
  var mm: LengthUnit = Millis
  var m: LengthUnit = Meters
  var km: LengthUnit = Kilometers
  var inch: LengthUnit = Inches
  var ft: LengthUnit = Feet
  var yds: LengthUnit = Yards
  var mi: LengthUnit = Miles
  
  var ns: TimeUnit = Nanos
  var mus: TimeUnit = Micros
  var ms: TimeUnit = Millis
  var s: TimeUnit = Seconds
  var min: TimeUnit = Minutes
  var hr: TimeUnit = Hours
  var d: TimeUnit = Days
  var wk: TimeUnit =  Weeks
  var yr: TimeUnit = Years
  
  var amu: WeightUnit = AMUs 
  var mug: WeightUnit = Micros
  var mg: WeightUnit = Millis
  var g: WeightUnit = Grams
  var kg: WeightUnit = Kilograms
  var ct: WeightUnit = Carats
  var dr: WeightUnit = Drams
  var gr: WeightUnit = Grains 
  var Nt: WeightUnit = Newtons
  var oz: WeightUnit = Ounces
  var ozt: WeightUnit = TroyOunces
  var lb: WeightUnit = Pounds
  var st: WeightUnit = Stones
  var sht: WeightUnit = Tons
  var lt: WeightUnit = TonsUK
  var tonne: WeightUnit = Tonnes
  var Mo: WeightUnit = Solars
  
  var mph: RateUnit = mi/hr  
  var Ns: MomentumUnit = kg*(m/s)  
  var N: ForceUnit = kg*(m/s/s)  
}