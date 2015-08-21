package gw.specification.statements.theAssertStatement

uses gw.BaseVerifyErrantTest

class TheAssertStatementTest extends BaseVerifyErrantTest {

  function testErrant_TheAssertStatementTest() {
    processErrantType(Errant_TheAssertStatementTest)
  }

  function testAssertionBasic() {
    try {
      assert 1 == 0
      fail()
    }
    catch(e : AssertionError) {    }
    try {
     assert 1 == 0 : "Hello"
     fail()
    }
    catch(e : AssertionError) {
      assertEquals("Hello", e.Message)
    }
    //assert 0
    //assert 0 ? "1"
  }
}