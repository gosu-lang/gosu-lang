package gw.internal.gosu.compiler.enhancements

uses gw.internal.gosu.compiler.sample.enhancements.*
uses java.lang.NullPointerException
uses gw.testharness.InProgress
uses gw.testharness.KnownBreak

class EnhancementTest extends gw.test.TestClass
{
  static var _setFlagCalled : boolean as SetFlagCalled = false

  // =======================================================================
  // Regular Enhancements
  // =======================================================================

  static function setFlag() : String {
    SetFlagCalled = true
    return ""
  }

  function testSimpleMethod() {
    assertEquals( "foo", new _Enhanced().simpleMethod() )
  }

  function testSimpleProperty() {
    assertEquals( "foo",   new _Enhanced().SimpleProperty )
  }

  function testPropertyThatAccessThis() {
    assertEquals( "A field",  new _Enhanced().returnAProperty() )
  }

  function testNoArgMethodThatAccessThis() {
    assertEquals( "A function",  new _Enhanced().returnAMethod() )  
  }

  function testMultiArgMethodThatAccessThis() {
    assertEquals( "A function with multi-args",  new _Enhanced().returnAMethodWithArgs( " with multi-args" ) )
  }

  function testSimpleStaticMethod() {
    assertEquals( "simpleStaticMethod",  _Enhanced.simpleStaticMethod() )
  }

  function testSimpleStaticProperty() {
    assertEquals( "SimpleStaticProperty",  _Enhanced.SimpleStaticProperty )
  }

  function testStaticMultiArgMethod() {
    assertEquals( "this is a test",  _Enhanced.staticMultiArgMethod("this", " is", " a", " test") )
  }

  function testMethodThatPassesArgsThrough() {
    assertEquals( "sampleFunctionWithArgs called with Test",  new _Enhanced().returnAMethodWithArgsPassedThrough( "Test" ) )
  }

  function testCallsReturnAMethodWithArgsPassedThrough() {
    assertEquals( "sampleFunctionWithArgs called with Test",  new _Enhanced().callsReturnAMethodWithArgsPassedThrough( "Test" ) )
  }

  function testCallsSimpleEnhancementMethodIndirectly() {
    assertEquals( "foo",  new _Enhanced().callsSimpleEnhancementMethodIndirectly() )
  }

  function testCallsSimpleEnhancementPropertyIndirectly() {
    assertEquals( "foo",  new _Enhanced().callsSimpleEnhancementPropertyIndirectly() )
  }

  function testCallsSimpleEnhancementMethodDirectly() {
    assertEquals( "foo",  new _Enhanced().callsSimpleEnhancementMethodDirectly() )
  }

  function testCallsSimpleEnhancementPropertyDirectly() {
    assertEquals( "foo",  new _Enhanced().callsSimpleEnhancementPropertyDirectly() )
  }

  function testMethodOnNullValue() {
    var x : _Enhanced
    assertCausesException(\ -> x.simpleMethod(), NullPointerException )
  }

  function testMethodOnNullValueWArg() {
    var x : _Enhanced
    assertCausesException(\ -> x.returnAMethodWithArgs( setFlag() ), NullPointerException )
    assertTrue( SetFlagCalled )
  }

  // =======================================================================
  // Generics and Enhancements
  // =======================================================================

  // Regular enhancement w/ generic methods

  function testGenericMethodWithString() {
    assertEquals( String,  new _Enhanced().genericMethodReturningType( "" ) )
  }

  function testGenericMethodExplicitlyParameterizedWithString() {
    assertEquals( Object,  new _Enhanced().genericMethodReturningType<Object>( "" ) )
  }

  function testGenericMethodExplicitlyParameterizedWithNull() {
    assertEquals( String,  new _Enhanced().genericMethodReturningType<String>( null ) )
  }

  function testGenericMethodExplicitlyParameterizedWithNull2() {
    assertEquals( Object,  new _Enhanced().genericMethodReturningType<Object>( null ) )
  }

