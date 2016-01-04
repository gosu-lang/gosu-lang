package gw.lang.spec_old.expressions
uses java.lang.Character
uses java.lang.StringBuilder
uses java.lang.Math
uses gw.lang.reflect.gs.IGosuMethodInfo
uses gw.lang.parser.IStatement
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.IDynamicFunctionSymbol

class CharLiteralTest extends gw.test.TestClass
{
  function testSingleCharIsCharLiteral()
  {
    var x = 'a'
    assertEquals( char, statictypeof x )
    assertEquals( "a".charAt( 0 ), x )
  }

  function testEscapedSpecialCharIsCharLiteral()
  {
    var x = '\n'
    assertEquals( char, statictypeof x )
    assertEquals( "\n".charAt( 0 ), x )
    
    x = '\t'
    assertEquals( char, statictypeof x )
    assertEquals( "\t".charAt( 0 ), x )
    
    x = '\b'
    assertEquals( char, statictypeof x )
    assertEquals( "\b".charAt( 0 ), x )
    
    x = '\r'
    assertEquals( char, statictypeof x )
    assertEquals( "\r".charAt( 0 ), x )
    
    x = '\f'
    assertEquals( char, statictypeof x )
    assertEquals( "\f".charAt( 0 ), x )
  }

  function testEscapedOctalCharIsCharLiteral()
  {
    var x = '\0'
    assertEquals( char, statictypeof x )
    assertEquals( "\0".charAt( 0 ), x )
    
    x = '\123'
    assertEquals( char, statictypeof x )
    assertEquals( 'S', x )
  }

  function testEmptyCharIsEmptyStringLiteral()
  {
    var x = ''
    assertEquals( String, statictypeof x )
    assertEquals( "", x )
  }
  
  function testMoreThanOneCharIsStringLiteral()
  {
    var x = 'ab'
    assertEquals( String, statictypeof x )
    assertEquals( "ab", x )
  }
  
  function testEmbededDoubleQuote()
  {
    var x = '"'
    assertEquals( char, statictypeof x )
    assertEquals( "\"".charAt( 0 ), x )
  }
  
  function testCharFieldInProgram()
  {
    var x = eval( "var c = 's'\nreturn c" )
    assertEquals( 's', x )
  }

//  function testValidUnicodeChars()
//  {
//    for( i in Character.MIN_VALUE..Character.MAX_VALUE )
//    {
//      assertEquals( i as char, eval( "'\\u" + String.format( "%1$04x", {i} ) + "'" ) )
//    }
//  }
  
  function testValidUnicodeCharsInString()
  {
    for( v in (Character.MIN_VALUE..Character.MAX_VALUE).step( 1000 ) )
    {
      var sb = new StringBuilder( "\"" )
      var max = Math.min(v+999, Character.MAX_VALUE)
      for( i in v..max )
      {
        sb.append( "\\u" + String.format( "%1$04x", {i} ) )
      }
      sb.append( "\"" )
      var res : String = eval( sb.toString() ) as String
      assertEquals( (max - v) + 1, res.length() )
      //print( sb )
      for( i in v..|v+res.length() )
      {
        //print( "i=" + (i as String) + " : " + (i as char) + "  charAt:" + (res.charAt( i-v ) as int) )
        assertEquals( i as char, res.charAt( i-v ) )
      }
    }
  }
  
  function testValidEscapeChars()
  {
    var chars = 
    {
     '\b',  // backspace
     '\t',  // tab
     '\n',  // line feed
     '\f',  // form feed
     '\r',  // carriage return
     '\"',  // "
     '\'',  // '
     '\\',  // backslash
     '\107' // octal for 'G' 
    }
    assertEquals( '\u0008', chars[0] )
    assertEquals( '\u0009', chars[1] )
    assertEquals( '\u000a', chars[2] )
    assertEquals( '\u000c', chars[3] )
    assertEquals( '\u000d', chars[4] )
    assertEquals( '\u0022', chars[5] )
    assertEquals( '\u0027', chars[6] )
    assertEquals( '\u005c', chars[7] )
    assertEquals( '\u0047', chars[8] )    
  }

  function testIllegalEscapeChars()
  {
    assertFalse( Errant_IllegalEscapeChars.Type.Valid )
    var errors = Errant_IllegalEscapeChars.Type.ParseResultsException.ParseExceptions
    assertEquals( 7, errors.Count )
    assertErrorInFunction( "illegalEscapeLetter", Res.MSG_INVALID_CHAR_AT )
    assertErrorInFunction( "illegalEscapeNumber", Res.MSG_INVALID_CHAR_AT )
    assertErrorInFunction( "illegalEscapeNonTerminated", Res.MSG_UNTERMINATED_STRING_LITERAL )
    assertErrorInFunction( "illegalEscapeUnicode0", Res.MSG_INVALID_CHAR_AT )
    assertErrorInFunction( "illegalEscapeUnicode1", Res.MSG_INVALID_CHAR_AT )
    assertErrorInFunction( "illegalEscapeUnicode2", Res.MSG_INVALID_CHAR_AT )
    assertErrorInFunction( "illegalEscapeUnicode3", Res.MSG_INVALID_CHAR_AT )    
  }
  
  private function assertErrorInFunction( strFunc: String, msgKey: ResourceKey )
  {
    var c = Errant_IllegalEscapeChars.Type as IGosuClass
    var m = c.TypeInfo.getMethod( strFunc, {} )
    var funcStmt = c.getFunctionStatement(m)
    assertEquals( 1, funcStmt.ParseExceptions.Count )
    assertEquals( msgKey, funcStmt.ParseExceptions[0].MessageKey )
  }
}
