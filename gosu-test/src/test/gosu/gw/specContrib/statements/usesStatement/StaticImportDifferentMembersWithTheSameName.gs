package gw.specContrib.statements.usesStatement

uses Constants#ConstFieldOrMethod
uses Constants#MethodOrProperty

uses gw.test.TestClass#assertEquals(Object, Object)

class StaticImportDifferentMembersWithTheSameName {
  static function test() {
    assertEquals("ConstFieldOrMethod field: from Constants", ConstFieldOrMethod)
    assertEquals("MethodOrProperty property: from Constants", MethodOrProperty)
  }
}