package gw.spec.core.statements.trycatch
uses java.lang.Throwable
uses java.lang.RuntimeException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class TryCatchFinally_ErrantCases {

  static class TryWithNoCatchOrFinally {
    function errant() {
      try {
        print("foo")
      }
    }
  }
  
  static class TryWithTwoFinallyBlocks {
    function errant() {
      try {
        print("foo")
      } finally {
        print("bar")  
      } finally {
        print("baz")
      }
    }
  }
  
  static class TryWithCatchWithNoParens {
    function errant() {
      try {
        print("foo")  
      } catch {
        
      }
    }
  }
  
  static class TryWithCatchWithEmptyParens {
    function errant() {
      try {
        print("foo")  
      } catch() {
        
      }
    }
  }
  
  static class TryWithCatchWithMissingLeftParen {
    function errant() {
      try {
        print("foo")  
      } catch e : RuntimeException) {
        
      }
    }
  }
  
  static class TryWithCatchWithMissingRightParen {
    function errant() {
      try {
        print("foo")  
      } catch (e : RuntimeException {
        
      }
    }
  }
  
  static class TryWithCatchThatCatchesNonThrowable {
    function errant() {
      try {
        print("foo")  
      } catch(e : Object) {
        
      }
    }
  }
  
  static class TryWithCatchThatCatchesTypeVariable {
    function errant<T extends Throwable>(arg : T) {
      try {
        print("foo")  
      } catch(e : T) {
        
      }
    }
  }
  
  static class TryWithNakedCatchStatement {
    function errant() {
      catch(e : RuntimeException) {
        
      }
    }
  }
  
  static class TryWithNakedFinallyStatement {
    function errant() {
      finally {
        
      }
    }
  }
  
  static class TryWithFinallyWithArgument {
    function errant() {
      try {
      } finally(e : RuntimeException) {
        
      }
    }
  }
  
  static class TryWithCatchWithAlreadyUsedVariableName {
    function errant() {
      var e : RuntimeException
      try {
      } catch (e : RuntimeException) {
        
      }
    }
  }

  
  //
  
  static class TryWithReturnInsideFinally {
    function errant() {
      try {
        print("foo")
      } finally {
        print("bar")
        return  
      }
    }
  }
  
  static class TryWithNonLocalContinueInsideFinally {
    function errant() {
      for (i in 0..|10) {
        try {
          print("foo")
        } finally {
          print("bar")
          continue  
        }
      }
    }
  }
  
  static class TryWithNonLocalBreakInsideFinally {
    function errant() {
      for (i in 0..|10) {
        try {
          print("foo")
        } finally {
          print("bar")
          break  
        }
      }
    }
  }

  function badCatches() {
    // errors
    try {} catch( e ) {} catch( e ){}
    try {} catch( e ) {} catch( e : RuntimeException ){}
    try {} catch( e ) {} catch( e : java.lang.Exception ){}
    try {} catch( e : Throwable ){} catch( e : RuntimeException ) {} 
    try {} catch( e : Throwable ){} catch( e ) {} 
    try {} catch( e : java.lang.Exception ){} catch( e ) {} 
    
    //no error
    try {} catch( e ) {} catch( e : Throwable ){}

    // errors
    try {} catch( e : java.io.IOException ){} catch( e ) {} catch( e ){}
    try {} catch( e : java.io.IOException ){} catch( e ) {} catch( e : RuntimeException ){}
    try {} catch( e : java.io.IOException ){} catch( e ) {} catch( e : java.lang.Exception ){}
    try {} catch( e : java.io.IOException ){} catch( e : Throwable ){} catch( e : RuntimeException ) {} 
    try {} catch( e : java.io.IOException ){} catch( e : Throwable ){} catch( e ) {}
    try {} catch( e : java.io.IOException ){} catch( e : java.lang.Exception ){} catch( e ) {} 

    // two errors
    try {} catch( e ) {} catch( e : java.io.IOException ){} catch( e ){}
    try {} catch( e ) {} catch( e : java.io.IOException ){} catch( e : RuntimeException ){}
    try {} catch( e ) {} catch( e : java.io.IOException ){} catch( e : java.lang.Exception ){}
    try {} catch( e : Throwable ){} catch( e : java.io.IOException ){} catch( e : RuntimeException ) {} 
    try {} catch( e : Throwable ){} catch( e : java.io.IOException ){} catch( e ) {} 
    try {} catch( e : java.lang.Exception ){} catch( e : java.io.IOException ){}  catch( e ) {} 
  }
}
