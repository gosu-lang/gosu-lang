package gw.specContrib.enhancements

uses gw.test.TestClass
uses gw.lang.reflect.gs.IGosuClass
uses java.math.BigDecimal

class AvoidBridgeMethodFromEnhancementTest extends TestClass {
  function testNoBridgeMethodsGeneratedForEnhancement() {
    // this code just loads the classes, which would otherwise
    // result in a JVM verify error if the bridge method were there
    var f: IFoo = new FooBar()
    var s: String = f.hello()
  }
}