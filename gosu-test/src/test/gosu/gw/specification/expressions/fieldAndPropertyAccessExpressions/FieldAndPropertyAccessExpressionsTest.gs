package gw.specification.expressions.fieldAndPropertyAccessExpressions

uses gw.BaseVerifyErrantTest

class FieldAndPropertyAccessExpressionsTest extends BaseVerifyErrantTest {
  var f0 : int = 8
  static var f1 : int = 9

  class C0 extends FieldAndPropertyAccessExpressionsTest {
    var f2 : int = 10

    function testFieldAccessInherited() {
      var i : int
      i = f0
      i = f1
      i = f2
    }
  }

  function testErrant_FieldAndPropertyAccessExpressionsTest() {
    processErrantType(Errant_FieldAndPropertyAccessExpressionsTest)
  }



  function testFieldAccess0() {
    var i : int
    i = f0
    assertEquals(8, i)
    i = f1
    assertEquals(9, i)

  }

}