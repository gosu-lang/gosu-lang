package gw.specContrib.statements.usesStatement

uses Constants#ConstFieldOrMethod
uses Constants#MethodOrProperty

class Errant_StaticImportDifferentMembersWithTheSameName {
  static function test() {
    ConstFieldOrMethod() //## issuekeys: MSG_NO_SUCH_FUNCTION
    MethodOrProperty()   //## issuekeys: MSG_NO_SUCH_FUNCTION
    print(ConstFieldOrMethod)
    print(MethodOrProperty)
  }
}