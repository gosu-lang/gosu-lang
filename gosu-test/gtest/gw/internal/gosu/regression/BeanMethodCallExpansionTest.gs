package gw.internal.gosu.regression
uses java.lang.Integer
uses java.util.ArrayList
uses gw.test.TestClass

class BeanMethodCallExpansionTest extends TestClass {

  construct() {

  }
  
  function testBeanMethodCallExpansionWithArrayAndThenList() {
    var root = new HasAnArrayProperty(2, 2)
    var result = root.ArrayProperty*.listMethod()  
    assertEquals(2, result.length)
    assertEquals(2, result[0].size())
    assertEquals(2, result[1].size())
    assertEquals(0, result[0].get(0))
    assertEquals(1, result[0].get(1))
    assertEquals(0, result[1].get(0))
    assertEquals(1, result[1].get(1))
  }
  
  function testBeanMethodCallExpansionWithArrayAndThenArray() {
    var root = new HasAnArrayProperty(2, 2)
    var result = root.ArrayProperty*.arrayMethod()  
    assertEquals(4, result.length)
    assertEquals(0, result[0])
    assertEquals(1, result[1])
    assertEquals(0, result[2])
    assertEquals(1, result[3])
  }
  
  function testMemberAccessExpansionWithArrayAndThenList() {
    var root = new HasAnArrayProperty(2, 2)
    var result = root.ArrayProperty*.ListProperty  
    assertEquals(2, result.length)
    assertEquals(2, result[0].size())
    assertEquals(2, result[1].size())
    assertEquals(0, result[0].get(0))
    assertEquals(1, result[0].get(1))
    assertEquals(0, result[1].get(0))
    assertEquals(1, result[1].get(1))
  }
  
  function testMemberAccessExpansionWithArrayAndThenArray() {
    var root = new HasAnArrayProperty(2, 2)
    var result = root.ArrayProperty*.ArrayProperty  
    assertEquals(4, result.length)
    assertEquals(0, result[0])
    assertEquals(1, result[1])
    assertEquals(0, result[2])
    assertEquals(1, result[3])
  }
  
  static class HasAnArrayProperty {
    private var _subObjects : HasAListAndAnArrayProperty[]
    
    construct(subObjects : int, theirSubObjects : int) {
      _subObjects = new HasAListAndAnArrayProperty[subObjects]
      for (i in 0..|subObjects) {
        _subObjects[i] = new HasAListAndAnArrayProperty(theirSubObjects)  
      }
    }
    
    property get ArrayProperty() : HasAListAndAnArrayProperty[] {
      return _subObjects  
    }
    
    function arrayMethod() : HasAListAndAnArrayProperty[] {
      return _subObjects  
    }
  }
  
  static class HasAListAndAnArrayProperty {
    private var _leafObjects : List<Integer>
    
    construct(subObjects : int) {
      _leafObjects = new ArrayList<Integer>()
      for (i in 0..|subObjects) {
        _leafObjects.add(i)  
      }
    }
    
    property get ListProperty() : List<Integer> {
      return _leafObjects  
    }
    
    function listMethod() : List<Integer> {
      return _leafObjects 
    }
    
    property get ArrayProperty() : Integer[] {
      return _leafObjects.toTypedArray()  
    }
    
    function arrayMethod() : Integer[] {
      return _leafObjects.toTypedArray()  
    }
  }
}
