package gw.internal.gosu.compiler.sample.enhancements

enhancement SimpleEnhancement : _Enhanced {

  function simpleMethod() : String {
    return "foo"
  }

  function simpleMethodWArg( s : String ) : String {
    return s
  }

  property get SimpleProperty() : String {
    return "foo"
  }

  function returnAProperty() : String {
    return this.SampleProp
  }

  function returnAMethod() : String {
    return this.sampleFunction()
  }

  function returnAMethodWithArgs( s : String ) : String {
    return this.sampleFunction() + s
  }

  function returnAMethodWithArgsPassedThrough( s : String ) : String {
    return this.sampleFunctionWithArgs( s )
  }

  function callsReturnAMethodWithArgsPassedThrough( s : String ) : String {
    return returnAMethodWithArgsPassedThrough( s )
  }

  static function simpleStaticMethod() : String {
    return "simpleStaticMethod"
  }

  static function simpleStaticMethodWArg( s : String ) : String {
    return s
  }

  static property get SimpleStaticProperty() : String {
    return "SimpleStaticProperty"
  }

  static function staticMultiArgMethod( s1 : String, s2 : String, s3 : String, s4 : String ) : String {
    return s1 + s2 + s3 + s4
  }

  function callsSimpleEnhancementMethodIndirectly() : String {
    return this.simpleMethod()
  }

  function callsSimpleEnhancementPropertyIndirectly() : String {
    return this.SimpleProperty
  }

  function callsSimpleEnhancementMethodDirectly() : String {
    return simpleMethod()
  }

  function callsSimpleEnhancementPropertyDirectly() : String {
    return SimpleProperty
  }

  function callsReturnAPropertyIndirectly() : String {
    return this.returnAProperty()
  }

  function callsReturnAMethodIndirectly() : String {
    return this.returnAMethod()
  }

  function callsReturnAMethodWithArgs( s : String ) : String {
    return this.returnAMethodWithArgs( s )
  }

  function genericMethodReturningType<T>( arg : T ) : Type {
    return T
  }

    static function basicStaticFunction() : String {
      return "func"
    }

    function basicFunction() : String {
      return "func"
    }

    property get Prop() : String {
      return "prop"
    }

    static property get StaticProp() : String {
      return "prop"
    }

    function parameterizedFunction<T>( s : T ) : Type {
      return T
    }

    static function staticParameterizedFunction<T>( s : T ) : Type {
      return T
    }

    function testBasicLocalInvocations() : String {
      var result = basicFunction() // func
      result += this.basicFunction() // funcfunc
      result += Prop  //funcfuncprop
      result += this.Prop  //funcfuncpropprop
      result += basicStaticFunction() //funcfuncproppropfunc
      result += this.basicStaticFunction() //funcfuncproppropfuncfunc
      result += _Enhanced.basicStaticFunction() //funcfuncproppropfuncfuncfunc
      result += StaticProp //funcfuncproppropfuncfuncfuncprop
      result += this.StaticProp //funcfuncproppropfuncfuncfuncpropprop
      result += _Enhanced.StaticProp //funcfuncproppropfuncfuncfuncproppropprop
      return result
    }

    function testGenericLocalInvocations() : String {
       var result = parameterizedFunction( "" ) as String //java.lang.String
       result += this.parameterizedFunction( "" )  //StringString
       result += parameterizedFunction<Object>( "" )  //StringStringObject
       result += this.parameterizedFunction<Object>( "" )  //StringStringObjectObject
       result += this.parameterizedFunction<Object>( "" )  //StringStringObjectObject
       result += staticParameterizedFunction( "" ) //StringStringObjectObjectString
       result += this.staticParameterizedFunction( "" ) //StringStringObjectObjectString
       result += _Enhanced.staticParameterizedFunction( "" ) //StringStringObjectObjectStringString
       result += staticParameterizedFunction<Object>( "" ) //StringStringObjectObjectStringStringStringObject
       result += this.staticParameterizedFunction<Object>( "" ) //StringStringObjectObjectStringStringObjectObject
       result += _Enhanced.staticParameterizedFunction<Object>( "" ) //StringStringObjectObjectStringStringObjectObjectObject
       return result
    }

  protected function protectedMethodFromSimpleEnhancement() : String {
    return "protected method from simple enhancement"
  }

  static protected function protectedStaticMethodFromSimpleEnhancement() : String {
    return "protected static method from simple enhancement"
  }

  protected property get ProtectedPropertyFromSimpleEnhancement() : String {
    return this._protectedField
  }

  protected property set ProtectedPropertyFromSimpleEnhancement(s : String ) {
    this._protectedField = s
  }

  protected static property get ProtectedStaticPropertyFromSimpleEnhancement() : String {
    return _Enhanced._staticProtectedField
  }

  protected static property set ProtectedStaticPropertyFromSimpleEnhancement(s : String ) {
    _Enhanced._staticProtectedField = s
  }

  function simpleEval() : String {
    return eval( "\"foo\"" ) as String
  }

  function evalWithCapture() : String {
    var x = "foo"
    return eval( "x" ) as String
  }

  function evalInBlock() : String {
    var x = \-> eval( "\"foo\"" ) as String
    return x()
  }

  function evalInBlockInBlock() : String {
    var x = \->\-> eval( "\"foo\"" ) as String
    var y = x()
    return y()
  }

  function blockInEval() : String {
    var x = eval( "\\-> \"foo\"" ) as block():String
    return x()
  }

  function thisInEval() : Object {
    return eval( "this" )
  }
}