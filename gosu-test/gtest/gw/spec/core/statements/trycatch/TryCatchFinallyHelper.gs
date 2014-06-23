package gw.spec.core.statements.trycatch
uses java.lang.RuntimeException
uses java.lang.IllegalArgumentException
uses java.lang.IllegalStateException
uses java.lang.OutOfMemoryError
uses java.lang.Throwable
uses gw.lang.parser.EvaluationException
uses java.lang.Exception
uses java.lang.Error

class TryCatchFinallyHelper {
  
  var _value : String as TestValue = "" 
  
  function tryFallsThroughFinallyFallsThrough() : String {
    var value = ""
    try {
      value = "try"   
    } finally {
      value += "finally"
    }
    return value
  }
  
  function tryFallsThroughFinallyThrowsExceptionWithNoCatchBlock() : String {
    var value = ""
    try {
      value = "try"   
    } finally {
      throw new RuntimeException(value)
    }
    return "error"   
  }
  
  function tryFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock() : String {
    var value = ""
    try {
      value = "try"   
    } catch (e : IllegalArgumentException) {
      return "error"  
    } finally {
      throw new RuntimeException(value)
    }
    return "error"  
  }
  
  function tryFallsThroughFinallyThrowsExceptionMatchingCatchBlock() : String {
    var value = ""
    try {
      value = "try"   
    } catch (e : RuntimeException) {
      return "error"  
    } finally {
      throw new RuntimeException(value)
    }
    return "error"  
  }

  function tryReturnsEarlyFinallyFallsThrough() {
    try {
      _value = "try"
      if (true) {
        return
      }
    } finally {
      _value += "finally"
    }
    _value += "error"
  }

  function tryReturnsEarlyFinallyThrowsExceptionWithNoCatchBlock() {
    try {
      _value = "try"
      if (true) {
        return
      }
    } finally {
      throw new RuntimeException(_value)
    }
    _value += "error"
  }

  function tryReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock() {
    try {
      _value = "try"
      if (true) {
        return
      }
    } catch (e : IllegalArgumentException) {
      _value += "error"
    } finally {
      throw new RuntimeException(_value)
    }
    _value += "error"
  }

  function tryReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock() {
    try {
      _value = "try"
      if (true) {
        return
      }
    } catch (e : RuntimeException) {
      _value = "error"
    } finally {
      throw new RuntimeException(_value)
    }
    _value = "error"
  }
  
  function tryReturnsEarlyWithValueFinallyFallsThrough() : String {
    var value = ""
    try {
      value = "try"
      if (true) {
        return value
      }
    } finally {
      _value += "finally"
    }

    return "error"
  }
  
  function tryReturnsEarlyWithValueFinallyClobbersReturnValue() : String {
    var value = ""
    try {
      value = "try"
      if (true) {
        return value
      }
    } finally {
      value = "finally"
    }

    return "error"
  }

  function tryReturnsEarlyWithValueFinallyThrowsExceptionWithNoCatchBlock() : String {
    var value = ""
    try {
      value = "try"
      if (true) {
        return value
      }
    } finally {
      throw new RuntimeException(value)
    }
    return "error"
  }

  function tryReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock() : String {
    var value = ""
    try {
      value = "try"
      if (true) {
        return value
      }
    } catch (e : IllegalArgumentException) {
      return "error"
    } finally {
      throw new RuntimeException(value)
    }
    return "error"
  }

  function tryReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock() : String {
    var value = ""
    try {
      value = "try"
      if (true) {
        return value
      }
    } catch (e : RuntimeException) {
      return "error"
    } finally {
      throw new RuntimeException(value)
    }
    return "error"
  }
  
  function tryBreaksOutOfLoopFinallyFallsThrough() : String {
    var value = ""
    for (i in 0..|10) {
      try {
        value += "try"
        break
      } finally {
        value += "finally"
      }
    }
    return value
  }
  
