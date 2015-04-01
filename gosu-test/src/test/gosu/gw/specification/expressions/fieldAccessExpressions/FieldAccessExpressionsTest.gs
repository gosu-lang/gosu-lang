package gw.specification.expressions.fieldAccessExpressions

uses gw.BaseVerifyErrantTest

class FieldAccessExpressionsTest extends BaseVerifyErrantTest {

  function testErrant_FieldAccessExpressionsTest() {
    processErrantType(Errant_FieldAccessExpressionsTest)
  }

  function testFieldAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    i = b.f0
    assertEquals(i, 30)
    i = b.sf1
    assertEquals(i, 31)
    i = B.sf1
    assertEquals(i, 31)
    i = b.f2
    assertEquals(i, 10)
    i = b.sf3
    assertEquals(i, 12)
    i = B.sf3
    assertEquals(i, 12)

    i = a.f0
    assertEquals(i, 30)
    i = a.sf1
    assertEquals(i, 31)
    i = A.sf1
    assertEquals(i, 31)
  }
}