  function testBasicLocalInvocations() {
    var x = new _Enhanced()
    assertEquals( "funcfuncproppropfuncfuncfuncproppropprop", x.testBasicLocalInvocations() )
  }
  function testStaticParameterizedFunctionWExplicitParameterizationIndirectly() {
    var x = new _Enhanced()
    assertEquals( Object, x.staticParameterizedFunction<Object>("") )
  }
  function testStaticParameterizedFunctionInirectly() {
    var x = new _Enhanced()
    assertEquals( String, x.staticParameterizedFunction("") )
  }
  function testStaticParameterizedFunctionWExplicitParameterizationDirectly() {
    var x = new _Enhanced()
    assertEquals( Object, _Enhanced.staticParameterizedFunction<Object>("") )
  }

  function testStaticParameterizedFunctionDirectly() {
    var x = new _Enhanced()
    assertEquals( String, _Enhanced.staticParameterizedFunction("") )
  }
  function testBasicParameterizedFunctionWExplicitParameterization() {
    var x = new _Enhanced()
    assertEquals( Object, x.parameterizedFunction<Object>("") )
  }
  function testBasicParameterizedFunction() {
    var x = new _Enhanced()
    assertEquals( String, x.parameterizedFunction("") )
  }
  function testStaticPropIndirectly() {
    var x = new _Enhanced()
    assertEquals( "prop", x.StaticProp )
  }
  function testStaticPropDirectly() {
    var x = new _Enhanced()
    assertEquals( "prop", _Enhanced.StaticProp )
  }

  function testStaticFunctionIndirectly() {
    var x = new _Enhanced()
    assertEquals( "func", x.basicStaticFunction() )
  }

  function testStaticFunctionDirectly() {
    var x = new _Enhanced()
    assertEquals( "func", _Enhanced.basicStaticFunction() )
  }

  function testBasicFunction() {
    var x = new _Enhanced()
    assertEquals( "func", x.basicFunction() )
  }

  function testBasicProp() {
    var x = new _Enhanced()
    assertEquals( "prop", x.Prop )
  }

  function testGenericLocalInvocations() {
    var x = new _Enhanced()
    var tmp = x.testGenericLocalInvocations()
    assertNotNull( tmp )
  }

  // Generic enhancement

  function testSimpleMethodOnGenericEnhancement() {
    assertEquals( "foo",  new _GenericEnhanced().simpleMethod() )
  }

  function testMethodThatReturnsGenericTypeOfGenericEnhancement() {
    assertEquals( Object,  new _GenericEnhanced().returnsGenericTypeVar() )
  }
  
