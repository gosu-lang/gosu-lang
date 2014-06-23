package gw.spec.core.annotations.javadoc

uses gw.test.TestClass
uses gw.lang.reflect.IAnnotatedFeatureInfo
uses java.lang.Exception
uses java.lang.RuntimeException

/**
 * This class provides a breadthwise test of JavaDoc-style annotations.  More
 * exhaustive testing is done in the generated tests.
 */
class JavaDocAnnotationInstanceTest extends TestClass {

  function testDeprecatedOnAllFeatureTypes() {
    assertDeprecatedMsg( DeprecatedClass.Type.TypeInfo, "foo foo foo" )
    assertDeprecatedMsg( DeprecatedIFace.Type.TypeInfo, "foo foo foo" )
    assertDeprecatedMsg( this.Type.TypeInfo.getConstructor({}), "foo foo foo" )
    assertDeprecatedMsg( this.Type.TypeInfo.getMethod("deprecatedMethod", {}), "foo foo foo" )
    assertDeprecatedMsg( this.Type.TypeInfo.getProperty("DeprecatedProperty"), "foo foo foo" )
  } 

  function testMultiLineDeprecated() {
    assertDeprecatedMsg( this.Type.TypeInfo.getMethod("multilineDeprecated", {}), "foo foo foo\nfoo foo foo" )
  }

  function testNonDeprecatedAfterDeprecatedIsNotDeprecated() {
    assertDoesNotHaveAnnotation( InnerTypeNotDeprecated.Type.TypeInfo, Deprecated )    
  }
  
  function testExceptionRelative() {
    assertDoesNotHaveAnnotation( ExceptionClassRelative.Type.TypeInfo, Throws )
    assertDoesNotHaveAnnotation( ExceptionIFaceRelative.Type.TypeInfo, Throws )
    assertThrows( this.Type.TypeInfo.getConstructor({String}), "foo foo foo", LocalThrowable )
    assertThrows( this.Type.TypeInfo.getMethod("exceptionMethodRelative", {}), "foo foo foo", LocalThrowable )
    assertThrows( this.Type.TypeInfo.getProperty("ExceptionPropertyRelative"), "foo foo foo", LocalThrowable )
  }

  function testThrowsRelative() {
    assertDoesNotHaveAnnotation( ThrowsClassRelative.Type.TypeInfo, Throws )
    assertDoesNotHaveAnnotation( ThrowsIFaceRelative.Type.TypeInfo, Throws )
    assertThrows( this.Type.TypeInfo.getConstructor({String, String}), "foo foo foo", LocalThrowable )
    assertThrows( this.Type.TypeInfo.getMethod("throwsMethodRelative", {}), "foo foo foo", LocalThrowable )
    assertThrows( this.Type.TypeInfo.getProperty("ThrowsPropertyRelative"), "foo foo foo", LocalThrowable )
  }

  function testThrowsFullyQualified() {
    assertDoesNotHaveAnnotation( ThrowsClassFullyQualified.Type.TypeInfo, Throws )
    assertDoesNotHaveAnnotation( ThrowsIFaceFullyQualified.Type.TypeInfo, Throws )
    assertThrows( this.Type.TypeInfo.getConstructor({String, String, String}), "foo foo foo", java.lang.RuntimeException )
    assertThrows( this.Type.TypeInfo.getMethod("throwsMethodFullyQualified", {}), "foo foo foo", java.lang.RuntimeException )
    assertThrows( this.Type.TypeInfo.getProperty("ThrowsPropertyFullyQualified"), "foo foo foo", java.lang.RuntimeException )
  }
  
  function testThrowsWithBadExceptionDoesNotCauseThrowsAnnotations() {
    assertDoesNotHaveAnnotation( ThrowsClassFullyQualified.Type.TypeInfo, Throws )
    assertDoesNotHaveAnnotation( ThrowsIFaceFullyQualified.Type.TypeInfo, Throws )
    assertDoesNotHaveAnnotation( this.Type.TypeInfo.getConstructor({String, String, String, String}), Throws )
    assertDoesNotHaveAnnotation( this.Type.TypeInfo.getMethod("throwsMethodBadExceptionName", {}), Throws )
    assertDoesNotHaveAnnotation( this.Type.TypeInfo.getProperty("ThrowsPropertyBadExceptionName"), Throws )
  }
    
  function testMultipleThrows() {
    var annotations = this.Type.TypeInfo.getProperty("MultipleThrows").Annotations
    print( annotations.map(\ i -> i.Instance + ":" + i.Description ) )
    assertTrue( annotations.hasMatch(\ i -> i.Instance typeis Throws and i.Instance.ExceptionDescription() == "foo foo foo" and i.Instance.ExceptionType() == RuntimeException ) )
    assertTrue( annotations.hasMatch(\ i -> i.Instance typeis Throws and i.Instance.ExceptionDescription() == "bar bar bar" and i.Instance.ExceptionType() == LocalThrowable ) )
    assertTrue( annotations.hasMatch(\ i -> i.Instance typeis Throws and i.Instance.ExceptionDescription() == "doh doh doh" and i.Instance.ExceptionType() == LocalThrowable ) )
  }
  
  function testParam() {
    assertDoesNotHaveAnnotation( ParamClass.Type.TypeInfo, Param )
    assertDoesNotHaveAnnotation( ParamIFace.Type.TypeInfo, Param )
    assertParam( this.Type.TypeInfo.getConstructor({String, String, String, String, String}), "foo", "foo foo" )
    assertParam( this.Type.TypeInfo.getMethod("paramMethod", {}), "foo", "foo foo"  )
    assertParam( this.Type.TypeInfo.getProperty("ParamProperty"), "foo", "foo foo" )
  }

