package gw.specContrib.typeinference

class Errant_PrimitiveTypeInferenceTest {

  function example<T>(t1 : T, t2 : T) : T { return t1 }

  function testTypeInferenceWithPrimitiveTypeResolvesProperly() {
    var x : java.io.Serializable & java.lang.Comparable<java.lang.Object> =  example(1.5f, "string")
  }

}