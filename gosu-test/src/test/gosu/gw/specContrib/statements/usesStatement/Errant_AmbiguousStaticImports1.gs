package gw.specContrib.statements.usesStatement

uses Constants#ConstField1
uses Constants#StaticFunc1(String)
uses Constants#StaticProp1

uses Constants2#ConstField1 // issuekeys: MSG_VARIABLE_ALREADY_DEFINED
uses Constants2#StaticFunc1(String)
uses Constants2#StaticProp1

class Errant_AmbiguousStaticImports1 {
  function foo() {
    StaticFunc1("")    // issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    print(ConstField1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
    print(StaticProp1) // issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
  }
}
