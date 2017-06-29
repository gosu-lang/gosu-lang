package gw.specContrib.statements.usesStatement

uses Constants#ConstFieldOrMethod()
uses Constants#MethodOrProperty()

class Errant_StaticImportDifferentMembersWithTheSameName2 {
  static function test() {
    print(ConstFieldOrMethod) //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    print(MethodOrProperty)   //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    ConstFieldOrMethod()
    MethodOrProperty()
  }
}