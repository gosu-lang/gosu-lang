package gw.internal.gosu.compiler.sample.statement

class HasWhileStatement
{
  static function hasWhile() : String
  {
    var str = "hello"
    while( str.length() > 2 )
    {
      str = str.substring( 1 )
    }
    return str
  }

  static function hasWhileNeverEnterBody() : String
  {
    var s = "pass"
    while( 0 > 1 )
    {
      s = "fail"
    }
    return s
  }

  static function hasWhileTerminal() : String
  {
    while( true )
    {
      return "pass"
    }
  }

/*
  TODO cgross - fix this
  static function incrementsInTerminal() : String {
    var i = 0
    while( true ) {
      i++
      if( i == 10 ){
        return "pass"
      }
    }
  }
*/

  static function hasWhileWithThrow() : String
  {
    var str = "hello"
    while( str.length() > 2 )
    {
      throw new java.lang.RuntimeException( "yay" )
    }
    return "fail"
  }

  static function hasWhileWithNonBooleanCondition() : int
  {
    var num = 8
    var c = 0
    while( num as boolean )
    {
      num--
      c++
    }
    return c
  }

  static function hasWhileWithWideNonBooleanCondition() : long
  {
    var num : long = 8
    var c : long = 0
    while( num as boolean )
    {
      num--
      c++
    }
    return c
  }

  function whileWithBoxBooleanCondition() : String
  {
    var str = "hello"
    var condition : Boolean = str.length() > 2
    while( condition )
    {
      str = str.substring( 1 )
      condition = str.length() > 2
    }
    return str
  }
}