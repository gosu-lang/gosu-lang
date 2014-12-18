package gw.specification.expressions.fieldAccessExpressions

class Errant_FieldAccessExpressionsTest {
  function testFieldAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    i = b.f0
    i = b.sf1  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sf1
    i = b.f2
    i = b.sf3  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sf3

    i = a.f0
    i = a.sf1  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = A.sf1
    i = a.f2  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    i = a.sf3  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    i = A.sf3  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }
}
