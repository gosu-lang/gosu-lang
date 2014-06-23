package gw.spec.core.expressions.operators.precedence

class ConditionalTernaryPrecedenceTest extends PrecedenceTestBase {

  // Conditional Ternary
  
  function testConditionalTernaryIsRightAssociative() {
    assertTrue(true ? true ? true : false : false)
    assertTrue(true ? (true ? true : false) : false)
  }

  // Block
  
  function testConditionalTernaryIsHigherPrecedenceThanBlock() {
    var x = \ -> true ? 2 : 3
    assertEquals(2, x())
    // TODO - AHK - This SHOULD be a parse errror, but currently it's a runtime error
    // See PL-11388
//    assertParseError("(\\ -> true) ? 2 : 3", "foobar")
  }
}
