package gw.internal.gosu.parser.annotation.validation
uses gw.test.TestClass
uses gw.lang.parser.resources.Res
uses gw.lang.parser.statements.IFunctionStatement
uses java.util.ArrayList
uses gw.lang.parser.statements.IPropertyStatement
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.gs.IGosuClass
uses gw.config.CommonServices

/**
 * Tests the basic annotation validation code
 */
class CoreAnnotationValidationTest extends TestClass {

  function testBasicUsageSiteValidationAnnotationWorks() {
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // no validation stuff in os gosu
      return;
    }

    assertFalse( HasValidatingAnnotations.Type.Valid )

    var classStmt = HasValidatingAnnotations.Type.ClassStatement
    assertTrue( classStmt.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    
    var functions = classStmt.Location.Children.where( \ i -> i.ParsedElement typeis IFunctionStatement )
    assertEquals( 3, functions.size() )
    for( func in functions ) {
      assertTrue( func.ParsedElement.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    }

    var properties = classStmt.Location.Children.where( \ i -> i.ParsedElement typeis IPropertyStatement )
    assertEquals( 2, properties.size() )
    for( prop in properties ) {
      assertTrue( prop.ParsedElement.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    }
  }

  function testBasicUsageSiteValidationAnnotationWorksOnInnerClasses() {
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // no validation stuff in os gosu
      return;
    }

    assertFalse( HasValidatingAnnotations.Inner.Type.Valid )

    var classStmt = HasValidatingAnnotations.Inner.Type.ClassStatement
    assertTrue( classStmt.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    
    var functions = classStmt.Location.Children.where( \ i -> i.ParsedElement typeis IFunctionStatement )
    assertEquals( 3, functions.size() )
    for( func in functions ) {
      assertTrue( func.ParsedElement.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    }

    var properties = classStmt.Location.Children.where( \ i -> i.ParsedElement typeis IPropertyStatement )
    assertEquals( 2, properties.size() )
    for( prop in properties ) {
      assertTrue( prop.ParsedElement.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_BAD_IDENTIFIER_NAME ) )
    }
  }

  function testBasicCallSiteValidationAnnotationReferenceWorks() {
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      // no validation stuff in os gosu
      return;
    }

    var t = TypeSystem.getByFullName( "gw.internal.gosu.parser.annotation.validation.HasCallSiteValidatingAnnotationReferences" ) as IGosuClass
    assertFalse( t.Valid )
    for( line in 10..15 ) {
      assertTrue( t.ParseResultsException.ParseIssues.hasMatch(\ i -> i.MessageKey == Res.MSG_ANY and i.Line == line ) )
    }
  }

}
