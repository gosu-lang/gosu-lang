package gw.internal.gosu.compiler.sample.enhancements

class _Enhanced
{
  var _sampleField : String as SampleProp = "A field"
  protected var _protectedField : String = "protected field"
  protected static var _staticProtectedField : String = "static protected field"
  var _protectedPropField : String = "protected property"
  var _i_protectedPropField : int = 42
  static var _staticProtectedPropField : String = "static protected property"
  static var _i_staticProtectedPropField : int = 42
  protected var _i_protectedField : int = 42
  protected static var _i_staticProtectedField : int = 42
  var _protectedConstructor : Boolean as readonly ProtectedConstructorCalled = false

  construct() {
  }
  
  protected construct( i : int ) {
    _protectedConstructor = true
  }

  function sampleFunction() : String {
    return "A function"
  }

  function sampleFunctionWithArgs( s : String ) : String {
    return "sampleFunctionWithArgs called with " + s
  }

  function test() : String {
    return this.SampleProp
  }

  function invokesPropertyIndirectly() : String {
    return this.SampleProp
  }

  protected function protectedMethod() : String {
    return "protected function"
  }

  protected static function staticProtectedMethod() : String {
    return "static protected function"
  }

  protected property get ProtectedProperty() : String {
    return _protectedPropField
  }

  protected property set ProtectedProperty( value : String ) {
    _protectedPropField = value
  }

  protected static property get StaticProtectedProperty() : String {
    return _staticProtectedPropField
  }

  protected static property set StaticProtectedProperty( value : String ) {
    _staticProtectedPropField = value
  }

  protected function i_protectedMethod() : int {
    return 42
  }

  protected static function i_staticProtectedMethod() : int {
    return 42
  }

  protected property get i_ProtectedProperty() : int {
    return _i_protectedPropField
  }

  protected property set i_ProtectedProperty( value : int ) {
    _i_protectedPropField = value
  }

  protected static property get i_StaticProtectedProperty() : int {
    return _i_staticProtectedPropField
  }

  protected static property set i_StaticProtectedProperty( value : int ) {
    _i_staticProtectedPropField = value
  }

  function callsSimpleMethod() : String {
    return simpleMethod()
  }

}