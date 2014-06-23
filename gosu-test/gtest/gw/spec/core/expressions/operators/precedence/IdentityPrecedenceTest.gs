package gw.spec.core.expressions.operators.precedence

class IdentityPrecedenceTest extends PrecedenceTestBase {

   // Conditional Ternary
  
  function testIdentityIsHigherPrecedenceThanConditionalTernary() {
    var x = "x"
    var y = "y"
    assertEquals(2, x === x ? 2 : 3)
    assertEquals(int.Type, statictypeof(x === x ? 2 : 3))
//    assertEquals(false, true === (true ? 2 : 3)) - AHK - Weird behavior around boolean coercions and equality
    // TODO - AHK - Need to fix bugs around coercions and identity
//    assertEquals(boolean.Type, statictypeof(true === (true ? 2 : 3)))

    assertEquals(true, true ? true : x === y)
    assertEquals(false, (true ? x : y) === y)
  }
  
  // Block
  
  function testIdentityIsHigherPrecedenceThanBlock() {
    var x = \ -> "foo" === "foo"
    assertEquals(true, x())
    var y = (\ -> "foo") as String === "foo"
    assertEquals(false, y)
  }

}