  function testReturn() {
    assertDoesNotHaveAnnotation( ReturnClass.Type.TypeInfo, Param )
    assertDoesNotHaveAnnotation( ReturnIFace.Type.TypeInfo, Param )
    assertParam( this.Type.TypeInfo.getConstructor({String, String, String, String, String}), "foo", "foo foo" )
    assertParam( this.Type.TypeInfo.getMethod("paramMethod", {}), "foo", "foo foo"  )
    assertParam( this.Type.TypeInfo.getProperty("ParamProperty"), "foo", "foo foo" )
  }

  //----------------------------------------------------------------------------------------------
  // Asserts
  //----------------------------------------------------------------------------------------------

  function assertDeprecatedMsg(  fi : IAnnotatedFeatureInfo, msg : String ) {
    var d = fi.getAnnotation(Deprecated)
    assertNotNull( "Should have been deprecated", d )
    assertEquals( msg, (d.Instance as Deprecated).value() )
  }
  
  function assertThrows(  fi : IAnnotatedFeatureInfo, msg : String, exceptionType : Type ) {
    var d = fi.getAnnotation(Throws)
    assertNotNull( "Should have found a throws annotation", d )
    assertEquals( exceptionType as java.lang.Class, (d.Instance as Throws).ExceptionType() as java.lang.Class )
    assertEquals( msg, (d.Instance as Throws).ExceptionDescription() )
  }
  
  function assertParam(  fi : IAnnotatedFeatureInfo, param : String, msg : String ) {
    var d = fi.getAnnotation(gw.lang.Param)
    assertNotNull( "Should have found a param annotation", d )
    assertEquals( param, (d.Instance as gw.lang.Param).FieldName() )
    assertEquals( msg, (d.Instance as gw.lang.Param).FieldDescription() )
  }
  
  function assertDoesNotHaveAnnotation(  fi : IAnnotatedFeatureInfo, anno : Type ) {
    var d = fi.getAnnotation(anno)
    assertNull( d )
  }
  
  //----------------------------------------------------------------------------------------------
  // Features
  //----------------------------------------------------------------------------------------------
  
  /* @deprecated foo foo foo */
  class DeprecatedClass{}

  /* @deprecated foo foo foo */
  class DeprecatedIFace{}
  
  /* @deprecated foo foo foo */
  construct(){}

  /* @deprecated foo foo foo */
  function deprecatedMethod(){}

  /* @deprecated foo foo foo */
  property get DeprecatedProperty() : String { return null }

  /* @deprecated foo foo foo
                 foo foo foo */
  function multilineDeprecated(){}

  class InnerTypeNotDeprecated{}

  /* @exception LocalThrowable foo foo foo */
  class ExceptionClassRelative{}

  /* @exception LocalThrowable foo foo foo */
  class ExceptionIFaceRelative{}
  
  /* @exception LocalThrowable foo foo foo */
  construct( s : String ){}

  /* @exception LocalThrowable foo foo foo */
  function exceptionMethodRelative(){}

  /* @exception LocalThrowable foo foo foo */
  property get ExceptionPropertyRelative() : String { return null }

  /* @throws LocalThrowable foo foo foo */
  class ThrowsClassRelative{}

  /* @throws LocalThrowable foo foo foo */
  class ThrowsIFaceRelative{}
  
  /* @throws LocalThrowable foo foo foo */
  construct( s : String, s2 : String ){}

  /* @throws LocalThrowable foo foo foo */
  function throwsMethodRelative(){}

  /* @throws LocalThrowable foo foo foo */
  property get ThrowsPropertyRelative() : String { return null }

  /* @throws java.lang.RuntimeException foo foo foo */
  class ThrowsClassFullyQualified{}
 
  /* @throws java.lang.RuntimeException foo foo foo */
  class ThrowsIFaceFullyQualified{}
  
  /* @throws java.lang.RuntimeException foo foo foo */
  construct( s : String, s2 : String , s3 : String ){}

  /* @throws java.lang.RuntimeException foo foo foo */
  function throwsMethodFullyQualified(){}

  /* @throws java.lang.RuntimeException foo foo foo */
  property get ThrowsPropertyFullyQualified() : String { return null }

  /* @throws java.lang.BadExceptionName foo foo foo */
  class ThrowsClassBadExceptionName{}
 
  /* @throws java.lang.BadExceptionName foo foo foo */
  class ThrowsIFaceBadExceptionName{}
  
  /* @throws java.lang.BadExceptionName foo foo foo */
  construct( s : String, s2 : String , s3 : String , s4 : String ){}

  /* @throws java.lang.BadExceptionName foo foo foo */
  function throwsMethodBadExceptionName(){}

  /* @throws java.lang.BadExceptionName foo foo foo */
  property get ThrowsPropertyBadExceptionName() : String { return null }


  /* @throws java.lang.RuntimeException foo foo foo 
     @throws LocalThrowable bar bar bar
   * @throws LocalThrowable doh doh doh  */
  property get MultipleThrows() : String { return null }

  /* @param foo foo foo */
  class ParamClass{}

  /* @param foo foo foo */
  class ParamIFace{}
  
  /* @param foo foo foo */
  construct( s : String, s1 : String , s3 : String , s4 : String , s5 : String ){}

  /* @param foo foo foo */
  function paramMethod(){}

  /* @param foo foo foo */
  property get ParamProperty() : String { return null }

  /* @return foo foo foo */
  class ReturnClass{}

  /* @return foo foo foo */
  class ReturnIFace{}
  
  /* @return foo foo foo */
  construct( s : String, s1 : String , s3 : String , s4 : String , s5 : String, s6 : String ){}

  /* @return foo foo foo */
  function returnMethod(){}

  /* @return foo foo foo */
  property get ReturnProperty() : String { return null }

}
