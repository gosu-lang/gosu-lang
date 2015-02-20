package gw.specContrib.typeinference

class Errant_CollectionInitializerTypeInference {
  function acceptObject(o: Object) {}

  function acceptArray(arr: Object[]) {}

  function test() {
    // IDE-1798
    acceptObject({1, 2}.toTypedArray())
    acceptArray({1, 2}.toTypedArray())
    acceptArray(({1, 2}).toTypedArray())
  }
}