package gw.internal.gosu.regression
uses gw.test.TestClass

class GenericClassToTypedArrayTest extends TestClass {

  construct() {
    
  }
  
  function testCallingToTypedArrayOnGenerifiedGosuClass() {
    var x = {new SomeGenerifiedClass<String>("Foo"), new SomeGenerifiedClass<String>("Bar")}  
    var result = x.toTypedArray()
    assertEquals("Foo", result[0].Value)
    assertEquals("Bar", result[1].Value)
  }
  
  public static final class SomeGenerifiedClass<T> {
    private var _value : T as Value
    construct(valueArg : T) {
      _value = valueArg  
    }
  }

}
