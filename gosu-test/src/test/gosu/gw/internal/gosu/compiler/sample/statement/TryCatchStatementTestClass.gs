package gw.internal.gosu.compiler.sample.statement
uses java.lang.Integer
uses java.lang.Exception
uses java.lang.NullPointerException

class TryCatchStatementTestClass
{
  static var _value : String as Value = ""

  static function testTryCatch_NoException() : String
  {
    _value = ""
    try
    {
      _value = "try"
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    return _value
  }

  static function testTryCatch_ExceptionTriggeredInTry_After() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      nullString.charAt( 0 ) // throws npe
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    return _value
  }

  static function testTryCatch_ExceptionTriggeredInTry_Before() : String
  {
    _value = ""
    var nullString : String
    try
    {
      nullString.charAt( 0 ) // throws npe
      _value = "try"
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    return _value
  }

  static function testTryCatch_ReturnFromTry() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      return _value
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    return "err"
  }

  static function testTryCatchFinally_NoException() : String
  {
    _value = ""
    try
    {
      _value = "try"
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithException() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      nullString.charAt( 0 )
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithNpeException() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      nullString.charAt( 0 )
    }
    catch( e : NullPointerException )
    {
      _value = _value + "npecatch"
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithSecondException() : String
  {
    _value = ""
    var nullString = ""
    try
    {
      _value = "try"
      nullString.charAt( 0 )
    }
    catch( e : NullPointerException )
    {
      _value = _value + "npecatch"
    }
    catch( e : Exception )
    {
      _value = _value + "catch"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithCatchThatThrows() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      nullString.charAt( 0 )
    }
    catch( e : Exception )
    {
      nullString.charAt( 0 )
      _value = _value + "catch"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithFinallyThatReturnsNull() : String
  {
    _value = ""
    try
    {
      _value = "try"
    }
    catch( ex : Exception )
    {
      return null
    }
    finally
    {
     _value = _value + "finally"
    }
    return _value
  }

  static function testTryCatchFinally_WithFinallyThatReturnsNull_AndTryThatThrows() : String
  {
    _value = ""
    var nullString : String
    try
    {
      nullString.charAt( 0 ) // throws npe
      _value = "try"
    }
    catch( ex : Exception )
    {
      _value = "catch"
      return null
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

//## todo:
//  static function testTryCatch_BreakFromTryFinallyInSwitch() : String
//  {
//    _value = ""
//    var nullString : String
//    var i = 0
//    switch( i )
//    {
//      case 0:
//        try
//        {
//          if( 2 > 0 )
//          {
//            break
//          }
//          _value = "try"
//        }
//        finally
//        {
//          _value = _value + "catch"
//        }
//    }
//    return _value
//  }
}