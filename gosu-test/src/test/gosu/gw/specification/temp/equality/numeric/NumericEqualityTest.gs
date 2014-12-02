package gw.specification.temp.equality.numeric

uses gw.BaseVerifyErrantTest
uses java.lang.Integer
uses java.lang.Float

class NumericEqualityTest extends BaseVerifyErrantTest {
  function testMixedBoxedPrimitiveOperandsAgainstNullValue() {
    var y: Integer = null
    assertTrue(y != 16)
    assertFalse(y == 16)

    assertTrue(y != new Float(16))
    assertFalse(y == new Float(16))

    assertTrue(16 != y)
    assertFalse(16 == y)

    assertTrue(new Float(16) != y)
    assertFalse(new Float(16) == y)

    //... etc.
  }
}
