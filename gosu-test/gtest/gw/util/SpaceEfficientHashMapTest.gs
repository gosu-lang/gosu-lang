package gw.util
uses gw.test.TestClass
uses java.lang.Integer
uses java.util.Map
uses java.util.HashMap
uses java.util.LinkedHashMap

class SpaceEfficientHashMapTest extends TestClass {
  
  static var _mediumSizedMap : Map
  
  override function beforeTestClass() {
    _mediumSizedMap = new LinkedHashMap<String, Integer>() {"a" -> 1, "b" -> 2, "c" -> 3, "d" -> 4}
  }
  
  function testKeySetRemoveWhenList() {
    var map = new SpaceEfficientHashMap(_mediumSizedMap)
    map.keySet().remove("b")
    assertEquals(new HashMap<String, Integer>() {"a" -> 1, "c" -> 3, "d" -> 4}, map)
  }
  
  function testKeySetIteratorRemoveWhenList() {
    var map = new SpaceEfficientHashMap(_mediumSizedMap)
    var keyIter = map.keySet().iterator()
    assertEquals("a", keyIter.next())
    assertEquals("b", keyIter.next())
    keyIter.remove()
    assertEquals(new HashMap<String, Integer>() {"a" -> 1, "c" -> 3, "d" -> 4}, map)
  }
  
  function testValuesRemoveWhenList() {
    var map = new SpaceEfficientHashMap(_mediumSizedMap)
    map.values().remove(2)
    assertEquals(new HashMap<String, Integer>() {"a" -> 1, "c" -> 3, "d" -> 4}, map)
  }

  function testValuesIteratorRemoveWhenList() {
    var map = new SpaceEfficientHashMap(_mediumSizedMap)
    var valuesIter = map.values().iterator()
    assertEquals(1 as Integer, valuesIter.next())
    assertEquals(2 as Integer, valuesIter.next())
    valuesIter.remove()
    assertEquals(new HashMap<String, Integer>() {"a" -> 1, "c" -> 3, "d" -> 4}, map)
  }

  function testEntrySetIteratorRemoveWhenList() {
    var map = new SpaceEfficientHashMap(_mediumSizedMap)
    var entryIter = map.entrySet().iterator()
    assertEquals("a", entryIter.next().Key)
    assertEquals("b", entryIter.next().Key)
    entryIter.remove()
    assertEquals(new HashMap<String, Integer>() {"a" -> 1, "c" -> 3, "d" -> 4}, map)
  }
  
}
