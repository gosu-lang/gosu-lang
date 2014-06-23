package gw.lang.parser
uses gw.test.TestClass
uses java.lang.Class
uses java.util.ArrayList
uses gw.lang.parser.expressions.*
uses gw.lang.parser.statements.*
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.interval.IntegerInterval
uses gw.lang.reflect.gs.IGosuClass

class FunctionPropertyConflictTest extends TestClass {

  override function beforeTestClass() {
    assertFalse( Errant_FunctionPropertyConflicts.Type.Valid ) 
  }
  
  function testFunctionsAfterVarPropertyAreFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 11 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 14, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IFunctionStatement, 15, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 18..22 )
  }

  function testReadFunctionAfterReadOnlyVarPropertyAreFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 27 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 30, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 33..38 )
  }

  function testWriteFunctionAfterWritePropertyAreFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 43 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IPropertyStatement, 45 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 48, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 51..56 )
  }

  function testReadFunctionAfterReadPropertyAreFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 61 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IPropertyStatement, 63 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 66, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 69..74 )
  }

  function testReadWriteFunctionAfterReadWritePropertyAreFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 79 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IPropertyStatement, 81 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IPropertyStatement, 82 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 85, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IFunctionStatement, 86, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 89..93 )
  }

  function testVarPropertyAfterSetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 99 ).hasParseExceptions() )
    assertErrorAt( IVarStatement, 102, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 105..109 )
  }

  function testReadOnlyVarPropertyAfterSetterIsNotFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 115 ).hasParseExceptions() )
    assertNoErrorAt( IVarStatement, 120..120 )
    assertNoErrorAt( IFunctionStatement, 121..125 )
  }

  function testVarPropertyAfterGetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 131 ).hasParseExceptions() )
    assertErrorAt( IVarStatement, 134, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 137..141 )
  }

  function testVarPropertyAfterGetterAndSetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 147 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 148 ).hasParseExceptions() )
    assertErrorAt( IVarStatement, 151, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 154..158 )
  }

  function testSetPropertyAfterGetterAndSetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 164 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 165 ).hasParseExceptions() )
    assertErrorAt( IPropertyStatement, 168, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 171..175 )
  }

  function testGetPropertyAfterGetterAndSetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 181 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 182 ).hasParseExceptions() )
    assertErrorAt( IPropertyStatement, 185, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 188..192 )
  }

  function testGetAndSetPropertyAfterGetterAndSetterIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 198 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 199 ).hasParseExceptions() )
    assertErrorAt( IPropertyStatement, 202, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IPropertyStatement, 203, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertNoErrorAt( IFunctionStatement, 206..210 )
  }

  function testVarPropertyThenFunctionGetterAndSetterReificaitonConflictIsFlagged() {
    assertFalse( getParsedElementAtLine( IVarStatement, 216 ).hasParseExceptions() )
    assertErrorAt( IFunctionStatement, 219, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IFunctionStatement, 220, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION )
    assertNoErrorAt( IFunctionStatement, 223..223 )
  }

  function testFunctionGetterAndSetterThenVarPropertyReificaitonConflictIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 229 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 230 ).hasParseExceptions() )
    assertErrorAt( IVarStatement, 233, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IVarStatement, 233, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION )
    assertNoErrorAt( IFunctionStatement, 236..236 )
  }

  function testFunctionGetterAndSetterThenPropertyGetterAndSetterReificaitonConflictIsFlagged() {
    assertFalse( getParsedElementAtLine( IFunctionStatement, 242 ).hasParseExceptions() )
    assertFalse( getParsedElementAtLine( IFunctionStatement, 243 ).hasParseExceptions() )
    assertErrorAt( IPropertyStatement, 246, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT )
    assertErrorAt( IPropertyStatement, 247, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION )
    assertNoErrorAt( IFunctionStatement, 250..250 )
  }
  
  function testInnerClassesDoNotConflictWithOuterClassNamespace() {
    for( i in 1..6 ) {
      var inner = Errant_FunctionPropertyConflicts.Type.getInnerClass("Inner1") as IGosuClass
      assertFalse( inner.ClassStatement.hasParseExceptions() )
    }
  }

  //-----------------------------------------------------------------
  //  Utility Methods  
  //-----------------------------------------------------------------  

  function assertErrorAt<T extends IParsedElement>( t : Class<T>, line : int, key : ResourceKey){
    var elt = getParsedElementAtLine(t, line)
    assertTrue( elt.ParseExceptions.hasMatch(\ i -> i.MessageKey == key ) )
  }

  function assertNoErrorAt<T extends IParsedElement>( t : Class<T>, lines : IntegerInterval){
    for( line in lines ) { 
      var elt = getParsedElementAtLine(t, line)
      assertFalse( elt.hasParseExceptions() )
    }
  }
  
  function getParsedElementAtLine<T extends IParsedElement>( t : Class<T>, line : int ) : T {
    var lst = getParsedElementOfType(t).where(\ i -> i.LineNum == line )
    if( lst.Empty ) 
    {
      throw "Could not find a ${t.Name} on line ${line}"
    }
    else
    {
      return lst.first() 
    }
  }
  
  function getParsedElementOfType<T extends IParsedElement>( t : Class<T> ) : List<T> {
    var lst = new ArrayList<T>()
    Errant_FunctionPropertyConflicts.Type.ClassStatement.getContainedParsedElementsByType(t, lst)
    return lst
  }

}
