package gw.lang.spec_old.classes

class Errant_IllegalAccessToPrivateClassMemberTest {

  construct() {
    new OtherClass().getPrivateClass().publicFunc()
  }

}
