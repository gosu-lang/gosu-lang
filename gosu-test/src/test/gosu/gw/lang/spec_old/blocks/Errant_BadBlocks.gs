package gw.lang.spec_old.blocks
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadBlocks
{

  var field0()
  var field1(arg1:String)
  var field2(arg1:String, arg2:String)
  var field3(arg1:String, arg2:String, arg3:String)
  var field4(arg1:String, arg2:String, arg3:String, arg4:String)
  var field5(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String)
  var field6(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String)
  var field7(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String)
  var field8(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
             arg8:String)
  var field16(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String)

  var field17(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String)

  var field17b : block(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String)

  function func17( arg(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String) ) {}

  function func17b( arg:block(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String) ) {}

  function func17c() : block(arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String) { return null }

  function declaresBadBlocks() {
    var goodish = \ arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String -> null 
    
    var bad = \ arg1:String, arg2:String, arg3:String, arg4:String, arg5:String, arg6:String, arg7:String,
              arg8:String, arg9:String, arg10:String, arg11:String, arg12:String, arg13:String,
              arg14:String, arg15:String, arg16:String, arg17:String -> null 
  }
  
}
