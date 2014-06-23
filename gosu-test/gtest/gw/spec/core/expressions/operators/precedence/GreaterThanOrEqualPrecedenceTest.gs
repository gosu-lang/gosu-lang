package gw.spec.core.expressions.operators.precedence

class GreaterThanOrEqualPrecedenceTest extends PrecedenceTestBase {
  // Relational

  function testGreaterThanOrEqualIsLeftAssociative() {
    // TODO - AHK - This test makes no sense, since a boolean can't be an argument to a relational operator
  }

  // Conditional Ternary

  function testGreaterThanOrEqualIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, 4 >= 3 ? 2 : 3)
    assertEquals(int.Type, statictypeof(4 >= 3 ? 2 : 3))

    assertEquals(false, (true ? 2 : 3) >= 4)
  }
  
  // Block
  
  function testGreaterThanOrEqualIsHigherPrecedenceThanBlock() {
    var x = \ -> 3 >= 2
    assertEquals(true, x())
    assertParseError("(\\ -> 3) >= 2", "The type \"int\" cannot be converted to \"block():int\"")
  }

}
