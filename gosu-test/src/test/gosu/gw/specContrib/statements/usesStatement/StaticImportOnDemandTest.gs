package gw.specContrib.statements.usesStatement

uses gw.specContrib.statements.usesStatement.Constants#*
uses gw.test.TestClass#assertTrue( String, boolean )

class StaticImportOnDemandTest extends SuperClassForStaticImportTesting {
  static var ConstField2: String = "ConstField2: from This"
  
  static function StaticFunc2( s: String ) : String {
     return "StaticFunc2: from This" 
  }
  
  static property get StaticProp2() : String {
    return "StaticProp2: from This" 
  }
  
  function testMe_NonStatic() {
    assertTrue("line 18", ConstField1 == "ConstField1: from Super" )
    assertTrue("line 19", ConstField2 == "ConstField2: from This" )
    assertTrue("line 20", ConstField3 == "ConstField3: from Constants" )
    
    assertTrue("line 22", StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue("line 23", StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue("line 24", StaticFunc3( "" ) == "StaticFunc3: from Constants" )
    
    assertTrue("line 26", StaticProp1 == "StaticProp1: from Super" )
    assertTrue("line 27", StaticProp2 == "StaticProp2: from This" )
    assertTrue("line 28", StaticProp3 == "StaticProp3: from Constants" )
  }
  
  static function testMe_Static() {
    assertTrue("line 32", ConstField1 == "ConstField1: from Super" )
    assertTrue("line 33", ConstField2 == "ConstField2: from This" )
    assertTrue("line 34", ConstField3 == "ConstField3: from Constants" )

    assertTrue("line 36", StaticFunc1( "" ) == "StaticFunc1: from Super" )
    assertTrue("line 37", StaticFunc2( "" ) == "StaticFunc2: from This" )
    assertTrue("line 38", StaticFunc3( "" ) == "StaticFunc3: from Constants" )

    assertTrue("line 40", StaticProp1 == "StaticProp1: from Super" )
    assertTrue("line 41", StaticProp2 == "StaticProp2: from This" )
    assertTrue("line 42", StaticProp3 == "StaticProp3: from Constants" )
  }
}
