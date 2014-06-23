package gw.internal.gosu.compiler.sample.statement
uses java.lang.Integer

class TryFinallyStatementTestClass
{
  static var _value : String as Value = ""

  static function testTryFinally_NoException() : String
  {
    _value = ""
    try
    {
      _value = "try"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryFinally_ExceptionTriggeredInTry_After() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      nullString.charAt( 0 ) // throws npe
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryFinally_ExceptionTriggeredInTry_Before() : String
  {
    _value = ""
    var nullString : String
    try
    {
      nullString.charAt( 0 ) // throws npe
      _value = "try"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryFinally_ReturnFromFinally() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      return _value
    }
    finally
    {
      _value = _value + "finally"
    }
  }

  static function testTryFinally_NotBreakFromTryFinally() : String
  {
    _value = ""
    var nullString : String
    try
    {
      while( true )
      {
        if( true )
        {
          break
        }
      }
      _value = "try"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryFinally_BreakFromTryFinally() : String
  {
    _value = ""
    var nullString : String
    while( true )
    {
      try
      {
        if( 2 > 0 )
        {
          break
        }
        _value = "try"
      }
      finally
      {
        _value = _value + "finally"
      }
    }
    return _value
  }

  static function testTryFinally_NotContinueFromTryFinally() : String
  {
    _value = ""
    var nullString : String
    try
    {
      var i = 0
      while( i < 2 )
      {
        i = i + 1
        if( true )
        {
          continue
        }
      }
      _value = "try"
    }
    finally
    {
      _value = _value + "finally"
    }
    return _value
  }

  static function testTryFinally_ContinueFromTryFinally() : String
  {
    _value = ""
    var nullString : String
    var i = 0
    while( i < 2 )
    {
      i = i + 1
      try
      {
        if( 2 > 0 )
        {
          continue
        }
        _value = "try"
      }
      finally
      {
        _value = _value + "finally"
      }
    }
    return _value
  }

  static function testTryFinally_InlinedFinallyNotInFinallyPartition() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      // In our bytecode the finally code is inlined here when no exception is thrown.
      // So the inlined finally code should not be part of the exception table.
      // If it is, we'll see _value with two "finally" strings concatenated.
    }
    finally
    {
      _value = _value + "finally"
      nullString.charAt( 0 )
    }
    return _value
  }

  static function testTryFinally_InlinedFinallyNotInFinallyPartitionWithReturn() : String
  {
    _value = ""
    var nullString : String
    try
    {
      _value = "try"
      // In our bytecode the finally code is inlined here when no exception is thrown.
      // So the inlined finally code should not be part of the exception table.
      // If it is, we'll see _value with two "finally" strings concatenated.
      // With a return-stmt the we first compile the return expr and inline the finally
      // between that and the ARETURN code here.
      return _value
    }
    finally
    {
      _value = _value + "finally"
      nullString.charAt( 0 )
    }
  }


  static function testTryFinally_BreakFromTryFinallyInSwitch() : String
  {
    _value = ""
    var nullString : String
    var i = 0
    switch( i )
    {
      case 0:
        try
        {
          if( 2 > 0 )
          {
            break
          }
          _value = "try"
        }
        finally
        {
          _value = _value + "finally"
        }
    }
    return _value
  }

  static function testTryFinally_FinallyAroundBlockIsNotExecutedInBlocksReturn() : String
  {
    _value = ""
    var blk : block()
    try {
      blk = \-> {
        _value += "inBlock"
        if( true ) {
          return
        }
        _value = "afterReturn"
      }
    } finally {
      _value += "finally"
    }
    blk();
    return _value
  }
}