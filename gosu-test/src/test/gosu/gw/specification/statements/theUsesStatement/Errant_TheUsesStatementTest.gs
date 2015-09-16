package gw.specification.statements.theUsesStatement

uses gw.specification.statements.theUsesStatement.a.A
uses gw.specification.statements.theUsesStatement.a.b.*

class Errant_TheUsesStatementTest {

  function testUsingBasic() {
    var o : Object
    o = new gw.specification.statements.theUsesStatement.a.A()
    o = new gw.specification.statements.theUsesStatement.a.b.B()
    o = new A()
    o = new B()
    o = new C()
    o = new InternalC()  //## issuekeys: MSG_CTOR_HAS_XXX_ACCESS, MSG_TYPE_HAS_XXX_ACCESS
    o = new LinkedList()
    o = new String()
  }

}
