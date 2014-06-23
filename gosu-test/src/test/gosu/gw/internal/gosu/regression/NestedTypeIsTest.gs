package gw.internal.gosu.regression
uses gw.test.TestClass

class NestedTypeIsTest extends TestClass   {

  construct() {

  }

  function testPathUsingString() {
    assertEquals("test-string", HasNestedTypeIs.useNestedTypeIs("test-string"))  
  }
  
  function testPathUsingSuperObject() {
    assertEquals("on-super", HasNestedTypeIs.useNestedTypeIs(new HasNestedTypeIs.FooSuper()))  
  }
  
  function testPathUsingSubObject() {
    assertEquals("only-on-sub", HasNestedTypeIs.useNestedTypeIs(new HasNestedTypeIs.FooSub()))   
  }
  
  function testPathUsingOtherObject() {
    assertEquals("other", HasNestedTypeIs.useNestedTypeIs(5))  
  }
}
