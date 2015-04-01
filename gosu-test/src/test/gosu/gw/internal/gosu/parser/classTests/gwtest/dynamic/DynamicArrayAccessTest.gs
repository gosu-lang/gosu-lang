package gw.internal.gosu.parser.classTests.gwtest.dynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal

class DynamicArrayAccessTest extends gw.BaseVerifyErrantTest {
  function testExpandoDefaultConstructor() {
    var d : dynamic.Dynamic
    d = {3, 4, 5}
    assertEquals( 5, d[2] ) // treated as array index

    d = {"mrs"->"farticle", "moneymarket"->"mutualfund"}
    assertEquals( "farticle", d["mrs"] ) // treated as map access

    d = new Dynamic()
    d.foo = "hi"
    assertEquals( "hi", d["foo"] ) // treated as property access
  }
}