package gw.internal.gosu.compiler.sample.statement

uses java.util.ArrayList
uses java.util.Iterator
uses java.lang.UnsupportedOperationException

class HasForeachStatement_Iterator
{
  static function hasForeachStringNoIndex() : String
  {
    var l = new ArrayList<String>() {"a", "b", "c"}
    var res = ""
    for( str in l )
    {
      res += str
    }
    return res
  }

  static function hasForeachString() : String
  {
    var l = new ArrayList<String>() {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      res += str + i
    }
    return res
  }

  static function hasForeachInt() : String
  {
    var l = new ArrayList<java.lang.Integer>() {5, 6, 7}
    var res = ""
    for( e in l index i )
    {
      res += "" + e + i
    }
    return res
  }

  static function hasForeachBreak() : String
  {
    var l = new ArrayList<java.lang.Integer>() {5, 6, 7}
    var res = ""
    for( e in l index i )
    {
      res += i
      if( i > 0 )
      {
        break
      }
    }
    return res
  }

  static function hasForeachContinue() : String
  {
    var l = new ArrayList<java.lang.Integer>() {5, 6, 7}
    var res = ""
    for( e in l index i )
    {
      if( i == 1 )
      {
        continue
      }
      res += i
    }
    return res
  }

  static function hasForeachNeverEnterBody() : String
  {
    var l = new ArrayList<String>() {}
    var res = ""
    for( str in l index i )
    {
      res += str + i
    }
    return res
  }

  static function hasForeachTerminal() : String
  {
    var l = new ArrayList<String>() {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      return "pass"
    }
    return "fail"
  }

  static function hasForeachWithThrow() : String
  {
    var l = new ArrayList<String>() {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      throw new java.lang.RuntimeException( "yay" )
    }
    return "fail"
  }

  static function hasForeachCharacters() : String
  {
    var l = "abc"
    var res = ""
    for( str in l )
    {
      res += str
    }
    return res
  }

  static function hasForeachNull() : String
  {
    var l : ArrayList<String> = null
    var res = "pass"
    for( str in l )
    {
      res = "fail"
    }
    return res
  }

  static function hasForeachIntNumber() : String
  {
    var res = ""
    for( i in 0..|10 )
    {
      res += i
    }
    return res
  }

  static function hasForeachBigInteger() : String
  {
    var res = ""
    for( i in (0 as java.math.BigInteger)..|(10.040 as java.math.BigInteger) )
    {
      res += i
    }
    return res
  }

  static function hasForeachIterator() : String
  {
    var l = new ArrayList<String>() {"a", "b", "c"}
    var iter = l.iterator()
    var res = ""
    for( str in iter )
    {
      res += str
    }
    return res
  }

  static function hasForeachWithEval() : String
  {
    var l = {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      res += eval( "str + i" )
    }
    return res
  }

  static function hasForeachWithIntegerInterval() : String
  {
    var res = ""
    for( n in 0..5 )
    {
      res += n
    }
    return res
  }

  static function hasForeachWithIntegerInterval_NoLoopVar() : int
  {
    var res = 0
    for( 0..5 )
    {
      res += 1
    }
    return res
  }

  static function hasForeachWithComplexIntegerInterval_NoLoopVar() : int
  {
    var res = 0
    for( (0..5).step( 2 ) )
    {
      res += 1
    }
    return res
  }

  static function hasForeachWithLongInterval() : String
  {
    var res = ""
    for( n in (0 as long)..(5 as long) )
    {
      res += n
    }
    return res
  }

  static function testGenericIterator() : String {
    var res = ""
    for( value in new ForGenericIterator<String>().foo() ) {
      res += value
    }
    return res
  }

  static function testStructuralIterable() : String {
    var res = ""
    for( value in new StructurallyIterable<String>() ) {
      res += value
    }
    return res
  }

  static class ForGenericIterator<T> {
    function foo() : Inner {
      return new Inner( {"hi" as T, "bye" as T}.iterator() )
    }

    class Inner implements java.util.Iterator<T> {
      delegate _wrappedIterator represents java.util.Iterator<T>

      private construct(wrappedIterator : java.util.Iterator<T>) {
        _wrappedIterator = wrappedIterator;
      }

      override function remove() {
        throw new UnsupportedOperationException();
      }
    }
  }

  static class StructurallyIterable<T> {
    var _list : List<T> = {"a", "b", "c"} as List<T>

    function iterator() : Iterator<T> {
      return _list.iterator()
    }
  }
}