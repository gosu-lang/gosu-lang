package gw.specContrib.scopes.accessibility.pkg2

uses gw.specContrib.scopes.accessibility.pkg1.Errant_ProtectedMembersA
uses gw.specContrib.scopes.accessibility.pkg1.Errant_ProtectedMembersAA

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

  class SubClass extends Errant_ProtectedMembersA {
    function test() {
      // IDE-2282
      var b1: Errant_ProtectedMembersA
      b1.protectedFun()

      var b2: Errant_ProtectedMembersAA
      b2.protectedFun()  //## issuekeys: 'protectedFun' IS NOT ACCESSIBLE
    }

    class InnerNotSubClass {
      function test(b: Errant_ProtectedMembersA) {
        b.protectedFun()
      }
    }
  }
}
