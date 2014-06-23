package gw.internal.gosu.regression
uses java.lang.Integer
uses java.util.ArrayList
uses gw.test.TestClass

class BridgeMethodForJavaSuperClassTest extends TestClass {

  function testBeanMethodCallExpansionWithArrayAndThenList() {
     var b = new BridgeMethodForJavaTestClass()
     assertEquals( 0, b.compare( new java.util.Date(), new java.util.Date() ) )
  }
  
}
