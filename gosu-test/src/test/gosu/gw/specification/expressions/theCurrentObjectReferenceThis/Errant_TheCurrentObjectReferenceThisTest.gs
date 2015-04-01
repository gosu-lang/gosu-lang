 package gw.specification.expressions.theCurrentObjectReferenceThis

class Errant_TheCurrentObjectReferenceThisTest {
  var foo : int
  class A {
    public var f : int
    function m() : boolean {
      return f == this.f
    }

    function m2() : boolean { return this.m() == m() }

    function m3() {
      var err = Errant_TheCurrentObjectReferenceThisTest.this.foo  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    }

  }

  function testThis0() {
    var a = new A()
    a.f = 8
    a.m()
    a.m2()
  }

}
