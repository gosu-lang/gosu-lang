package gw.internal.gosu.parser.blocks

class TestClass3 {

  var foo = new TestClass4()
  function shaneTest() : String
  {
    var str = new TestClass4()
    return waitFor( \ -> {
      var local = getFoo()
      return local.getStr() + str.getStr()
    })
  }

  function waitFor( fun():String ) : String
  {
    return fun()
  }

  function getFoo() : TestClass4
  {
    var x1 = 10
    var x2 = 10
    var x3 = 10
    var x4 = 10
    var x5 = 10
    var x6 = 10
    return foo
  }

}
