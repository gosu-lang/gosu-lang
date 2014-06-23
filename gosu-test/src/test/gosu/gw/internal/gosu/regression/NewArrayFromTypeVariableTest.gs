package gw.internal.gosu.regression
uses gw.test.TestClass

class NewArrayFromTypeVariableTest extends TestClass {
  
  function testCreateNewArrayFromTypeParameterWithJavaType() {
    var result = parameterized(String, 5)
    assertTrue(result typeis String[])
    assertEquals(5, result.length)  
  }
  
  function testCreateNewArrayFromTypeParameterWithGosuType() {
    var result = parameterized(TestClass, 5)
    assertTrue(result typeis TestClass[])
    assertEquals(5, result.length)  
  }
  
  function parameterized<T>(arrayType : Type<T>, length : int) : T[] {
    return new T[length]  
  }
  
  static class TestClass {
    
  }

}
