package gw.internal.gosu.regression
uses gw.test.TestClass

class GenericClassUsingThisConstructorTest extends TestClass {

  function testChainedConstructors() {
    assertValues(new GenericClassUsingThisConstructor(), null, java.lang.Number)
    assertValues(new GenericClassUsingThisConstructor<java.lang.Integer>(), null, java.lang.Integer)
    assertValues(new GenericClassUsingThisConstructor(5), 5, java.lang.Integer)
    assertValues(new GenericClassUsingThisConstructor(5), 5, java.lang.Integer)
    assertValues(new GenericClassUsingThisConstructor<java.lang.Double>(5, 10), 5.0, java.lang.Double)
    assertValues(new GenericClassUsingThisConstructor<java.lang.Double>(5, 10), 5.0, java.lang.Double)
    assertValues(new GenericClassUsingThisConstructor("foo"), null, java.lang.Number)
    assertValues(new GenericClassUsingThisConstructor("foo", 10), null, java.lang.Number)
    assertValues(new GenericClassUsingThisConstructor<java.lang.Double>("foo"), null, java.lang.Double)
    assertValues(new GenericClassUsingThisConstructor<java.lang.Double>("foo", 10), null, java.lang.Double)
  }
  
  private function assertValues(obj : GenericClassUsingThisConstructor, value : Object, tType : Type) {
    assertEquals(value, obj.Value)
    assertEquals("init", obj.OtherValue)
    assertEquals(tType, obj.TClass)    
  }

}
