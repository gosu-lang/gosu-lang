package gw.specContrib.statements.usesStatement

uses Constants#ConstFieldOrMethod()
uses Constants#MethodOrProperty()

uses gw.test.TestClass#assertEquals(Object, Object)

class StaticImportDifferentMembersWithTheSameName2 {
  static function test() {
    assertEquals("ConstFieldOrMethod method: from Constants", ConstFieldOrMethod())
    assertEquals("MethodOrProperty method: from Constants" , MethodOrProperty())
  }
}