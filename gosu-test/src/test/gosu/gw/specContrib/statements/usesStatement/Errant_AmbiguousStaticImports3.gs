package gw.specContrib.statements.usesStatement

uses ConstantsClass2#*

uses ConstantsClass#*

class Errant_AmbiguousStaticImports3 {
  function foo() {
    StaticFunc1("")    // issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    print(ConstField1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
    print(StaticProp1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
  }
}
