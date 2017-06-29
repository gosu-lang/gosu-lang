package gw.specContrib.statements.usesStatement

uses Constants#ConstField1
uses Constants#StaticFunc1
uses Constants#StaticFunc2(String)
uses Constants#StaticProp1

class Errant_ExplicitStaticImportsAllowedByUnqualifiedClassNameFromTheSamePackage {
  function foo() {
    print(ConstField1)
    print(StaticFunc1(""))
    print(StaticFunc2(""))
    print(StaticProp1)
  }
}