package gw.spec.core.statements.loops
uses gw.test.TestClass

class InfiniteLoopTest extends TestClass {

  function testInfiniteLoop() {
    assertEquals(1, InfiniteLoop.whileLoop0());
    assertEquals(1, InfiniteLoop.doWhileLoop0());
    assertEquals(1, InfiniteLoop.whileLoop1());
    assertEquals(1, InfiniteLoop.doWhileLoop1());
    assertEquals(1, InfiniteLoop.whileLoop2());
    assertEquals(1, InfiniteLoop.doWhileLoop2());
    assertEquals(1, InfiniteLoop.whileLoop3());
    assertEquals(1, InfiniteLoop.doWhileLoop3());
    assertEquals(1, InfiniteLoop.whileLoop4());
    assertEquals(1, InfiniteLoop.doWhileLoop4());
    assertEquals(1, InfiniteLoop.whileLoop5()(1));
    assertEquals(1, InfiniteLoop.doWhileLoop5()(1));
  }

}
