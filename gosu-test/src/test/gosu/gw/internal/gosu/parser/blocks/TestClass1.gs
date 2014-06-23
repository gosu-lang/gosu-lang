package gw.internal.gosu.parser.blocks

class TestClass1 {

  internal var x : int = 10

  function TestClass1( y : int )
  {
    x = y
  }

  function getX() : int
  {
    return x
  }

  function getXUpdater() : block(int)
  {
    return \ y : int -> { x = y; }
  }

  function getXUpdaterUsingThis() : block(int)
  {
    return \ y : int -> { this.x = y; }
  }

  function recursivelyCallUpdater() : int
  {
    var c = new TestClass1(10)
    var blk = c.getXUpdater()
    blk(20)
    return c.getX()
  }
}