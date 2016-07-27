package gw.specContrib.statements.usesStatement
uses gw.specContrib.statements.usesStatement.MuhJavaClass#staticFunc

uses gw.test.TestClass#assertTrue( boolean )

class StaticImportMiscTest {
  static function testMe() : String {
    return staticFunc( "hi" )
  }
}
