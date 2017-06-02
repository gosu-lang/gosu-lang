package gw.specContrib.statements.usesStatement

uses gw.specContrib.statements.usesStatement.Constants#StaticFunc1(String)
uses gw.test.TestClass#assertTrue(boolean)

class StaticImportExplicit2Test extends SuperClassForStaticImportTesting {
  private static function StaticFunc1(s: String): String {
    return "StaticFunc1: from this"
  }

  static function testMe() {

    assertTrue( StaticFunc1( "" ) == "StaticFunc1: from this" )
  }
}
