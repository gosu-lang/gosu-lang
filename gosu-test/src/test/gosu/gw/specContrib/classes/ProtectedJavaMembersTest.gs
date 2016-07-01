package gw.specContrib.classes

uses gw.BaseVerifyErrantTest

class ProtectedJavaMembersTest extends BaseVerifyErrantTest {

  function testProtectedFieldAndPropertyAccess() {
    var fu = new FUAlso()
    var f1 = fu.foo()
    var f2 = fu.bar()
    assertTrue( f1 === f2 )
  }
}