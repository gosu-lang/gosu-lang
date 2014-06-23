package gw.specification.classes.method_Declarations

class Errant_FinalMethodTest extends A {
  final function fm() {}  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL, MSG_MISSING_OVERRIDE_MODIFIER
}
