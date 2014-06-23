package gw.spec.core.expressions.operators.precedence

class ShiftGroupPrecedenceTest extends PrecedenceTestBase {

  function testShiftLeftIsSamePrecedenceAsShiftRightAndLeftAssociative() {
    assertEquals(1, 3 << 4 >> 5)
    assertEquals(3, 3 << (4 >> 5))
    assertEquals(4, 10 >> 3 << 2)
    assertEquals(0, 10 >> (3 << 2))
  }

  function testShiftLeftIsSamePrecedenceAsUnsignedShiftRightAndLeftAssociative() {
    assertEquals(1, 3 << 4 >>> 5)
    assertEquals(3, 3 << (4 >>> 5))
    assertEquals(4, 10 >>> 3 << 2)
    assertEquals(0, 10 >>> (3 << 2))
  }

  function testShiftRightIsSamePrecedenceAsUnsignedShiftRightAndLeftAssociative() {
    assertEquals(0, 3 >> 4 >>> 5)
    assertEquals(3, 3 >> (4 >>> 5))
    assertEquals(0, 10 >>> 3 >> 2)
    assertEquals(10, 10 >>> (3 >> 2))
  }

}
