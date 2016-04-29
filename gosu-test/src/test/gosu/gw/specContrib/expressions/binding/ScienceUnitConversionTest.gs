package gw.specContrib.expressions.binding

uses gw.test.TestClass
uses gw.util.science.*
uses gw.util.science.UnitConstants#*
uses java.math.BigDecimal

class ScienceUnitConversionTest extends TestClass {
  function testLengthUnitConversions() {
    for( unit in LengthUnit.AllValues ) {
      var len = 1 unit
      for( unit2 in LengthUnit.AllValues ) {
        assertEquals( len, len.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testTimeUnitConversions() {
    for( unit in TimeUnit.AllValues ) {
      var time = 1 unit
      for( unit2 in TimeUnit.AllValues ) {
        assertEquals( time, time.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testMassUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var mass = 1 unit
      for( unit2 in MassUnit.AllValues ) {
        assertEquals( mass, mass.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testTemperatureUnitConversions() {
    for( unit in TemperatureUnit.AllValues ) {
      var temperature = 1 unit
      for( unit2 in TemperatureUnit.AllValues ) {
        assertEquals( temperature, temperature.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testAngleUnitConversions() {
    for( unit in AngleUnit.AllValues ) {
      var angle = 1 unit
      for( unit2 in AngleUnit.AllValues ) {
        assertEquals( angle, angle.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testChargeUnitConversions() {
    for( unit in ChargeUnit.AllValues ) {
      var charge = 1 unit
      for( unit2 in ChargeUnit.AllValues ) {
        assertEquals( charge, charge.to( unit2 ).to( unit ) ) 
      }
    }
  }
  function testAreaUnitConversions() {
    for( unit in LengthUnit.AllValues ) {
      var theUnit = unit * unit
      var area = 1 theUnit
      for( u in LengthUnit.AllValues ) {
        var toUnit = u * u
        assertEquals( area, area.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testVolumeUnitConversions() {
    for( unit in LengthUnit.AllValues ) {
      var theUnit = unit * unit * unit
      var volume = 1 theUnit
      for( u in LengthUnit.AllValues ) {
        var toUnit = u * u * u
        assertEquals( volume, volume.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testVelocityUnitConversions() {
    for( unit in LengthUnit.AllValues ) {
      var theUnit = unit / s
      var velocity = 1 theUnit
      for( u in LengthUnit.AllValues ) {
        var toUnit = u / s
        assertEquals( velocity, velocity.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testAccelerationUnitConversions() {
    for( unit in LengthUnit.AllValues ) {
      var theUnit = unit / s / s
      var acceleration = 1 theUnit
      for( u in LengthUnit.AllValues ) {
        var toUnit = u / s / s
        assertEquals( acceleration, acceleration.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testCurrentUnitConversions() {
    for( unit in ChargeUnit.AllValues ) {
      var theUnit = unit / s
      var current = 1 theUnit
      for( u in ChargeUnit.AllValues ) {
        var toUnit = u / s
        assertEquals( current, current.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testDensityUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit / (m*m*m)
      var density = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u / (m*m*m)
        assertEquals( density, density.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testForceUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit * (m/s/s)
      var force = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u * (m/s/s)
        assertEquals( force, force.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testFrequencyUnitConversions() {
    for( unit in AngleUnit.AllValues ) {
      var theUnit = unit / s
      var frequency = 1 theUnit
      for( u in AngleUnit.AllValues ) {
        var toUnit = u / s
        assertEquals( frequency, frequency.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testMomentumUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit * (m/s)
      var momentum = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u * (m/s)
        assertEquals( momentum, momentum.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testEnergyUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit*(m/s/s) * m
      var energy = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u*(m/s/s) * m
        assertEquals( energy, energy.to( toUnit ).to( theUnit ) )
      }
    }
  }
  function testPowerUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit*(m/s/s)*m / s
      var power = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u*(m/s/s)*m / s
        assertEquals( power, power.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
  function testPressureUnitConversions() {
    for( unit in MassUnit.AllValues ) {
      var theUnit = unit/(m*m)
      var pressure = 1 theUnit
      for( u in MassUnit.AllValues ) {
        var toUnit = u/(m*m)
        assertEquals( pressure, pressure.to( toUnit ).to( theUnit ) ) 
      }
    }
  }
}