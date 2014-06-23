package gw.internal.gosu.compiler.enhancements
uses gw.test.TestClass
uses gw.internal.gosu.compiler.sample.enhancements._Enhanced
uses gw.testharness.Disabled

class EnhancementExpansionOperatorTest extends TestClass {


  function testObjectExpansionWorksWithNonStaticPropertyOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.SimpleProperty )
  }

  function testObjectExpansionWorksWithNonStaticMethodOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleMethod() )
  }

  function testObjectExpansionWorksWithNonStaticMethodWArgOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleMethodWArg( "foo" ) )
  }

  function testObjectExpansionWorksWithNonStaticGenericMethodWArgOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {String, String}, lst*.parameterizedFunction( "foo" ) )
  }

  function testObjectExpansionWorksWithStaticPropertyOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"SimpleStaticProperty", "SimpleStaticProperty"}, lst*.SimpleStaticProperty )
  }

  function testObjectExpansionWorksWithStaticMethodOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"simpleStaticMethod", "simpleStaticMethod"}, lst*.simpleStaticMethod() )
  }

  function testObjectExpansionWorksWithStaticMethodWArgOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleStaticMethodWArg( "foo" ) )
  }

  function testObjectExpansionWorksWithStaticGenericMethodWArgOnList() {
    var lst = { new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {String, String}, lst*.staticParameterizedFunction( "foo" ) )
  }

  function testObjectExpansionWorksWithNonStaticPropertyOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.SimpleProperty )
  }

  function testObjectExpansionWorksWithNonStaticMethodOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleMethod() )
  }

  function testObjectExpansionWorksWithNonStaticMethodWArgOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleMethodWArg( "foo" ) )
  }

  function testObjectExpansionWorksWithNonStaticGenericMethodWArgOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {String, String}, lst*.parameterizedFunction( "foo" ) )
  }

  function testObjectExpansionWorksWithStaticPropertyOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"SimpleStaticProperty", "SimpleStaticProperty"}, lst*.SimpleStaticProperty )
  }

  function testObjectExpansionWorksWithStaticMethodOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"simpleStaticMethod", "simpleStaticMethod"}, lst*.simpleStaticMethod() )
  }

  function testObjectExpansionWorksWithStaticMethodWArgOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {"foo", "foo"}, lst*.simpleStaticMethodWArg( "foo" ) )
  }

  function testObjectExpansionWorksWithStaticGenericMethodWArgOnArray() {
    var lst = new _Enhanced[]{ new _Enhanced(), new _Enhanced() }
    assertArrayEquals( {String, String}, lst*.staticParameterizedFunction( "foo" ) )
  }
}
