package gw.spec.core.expressions.operators.precedence

class BlockPrecedenceTest extends PrecedenceTestBase {

  // Block
  
  function testBlockIsRightAssociative() {
    var x = \ -> \ -> "foo"
    // TODO - AHK - Currently typeis doesn't work with blocks:  See PL-11406
//    assertEquals(true, x() typeis block(): String)  
  }

}
