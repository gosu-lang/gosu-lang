package gw.specContrib.classes

class Errant_BadConstructorOverrideInAnonymousClass {

  function test1() {
    var x = new Object() {
      construct(s:String) {}  //## issuekeys: MSG_ANON_CTOR_PARAMS_CONFLICT_WITH_CALL_SITE
    }
  }

}
