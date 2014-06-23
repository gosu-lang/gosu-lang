package gw.internal.gosu.regression
uses gw.test.TestClass

class EvalInInitializersTest extends TestClass {

  construct() {

  }
  
  function testStaticInitializerWithEval() {
    assertEquals("static-value", HasEvalInStaticInitializer.StaticVar)   
  }
  
  function testInstanceInitializerWithEval() {
    assertEquals("instance-value", new HasEvalInStaticInitializer().InstanceVar)  
  }

}
