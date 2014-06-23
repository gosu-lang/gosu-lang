package gw.internal.gosu.regression

uses gw.test.TestClass
uses gw.lang.reflect.features.PropertyReference

class AmbiguousMethodCallWithGenericPropertyRefTest extends TestClass {

  function testThereIsNoWarningOnXImpl() {
    assertTrue(XImpl.Type.Valid)
    assertNotNull(XImpl.Type.ClassStatement)
    assertTrue("\n\n\t" + XImpl.Type.ClassStatement.ParseWarnings*.UIMessage.join("\n\n\t"),
        XImpl.Type.ClassStatement.ParseWarnings.Empty)
  }

  static interface X {
    function f<T>(s:block():T)
  }

  static class XImpl implements X  {
    var _b : Boolean as B

    override function f<T>(s:block():T) {}

    function foo() {
      f(null)
    }
  }
}