  function testMethodThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( String,  new _GenericEnhanced<String>().returnsGenericTypeVar() )
  }

  function testMethodThatReturnsGenericTypeOfGenericEnhancement3() {
    var x : _GenericEnhanced<Object> = new _GenericEnhanced<String>()
    assertEquals( Object,  x.returnsGenericTypeVar() )
  }

  function testMethodWithArgThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().returnsGenericTypeVarWithArg( "" ) )
  }

  function testMethodWithArgThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( String,  new _GenericEnhanced<String>().returnsGenericTypeVarWithArg( "" ) )
  }

  function testMethodWithArgOnGenericEnhancement() {
    assertEquals( "yay",  new _GenericEnhanced().methodWithArg( "yay" ) )
  }

  function testMethodWitVarThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().methodWithVar() )
  }

  function testMethodWitVarThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWithVar() )
  }

  function testMethodWitVarAndArgThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().methodWithVarAndArg( "asdf" ) )
  }

  function testMethodWitVarAndArgThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWithVarAndArg( "asdf" ) )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }
  
  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }
  
  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParam1() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamThatReturnsMethodParameterization() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParamt2() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamThatReturnsMethodParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParamt3() {
    assertEquals( String,  new _GenericEnhanced().methodWTypeParamThatReturnsMethodParameterization<String>() )
  }
  
  function testMethodWTypeParamThatReturnsMethodsTypeParam4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsMethodParameterization() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParam5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsMethodParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParam6() {
    assertEquals( String,  new _GenericEnhanced<Object>().methodWTypeParamThatReturnsMethodParameterization<String>() )
  }
  
  function testMethodWTypeParamThatReturnsMethodsTypeParam7() {
    assertEquals( Object,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsMethodParameterization() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParam8() {
    assertEquals( Object,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsMethodParameterization<Object>() )
  }

  function testMethodWTypeParamThatReturnsMethodsTypeParam9() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamThatReturnsMethodParameterization<String>() )
  }
  
  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }
  
  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }
  
  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }
  
  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam1() {
    assertEquals( String,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParamt2() {
    assertEquals( Object,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParamt3() {
    assertEquals( String,  new _GenericEnhanced().methodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }
  
  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam4() {
    assertEquals( String,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam6() {
    assertEquals( String,  new _GenericEnhanced<Object>().methodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }
  
  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam7() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam8() {
    assertEquals( Object,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testMethodWTypeParamAndArgThatReturnsMethodsTypeParam9() {
    assertEquals( String,  new _GenericEnhanced<String>().methodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }
  
  //-----------------------------------------------------------------
  // Direct enhancement type var propogation
  //-----------------------------------------------------------------
  
  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  } 
  
  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }
  
  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod1() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod3() {
    assertEquals( String,  new _GenericEnhanced().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  } 
  
  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod6() {
    assertEquals( String,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  }
  
  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod7() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod9() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod1() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod3() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  } 
  
  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  }
  
  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod7() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testDirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod9() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }
  
  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }
  
  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod1() {
    assertEquals( String,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod3() {
    assertEquals( String,  new _GenericEnhanced().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }
  
  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod4() {
    assertEquals( String,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod6() {
    assertEquals( String,  new _GenericEnhanced<Object>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }
  
  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod7() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testDirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod9() {
    assertEquals( String,  new _GenericEnhanced<String>().directlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }

  //-----------------------------------------------------------------
  // Indirect enhancement type var propogation
  //-----------------------------------------------------------------

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }
  
  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }
 
  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamThatReturnsEnhancementParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod1() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod3() {
    assertEquals( String,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod6() {
    assertEquals( String,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod7() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamExplicitlySetThatReturnsGenericTypeOfMethod9() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamExplicitlySetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod1() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod3() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod7() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<Object>() )
  }

  function testIndirectInvocationOfMethodWTypeParamNotSetThatReturnsGenericTypeOfMethod9() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamNotSetThatReturnsMethodParameterization<String>() )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement1() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement2() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement3() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement4() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement6() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement7() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement8() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfGenericEnhancement9() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsEnhancementParameterization<String>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod1() {
    assertEquals( String,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod2() {
    assertEquals( Object,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod3() {
    assertEquals( String,  new _GenericEnhanced().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod4() {
    assertEquals( String,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod5() {
    assertEquals( Object,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod6() {
    assertEquals( String,  new _GenericEnhanced<Object>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod7() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod8() {
    assertEquals( Object,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<Object>( "" ) )
  }

  function testIndirectInvocationOfMethodWTypeParamAndArgThatReturnsGenericTypeOfMethod9() {
    assertEquals( String,  new _GenericEnhanced<String>().indirectlyInvokesMethodWTypeParamAndArgThatReturnsMethodParameterization<String>( "" ) )
  }

  function testBlockToBlockToBlockToQ() {
    var x = new _GenericEnhanced<Boolean>()
    var blk1 = x.returnsBlockToBlockToBlockToQ<String>()
    var blk2 = blk1() 
    var blk3 = blk2() 
    assertEquals( String, blk3() )
  }

  function testBlockToBlockToBlockToT() {
    var x = new _GenericEnhanced<String>()
    var blk1 = x.returnsBlockToBlockToBlockToT()
    var blk2 = blk1() 
    var blk3 = blk2() 
    assertEquals( String, blk3() )
  }

  function testBlockToBlockToQ() {
    var x = new _GenericEnhanced<Boolean>()
    var blk = x.returnsBlockToBlockToQ<String>()
    var blk2 = blk()
    assertEquals( String, blk2() )
  }

  function testBlockToBlockToT() {
    var x = new _GenericEnhanced<String>()
    var blk1 = x.returnsBlockToBlockToBlockToT()
    var blk2 = blk1() 
    var blk3 = blk2()
    assertEquals( String, blk3() )
  }

  function testBlockToT() {
    var x = new _GenericEnhanced<String>()
    var blk = x.returnsBlockToT()
    assertEquals( String, blk() )
  }

  function testCallsBasicGenericEnhancementMethodIndirectlyReturnsQWithT() {
    var x = new _GenericEnhanced<String>()
    assertEquals( String, x.callsGenericMethodReturnsQIndirectlyWithT<Boolean>() )
  } 

  function testCallsBasicGenericEnhancementMethodIndirectlyReturnsQWithQ() {
    var x = new _GenericEnhanced<String>()
    assertEquals( Boolean, x.callsGenericMethodReturnsQIndirectlyWithQ<Boolean>() )
  } 

  function testCallsBasicGenericEnhancementMethodReturnsQWithT() {
    var x = new _GenericEnhanced<String>()
    assertEquals( String, x.callsGenericMethodReturnsQDirectlyWithT<Boolean>() )
  } 

  function testCallsBasicGenericEnhancementMethodReturnsQWithQ() {
    var x = new _GenericEnhanced<String>()
    assertEquals( Boolean, x.callsGenericMethodReturnsQDirectlyWithQ<Boolean>() )
  } 

  function testBasicGenericEnhancementMethodReturnsQ() {
    var x = new _GenericEnhanced<String>()
    assertEquals( Boolean, x.basicGenericMethodReturnsQ<Boolean>() )
  }

  function testBasicGenericEnhancementMethodReturnsT() {
    var x = new _GenericEnhanced<String>()
    assertEquals( String, x.basicGenericMethodReturnsT() )
  }

  // =======================================================================
  // Blocks and Enhancements
  // =======================================================================

  function testBlockInvocation() {
    var x = new _BlockEnhanced<String>(){ "a", "ab", "abc" }
    assertEquals( {1, 2, 3},  x.testBlockInvocation( \ s -> s.length() ) )
  }

  function testDeclareAndExecuteBlock() {
    var x =  new _BlockEnhanced<String>()
    assertEquals( "declaredAndInvokedInEhancement",  x.declareAndExecuteBlock() )
  }

  function testDeclaredBlockNoCapture() {
    var x =  new _BlockEnhanced<String>()
    var blk = x.produceBlockWithNoCapture()
    assertEquals( "declaredInEnhancement",  blk() )
  }
 
  function testDeclaredBlockLocalVarCapture() {
    var x =  new _BlockEnhanced<String>()
    var pairOfBlocks = x.produceBlockWithLocalVarCaputure()
    var setLocalVal = pairOfBlocks.First
    var getLocalVal = pairOfBlocks.Second
    assertEquals( 0, getLocalVal() )
    setLocalVal()
    assertEquals( 42, getLocalVal() )
  }

  function testDeclaredBlockPropertyCapture() {
    var x =  new _BlockEnhanced<String>() 
    var blk = x.produceBlockWithPropertyCaputure()
    assertEquals( 42, blk() )
  }

  function testDeclaredBlockIndirectPropertyCapture() {
    var x =  new _BlockEnhanced<String>()
    var blk = x.produceBlockWithIndirectPropertyCaputure()
    assertEquals( 42, blk() )
  }

  function testDeclaredBlockFunctionCapture() {
    var x =  new _BlockEnhanced<String>() 
    var blk = x.produceBlockWithPropertyCaputure()
    assertEquals( 42, blk() )
  }

  function testDeclaredBlockIndirectFunctionCapture() {
    var x =  new _BlockEnhanced<String>()
    var blk = x.produceBlockWithIndirectPropertyCaputure()
    assertEquals( 42, blk() )
  }

  function testEvalWorksInEnhancement() {
      assertEquals( "foo", new _Enhanced().simpleEval() )
  }

  function testEvalCaptureWorksInEnhancement() {
      assertEquals( "foo", new _Enhanced().evalWithCapture() )
  }

  function testEvalInBlock() {
      assertEquals( "foo", new _Enhanced().evalInBlock() )
  }

  function testEvalInBlockInBlock() {
      assertEquals( "foo", new _Enhanced().evalInBlockInBlock() )
  }

  function testBlockInEval() {
      assertEquals( "foo", new _Enhanced().blockInEval() )
  }

  function testThisInInEval() {
    var foo = new _Enhanced()
    assertEquals( foo, foo.thisInEval() )
  }

  function testDirectReferenceOfEnhancementWithinEnhancedClassWorks() {
    var foo = new _Enhanced()
    assertEquals( "foo", foo.callsSimpleMethod() )
  }
}