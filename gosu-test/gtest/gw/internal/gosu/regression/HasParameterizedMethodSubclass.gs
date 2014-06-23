package gw.internal.gosu.regression
uses java.util.Collection

class HasParameterizedMethodSubclass extends HasParameterizedMethod {

  construct() {
  }
  
  function testMethod1<T>(arg1 : Collection<T>, arg2 : Collection<T>) : boolean {
    return areCollectionSizesEquals(arg1, arg2)  
  }
  
  function testMethod2(arg1 : Collection<String>, arg2 : Collection<String>) : boolean {
    return areCollectionSizesEquals(arg1, arg2)  
  }

}
