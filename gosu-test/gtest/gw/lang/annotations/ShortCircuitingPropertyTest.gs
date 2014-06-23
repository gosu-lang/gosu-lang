package gw.lang.annotations
uses gw.test.TestClass
uses java.lang.NullPointerException

class ShortCircuitingPropertyTest extends TestClass   {

  function testBooleanProperties() {
    // Boolean properties always short-circuit to false, regardless of the annotation
    var x : HasShortCircuitingProperties = null
    assertFalse(x?.NonShortCircuitBooleanProperty)
    assertFalse(x?.ShortCircuitBooleanProperty)
  }
  
  function testByteProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitByteProperty)
    assertEquals(0 as byte, x?.ShortCircuitByteProperty)
  }
  
  function testShortProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitShortProperty)
    assertEquals(0 as short, x?.ShortCircuitShortProperty)
  }
  
  function testCharProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitCharProperty)
    assertEquals(0 as char, x?.ShortCircuitCharProperty)
  }
  
  function testIntProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitIntProperty)
    assertEquals(0, x?.ShortCircuitIntProperty)
  }
  
  function testLongProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitLongProperty)
    assertEquals(0 as long, x?.ShortCircuitLongProperty)
  }
  
  function testFloatProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitFloatProperty)
    assertEquals(0.0 as float, x?.ShortCircuitFloatProperty, 0.0 as float)
  }
  
  function testDoubleProperties() {
    var x : HasShortCircuitingProperties = null
    assertThrowsNPE(\ -> x.NonShortCircuitDoubleProperty)
    assertEquals(0.0, x?.ShortCircuitDoubleProperty, 0.0)
  }
  
  function testObjectProperties() {
    // Object properties always null-short-circuit
    var x : HasShortCircuitingProperties = null
    assertNull(x?.NonShortCircuitStringProperty)
    assertNull(x?.ShortCircuitStringProperty)
  }

  function testNestedPropertiesNullShortCircuit() {
    var x : HasShortCircuitingProperties = null
    assertEquals( 0, x?.Chained?.ShortCircuitIntProperty )
    assertEquals( 0, x?.Chained?.Chained?.ShortCircuitIntProperty )
    assertEquals( 0, x?.Chained?.Chained?.Chained?.ShortCircuitIntProperty )
    assertEquals( 0, x?.Chained?.Chained?.Chained?.Chained?.ShortCircuitIntProperty )
  }
  
  function testShortCircuitingWorksInEnhancement() {
    var x = new HasShortCircuitingProperties()
    assertEquals( 0, x?.callShortCircuitingIntPropInEnhancement() )
  }

  function testNestedPropertiesNullShortCircuitWithEnhancementProp() {
    var x : HasShortCircuitingProperties = null
    assertEquals( 0, x?.ShortCircuitIntPropertyEnh )
    assertEquals( 0, x?.Chained?.ShortCircuitIntPropertyEnh )
  }

  function testCountFunctionalityInEnhancements() {
    var x = new HasShortCircuitingProperties()
    assertEquals( 0, x?.callCountMethodOnArray() )
    assertEquals( 0, x?.callCountMethodOnList() )
  }
 
  private function assertThrowsNPE(arg()) {
    try {
      arg()
      fail("Expected NPE to be thrown")  
    } catch (e : NullPointerException) {
      // Expected  
    }
  }
}
