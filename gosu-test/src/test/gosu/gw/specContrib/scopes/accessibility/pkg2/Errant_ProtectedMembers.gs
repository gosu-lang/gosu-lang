package gw.specContrib.scopes.accessibility.pkg2

uses gw.specContrib.scopes.accessibility.pkg1.Errant_ProtectedMembersA

class Errant_ProtectedMembers {
  class InnerB extends Errant_ProtectedMembersA {}

  // IDE-1837
  function test() {
    var b1 = new InnerB()
    b1.protectedFun()

    var b2 = new Errant_ProtectedMembersB()
    b2.protectedFun()

    var a1 = new Errant_ProtectedMembersA()
    a1.protectedFun()     //## issuekeys: 'protectedFun' IS NOT ACCESSIBLE

    var a2 = new Errant_ProtectedMembersA() {}
    a2.protectedFun()
  }
}