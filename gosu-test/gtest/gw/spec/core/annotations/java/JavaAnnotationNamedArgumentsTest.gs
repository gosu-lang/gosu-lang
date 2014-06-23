package gw.spec.core.annotations.java

uses gw.test.TestClass
uses gw.lang.reflect.IAnnotationInfo

class JavaAnnotationNamedArgumentsTest extends TestClass {

  @SingleStringAnnotation(:value = "blah")
  function testBasicStuffWorks() {
    assertEquals("blah", #testBasicStuffWorks().MethodInfo.getAnnotation(SingleStringAnnotation).getFieldValue('value'))
  }
  
  @SingleAnnotationAnnotation(:value = new SingleStringAnnotation( :value = "blah" ))
  function testNestedNamesWorks() {
    var ann = #testNestedNamesWorks().MethodInfo.getAnnotation(SingleAnnotationAnnotation)
    var nestedAnnotation = ann.getFieldValue( "value" ) as IAnnotationInfo
    assertEquals( "blah", nestedAnnotation.getFieldValue( "value" ) )
  }

  function testMultiAnnotationVarArgsWork() {
    var ann = #f1().MethodInfo.getAnnotation(MultiValueAnnotation)
    assertEquals( "foo0", ann.getFieldValue( "str1" ) )
    assertEquals( "bar", ann.getFieldValue( "str2" ) )
    assertEquals( "doh", ann.getFieldValue( "str3" ) )
    
    ann = #f2().MethodInfo.getAnnotation(MultiValueAnnotation)
    assertEquals( "foo1", ann.getFieldValue( "str1" ) )
    assertEquals( "bar", ann.getFieldValue( "str2" ) )
    assertEquals( "doh", ann.getFieldValue( "str3" ) )

    ann = #f3().MethodInfo.getAnnotation(MultiValueAnnotation)
    assertEquals( "foo2", ann.getFieldValue( "str1" ) )
    assertEquals( "bar", ann.getFieldValue( "str2" ) )
    assertEquals( "doh", ann.getFieldValue( "str3" ) )

  }
  
  @MultiValueAnnotation( :str1 = "foo0", :str2 = "bar", :str3 = "doh" )
  function f1() {}

  @MultiValueAnnotation( :str1 = "foo1", :str3 = "doh", :str2 = "bar" )
  function f2() {}

  @MultiValueAnnotation( :str2 = "bar", :str1 = "foo2", :str3 = "doh" )
  function f3() {}

}