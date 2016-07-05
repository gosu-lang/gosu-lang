package gw.specContrib.classes

uses gw.BaseVerifyErrantTest

class ProtectedJavaMembersTest extends BaseVerifyErrantTest {

  function testProtectedFieldAndPropertyAccess() {
    var fu = new FUAlso()
    var f1 = fu.prop()
    var f2 = fu.field()
    var f3 = fu.fieldProp()
    assertTrue( f1 === f2 )
    assertTrue( f2 === f3 )
  }
}