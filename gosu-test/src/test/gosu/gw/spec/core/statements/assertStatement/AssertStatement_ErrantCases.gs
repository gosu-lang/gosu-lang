package gw.spec.core.statements.assertStatement

uses gw.test.TestClass
uses java.lang.*
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class AssertStatement_ErrantCases extends TestClass {
  static class AssertWithNoCondNoColonNoMsg {
    function errant() {
      assert
    }
  }

  static class AssertWithNoCondNoMsg {
    function errant() {
      assert :
    }
  }

  static class AssertWithNoCond {
    function errant() {
      assert : "hello"
    }
  }


  static class AssertWithNoColon {
    function errant() {
      assert true "hello"
    }
  }

  static class AssertWithNoMsg {
    function errant() {
      assert true :
    }
  }
}