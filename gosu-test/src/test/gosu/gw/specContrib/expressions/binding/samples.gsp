uses gw.util.science.*
uses gw.util.time.*
uses java.math.BigDecimal
uses java.time.ZoneId
uses java.net.URL
uses gw.lang.parser.IParseIssue
uses UnitConstants#*
uses TimeConstants#*
uses gw.util.money.IMoneyConstants#*
uses gw.util.science.InformationCapacityUnit#*
uses gw.util.science.MetricScaleUnit#*
uses gw.lang.parser.exceptions.ParseException

var y = 6 kg m/s
print( y ) 
var z = 9 kg m/s/s
print( z )
print( 9 mi/hr ) 
   
//   
// Gosu supports math operations naturally on "Dimensions".  A dimension can be a physical 
// dimension like Length, Time, Weight, etc. it can also be abstract, for example, like   
// Money. Basically, a dimension is a quantifiable measure of something in specific 
// units.  For example, 9 feet, 150 mph, 400 grams, 9.8 m/s/s, 49.99 USD, etc. 
//
// Arithmetic operations energy directly and type-safely with dimensions as operand.  For instance,
// you can implement a Velocity, Time, Length dimensions and define a multiply method on Time like this:
//
//   function multiply( velocity: Velocity ) : Length...
//
// Gosu uses structural typing to determine that Time implements the multiplication operator on Velocity 
// resulting in Time.  Effectively Gosu infers all the types for you:
//
   var velocity = new Velocity( 50, VelocityUnit.get( Mile, Hour ) )
   var time = new Time( 3, Hour )
   var distance = velocity * time // result is a Length of 150 miles  
// 
// As you can see you can clearly specify units with dimensions and perform arithmetic 
// directly.
//
// You can more naturally express units with dimensions, however, via "Unit Binder Expressions".
// Gosu's grammar supports the direct association of a unit with a measure.  So the previous example
// can be written like this:
// 
    distance = 50 mi/hr * 3 hr
//  Or like this:
    distance = 50mph * 3hr
    print( distance )
    
       
    print( 50mi.to( km ) )
    var acc = 9.8 m/s/s  
    var kdl = 0.44704 kg m/s 
    print( kdl )
    var mmm : Momentum = 4g * 5m/s
    print( mmm )

velocity = 50 mph  
time = 3 min
distance = velocity * time

assertTrue( 4.02336 km == distance.to( km ) )
time = distance / velocity
assertTrue( 3 min == distance / velocity )
var mass = 20 g.to( lb )
acc = 9.8 (m/s/s)
var force = mass * acc
assertTrue( 196 g*(m/s/s) == force )
assertTrue( 0.196bd kg*(m/s/s) == force.to( kg*(m/s/s) ) )

assertTrue( "1.417671871246786051071646520912871 lb⋅ft/s/s" == force.to( lb*(ft/s/s) ).toString() )
assertTrue( "1.417671871246786051071646520912871 lb⋅ft/s/s" == force.to( lb ft/s/s ).toString() )

var momentum = mass * velocity
assertTrue( 1000 g*(mi/hr) == mass * velocity )
assertTrue( 0.44704 kg*(m/s) == momentum.to( Ns ) )    

function assertTrue( b: boolean, sv: String = null ) {
  if( !b ) {
    print( sv )
    throw new Exception()
  }
}

print( 2 dC.to( dF ).to( dC ) )

var x = 5 mm*inch
print( x ) 

var pressure = 50 lb / 2mm*mm
print( pressure )

var density = 63 kg / 9mm*mm*mm
print( density )

var f = 5kg * 5 m/s/s
var energy = f * 6m
print( energy )

var power = energy / 6 s
print( energy.toNumber() )
print( 6 s.toNumber() )
print( power )
 
var f1: EnergyUnit = (kg*(m/s/s))*m
var f2: EnergyUnit = kg*(m/s/s)*m
var f3: EnergyUnit = kg m/s/s m
print( f1.UnitName )
print( f2.UnitName )
print( f3.UnitName )

var freq = 7 Hz
print( freq )
freq = 1 deg/s
print( freq.to( cyc/s ) )  

var degr = 1 deg
print( degr )
print( degr.to( rad ) )

var amps = 5 coulomb/s    
print( amps )
print( 5amp == 5coulomb/s )    
   
var date1 = 1966 Apr 19 12
print( date1 )
var theTime = 1 day - 1 s
var date2 = 1966 Apr 19 theTime
print( date2 )

var tim = 2:35:53:555 PM
print( tim )

var date = 1966-Apr-19 2:35:53:909 PM Z
print( date )
var date5 = 1966-Apr-19 2:35:53:909 PM PST
print( date5 )
var date6 = 1966-Apr-19 12:35:03:789 Z
print( date6 )
var date3 = 1966-Apr-19 0235
print( date3 )
var date4 = 1966 Apr 19 133508.012
print( date4 )     
var dt = 1966 Apr 19 (12hr+3min) AM
print( dt )
dt = 1966 Apr 19 (12hr+3min) PM
print( dt )
dt = 1966 Apr 19 (1hr+3min) PM 
print( dt )

var jdate = 27 Apr 19 Heisei
print( jdate )
var jdate2 = Heisei 27 Apr 19
print( jdate2 )

var ww = 3 day + 1 wk
print( ww )
 
var bucks = 5005 USD                  
print( bucks )
print( "---" )
var ers = 5.32 EUR
print( ers )
print( "---" )
var sum = bucks + ers
print( sum )
print( "---" )
var wsum = bucks.weightedExchange( sum )
print( wsum )
print( "---" )
print( wsum.exchange( USD ) )
print( "---" )

var n1 = -10 USD
var n2 = 15 EUR
var n3 = -10 USD + 15 EUR
print( n2.weightedExchange( n3 ).exchange( EUR ) )
print( "---" )
print( -n3 )
print( "---" )

var fiveSecs : Time = 5s
print( fiveSecs )
print( "---" )

for( l in (1m..(1m + 6inch)).step( 2 ).unit( inch ) ) {
  print( l )
}

for( temp in (1dC..100dC).step( 2 ).unit( dF ) ) {
  print( temp )
}
print( 6inch.to( m ) )

for( temp in (1m/s/s..100m/s/s).step( 2 ).unit( yd/s/s ) ) {
  print( temp )
}

var t2 = 1mo
print( t2.to( s ) )
var t3 = 1tmo
print( t3.to( s ) )

print( 2ft * 6inch )
print( 2ft / 6inch )

print( 1 USD / 1 EUR )

var cals = 5 kcal
print( cals.to( J ).to( kcal ) )

var bytes = 50 GiB
print( bytes + 1024 MiB )

var amt = 50M USD
print( amt )
