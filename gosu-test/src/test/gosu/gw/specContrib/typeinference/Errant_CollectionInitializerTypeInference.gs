package gw.specContrib.typeinference

class Errant_CollectionInitializerTypeInference {
  class A {}
  class B {}

  function acceptObject(o: Object) {}

  function acceptArray(arr: Object[]) {}

  function test() {
    // IDE-1798
    acceptObject({1, 2}.toTypedArray())
    acceptArray({1, 2}.toTypedArray())
    acceptArray(({1, 2}).toTypedArray())

    // IDE-2279
    var map = { "string" -> null }
    var map2: java.util.Map<String, Object> = map

    var list1 = { null }
    var list2: java.util.ArrayList<Object> = list1

    // IDE-2538
    var a1: block(p: A)
    var b1: block(p: B)
    var l1 = {a1, b1}

    var a2: java.util.ArrayList<block(p: A)>
    var b2: java.util.List<block(p: B)>
    var l2 = {a2, b2}
  }

  // IDE-2279, IDE-2517
  function list() {
    var list = {1, 2, null}
    var list2: List<Integer> = list

    var map1 = {"string"->null}
    var map2: Map<String, Object> = map1

    var map3 = {null->"string"}
    var map4: Map<String, Object> = map1

    var map5 = {null->null}
    var map6: Map<Object, Object> = map5

    var map7 = {"key1"->"str", "key2"->null}
    var map8: Map<String, String> = map7
  }

}