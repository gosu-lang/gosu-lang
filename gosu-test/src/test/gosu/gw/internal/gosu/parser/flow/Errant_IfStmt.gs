package gw.internal.gosu.parser.flow
uses java.lang.RuntimeException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IfStmt {
  function f1( cond: boolean ) : int {
    if( cond ) {
    }
    else {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2( cond: boolean ) : int {
    if( cond ) {
      return 1
    }
    else {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3( cond: boolean ) : int {
    if( cond ) {
      throw new RuntimeException()
    }
    else {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4( cond: boolean ) : int {
    if( cond ) {
      assert cond
    }
    else {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5( cond: boolean ) : int {
    if( cond ) {
      while( true ) {
      }
    }
    else {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-----------

  function f2_1( cond: boolean ) : int {
    if( cond ) {
    }
    else {
      return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1( cond: boolean ) : int {
    if( cond ) {
    }
    else {
      throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1( cond: boolean ) : int {
    if( cond ) {
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1( cond: boolean ) : int {
    if( cond ) {
    }
    else {
      while( true ) {
      }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //---------------------

  function f2_2( cond: boolean ) : int {
    if( cond ) {
      return 1
    }
    else {
      return 1
    }
  }

  function f3_2( cond: boolean ) : int {
    if( cond ) {
      return 1
    }
    else {
      throw new RuntimeException()
    }
  }

  function f4_2( cond: boolean ) : int {
    if( cond ) {
      return 1
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2( cond: boolean ) : int {
    if( cond ) {
      return 1
    }
    else {
      while( true ) {
      }
    }
  }

  //-------------

  function f2_3( cond: boolean ) : int {
    if( cond ) {
      throw new RuntimeException()
    }
    else {
      return 1
    }
  }

  function f3_3( cond: boolean ) : int {
    if( cond ) {
      throw new RuntimeException()
    }
    else {
      throw new RuntimeException()
    }
  }

  function f4_3( cond: boolean ) : int {
    if( cond ) {
      throw new RuntimeException()
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3( cond: boolean ) : int {
    if( cond ) {
      throw new RuntimeException()
    }
    else {
      while( true ) {
      }
    }
  }

  //-------------

  function f2_4( cond: boolean ) : int {
    if( cond ) {
      assert cond
    }
    else {
      return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4( cond: boolean ) : int {
    if( cond ) {
      assert cond
    }
    else {
      throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4( cond: boolean ) : int {
    if( cond ) {
      assert cond
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4( cond: boolean ) : int {
    if( cond ) {
      assert cond
    }
    else {
      while( true ) {
      }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------------

  function f2_5( cond: boolean ) : int {
    if( cond ) {
      while( true ) {
      }
    }
    else {
      return 1
    }
  }

  function f3_5( cond: boolean ) : int {
    if( cond ) {
      while( true ) {
      }
    }
    else {
      throw new RuntimeException()
    }
  }

  function f4_5( cond: boolean ) : int {
    if( cond ) {
      while( true ) {
      }
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5( cond: boolean ) : int {
    if( cond ) {
      while( true ) {
      }
    }
    else {
      while( true ) {
      }
    }
  }

  //-------------

  function f2_6( cond: boolean ) : int {
    if( cond ) {
      do {
      } while( true )
    }
    else {
      return 1
    }
  }

  function f3_6( cond: boolean ) : int {
    if( cond ) {
      do {
      } while( true )
    }
    else {
      throw new RuntimeException()
    }
  }

  function f4_6( cond: boolean ) : int {
    if( cond ) {
      do {
      } while( true )
    }
    else {
      assert cond
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6( cond: boolean ) : int {
    if( cond ) {
      do {
      } while( true )
    }
    else {
      while( true ) {
      }
    }
  }

}