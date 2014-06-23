package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.reflect.features.PropertyReference

class FunctionsWithBlockArgsReificationErrorTest extends TestClass {
  
  function testAccessPropertyAsIdentifier() {
    assertNotNull(new JlfTempImpl<Object>())
  }

  interface JlfTemp<E extends Object> {
    function f1<T extends Object>(
      p : PropertyReference<E,T>,
      b : block(xArg:JlfTemp<T>))

    function f2<T extends Object>(
      p : PropertyReference<E,T>,
      b : block(xArg:JlfTemp<T>))
  }

  class JlfTempImpl<E extends Object> implements JlfTemp<E> {
    override function f1<T extends Object>(
      p : PropertyReference<E,T>,
      b : block(xArg:JlfTemp<T>)) {}

    override function f2<T extends Object>(
      p : PropertyReference<E,T>,
      b : block(xArg:JlfTemp<T>)) {}
  }
}
