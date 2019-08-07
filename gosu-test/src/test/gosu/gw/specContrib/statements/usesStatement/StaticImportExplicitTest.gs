package gw.specContrib.statements.usesStatement

uses gw.specContrib.statements.usesStatement.Constants#ConstField1
uses gw.specContrib.statements.usesStatement.Constants#ConstField2
uses gw.specContrib.statements.usesStatement.Constants#ConstField3

uses gw.specContrib.statements.usesStatement.Constants#StaticFunc1( String )
uses gw.specContrib.statements.usesStatement.Constants#StaticFunc2( String )
uses gw.specContrib.statements.usesStatement.Constants#StaticFunc3( String )

uses gw.specContrib.statements.usesStatement.Constants#StaticProp1
uses gw.specContrib.statements.usesStatement.Constants#StaticProp2
uses gw.specContrib.statements.usesStatement.Constants#StaticProp3

uses gw.test.TestClass#assertTrue( String, boolean )

class StaticImportExplicitTest extends SuperClassForStaticImportTesting {
  static var ConstField2: String = "ConstField2: from This"
  
  static function StaticFunc2( s: String ) : String {
     return "StaticFunc2: from This" 
  }
  
  static property get StaticProp2() : String {
    return "StaticProp2: from This" 
  }
  
  function testMe_NonStatic() {
    assertTrue("line 29", ConstField1 == "ConstField1: from Super" )
    assertTrue("line 30", ConstField2 == "ConstField2: from This" )
    assertTrue("line 31", ConstField3 == "ConstField3: from Constants" )

    assertTrue("line 33", StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue("line 34", StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue("line 35", StaticFunc3( "" ) == "StaticFunc3: from Constants" )

    assertTrue("line 37", StaticProp1 == "StaticProp1: from Super" )
    assertTrue("line 38", StaticProp2 == "StaticProp2: from This" )
    assertTrue("line 39", StaticProp3 == "StaticProp3: from Constants" )
  }
  
  static function testMe_Static() {
    assertTrue("line 43", ConstField1 == "ConstField1: from Super" )
    assertTrue("line 44", ConstField2 == "ConstField2: from This" )
    assertTrue("line 45", ConstField3 == "ConstField3: from Constants" )

    assertTrue("line 47", StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue("line 48", StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue("line 49", StaticFunc3( "" ) == "StaticFunc3: from Constants" )

    assertTrue("line 51", StaticProp1 == "StaticProp1: from Super" )
    assertTrue("line 52", StaticProp2 == "StaticProp2: from This" )
    assertTrue("line 53", StaticProp3 == "StaticProp3: from Constants" )
  }
}
