package gw.internal.gosu.parser.blocks

class TestClass2 {

  var z = 20

  function doIt() : int
  {
    var c = new TestClass1(10)
    var blk = c.getXUpdater()
    blk(20)
    return c.getX()
  }

}