package gw.internal.gosu.regression
uses gw.test.TestClass

class GosuMethodInfoReflectionTest extends TestClass {

  construct() {

  }
  
  function testVoidMethod() {
    var methodInfo = GosuMethodInfoReflectionTest.Type.TypeInfo.getMethod("voidMethod", {})    
    methodInfo.CallHandler.handleCall(this, {})
  }
  
  function testStringMethod() {
    var methodInfo = GosuMethodInfoReflectionTest.Type.TypeInfo.getMethod("stringMethod", {})    
    assertEquals("foo", methodInfo.CallHandler.handleCall(this, {}))    
  }
  
  function testGenericMethod() {
    var methodInfo = GosuMethodInfoReflectionTest.Type.TypeInfo.Methods.firstWhere(\m -> m.DisplayName == "genericMethod")
    assertEquals("fooObject", methodInfo.CallHandler.handleCall(this, {"foo"}))  
  }
  
  function testStringProp() {
    var methodInfo = GosuMethodInfoReflectionTest.Type.TypeInfo.getMethod("@StringProp", {})
    assertEquals("stringProp", methodInfo.CallHandler.handleCall(this, {}))  
  }
  
  function testBooleanProp() {
    var methodInfo = GosuMethodInfoReflectionTest.Type.TypeInfo.getMethod("@BooleanProp", {})
    assertEquals(true, methodInfo.CallHandler.handleCall(this, {}))  
  }
  
  function voidMethod() {
    
  }
  
  function stringMethod() : String {
    return "foo"  
  }
  
  reified function genericMethod<T>( arg : T) : String {
    return arg + T.Type.RelativeName 
  }
  
  property get StringProp() : String {
    return "stringProp"  
  }
  
  property get BooleanProp() : boolean {
    return true  
  }

}
