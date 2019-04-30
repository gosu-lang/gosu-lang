package gw.internal.gosu.regression

uses java.util.function.Function
uses gw.test.TestClass

class ReifiedTypeInsideInterfaceTest extends TestClass   {
  function testReifiedViaInterface() {
    assertEquals((\x:Object->"hi").toString(), BFace.getData()[0].toString())
  }

  interface BFace {
    static function getData() : Function[] {
      var data = new ArrayList<Function>(){\x->"hi"}
      return data.toTypedArray()
    }
  }
}
