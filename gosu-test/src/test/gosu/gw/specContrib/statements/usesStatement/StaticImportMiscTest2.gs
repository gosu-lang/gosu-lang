package gw.specContrib.statements.usesStatement

uses gw.specContrib.statements.usesStatement.MuhClass#*

class StaticImportMiscTest2 {
  static function testMe() : Type {
    return staticFunc( "hi" )
  }

  static function testMe2() : String {
    return staticFunc2( "hi" )
  }
}
