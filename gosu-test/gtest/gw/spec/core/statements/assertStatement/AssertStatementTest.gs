package gw.spec.core.statements.assertStatement

uses gw.test.TestClass
uses java.lang.*

class AssertStatementTest extends TestClass {

  function testAssertStatementConditionTrue() {
    var x = 8
    assert x > 0
    assertEquals(x, 8)
  }

  function testAssertStatementConditionFalse() {
    var x = 8
    try {
      assert x > 10
    } catch (ae : AssertionError) {
      x = 0
    }
    assertEquals(x, 0)
  }

  function testAssertStatementConditionFalseMessage00() {
    var x = 8
    var msg = ""
    try {
      assert x > 10 : "foo"
    } catch (ae : AssertionError) {
      x = 0
      msg = ae.getMessage()
    }
    assertEquals(x, 0)
    assertEquals(msg, "foo")
  }

    function testAssertStatementConditionFalseMessage01() {
      var x = 8
      var msg = ""
      try {
        assert x > 10 : 123
      } catch (ae : AssertionError) {
        x = 0
        msg = ae.getMessage()
      }
      assertEquals(x, 0)
      assertEquals(msg, "123")
    }

}