package gw.specContrib.statements.usesStatement

uses Constants#*

class Errant_StaticImportDifferentMembersWithTheSameName3 {
  static function test() {
    ConstFieldOrMethod()
    MethodOrProperty()
    print(ConstFieldOrMethod)
    print(MethodOrProperty)
  }
}