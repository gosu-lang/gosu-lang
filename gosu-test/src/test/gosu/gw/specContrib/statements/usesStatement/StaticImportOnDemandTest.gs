package gw.specContrib.statements.usesStatement

uses gw.specContrib.statements.usesStatement.Constants#*
uses gw.test.TestClass#assertTrue( boolean )

class StaticImportOnDemandTest extends SuperClassForStaticImportTesting {
  static var ConstField2: String = "ConstField2: from This"
  
  static function StaticFunc2( s: String ) : String {
     return "StaticFunc2: from This" 
  }
  
  static property get StaticProp2() : String {
    return "StaticProp2: from This" 
  }
  
  function testMe_NonStatic() {
    assertTrue( ConstField1 == "ConstField1: from Super" )
    assertTrue( ConstField2 == "ConstField2: from This" )
    assertTrue( ConstField3 == "ConstField3: from Constants" )
    
    assertTrue( StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue( StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue( StaticFunc3( "" ) == "StaticFunc3: from Constants" )
    
    assertTrue( StaticProp1 == "StaticProp1: from Super" )
    assertTrue( StaticProp2 == "StaticProp2: from This" )
    assertTrue( StaticProp3 == "StaticProp2: from Constants" )
  }
  
  static function testMe_Static() {
    assertTrue( ConstField1 == "ConstField1: from Super" )
    assertTrue( ConstField2 == "ConstField2: from This" )
    assertTrue( ConstField3 == "ConstField3: from Constants" )

    assertTrue( StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue( StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue( StaticFunc3( "" ) == "StaticFunc3: from Constants" )

    assertTrue( StaticProp1 == "StaticProp1: from Super" )
    assertTrue( StaticProp2 == "StaticProp2: from This" )
    assertTrue( StaticProp3 == "StaticProp2: from Constants" )
  }
}