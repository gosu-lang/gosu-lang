package gw.specification.expressions.propertyAccessExpression

uses gw.BaseVerifyErrantTest
uses java.lang.NullPointerException

class PropertyAccessExpressionsTest extends BaseVerifyErrantTest {

  function testErrant_PropertyAccessExpressionsTest() {
    processErrantType(Errant_PropertyAccessExpressionsTest)
  }

  function testPropertyAccessNPE() {
    var a : A  = null
    var i : int  = 0
    try { i = a.p0 }
    catch(e : NullPointerException) { i = -1}
    assertEquals(i, -1)
    i = 0
    try { a.p0 = 1 }
    catch(e : NullPointerException) { i = -1}
    assertEquals(i, -1)
  }

  function testPropertyCompoundAssignment() {
    var c : C  = new C(10)
    var i : int  = 0

    var action = \  -> { i++
                        return c
                      }
    action().p++
    assertEquals(1, i)
    assertEquals(c.p, 11)

   }

  function testSuperProperty() {
    var b : B  = new B(10)
    b.superProp()
    assertEquals(b.p0, 40)
   }

  function testPropertyGetAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    i = b.p0
    assertEquals(i, 30)
    i = b.sp1
    assertEquals(i, 31)
    i = B.sp1
    assertEquals(i, 31)
    i = b.p2
    assertEquals(i, 10)
    i = b.sp3
    assertEquals(i, 12)
    i = B.sp3
    assertEquals(i, 12)

    i = a.p0
    assertEquals(i, 30)
    i = a.sp1
    assertEquals(i, 31)
    i = A.sp1
    assertEquals(i, 31)
    
    i = b.p00
    assertEquals(i, 30)
    i = b.sp11
    assertEquals(i, 31)
    i = B.sp11
    assertEquals(i, 31)
    i = b.p22
    assertEquals(i, 10)
    i = b.sp33
    assertEquals(i, 12)
    i = B.sp33
    assertEquals(i, 12)

    i = a.p00
    assertEquals(i, 30)
    i = a.sp11
    assertEquals(i, 31)
    i = A.sp11
    assertEquals(i, 31)
  }

  function testPropertySetAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    b.p0 = 8
    assertEquals(b._p0, 8)
    b.sp1 = 8
    assertEquals(b._sp1, 8)
    B.sp1 = 8
    assertEquals(B._sp1, 8)
    b.p2 = 8
    assertEquals(b._p2, 8)
    b.sp3 = 8
    assertEquals(b._sp3, 8)
    B.sp3 = 8
    assertEquals(B._sp3, 8)

    a.p0 = 8
    assertEquals(a._p0, 8)
    a.sp1 = 8
    assertEquals(a._sp1, 8)
    A.sp1 = 8
    assertEquals(A._sp1, 8)

    b.p00 = 8
    assertEquals(b._p00, 8)
    b.sp11 = 8
    assertEquals(b._sp11, 8)
    B.sp11 = 8
    assertEquals(B._sp11, 8)
    b.p22 = 8
    assertEquals(b._p22, 8)
    b.sp33 = 8
    assertEquals(b._sp33, 8)
    B.sp33 = 8
    assertEquals(B._sp33, 8)

    a.p00 = 8
    assertEquals(a._p00, 8)
    a.sp11 = 8
    assertEquals(a._sp11, 8)
    A.sp11 = 8
    assertEquals(A._sp11, 8)
  }
}
