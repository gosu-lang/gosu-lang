package gw.specification.statements.theUsesStatement

uses gw.specification.statements.theUsesStatement.a.A
uses gw.specification.statements.theUsesStatement.a.A#s1(int)
uses gw.specification.statements.theUsesStatement.a.A#S0
uses gw.specification.statements.theUsesStatement.a.A#s3
uses gw.specification.statements.theUsesStatement.a.b.*
uses gw.specification.statements.theUsesStatement.a.b.B#*

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
    var i = S0
    s2(1)
    s2(1.0)
    s1(1)
    s1(1.0) //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    s3(1)
    s3(1.0)
  }

}
