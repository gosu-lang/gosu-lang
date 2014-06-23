package gw.internal.gosu.regression
uses gw.test.TestClass

class InnerGenericClassCanRefOuterConstructorTest extends TestClass {

  function testInnerCanBeConstructed() {
    assertNotNull(new InnerGenericClassCanRefOuterConstructorTest.Test.SubClass2(10))
  }
  
  static abstract class Test<T> { 
    private construct() { } 
    static class SubClass2<A> extends Test<A> { 
      construct(n : int) {
      } 
    } 
  }

}
