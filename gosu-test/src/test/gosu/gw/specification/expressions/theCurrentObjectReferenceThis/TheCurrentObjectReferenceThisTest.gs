package gw.specification.expressions.theCurrentObjectReferenceThis
uses gw.BaseVerifyErrantTest

class TheCurrentObjectReferenceThisTest extends BaseVerifyErrantTest {
  class A {
    public var f : int
    function m() : boolean {
       return f == this.f
    }

    function m2() : boolean { return this.m() == m() }

  }

  function testErrant_Errant_TheCurrentObjectReferenceThisTest() {
    processErrantType( Errant_TheCurrentObjectReferenceThisTest)
  }

  function testThis0() {
    var a = new A()
    a.f = 8
    assertTrue(a.m())
    assertTrue(a.m2())
  }

}
