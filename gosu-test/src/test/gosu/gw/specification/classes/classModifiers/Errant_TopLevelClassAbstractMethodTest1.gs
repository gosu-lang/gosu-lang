package gw.specification.classes.classModifiers

class Errant_TopLevelClassAbstractMethodTest1 {
  function m0() {}
  abstract function m1()  //## issuekeys: MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS
}
