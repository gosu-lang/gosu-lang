package gw.specification.temp.generic_signature

uses gw.BaseVerifyErrantTest

class GenericSignatureTest extends BaseVerifyErrantTest {
  function testRuntime() {
    var t = new BaseGenericClass<List<String>>() {}
    assertEquals( "gw.specification.temp.generic_signature.BaseGenericClass<java.util.List<java.lang.String>>", t.getType().toString() )
  }
}
