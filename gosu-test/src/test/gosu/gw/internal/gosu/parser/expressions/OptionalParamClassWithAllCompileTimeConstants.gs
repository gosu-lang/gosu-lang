package gw.internal.gosu.parser.expressions

class OptionalParamClassWithAllCompileTimeConstants {
  static final var STRING_CONST : String = "const"
  static final var INT_CONST : int = 32
  
  function stringRelated( 
    stringLiteral : String = "stringLiteral",
    stringLiteralConcat : String = "abc" + "def",
    stringConst : String = STRING_CONST )
  {
  }
  
  function numberRelated( 
    intLiteral : int = 1,
    intAdd : int = 1 + 2,
    intConst : int = INT_CONST )
  {
  }
  
}
