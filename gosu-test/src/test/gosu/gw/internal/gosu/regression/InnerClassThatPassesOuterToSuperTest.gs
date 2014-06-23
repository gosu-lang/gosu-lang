package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.testharness.KnownBreak

class InnerClassThatPassesOuterToSuperTest extends TestClass {
  
  //@KnownBreak("PL-15909", "eng/diamond/pl2/active/core", "smckinney")
  function testInnerClassPassingOuterToSuper() {
    assertEquals("InnerClassThatPassesOuterToSuperTest", new InnerClassSub().ClassName)  
  }
  
  class InnerClassSuper {
    var _testClass : TestClass
    construct(arg : TestClass) {
      _testClass = arg  
    }
  
    property get ClassName() : String {
      return _testClass.ClassName    
    }
  }
  
  class InnerClassSub extends InnerClassSuper {
    construct() {
      super(outer) 
    }
  }

}
