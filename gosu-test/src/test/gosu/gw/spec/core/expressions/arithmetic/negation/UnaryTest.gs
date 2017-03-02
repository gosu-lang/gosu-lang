package gw.spec.core.expressions.arithmetic.negation

uses java.math.BigDecimal

class UnaryTest extends gw.test.TestClass {

  function testBigDecimal() {
    assertEquals( 8bd, +8bd )
    assertEquals( 0bd - 8bd, -8bd )
  }

}

