package gw.specContrib.typeinference

uses gw.test.TestClass

class PrimitiveTypeInferenceTest extends TestClass {

  function example<T>(t1 : T, t2 : T) : T { return t1 }

  function testTypeInferenceWithPrimitiveTypeResolvesProperly() {
    var typé = statictypeof( example(1.5f, "string")  );
    assertEquals( "java.io.Serializable & java.lang.Comparable<java.lang.Object>",
                  typé.Name )
  }

}