package gw.lang.spec_old.classes.test
uses gw.testharness.DoNotVerifyResource
uses gw.lang.spec_old.classes.OtherClass

@DoNotVerifyResource
class Errant_IllegalAccessToInternalClassMemberTest {

  construct() {
    new OtherClass().callme().publicFunc()
  }

}
