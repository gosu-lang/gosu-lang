package gw.specContrib.statements.usesStatement

uses ClassWithOverriddenMethods#f1(int)
uses ClassWithOverriddenMethods#*

class StaticImportOnDemand2Test {
  function testMe_NonStatic() {
    f1("")
  }
}