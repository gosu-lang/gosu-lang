package gw.internal.gosu.compiler.sample.expression

uses java.util.ArrayList

class HasExistsExpression_Iterator
{
  static function hasExistsStringNoIndex() : boolean
  {
    var l = {"a", "b", "c"}
    return exists( str in l where str == "b" )
  }

  static function hasExistsString() : boolean
  {
    var l = {"a", "b", "c"}
    return exists( str in l index i where str == "b" )
  }

  static function hasExistsInt() : boolean
  {
    var l = {5, 6, 7}
    return exists( e in l index i where e == 7 && i == 2 )
  }

  static function hasExistsLong() : boolean
  {
    var l = {5, 6, 7}
    return exists( e in l index i where e == 5 && i == 0 ) &&
           exists( e in l index i where e == 6 && i == 1 ) &&
           exists( e in l index i where e == 7 && i == 2 )
  }

  static function hasExistsNeverEnterBody() : boolean
  {
    var l = {}
    return exists( e in l index i where e == "" as Object )
  }

  static function hasExistsTerminal() : boolean
  {
    var l = {"a", "b", "c"}
    return exists( e in l index i where e == "z" )
  }

  static function hasExistsNull() : boolean
  {
    var l : ArrayList<String> = null
    return exists( e in l index i where e == "z" )
  }

  static function hasExistsIterator() : boolean
  {
    var l = {"a", "b", "c"}
    var iter = l.iterator()
    return exists( e in iter where e == "a" ) &&
           exists( e in iter where e == "b" ) &&
           exists( e in iter where e == "c" )
  }
}