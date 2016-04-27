package gw.specContrib.expressions.binding

uses gw.test.TestClass
uses gw.util.science.*
uses gw.util.science.UnitConstants#*

class ScienceTest extends TestClass {
  function testLengthUnits() {
    for( unit in LengthUnit.AllValues ) {
      var one = 1 unit 
      assertEquals( LengthUnit.BaseUnit, one.BaseUnit )
      assertEquals( unit, one.Unit )
      assertEquals( one.to( LengthUnit.BaseUnit ).toNumber(), one.toNumber() )
      assertEquals( 1 unit, one )
    }
  }
  
  function testLengthMath() {
    for( unit in LengthUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in LengthUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Length( a.toNumber() + b.toNumber(), LengthUnit.BaseUnit, unit ), a + b )
        assertEquals( new Length( a.toNumber() - b.toNumber(), LengthUnit.BaseUnit, unit ), a - b )
        assertEquals( new Area( a.toNumber() * b.toNumber(), LengthUnit.BaseUnit * LengthUnit.BaseUnit, unit * unit2 ), a * b )
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      } 
      
      // Length / Time = Velocity
      var time = 2s
      var velocity = new Velocity( a.toNumber() / time.toNumber(), VelocityUnit.BASE, unit / time.Unit )
      assertEquals( velocity, a / time )
      
      // Length / Velocity = Time
      assertEquals( time, a / velocity )
      
      // Length * Area = Volume
      var area = a * a
      assertEquals( new Volume( a.toNumber() * area.toNumber(), VolumeUnit.BASE, unit * area.Unit ), a * area )
      
      // Length * Force = Work
      var force = 2 kg m/s/s
      assertEquals( new Work( a.toNumber() * force.toNumber(), WorkUnit.BASE, unit * force.Unit ), a * force )
    }
  }
  
  function testTimeMath() {
    for( unit in TimeUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in TimeUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Time( a.toNumber() + b.toNumber(), TimeUnit.BaseUnit, unit ), a + b )
        assertEquals( new Time( a.toNumber() - b.toNumber(), TimeUnit.BaseUnit, unit ), a - b )
        // multiplication undefined
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      }
      
      // Time * Velocity = Length
      var len = 1m
      var velocity = new Velocity( len.toNumber() / a.toNumber(), VelocityUnit.BASE, len.Unit / unit )
      assertEquals( len, a * velocity )
      
      // Time * Acceleration = Velocity
      var acceleration = new Acceleration( velocity.toNumber() / a.toNumber(), AccelerationUnit.BASE, velocity.Unit / unit )
      assertEquals( velocity, a * acceleration )  
      
      // Time * Current = Charge
      var charge = 2 coulomb          
      var current = new Current( charge.toNumber() / a.toNumber(), CurrentUnit.BASE, charge.Unit / unit )
      assertEquals( charge, a * current )    
      
      // Time * Frequency = Angle
      var frequency = 2 Hz
      var angle = new Angle( a.toNumber() * frequency.toNumber(), AngleUnit.BaseUnit, unit * frequency.Unit )
      assertEquals( angle, a * frequency )
      
      // Time * Power = Work
      var power = 2 watt
      var work = new Work( a.toNumber() * power.toNumber(), WorkUnit.BASE, unit * power.Unit )
      assertEquals( work, a * power )
    }
  }
  
  function testMassMath() {
    for( unit in MassUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in MassUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Mass( a.toNumber() + b.toNumber(), MassUnit.BaseUnit, unit ), a + b )
        assertEquals( new Mass( a.toNumber() - b.toNumber(), MassUnit.BaseUnit, unit ), a - b )
        // multiplication undefined
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      }      
    }
  }
  
  function testTemperatureMath() {
    for( unit in TemperatureUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in TemperatureUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Temperature( a.toNumber() + b.toNumber(), TemperatureUnit.BaseUnit, unit ), a + b )
        assertEquals( new Temperature( a.toNumber() - b.toNumber(), TemperatureUnit.BaseUnit, unit ), a - b )
        // multiplication undefined
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      }      
    }
  }
  
  function testChargeMath() {
    for( unit in ChargeUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in ChargeUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Charge( a.toNumber() + b.toNumber(), ChargeUnit.BaseUnit, unit ), a + b )
        assertEquals( new Charge( a.toNumber() - b.toNumber(), ChargeUnit.BaseUnit, unit ), a - b )
        // multiplication undefined
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      }      
    }
  }
  
  function testAngleMath() {
    for( unit in AngleUnit.AllValues ) {
      var a = 1 unit 
      for( unit2 in AngleUnit.AllValues ) {
        var b = 1 unit 
        assertEquals( new Angle( a.toNumber() + b.toNumber(), AngleUnit.BaseUnit, unit ), a + b )
        assertEquals( new Angle( a.toNumber() - b.toNumber(), AngleUnit.BaseUnit, unit ), a - b )
        // multiplication undefined
        assertEquals( a.toNumber() / b.toNumber(), a / b )
        assertEquals( a.toNumber() % b.toNumber(), a % b )
      }      
    }
  }
  
  
}