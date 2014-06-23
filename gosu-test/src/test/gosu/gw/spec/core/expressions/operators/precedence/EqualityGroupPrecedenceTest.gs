package gw.spec.core.expressions.operators.precedence

class EqualityGroupPrecedenceTest extends PrecedenceTestBase {
  
  function testEqualsIsSamePrecedenceAsNotEqualsAndLeftAssociative() {
    assertEquals(true, 2 == 2 != false)

    assertEquals(false, 2 != 2 == true)
  }
  
  function testEqualsIsSamePrecedenceAsIdentityAndLeftAssociative() {
    var x = "x"
    // TODO - AHK - Still some bugs with identity expressions and coercions that need to be fixed
//    assertEquals(false, true == 2 === 2)
    assertEquals(true, true == (x === x))
    assertEquals(true, x === x == true)
//    assertEquals(false, 2 === (2 == true))  
  }

}
