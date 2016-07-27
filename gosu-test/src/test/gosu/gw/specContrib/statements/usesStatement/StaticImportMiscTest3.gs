package gw.specContrib.statements.usesStatement
uses gw.specContrib.statements.usesStatement.MuhJavaClass#staticFunc( Object )

uses gw.test.TestClass#assertTrue( boolean )

class StaticImportMiscTest3 {
  static function testMe() : String {
    return staticFunc( "hi" )
  }
}
