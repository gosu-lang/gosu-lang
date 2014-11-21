package gw.specification.temp.typeLiterals

uses gw.BaseVerifyErrantTest

class TypeResolveTest extends BaseVerifyErrantTest {
  function testUsesStatementHasPrecedenceOverRelativeDefaultTypeLiteral() {
    assertTrue( UsesStatementHasPrecedenceOverRelativeDefaultTypeLiteral.Type.TypeInfo.getMethod( "foo", {} ).ReturnType
      .Name == "java.lang.Number" )
  }
}