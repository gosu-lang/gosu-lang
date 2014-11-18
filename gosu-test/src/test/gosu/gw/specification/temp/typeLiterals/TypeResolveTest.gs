package gw.specification.temp.typeLiterals

class TypeResolveTest {
  function testUsesStatementHasPrecedenceOverRelativeDefaultTypeLiteral() {
    assertTrue( UsesStatementHasPrecedenceOverRelativeDefaultTypeLiteral.Type.TypeInfo.getMethod( "foo", {} ).ReturnType
      .Name == "java.lang.Number" )
  }
}