package gw.specContrib.typeinference

class Errant_CollectionInitializerTypeInference {
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
  }
}