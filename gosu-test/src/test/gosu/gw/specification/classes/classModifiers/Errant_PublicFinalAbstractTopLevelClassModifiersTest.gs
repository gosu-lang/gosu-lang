package gw.specification.classes.classModifiers

uses gw.specification.classes.classModifiers.p0.C0
uses gw.specification.classes.classModifiers.p0.C1
uses gw.specification.classes.classModifiers.p0.C2
uses gw.specification.classes.classModifiers.p0.C3
uses gw.specification.classes.classModifiers.p0.C4

class Errant_PublicFinalAbstractTopLevelClassModifiersTest {

  class Sub0 extends C3 {}    //## issuekeys: MSG_CANNOT_EXTEND_FINAL_TYPE
  class Sub1 extends C4 {}


  function testPublicDefaultAbstractModifier() {
    var c0 = new C0()
    var c1 = new C1()
    var c2 = new C2()    //## issuekeys: MSG_CTOR_HAS_XXX_ACCESS, MSG_TYPE_HAS_XXX_ACCESS
    var c4 = new C4()   //## issuekeys: MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS
    var s1 = new Sub1()
  }

}
