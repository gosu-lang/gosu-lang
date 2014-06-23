package gw.internal.gosu.regression
uses gw.test.TestClass

class InnerClassWithSameNameAsStaticMethodTest extends TestClass {

  function testCallStaticMethod() {
    assertNotNull(HasInnerClassWithSameNameAsStaticMethod.someInnerClass("foo"))    
  }

  function testConstructInnerClassInstance() {
    assertNotNull(new HasInnerClassWithSameNameAsStaticMethod.SomeInnerClass())  
  }

}
