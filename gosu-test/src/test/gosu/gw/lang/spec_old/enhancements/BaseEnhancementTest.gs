package gw.lang.spec_old.enhancements
uses gw.lang.parser.resources.Res
uses java.util.Date
uses java.util.concurrent.Callable

uses gw.lang.reflect.gs.IGosuEnhancement

class BaseEnhancementTest extends gw.test.TestClass
{
  function testEnhancementsCannotEnhanceOtherEnhancements()
  {
    var be = BadEnhancement.Type as IGosuEnhancement
    assertFalse( be.isValid() )
    assertTrue( be.ParseResultsException.ParseExceptions.hasMatch( \ pi -> pi.MessageKey == Res.MSG_ENHANCEMENTS_CANNOT_ENHANCE_OTHER_ENHANCEMENTS ) )
  }

  function testEnhancementsAvailableOnGosuArrays()
  {
    var arr = new SampleClass[]{new SampleClass("test"), new SampleClass("test")}
    assertEquals(2, arr.where( \ elt -> elt.Data == "test" ).length )
  }

  function testAnonymousInnerClassesInEnhancementsAreProperlyRestrictedWithRespectToEnclosingType() {
    var type = Errant_BaseEnhancementTestEnhancement.Type
    assertFalse( type.Valid )
    var issues = type.ParseResultsException.ParseIssues
    assertEquals( 5, issues.Count )
    assertTrue( issues.hasMatch( \ i -> i.Line == 11 and i.MessageKey == Res.MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS ) )
    assertTrue( issues.hasMatch( \ i -> i.Line == 21 and i.MessageKey == Res.MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS ) )
    assertTrue( issues.hasMatch( \ i -> i.Line == 31 and i.MessageKey == Res.MSG_CANNOT_REFERENCE_ENCLOSING_METHODS_WITHIN_ENHANCEMENTS ) )
    assertTrue( issues.hasMatch( \ i -> i.Line == 41 and i.MessageKey == Res.MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS ) )
    assertTrue( issues.hasMatch( \ i -> i.Line == 51 and i.MessageKey == Res.MSG_CANNOT_REFERENCE_ENCLOSING_PROPERTIES_WITHIN_ENHANCEMENTS ) )
  }

  function testVariableCaptureInAnonymousInnerClassesWorksProperly() {
    assertEquals( this, captureThis().call() )
    assertEquals( "test", captureLocalString().call() )
    assertEquals( this, this.captureThis().call() )
    assertEquals( "test", this.captureLocalString().call() )
  }

  function captureThis() : Callable<BaseEnhancementTest> {
    var capturedThis = this
    return new Callable<BaseEnhancementTest>() {
      override function call() : BaseEnhancementTest {
        return capturedThis
      }
    }
  }

  function captureLocalString() : Callable<String> {
    var capturedString = "test"
    return new Callable<String>() {
      override function call() : String {
        return capturedString
      }
    }
  }
}
