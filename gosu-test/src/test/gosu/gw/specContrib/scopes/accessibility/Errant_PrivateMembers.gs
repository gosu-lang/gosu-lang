package gw.specContrib.scopes.accessibility

class Errant_PrivateMembers {
   static class A {
     var f: int
   }

   static class B1 extends A {
     construct() {
       this.f = 0
     }

     static class C {
       construct(b: B1) {
         b.f = 0
       }
     }
   }

   function test(b1: B1, b2: Errant_PrivateMembersB2) {
     b1.f = 0
     b2.f = 0     //## issuekeys: 'f' NOT ACCESSIBLE
   }
}