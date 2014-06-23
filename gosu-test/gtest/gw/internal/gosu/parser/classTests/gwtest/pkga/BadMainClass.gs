package gw.internal.gosu.parser.classTests.gwtest.pkga
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class BadMainClass {
  public function badReferenceToClassNotImported() {
    var a  = new SomeClassB()
  }
}
