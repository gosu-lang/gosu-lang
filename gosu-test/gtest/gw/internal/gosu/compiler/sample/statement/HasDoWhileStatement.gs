package gw.internal.gosu.compiler.sample.statement

class HasDoWhileStatement
{
  static function hasDoWhile() : String
  {
    var str = "hello"
    do
    {
      str = str.substring( 1 )
    }while( str.length() > 2 )
    return str
  }

  static function hasDoWhileTerminal() : String
  {
    do
    {
      return "pass"
    }while( true )
  }

  static function hasDoWhileWithThrow() : String
  {
    var str = "hello"
    do
    {
      throw new java.lang.RuntimeException( "yay" )
    }while( str.length() > 2 )
  }

  static function hasDoWhileWithNonBooleanCondition() : int
  {
    var num = 8
    var c = 0
    do
    {
      num--
      c++
    }while( num as boolean )
    return c
  }

  static function hasDoWhileWithWideNonBooleanCondition() : long
  {
    var num : long = 8
    var c : long = 0
    do
    {
      num--
      c++
    }while( num as boolean )
    return c
  }

  function hasDoWhileWithBoxBooleanCondition() : String
  {
    var str = "hello"
    var condition : Boolean
    do
    {
      str = str.substring( 1 )
      condition = str.length() > 2
    }while( condition )
    return str
  }
}