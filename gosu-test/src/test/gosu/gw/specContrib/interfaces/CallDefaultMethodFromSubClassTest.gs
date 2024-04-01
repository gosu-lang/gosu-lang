package gw.specContrib.interfaces
uses junit.framework.TestCase

class CallDefaultMethodFromSubClassTest extends gw.BaseVerifyErrantTest {

    interface IFoo {
      function _default(): String { return "default" }
    }

    static class BaseFoo implements IFoo {}

    static class SubFoo extends BaseFoo {
      override function _default(): String {
        return "not" + super._default()
      }
    }

    function testInvokeSpecial() {
      var result = new SubFoo()._default()
      assertEquals( "notdefault", result )
    }
}