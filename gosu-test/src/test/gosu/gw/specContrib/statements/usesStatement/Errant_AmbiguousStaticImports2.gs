package gw.specContrib.statements.usesStatement

uses ConstantsClass2#ConstField1
uses ConstantsClass2#StaticFunc1(String)
uses ConstantsClass2#StaticProp1

uses ConstantsClass#ConstField1 // issuekeys: MSG_VARIABLE_ALREADY_DEFINED
uses ConstantsClass#StaticFunc1(String)
uses ConstantsClass#StaticProp1

class Errant_AmbiguousStaticImports2 {
  function foo() {
    StaticFunc1("")    // issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    print(ConstField1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
    print(StaticProp1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
  }
}