  function tryBreaksOutOfLoopFinallyThrowsExceptionWithNoCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        break
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }
  }
  
  function tryBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        break
      } catch (e : IllegalArgumentException) {
        _value += "error"
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }  
  }
  
  function tryBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        break
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }    
  }
  
  function tryContinuesFinallyFallsThrough() : String {
    var value = ""
    for (i in 0..|2) {
      try {
        value += "try"
        if (true) {
          continue
        }
        value += "error"
      } finally {
        value += "finally"
      }
    }
    return value
  }
  
  function tryContinuesLoopFinallyThrowsExceptionWithNoCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        continue
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }
  }
  
  function tryContinuesFinallyThrowsExceptionNotMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        continue
      } catch (e : IllegalArgumentException) {
        _value += "error"
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }  
  }
  
  function tryContinuesOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        continue
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "finally"
        throw new RuntimeException(_value)
      }
    }    
  } 
  
  function tryThrowsUncaughtExceptionFinallyFallsThrough() {
    try {
      _value = "try"
      if (true) {
        throw new RuntimeException(_value)
      }
    } finally {
      _value += "finally"
    }
    _value += "error"
  }

  function tryThrowsUncaughtExceptionFinallyThrowsExceptionWithNoCatchBlock() {
    try {
      _value = "try"
      if (true) {
        throw new RuntimeException("try")
      }
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
    _value += "error"
  }

  function tryThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock() {
    try {
      _value = "try"
      if (true) {
        throw new RuntimeException("try")
      }
    } catch (e : IllegalArgumentException) {
      _value += "error"
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
    _value += "error"
  }

  function tryThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock() {
    try {
      _value = "try"
      if (true) {
        throw new OutOfMemoryError()
      }
    } catch (e : RuntimeException) {
      _value += "error"
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
    _value = "error"
  }
  
  function catchFallsThroughFinallyFallsThrough() {
    try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"  
    } finally {
      _value += "finally"
    }
  }

  function catchFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock() {
    try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"  
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
    _value += "error"  
  }
  
  function catchFallsThroughFinallyThrowsExceptionMatchingCatchBlock() {
    try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"  
    } finally {
      _value += "finally"
      throw new IllegalArgumentException("finally")
    }
    _value += "error"  
  }
  
  function catchReturnsEarlyFinallyFallsThrough() {
    try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        return
      }
      _value += "error"
    } finally {
      _value += "finally"
    }
    _value += "error"
  }

  function catchReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock() {
     try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        return
      }
      _value += "error"
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
    _value += "error"
  }

  function catchReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock() {
    try {
      _value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        return
      }
      _value += "error"
    } finally {
      _value += "finally"
      throw new IllegalArgumentException("finally")
    }
    _value = "error"
  }
  
  function catchReturnsEarlyWithValueFinallyFallsThrough() : String {
    var value = ""
    try {
      _value = "try"   
      value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      value += "catch"
      if (true) {
        return value
      }
      _value += "error"
    } finally {
      _value += "finally"
    }

    return "error"
  }
  
  function catchReturnsEarlyWithValueFinallyClobbersReturnValue() : String {
    var value = ""
    try {
      _value = "try"   
      value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      value += "catch"
      if (true) {
        return value
      }
      _value += "error"
    } finally {
      _value += "finally"
      value += "finally"
    }

    return "error"  
  }

  function catchReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock() : String {
    var value = ""
    try {
      _value = "try"   
      value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      value += "catch"
      if (true) {
        return value
      }
      _value += "error"
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }

    return "error"  
  }

  function catchReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock() : String {
    var value = ""
    try {
      _value = "try"   
      value = "try"   
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      value += "catch"
      if (true) {
        return value
      }
      _value += "error"
    } finally {
      _value += "finally"
      throw new IllegalArgumentException("finally")
    }

    return "error"  
  }
  
  function catchBreaksOutOfLoopFinallyFallsThrough() {
    for (i in 0..|10) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        break
      } finally {
        _value += "finally"
      }
    }
  }
  
  function catchBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        break
      } finally {
        _value += "finally"
        throw new RuntimeException("finally")
      }
    }
  }
  
  function catchBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        break
      } finally {
        _value += "finally"
        throw new IllegalArgumentException("finally")
      }
    }
  }
  
  function catchContinuesFinallyFallsThrough() {
    for (i in 0..|2) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        if(true) {
          continue
        }
      } finally {
        _value += "finally"
      }
      _value += "error"
    }
  }
  
  function catchContinuesFinallyThrowsExceptionNotMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        if(true) {
          continue
        }
      } finally {
        _value += "finally"
        throw new RuntimeException("finally")
      }
      _value += "error"
    }
  }
  
  function catchContinuesFinallyThrowsExceptionMatchingCatchBlock() {
    for (i in 0..|10) {
      try {
        _value += "try"
        if (true) {
          throw new IllegalArgumentException("try")
        }
        _value += "error"
      } catch (e : IllegalArgumentException) {
        _value += "catch"
        if(true) {
          continue
        }
      } finally {
        _value += "finally"
        throw new IllegalArgumentException("finally")
      }
      _value += "error"
    }
  }
  
  function catchThrowsUncaughtExceptionFinallyFallsThrough() {
    try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      throw new RuntimeException("catch")
    } finally {
      _value += "finally"
    }
    _value += "error"
  }
  
  function catchThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock() {
     try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      throw new RuntimeException("catch")
    } finally {
      _value += "finally"
      throw new RuntimeException("finally")
    }
  }
  
  function catchThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock() {
     try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      throw new RuntimeException("catch")
    } finally {
      _value += "finally"
      throw new IllegalArgumentException("finally")
    }
  }
  
  // ----------------------------- Tests around just catch blocks
  
  function catchThrowsExceptionMatchingItsDeclarationNoFinally() {
    try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        throw new IllegalArgumentException("catch")  
      }
      _value += "error"
    }        
  }
  
  function catchThrowsExceptionMatchingEarlierCatchBlockNoFinally() {
   try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalStateException) {
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        throw new IllegalStateException("catch")  
      }
      _value += "error"
    }  
  }
  
  function catchThrowsExceptionMatchingLaterCatchBlockNoFinally() {
     try {
      _value += "try"
      if (true) {
        throw new IllegalArgumentException("try")
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "catch"
      if (true) {
        throw new IllegalStateException("catch")  
      }
      _value += "error"
    } catch (e : IllegalStateException) {
      _value += "error"  
    }
  }
    
  // Tests with multiple things in the try block
  
  function tryWithEveryPossibleExitPoint(tryPath : int, catchPath : int, finallyPath : int) {
    for (i in 0..|1) {
      try {
        _value += "try"
        if (tryPath == 1) {
          _value += "1"
          return  
        } else if (tryPath == 2) {
          _value += "2"
          throw new IllegalArgumentException("try")    
        } else if (tryPath == 4) {
          _value += "4"
          throw new OutOfMemoryError("try")
        } else if (tryPath == 5) {
          _value += "5"
          break            
        } else if (tryPath == 6) {
          _value += "6"
          continue  
        } else if (tryPath == 3) {
          _value += "3"
          throw new RuntimeException("try")   
        }
        _value += "0"
      } catch (e : IllegalArgumentException) {
        _value += "-catchillegal"
        if (catchPath == 1) {
          _value += "1"
          return  
        } else if (catchPath == 2) {
          _value += "2"
          throw new IllegalArgumentException("catchillegal")      
        } else if (catchPath == 4) {
          _value += "4"
          throw new OutOfMemoryError("catchillegal")
        } else if (catchPath == 5) {
          _value += "5"
          break            
        } else if (catchPath == 6) {
          _value += "6"
          continue
        } else if (catchPath == 3) {
          _value += "3"
          throw new RuntimeException("catchillegal") 
        }
        _value += "0"
      } catch (e : RuntimeException) {
        _value += "-catchruntime"
        if (catchPath == 1) {
          _value += "1"
          return  
        } else if (catchPath == 2) {
          _value += "2"
          throw new IllegalArgumentException("catchruntime")       
        } else if (catchPath == 4) {
          _value += "4"
          throw new OutOfMemoryError("catchruntime")
        } else if (catchPath == 5) {
          _value += "5"
          break            
        } else if (catchPath == 6) {
          _value += "6"
          continue  
        } else if (catchPath == 3) {
          _value += "3"
          throw new RuntimeException("catchruntime")  
        }
        _value += "0"  
      } finally {
        _value += "-finally"
        if (finallyPath == 1) {
          _value += "1"
          throw new IllegalArgumentException("finally")  
        } else if (finallyPath == 2) {
          _value += "2"
          throw new RuntimeException("finally")       
        } else if (finallyPath == 3) {
          _value += "3"
          throw new OutOfMemoryError("finally")
        }
        _value += "0"  
      }
    }
  }
  
  // Nested tests
  
  function bothTrysFallThrough() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }
  }
  
  function innerTryReturnsEarly() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          return  
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }
  }
  
  function outerTryReturnsEarly() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        return  
      }
      _value += "error"
    } finally {
      _value += "outer_finally_"  
    }  
  }
  
  function innerTryReturnsWithValue() : String {
     try {      
      try {
        _value += "inner_try_"
        if (true) {
          return "inner"  
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }  
    
    return "error"
  }
  
  function outerTryReturnsWithValue() : String {
     try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        return "outer"  
      }
      _value += "error"
    } finally {
      _value += "outer_finally_"  
    }     
    
    return "error"
  }
  
  function innerTryThrowsUncaughtException() {
     try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }  
  }
  
  function outerTryThrowsUncaughtException() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("outer")  
      }
      _value += "error"
    } finally {
      _value += "outer_finally_"  
    }    
  }
  
  function innerTryBreaksOutOfLoopThatIncludesOnlyInnerTry() {
    try {      
      for (i in 0..|10) {
        try {
          _value += "inner_try_"
          if (true) {
            break
          }
        } finally {
          _value += "inner_finally_"
        }
        _value += "error"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }        
  }
  
  function innerTryBreaksOutOfLoopThatIncludesBothTryStatements() {
    for (i in 0..|10) {
      try {            
        try {
          _value += "inner_try_"
          if (true) {
            break
          }
          _value += "error"
        } finally {
          _value += "inner_finally_"
        }       
        _value += "outer_try_"
      } finally {
        _value += "outer_finally_"  
      }   
    }
  }
  
  function innerTryBreaksOutOfLoopEntirelyWithinInnerTryStatement() {
    try {      
      try {
        _value += "inner_try_"
        for (i in 0..|10) {
          if (true) {
            break
          }
          _value += "error"
        }
      } finally {
        _value += "inner_finally_"
      }   
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }          
  }
  
  function outerTryBreaksOutOfLoopContainingBothTryStatements() {
    for (i in 0..|10) {
      try {            
        try {
          _value += "inner_try_"
        } finally {
          _value += "inner_finally_"
        }       
        _value += "outer_try_"
        if (true) {
          break
        }
        _value += "error"
      } finally {
        _value += "outer_finally_"  
      }   
    }  
  }
  
  function outerTryBreaksOutOfLoopEntirelyWithinOuterTryStatement() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }   
      _value += "outer_try_"
      for (i in 0..|10) {
        if (true) {
          break
        }
        _value += "error"
      }
    } finally {
      _value += "outer_finally_"  
    }            
  }
  
  function innerTryContinuesWithinLoopThatIncludesOnlyInnerTry() {
    try {      
      for (i in 0..|2) {
        try {
          _value += "inner_try_"
          if (true) {
            continue
          }
        } finally {
          _value += "inner_finally_"
        }
        _value += "error"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }        
  }
  
  function innerTryContinuesWithinLoopThatIncludesBothTryStatements() {
    for (i in 0..|2) {
      try {            
        try {
          _value += "inner_try_"
          if (true) {
            continue
          }
          _value += "error"
        } finally {
          _value += "inner_finally_"
        }       
        _value += "outer_try_"
      } finally {
        _value += "outer_finally_"  
      }   
    }  
  }
  
  function innerTryContinuesWithinLoopEntirelyWithinInnerTryStatement() {
    try {      
      try {
        _value += "inner_try_"
        for (i in 0..|10) {
          if (true) {
            continue
          }
          _value += "error"
        }
      } finally {
        _value += "inner_finally_"
      }   
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }          
  }
  
  function outerTryContinuesWithinLoopContainingBothTryStatements() {
    for (i in 0..|2) {
      try {            
        try {
          _value += "inner_try_"
        } finally {
          _value += "inner_finally_"
        }       
        _value += "outer_try_"
        if (true) {
          continue
        }
        _value += "error"
      } finally {
        _value += "outer_finally_"  
      }   
    }     
  }
  
  function outerTryContinuesWithinLoopEntirelyWithinOuterTryStatement() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }   
      _value += "outer_try_"
      for (i in 0..|10) {
        if (true) {
          continue
        }
        _value += "error"
      }
    } finally {
      _value += "outer_finally_"  
    }            
  }
  
  function innerFinallyThrowsUncaughtException() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
        throw new RuntimeException("inner")
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }        
  }
  
  function outerFinallyThrowsUncaughtException() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }        
  }
  
  function bothFinallyBlocksThrowUncaughtExceptions() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
        throw new RuntimeException("inner")
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }        
  }
  
  function innerFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "inner_finally_"
        throw new RuntimeException("inner")
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }          
  }
  
  function innerTryFallsThroughAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
        throw new RuntimeException("inner")
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }   
  }
  
  function innerTryReturnsAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          return
        }
      } finally {
        _value += "inner_finally_"
        throw new RuntimeException("inner")
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }   
  }
  
  function outerFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }       
  }
  
  function innerTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          return
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }       
  }
  
  function outerTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        return
      }
      _value += "error"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }       
  }
  
  function outerFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "error"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }           
  }
  
  function innerTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          return  
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "error"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }           
  }
  
  function outerTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        return  
      }
    } catch (e : RuntimeException) {
      _value += "error"
    } finally {
      _value += "outer_finally_"  
      throw new RuntimeException("outer")
    }           
  }
  
  function bothCatchesFallThrough() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("outer")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }             
  }
  
  function innerCatchReturnsEarly() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        return
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }               
  }
  
  function outerCatchReturnsEarly() {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("inner")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      return
    } finally {
      _value += "outer_finally_"  
    }           
    _value += "error"
  }
  
  function innerCatchReturnsWithValue() : String {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        return "catch"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }    
    return "error"          
  }
  
  function outerCatchReturnsWithValue() : String {
    try {      
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("inner")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      return "catch"
    } finally {
      _value += "outer_finally_"  
    }           
    return "error"  
  }
  
  function innerCatchThrowsUncaughtException() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        throw new OutOfMemoryError("catch")
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }           
  }
  
  function outerCatchThrowsUncaughtException() {
    try {      
      try {
        _value += "inner_try_"        
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("inner")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      throw new OutOfMemoryError("catch")
    } finally {
      _value += "outer_finally_"  
    }             
  }
  
  function innerCatchThrowsExceptionCaughtByInnerCatch() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        throw new RuntimeException("catch")
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }             
  }
  
  function innerCatchThrowsExceptionCaughtByOuterCatch() {
    try {      
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        throw new RuntimeException("catch")
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
    } finally {
      _value += "outer_finally_"  
    }     
  }
  
  function outerCatchThrowsExceptionCaughtByInnerCatch() {
    try {      
      try {
        _value += "inner_try_"
      } catch (e : RuntimeException) {
        _value += "error"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new IllegalArgumentException("outer")  
      }
      _value += "error"
    } catch (e : IllegalArgumentException) {
      _value += "outer_catch_"
      throw new RuntimeException("catch")
    } finally {
      _value += "outer_finally_"  
    }       
  }
  
  function outerCatchThrowsExceptionCaughtByOuterCatch() {
    try {      
      try {
        _value += "inner_try_"       
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("outer")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      throw new RuntimeException("catch")
    } finally {
      _value += "outer_finally_"  
    }       
  }
  
  function innerCatchBreaksOutOfLoopThatIncludesOnlyInnerTry() {
    try {  
      for (i in 0..|10) {    
        try {
          _value += "inner_try_"
          if (true) {
            throw new RuntimeException("inner")  
          }
          _value += "error"
        } catch (e : RuntimeException) {
          _value += "inner_catch_"
          break      
        } finally {
          _value += "inner_finally_"
        }
      }
      _value += "outer_try_"  
    } finally {
      _value += "outer_finally_"  
    }         
  }
  
  function innerCatchBreaksOutOfLoopThatIncludesBothTryStatements() {
    for (i in 0..|10) {
      try {           
        try {
          _value += "inner_try_"
          if (true) {
            throw new RuntimeException("inner")  
          }
          _value += "error"
        } catch (e : RuntimeException) {
          _value += "inner_catch_"
          break      
        } finally {
          _value += "inner_finally_"
        }
        _value += "outer_try_"
      } finally {
        _value += "outer_finally_"  
      }  
    }         
  }
  
  function innerCatchBreaksOutOfLoopEntirelyWithinTryStatement() {
    try {           
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        for (i in 0..|10) {
          if (true) {
            break      
          }
          _value += "error"
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }    
  }
  
  function outerCatchBreaksOutOfLoopContainingBothTryStatements() {
    for (i in 0..|10) {
      try {           
        try {
          _value += "inner_try_"
        } catch (e : RuntimeException) {
          _value += "outer_catch_"
          break      
        } finally {
          _value += "inner_finally_"
        }
        _value += "outer_try_"
        if (true) {
          throw new RuntimeException("outer")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "outer_catch_"
        break  
      } finally {
        _value += "outer_finally_"  
      }  
    }           
  }
  
  function outerCatchBreaksOutOfLoopEntirelyWithinCatchStatement() {
    try {           
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("outer")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      for (i in 0..|10) {
        if (true) {
          break      
        }
        _value += "error"
      }
    } finally {
      _value += "outer_finally_"  
    }      
  }
  
  function innerCatchContinuesWithinLoopThatIncludesOnlyInnerTry() {
     try {  
      for (i in 0..|2) {    
        try {
          _value += "inner_try_"
          if (true) {
            throw new RuntimeException("inner")  
          }
          _value += "error"
        } catch (e : RuntimeException) {
          _value += "inner_catch_"
          continue      
        } finally {
          _value += "inner_finally_"
        }
        _value += "error"
      }
      _value += "outer_try_"  
    } finally {
      _value += "outer_finally_"  
    }           
  }
  
  function innerCatchContinuesWithinLoopThatIncludesBothTryStatements() {
    for (i in 0..|2) {
      try {           
        try {
          _value += "inner_try_"
          if (true) {
            throw new RuntimeException("inner")  
          }
          _value += "error"
        } catch (e : RuntimeException) {
          _value += "inner_catch_"
          continue      
        } finally {
          _value += "inner_finally_"
        }
        _value += "outer_try_"
      } finally {
        _value += "outer_finally_"  
      }  
    }           
  }
  
  function innerCatchContinuesWithinLoopEntirelyWithinCatchStatement() {
     try {           
      try {
        _value += "inner_try_"
        if (true) {
          throw new RuntimeException("inner")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "inner_catch_"
        for (i in 0..|10) {
          if (true) {
            continue      
          }
          _value += "error"
        }
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
    } finally {
      _value += "outer_finally_"  
    }      
  }
  
  function outerCatchContinuesWithinLoopContainingBothTryStatements() {
    for (i in 0..|2) {
      try {           
        try {
          _value += "inner_try_"
        } catch (e : RuntimeException) {
          _value += "outer_catch_"
          break      
        } finally {
          _value += "inner_finally_"
        }
        _value += "outer_try_"
        if (true) {
          throw new RuntimeException("outer")  
        }
        _value += "error"
      } catch (e : RuntimeException) {
        _value += "outer_catch_"
        continue  
      } finally {
        _value += "outer_finally_"  
      }
      _value += "error"  
    }  
  }
  
  function outerCatchContinuesWithinLoopEntirelyWithinCatchStatement() {
    try {           
      try {
        _value += "inner_try_"
      } finally {
        _value += "inner_finally_"
      }
      _value += "outer_try_"
      if (true) {
        throw new RuntimeException("outer")  
      }
      _value += "error"
    } catch (e : RuntimeException) {
      _value += "outer_catch_"
      for (i in 0..|10) {
        if (true) {
          continue      
        }
        _value += "error"
      }
    } finally {
      _value += "outer_finally_"  
    }  
  }
  
  // One-off sorts of tests
  
  function tryWithCatchBlockThatCatchesThrowable(path : int) {
    try {
      _value += "try_"
      if (path == 1 || path == 5 || path == 6) {
        throw new RuntimeException("1")  
      } else if (path == 2) {
        throw new OutOfMemoryError("2")  
      }
    } catch (e : Throwable) {
      _value += "catch_"
      _value += (typeof e).RelativeName  
      _value += "_" 
      if (path == 5) {
        throw new RuntimeException("5")  
      } else if (path == 6) {
        throw new OutOfMemoryError("6")  
      }
    } finally {
      _value += "finally"
      if (path == 3) {
        throw new RuntimeException("3")  
      } else if (path == 4) {
        throw new OutOfMemoryError("4")  
      }
    }
  }
  
  function tryWithCatchBlockWithNoTypeDeclared(path : int) {
    try {
      _value += "try_"
      if (path == 1 || path == 5 || path == 6) {
        throw new RuntimeException("1")  
      } else if (path == 2) {
        throw new OutOfMemoryError("2")  
      }
    } catch (e) {
      _value += "catch_"
      _value += (e typeis RuntimeException)
      _value += "_" 
      if (path == 5) {
        throw new RuntimeException("5")  
      } else if (path == 6) {
        throw new OutOfMemoryError("6")  
      }
    } finally {
      _value += "finally"
      if (path == 3) {
        throw new RuntimeException("3")  
      } else if (path == 4) {
        throw new OutOfMemoryError("4")  
      }
    }  
  }
  
  function tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(path : int) {
    try {
      _value += "try_"
      if (path == 1 || path == 4 || path == 5 || path == 6) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwRuntimeException()
      } else if (path == 2) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwOutOfMemoryError() 
      } else if (path == 3) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwException() 
      }
    } catch (e : Throwable) {
      _value += "catch_"
      _value += (typeof e).RelativeName  
      _value += "_" 
      if (path == 4) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwRuntimeException()
      } else if (path == 5) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwOutOfMemoryError() 
      } else if (path == 6) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwException() 
      }
    } finally {
      _value += "finally_"
      if (path == 7) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwRuntimeException()
      } else if (path == 8) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwOutOfMemoryError() 
      } else if (path == 9) {
        gw.spec.core.statements.trycatch.TryCatchFinallyJavaHelper.throwException() 
      }
    }
    _value += "end"   
  }

  function finallyContainingBreakOutOfLoopEntirelyWithinFinally() {
    try {
      _value += "try_"
    } finally {
      _value += "finally"
      for (i in 0..|10) {
        if (true) {
          break  
        }
        _value += "error"
      }
    }
  }
  
  function finallyContainingContinueInLoopEntirelyWithinFinally() {
    try {
      _value += "try_"
    } finally {
      _value += "finally"
      for (i in 0..|10) {
        if (true) {
          continue  
        }
        _value += "error"
      }
    }  
  }
  
  function catchBlockCatchesAssignableExceptions(path : int) {
    try {
      _value += "try_"
      if (path == 1) {
        throw new IllegalArgumentException("1")    
      } else if (path == 2) {
        throw new IllegalStateException("2")  
      } else if (path == 3) {
        throw new RuntimeException("3")
      } else if (path == 4) {
        throw new Exception("4")  
      } else if (path == 5) {
        throw new OutOfMemoryError("5")  
      }
    } catch (e : RuntimeException) {
      _value += "catch"
    }
  }
  
  function multipleCatchBlocks(path : int) {
    try {
      _value += "try_"
      if (path == 1) {
        throw new IllegalArgumentException("1")    
      } else if (path == 2) {
        throw new IllegalStateException("2")  
      } else if (path == 3) {
        throw new RuntimeException("3")
      } else if (path == 4) {
        throw new Exception("4")  
      } else if (path == 5) {
        throw new OutOfMemoryError("5")  
      }
    } catch (e: IllegalArgumentException) {
      _value += "catch_1"
    } catch (e : RuntimeException) {
      _value += "catch_2"
    } catch (e : Error) {
      _value += "catch_3"  
    } catch (e : Exception) {
      _value += "catch_4"  
    }
  }
  
  function tryCatchFinallyWithinFinallyBlock(path : int) {
    try {
      _value += "try_"
      if (path == 1) {
        throw new RuntimeException("1")  
      }
    } finally {
      _value += "finally_"
      if (path == 2) {
        throw new RuntimeException("2")  
      }
      try {
        _value += "innertry_"
        if (path == 3 || path == 5 || path == 6) {
          throw new RuntimeException("3")    
        } else if (path == 4) {
          throw new Exception("4")  
        }
      } catch (e : RuntimeException) {
        _value += "catch_"
        if (path == 5) {
          throw new RuntimeException("5")    
        } else if (path == 6) {
          throw new Exception("6")  
        } 
      } finally {
        _value += "innerfinally_" 
        if (path == 7) {
          throw new RuntimeException("7")    
        } else if (path == 8) {
          throw new Exception("8")  
        } 
      }
      _value += "end"
    }
  }
  
  function tryCatchFinallyWithinCatchBlock(path : int) {
    try {
      _value += "try_"
      if (path > 0) {
        throw new RuntimeException("1")  
      }
    } catch (e : RuntimeException) {  
      _value += "catch_"    
      if (path == 2) {
        throw new RuntimeException("2")  
      }
      try {
        _value += "innertry_"
        if (path == 3 || path == 5 || path == 6) {
          throw new RuntimeException("3")    
        } else if (path == 4) {
          throw new Exception("4")  
        }
      } catch (e2 : RuntimeException) {
        _value += "innercatch_"
        if (path == 5) {
          throw new RuntimeException("5")    
        } else if (path == 6) {
          throw new Exception("6")  
        } 
      } finally {
        _value += "innerfinally_" 
        if (path == 7) {
          throw new RuntimeException("7")    
        } else if (path == 8) {
          throw new Exception("8")  
        } 
      }        
    } finally {
      _value += "finally_"  
    }  
    _value += "end" 
  }
  
  function doSomethingWithCaughtException() {
    try {
      _value += "try_"
      if (true) {
        throw new RuntimeException("original")  
      }
    } catch (e : RuntimeException) {
      _value += "catch_"
      _value += e.Message
      _value += "_"
      _value += (typeof e).RelativeName   
    }  
  }
  
  function rethrowOfCaughtException() {
    try {
      _value += "try_"
      if (true) {
        throw new RuntimeException("original")  
      }
    } catch (e : RuntimeException) {
      _value += "catch_"  
      throw e  
    }
  }
  
  function nestedEmptyTryCatch() {
    try {
    } catch (e : RuntimeException) {
    }
  }
  
  function nestedEmptyTryFinally() {
    try {
    } finally {
    }
  }
  
  function nestedEmptyTryCatchFinally() {
    try {
    } catch (e : RuntimeException) {
    } finally {
    }
  }
  
  function nestedEmptyTryCatchFinallyStatements() {
    try {
      try {
      } catch (e : RuntimeException) {
      } finally {
      }
    } catch (e : RuntimeException) {
    } finally {
    }
  }

  function nestedEmptyTryCatchFinallyStatementsWithNonEmptyInnerFinally() {
    try {
      try {
      } catch (e : RuntimeException) {
      } finally {
        _value += "inner_finally"
      }
    } catch (e : RuntimeException) {
    } finally {
    }
  }
  
  function nestedEmptyTryCatchFinallyStatementsWithNonEmptyOuterFinally() {
    try {
      try {
      } catch (e : RuntimeException) {
      } finally {        
      }
    } catch (e : RuntimeException) {
    } finally {
      _value += "outer_finally"
    }
  }
  
  function lotsOfPathologicalNesting(path : int) {
    try {
      _value += "try1_"
      if (path == 1) {
        return  
      } else if (path == 2 || (path >= 36 and path <= 38)) {
        throw new RuntimeException("2")   
      } else if (path == 3) {
        throw new Exception("3")
      }
      try {
        _value += "try2_"
        if (path == 4) {
          return  
        } else if (path == 5) {
          throw new RuntimeException("5")   
        } else if (path == 6) {
          throw new Exception("6")
        }  
        try {
          _value += "try3_"
          if (path == 7) {
            return  
          } else if (path == 8 || (path >= 25 and path <= 27)) {
            throw new IllegalArgumentException("8")   
          } else if (path == 9) {
            throw new Exception("9")
          }  
          try {
            _value += "try4_"
            if (path == 10) {
              return  
            } else if (path == 11 || (path >= 12 and path <= 22)) {
              throw new RuntimeException("11")   
            } else if (path == 41) {
              throw new Exception("41")
            }  
          } catch (e : RuntimeException) {
            _value += "catch4_"
            if (path == 12) {
              return  
            } else if (path == 13) {
              throw new RuntimeException("13")   
            } else if (path == 14) {
              throw new Exception("14")
            }  
            try {
              _value += "try5_"
              if (path == 15) {
                return  
              } else if (path == 16) {
                throw new RuntimeException("16")   
              } else if (path == 17 || (path >= 18 and path <= 20)) {
                throw new Exception("17")
              }     
            } catch (e2 : Exception) {
              _value += "catch5_"
              if (path == 18) {
                return  
              } else if (path == 19) {
                throw new RuntimeException("19")   
              } else if (path == 20) {
                throw new Exception("20")
              }       
            } finally {
              _value += "finally5_"
              if (path == 21) {
                throw new RuntimeException("21")   
              } else if (path == 22) {
                throw new Exception("22")
              }       
            }            
          } finally {
            _value += "finally4_"
            if (path == 23) {
              throw new RuntimeException("23")   
            } else if (path == 24) {
              throw new Exception("24")
            }       
          }         
        } catch (e : IllegalArgumentException) {
          _value += "catch3_"
          if (path == 25) {
            return  
          } else if (path == 26) {
            throw new RuntimeException("26")   
          } else if (path == 27) {
            throw new Exception("27")
          }           
        } finally {
          _value += "finally3_"
          if (path == 28) {
            throw new RuntimeException("28")   
          } else if (path == 29) {
            throw new Exception("29")
          }
          try {
            _value += "try6_"
            if (path == 30) {
              throw new RuntimeException("30")   
            } else if (path == 31) {
              throw new Exception("31")
            }  
          } finally {
            _value += "finally6_"
            if (path == 32) {
              throw new RuntimeException("32")   
            } else if (path == 33) {
              throw new Exception("33")
            }  
          }          
        }        
      } finally {
        _value += "finally2_"
        if (path == 34) {
          throw new RuntimeException("34")   
        } else if (path == 35) {
          throw new Exception("35")
        }  
      }      
    } catch (e : RuntimeException) {
      _value += "catch1_"
      if (path == 36) {
        return  
      } else if (path == 37) {
        throw new RuntimeException("37")   
      } else if (path == 38) {
        throw new Exception("38")
      }         
    } finally {
      _value += "finally1_"
      if (path == 39) {
        throw new RuntimeException("39")   
      } else if (path == 40) {
        throw new Exception("40")
      }    
    }
    
    _value += "end"
  }

  function tryWithBlockWithReturn() {
    try {
      _value += "try_"    
      var x : block() = \ -> {
        _value += "block_"
        return  
      }
      x()
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithBlockWithReturnWithValue() {
    try {
      _value += "try_"    
      var x : block() : String = \ -> {
        _value += "block_"
        return "block" 
      }
      x()
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithBlockWithBreak() {
    try {
      _value += "try_"   
      var x : block() = \ -> {
        _value += "block_"
        for (i in 0..|10) {
          break 
        }
      }
      x()
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithBlockWithContinue() {
    try {
      _value += "try_"   
      var x : block() = \ -> {
        _value += "block_"
        for (i in 0..|10) {
          continue 
        }
      }
      x()
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithBlockWithUncaughtException() {
    try {
      _value += "try_"   
      var x : block() = \ -> {
        _value += "block_"
        throw new RuntimeException("block")
      }
      x()
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithBlockWithCaughtException() {
    try {
      _value += "try_"   
      var x : block() = \ -> {
        _value += "block_"
        throw new RuntimeException("block")
      }
      x()
    } catch (e : RuntimeException) {
      _value += "catch_"
    } finally {
      _value += "finally_"  
    }
  }
  
  function tryWithReturnInsideBlock() {   
    var x : block() = \ -> {
      _value += "block_"
      try {
        _value += "try_"
        return
      } finally {
        _value += "finally_"
      }
    }
    x()
  }
  
  function tryWithReturnWithValueInsideBlock() {   
    var x : block() : String = \ -> {
      _value += "block_"
      try {
        _value += "try_"
        return "foo"
      } finally {
        _value += "finally_"
      }
    }
    x()
  }
  
  function tryWithBreakInsideBlock() {   
    var x : block() = \ -> {
      _value += "block_"
      for (i in 0..|10) {
        try {
          _value += "try_"
          if (true) {
            break  
          }
          _value += "error"
        } finally {
          _value += "finally_"
        }
      }
    }
    x()
  }
  
  function tryWithContinueInsideBlock() {   
    var x : block() = \ -> {
      _value += "block_"
      for (i in 0..|2) {
        try {
          _value += "try_"
          if (true) {
            continue  
          }
          _value += "error"
        } finally {
          _value += "finally_"
        }
      }
    }
    x()
  }
  
  function tryWithUncaughtExceptionInsideBlock() {   
    var x : block() = \ -> {
      _value += "block_"
      try {
        _value += "try_"
        throw new RuntimeException("block")
      } finally {
        _value += "finally_"
      }
    }
    x()
  }
  
  function tryWithCaughtExceptionInsideBlock() {   
    var x : block() = \ -> {
      _value += "block_"
      try {
        _value += "try_"
        throw new RuntimeException("block")
      } catch (e : RuntimeException) {
        _value += "catch_"
      } finally {
        _value += "finally_"
      }
    }
    x()
  }

  function returnStatementAfterFinallyCloses() {
    try {
      _value += "try_"
    } finally {
      _value += "finally_" 
    }
    
    _value += "end_"
    return
  }
  
  function returnStatementWithValueAfterFinallyCloses() : String {
    try {
      _value += "try_"
    } finally {
      _value += "finally_" 
    }
    
    _value += "end_"
    return "end"
  }

  function tryReturnInTryWithFinallyWithNestedTryCatch( b : boolean ) : String {
    try {
      _value += "try_"
      return "passed"
    } finally {
      _value += "finally_"
      try {
        _value += "try_"
        if( b ) throw "foo"
      } catch( e : Exception ) {
        _value += "catch_"
      }
    }
  }

  function tryDeeplyNestedReturnInTryWithFinallyWithNestedTryCatch( b : boolean ) : String {
    try {
      _value += "try_"
      if(true) {
        if(true) {
          if(true) {
            return "passed"
          }
        }
      }
      return "failed"
    } finally {
      _value += "finally_"
      try {
        _value += "try_"
        if( b ) throw "foo"
      } catch( e : Exception ) {
        _value += "catch_"
      }
    }
  }


}