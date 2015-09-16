package gw.specification.statements.loopStatements.theForStatement

uses gw.BaseVerifyErrantTest

class TheForStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheForStatementTest() {
    processErrantType(Errant_TheForStatementTest)
  }

  function testForBasic() {
    var j = 0
    for(0..2) {
      j++
    }
    assertEquals(3, j)
    j = 0
    for(var x in 0..2) {
      assertEquals(j, x)
      j++
    }
    assertEquals(3, j)
    j = 0
    for(x in 0..2) {
      assertEquals(j, x)
      j++
    }
    assertEquals(3, j)
    //for(x : int in 0..2) { }
  }

  function testForExpressionTypes() {
    var j = 0
    var ints : int[] = {0, 1, 2}
    for(x in ints) {
      assertEquals(j, x)
      j++
    }
    assertEquals(3, j)

    var intList : ArrayList<Integer> = {0, 1, 2}
    var iterable : Iterable<Integer> =  intList
    j = 0
    for(x in iterable) {
      assertEquals(j, x)
      j++
    }
    assertEquals(3, j)

    j = 0
    var iter : Iterator<Integer> = intList.iterator()
    for(x in iter) {
      assertEquals(j, x)
      j++
    }
    assertEquals(3, j)

    j = 0
    for(x in "012") {
      assertEquals(j, Integer.parseInt(x))
      j++
    }
    assertEquals(3, j)
    // for("012") {}
    // for(3) {)
  }

  function testLoopVariable() {
    var k = 1
    //for(k in ints) { }

    var ints : int[] = {0, 1, 2}
    for(x in ints) { x = 8 }
    assertTrue(Arrays.equals({0, 1, 2}, ints))

    var intList : ArrayList<Integer> = {0, 1, 2}
    for(x in intList) { x = 8 }
    assertTrue({0, 1, 2}.equals(intList))
    var flag = false
    try {
      for(x in intList) { intList.remove(0) }
    } catch(ex : ConcurrentModificationException) { flag = true}
    assertTrue(flag)
  }

  function testIteratorAndIndexKeyword() {
    var ints : int[] = {0, 1, 2}
    // for(x in ints iterator it) {  }
    var intList : ArrayList<Integer> = {0, 1, 2}
   for(x in intList iterator it) {
      if(x > 0) {
        it.remove()
        //it = null;
      }
    }
    assertTrue({0}.equals(intList))

    intList = {0, 1, 2}
    var j = 0
    for(x in intList index i) {
      assertEquals(j, i)
      j++
      // i = 0
    }

    for(x in intList index i iterator it) { }
    for(x in intList iterator it index i ) { }
    //for(x in intList iterator i index i ) { }
    // for(x in intList iterator it iterator i ) { }
    //for(x in intList index it index i ) { }
  }

  function testLoneInterval() {
    for(0..2) {  }
    var j = 0
    for(0..2 index i) {
      assertEquals(j, i)
      j++
    }
    // for(0..2 iterator it) {  }
  }
}