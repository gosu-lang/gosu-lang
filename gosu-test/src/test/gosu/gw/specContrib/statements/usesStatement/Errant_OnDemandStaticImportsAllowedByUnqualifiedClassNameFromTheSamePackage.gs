package gw.specContrib.statements.usesStatement

uses Constants#*

class Errant_OnDemandStaticImportsAllowedByUnqualifiedClassNameFromTheSamePackage {
  function foo() {
    print(ConstField1)
    print(StaticFunc1(""))
    print(StaticProp1)
  }
}