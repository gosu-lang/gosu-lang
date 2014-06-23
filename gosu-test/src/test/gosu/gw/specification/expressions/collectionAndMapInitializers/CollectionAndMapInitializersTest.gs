package gw.specification.expressions.collectionAndMapInitializers

uses gw.BaseVerifyErrantTest
uses java.util.*
uses java.lang.*

class CollectionAndMapInitializersTest extends BaseVerifyErrantTest {

  function testErrant_CollectionAndMapInitializersTest() {
    processErrantType(Errant_CollectionAndMapInitializersTest)
  }

  function testBasicCollection() {
    var x0 = {}
    assertEquals(ArrayList, typeof x0)
    var x1 : List = {}
    assertEquals(ArrayList, typeof x1)
    var x4 : Collection = {1, 2, 3}
    assertEquals(ArrayList, typeof x4)
    assertTrue(x4.toArray()[0] typeis Integer )
    var x5 : List<List<Integer>>  = {{1}, {1, 2}}
    assertEquals(ArrayList, typeof x5)
    assertEquals(2, x5.size())
    assertEquals(1, x5.get(0).size())
    assertEquals(2, x5.get(1).size())
    assertEquals(1, x5.get(0).get(0))
    assertEquals(1, x5.get(1).get(0))
    assertEquals(2, x5.get(1).get(1))
    var x6 : Collection = new ArrayList() {1, 2, 3}
    var x15 : Collection = new ArrayList(2) {1, 2, 3}
    var x16 : List = new ArrayList({4,5}) {1, 2, 1+2}
    assertEquals(5, x16.size())
    for( i in {4, 5, 1, 2, 3} index j) {
      assertEquals(x16[j], i)
    }
    var x8 : Collection = {null}
  }

  function testBasicMap() {
    var x0 : Map = {}
    assertEquals(HashMap, typeof x0)
    var x2 = { 1-> "2", 2 -> "4"}
    assertEquals(HashMap, typeof x2)
    assertTrue(x2.keySet().toArray()[0] typeis Integer)
    assertTrue(x2.values().toArray()[0] typeis String)
    assertEquals("2", x2[1])
    assertEquals("4", x2[2])
    var x3 : Map = {{1->2} -> 3}
    assertEquals(1, x3.size())
    assertEquals(3, x3[{1->2}])
    assertEquals(2, (x3.keySet().toArray()[0] as Map)[1])
  }
}