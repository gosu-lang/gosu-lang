package gw.internal.gosu.regression
uses gw.test.TestClass

class StringTemplateTest extends TestClass {

  function testStaticFunctionWithStringTemplate() {
    assertEquals("foo-gotcha-bar", valueOfTemplate())  
  }
  
  static function valueOfTemplate() : String {
    var valueOfTemplate = "gotcha"
    return "foo-${valueOfTemplate}-bar"  
  }

}
