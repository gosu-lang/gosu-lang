package gw.internal.gosu.compiler.sample.expression

uses java.lang.Integer
uses gw.lang.reflect.IType

class TestStaticTypeofExpression
{
  static function testSimple() : IType
  {
    return statictypeof new TestStaticTypeofExpression()
  }
}