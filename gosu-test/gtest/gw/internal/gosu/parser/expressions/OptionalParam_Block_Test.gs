package gw.internal.gosu.parser.expressions
uses gw.lang.parser.resources.Res
uses java.lang.StringBuilder

class OptionalParam_Block_Test extends gw.test.TestClass 
{
  function testBlock_AllParamsOutOfOrder_CheckEvaluatedInLexicalOrder()
  {
    var call = \ p1: String, p2: int -> p1 + ", " + p2
    var sb = new StringBuilder()
    var res = call( :p2 = getValue( 5, sb ), :p1 = getValue( "hello", sb ) )
    assertEquals( "hello, 5", res )
    assertEquals( "5 hello ", sb.toString() ) // verifies that the named args are evaluated in lexcial order
  }

  static function getValue<V>( value: V, sb: StringBuilder ) : V
  {
    sb.append( value as Object ).append( " " )
    return value
  }
}
