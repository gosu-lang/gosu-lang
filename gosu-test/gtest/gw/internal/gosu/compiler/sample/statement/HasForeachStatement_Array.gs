package gw.internal.gosu.compiler.sample.statement

uses java.util.ArrayList

class HasForeachStatement_Array
{
  static function hasForeachStringNoIndex() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l )
    {
      res += str
    }
    return res
  }

  static function hasForeachString() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      res += str + i
    }
    return res
  }

  static function hasForeachInt() : String
  {
    var l = new int[] {5, 6, 7}
    var res = ""
    for( e in l index i )
    {
      res += "" + e + i
    }
    return res
  }

  static function hasForeachLong() : String
  {
    var l = new long[] {5, 6, 7}
    var res = ""
    for( e in l index i )
    {
      res += "" + e + i
    }
    return res
  }

  static function hasForeachBreak() : String
  {
    var l = new int[] {5, 6, 7}
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
    var l = new int[] {5, 6, 7}
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
    var l = new String[] {}
    var res = ""
    for( str in l index i )
    {
      res += str + i
    }
    return res
  }

  static function hasForeachTerminal() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      return "pass"
    }
    return "fail"
  }

  static function hasForeachWithThrow() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      throw new java.lang.RuntimeException( "yay" )
    }
    return "fail"
  }

  static function hasForeachNull() : String
  {
    var l : String[] = null
    var res = "pass"
    for( str in l )
    {
      res = "fail"
    }
    return res
  }

  static function hasForeachWithEval() : String
  {
    var l = new String[] {"a", "b", "c"}
    var res = ""
    for( str in l index i )
    {
      res += eval( "str + i" )
    }
    return res
  }
}