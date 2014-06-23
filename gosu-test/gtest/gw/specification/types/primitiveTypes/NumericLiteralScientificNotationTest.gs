package gw.specification.types.primitiveTypes

uses gw.BaseVerifyErrantTest

class NumericLiteralScientificNotationTest extends BaseVerifyErrantTest {

  function testSimpleNotation() : void {
    var x0 : float = 1e2
    assertTrue(typeof x0 == float)
    assertEquals(x0, 100.0f, 0)
     
    var x1 : float = 1.0e2
    assertTrue(typeof x1 == float)
    assertEquals(x1, 100.0f, 0)
     
    var x2 : float = 1.e2
    assertTrue(typeof x2 == float)
    assertEquals(x2, 100.0f, 0)
     
    var x3 : double = 1e2
    assertTrue(typeof x3 == double)
    assertEquals(x3, 100.0, 0)
  }

  function testNotationWithSigns() : void {
    var x0 : double = 1e+2
    assertTrue(typeof x0 == double)
    assertEquals(x0, 100.0, 0)

    var x1 : double = 1e-2
    assertTrue(typeof x1 == double)
    assertEquals(x1, 0.01, 0)

    var x2 : double = -1e-2
    assertTrue(typeof x2 == double)
    assertEquals(x2, -0.01, 0)
  }

  function testScientificNotationInExpression() : void {
    var x : double = 1e2 * 3 - 150.8
    assertTrue(typeof x == double)
    assertEquals(x, 149.2, 0)
  }

  function testScientificNotationWithSuffix() : void {
    var x0 = 1e2f
    assertTrue(typeof x0 == float)
    assertEquals(x0, 100.0f, 0)
    
    var x1 : float = 1e+2F
    assertTrue(typeof x1 == float)
    assertEquals(x1, 100.0f, 0)
    
    var x2 = 1e2d
    assertTrue(typeof x2 == double)
    assertEquals(x2, 100.0, 0)
    
    var x3 : double = 1e+2D
    assertTrue(typeof x3 == double)
    assertEquals(x3, 100.0, 0)
  }

  function testErrant_NumericLiteralScientificNotationTest() {
    processErrantType(Errant_NumericLiteralScientificNotationTest)
  }
}
