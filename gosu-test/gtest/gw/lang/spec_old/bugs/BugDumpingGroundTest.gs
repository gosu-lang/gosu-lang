package gw.lang.spec_old.bugs
uses gw.test.TestClass
uses java.lang.Runnable
uses gw.lang.parser.resources.Res

class BugDumpingGroundTest extends TestClass {

    var bk1(p : int) = \ p : int -> {}
    var bk2(p : int) = \ p -> {}
    var bk3(p : int) as Prop3 = \ p : int -> {}
    var bk4(p : int) as Prop4 = \ p -> {}
    static var bk5(p : int) = \ p : int -> {}
    static var bk6(p : int) = \ p -> {}
    static var bk7(p : int) as Prop7 = \ p : int -> {}
    static var bk8(p : int) as Prop8 = \ p -> {}
    
  function testBlocksAndShorthandPropertySyntaxPlayNicelyPL_7095() {
    //The code above just needs to compile
  }
  
  function testJavaInterfaceTypesIncludeMethodsFromObject() {
    var l : List<Object> = {}
    assertEquals( "[]", l.toString() )
    var r : Runnable = new TestRunnable()
    assertEquals( "This is a runnable", r.toString())
  }
  
  function testMultipleAbstractDefinitionsOfPropertiesCausesErrors(){
    assertFalse( Errant_DuplicateAbstractPropertiesDefined.Type.Valid )
    var issues = Errant_DuplicateAbstractPropertiesDefined.Type.ParseResultsException.ParseIssues
    assertEquals( 12, issues.Count )
    issues.hasMatch(\ i -> i.Line == 8 and i.MessageKey == Res.MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED )
    issues.hasMatch(\ i -> i.Line == 11 and i.MessageKey == Res.MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED )
    issues.hasMatch(\ i -> i.Line == 14 and i.MessageKey == Res.MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED )
    issues.hasMatch(\ i -> i.Line == 19 and i.MessageKey == Res.MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED )
  }

  function testBadFunctionNameThatMatchesTypeWhenFunctionStartsWithIsOrGetDoesNotParsePL_7288() {
    assertFalse( Errant_BadStaticMethodReference.Type.Valid )
    var issues = Errant_BadStaticMethodReference.Type.ParseResultsException.ParseIssues
    assertEquals( 1, issues.Count )
    assertTrue( issues.hasMatch(\ i -> i.Line == 12 and i.MessageKey == Res.MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT ) )
  }
  
  function testBadWildCardImportsAreFlaggedAsErrorsPL_7265() {
    assertFalse( Errant_BadImports.Type.Valid )
    var issues = Errant_BadImports.Type.ParseResultsException.ParseIssues
    assertEquals( 2, issues.Count )
    assertTrue( issues.hasMatch(\ i -> i.Line == 3 and i.MessageKey == Res.MSG_BAD_NAMESPACE ) )
    assertTrue( issues.hasMatch(\ i -> i.Line == 4 and i.MessageKey == Res.MSG_BAD_NAMESPACE ) )
  }
  
  class TestRunnable implements Runnable {
    override function run() : void {
      print( "Yay" )
    }
    override function toString() : String {
      return "This is a runnable" 
    }
  }

}
