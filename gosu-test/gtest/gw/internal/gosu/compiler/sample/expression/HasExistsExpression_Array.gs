package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList

class HasExistsExpression_Array
{
  static function hasExistsStringNoIndex() : boolean
  {
    var l = new String[] {"a", "b", "c"}
    return exists( str in l where str == "b" )
  }

  static function hasExistsString() : boolean
  {
    var l = new String[] {"a", "b", "c"}
    return exists( str in l index i where str == "b" )
  }

  static function hasExistsInt() : boolean
  {
    var l = new int[] {5, 6, 7}
    return exists( e in l index i where e == 7 && i == 2 )
  }

  static function hasExistsLong() : boolean
  {
    var l = new long[] {5, 6, 7}
    return exists( e in l index i where e == 7 && i == 2 )
  }

  static function hasExistsNeverEnterBody() : boolean
  {
    var l = new String[] {}
    return exists( e in l index i where e == "" )
  }

  static function hasExistsTerminal() : boolean
  {
    var l = new String[] {"a", "b", "c"}
    return exists( e in l index i where e == "z" )
  }

  static function hasExistsNull() : boolean
  {
    var l : String[] = null
    return exists( e in l index i where e == "z" )
  }
}