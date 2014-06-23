package gw.internal.gosu.parser.generics
uses gw.test.TestClass
uses java.lang.CharSequence

class ContextTypeThroughReturnTypeInferenceTest extends TestClass {
  
  var _lastT : Type

  function testBasicFunctionalityWorksInBeanMethodCalls() {
    var t : String = this.genericFunction()
    assertEquals( String, _lastT )
  }

  function testBasicFunctionalityWorksInMethodCalls() {
    var t : String = genericFunction()
    assertEquals( String, _lastT )
  }

  function testExplicitParamertizationDominatesReturnTypesInBeanMethodCalls() {
    var t : CharSequence = this.genericFunction<String>()
    assertEquals( String, _lastT )
  }

  function testExplicitParamertizationDominatesReturnTypesInMethodCalls() {
    var t : CharSequence = genericFunction<String>()
    assertEquals( String, _lastT )
  }

  function testParameterDominatesReturnTypesInBeanMethodCalls() {
    var t : CharSequence = this.genericFunctionWithArg(String)
    assertEquals( String, _lastT )
  }

  function testParameterDominatesReturnTypesInMethodCalls() {
    var t : CharSequence = genericFunctionWithArg(String)
    assertEquals( String, _lastT )
  }

  function testBasicFunctionalityWorksInBeanMethodCallsList() {
    var t : List<String> = this.genericFunctionList()
    assertEquals( String, _lastT )
  }

  function testBasicFunctionalityWorksInMethodCallsList() {
    var t : List<String> = genericFunctionList()
    assertEquals( String, _lastT )
  }

  function testExplicitParamertizationDominatesReturnTypesInBeanMethodCallsList() {
    var t : List<CharSequence> = this.genericFunctionList<String>()
    assertEquals( String, _lastT )
  }

  function testExplicitParamertizationDominatesReturnTypesInMethodCallsList() {
    var t : List<CharSequence> = genericFunctionList<String>()
    assertEquals( String, _lastT )
  }

  function testParameterDominatesReturnTypesInBeanMethodCallsList() {
    var t : List<CharSequence> = this.genericFunctionWithArgList(String)
    assertEquals( String, _lastT )
  }

  function testParameterDominatesReturnTypesInMethodCallsList() {
    var t : List<CharSequence> = genericFunctionWithArgList(String)
    assertEquals( String, _lastT )
  }

  function genericFunction<T>() : T {
    _lastT = T
    return null
  }
  
  function genericFunctionWithArg<T>(tPrime : Type<T>) : T {
    _lastT = T
    return null
  }
  
  function genericFunctionList<T>() : List<T> {
    _lastT = T
    return null
  }
  
  function genericFunctionWithArgList<T>(tPrime : Type<T>) : List<T> {
    _lastT = T
    return null
  }

}
