package gw.specContrib.classes

class Errant_BadConstructorOverrideInAnonymousClass {

  function test1() {
    var x = new Object() {
      construct(s:String) {}  //## issuekeys: MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT
    }
  }

}
