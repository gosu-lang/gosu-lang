package gw.internal.gosu.parser.flow
uses java.lang.RuntimeException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DoWhileStmt {
  function f1() : int {
    do {
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2() : int {
    do {
      continue
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3() : int {
    do {
      break
    } while( true )
    return 1
  }

  function f4() : int {
    do {
      return 1
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5() : int {
    do {
      throw new RuntimeException()
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6() : int {
    do {
      assert true
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a( cond: boolean ) : int {
    do {
    } while( cond )
    return 1
  }

  function f2a( cond: boolean ) : int {
    do {
      continue
    } while( cond )
    return 1
  }

  function f3a( cond: boolean ) : int {
    do {
      break
    } while( cond )
    return 1
  }

  function f4a( cond: boolean ) : int {
    do {
      return 1
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5a( cond: boolean ) : int {
    do {
      throw new RuntimeException()
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6a( cond: boolean ) : int {
    do {
      assert cond
    } while( cond )
    return 1
  }

  //-------------------------------------------------

  function f1_1( cond: boolean ) : int {
    do {
      if( cond ) {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
    } while( true )
    return 1
  }

  function f4_1( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1b( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
          break
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1( cond: boolean ) : int {
    do {
      if( cond ) {
      }
    } while( cond )
    return 1
  }

  function f2a_1( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
    } while( cond )
    return 1
  }

  function f3a_1( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
    } while( cond )
    return 1
  }

  function f4a_1( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
    } while( cond )
    return 1
  }

  function f5a_1( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
    } while( cond )
    return 1
  }

  function f6a_1( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f6a_1a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
    } while( cond )
    return 1
  }

  //-------------------------------------------------

  function f1_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
      }
    } while( true )
    return 1
  }

  function f4_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_1a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f2a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f3a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f4a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f5a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f6a_1_1( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
      }
    } while( cond )
    return 1
  }

  function f6a_1_1a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
      }
    } while( cond )
    return 1
  }

  //-------------------------------------------------

  function f1_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        continue
      }
    } while( true )
    return 1
  }

  function f4_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_2a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        continue
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f2a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f3a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f4a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f5a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f6a_1_2( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f6a_1_2a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  //-------------------------------------------------

  function f1_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f2_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f3_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f4_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f5_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f6_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f6_1_3a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        break
      }
    } while( true )
    return 1
  }

  function f1a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  function f2a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  function f3a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        continue
      }
    } while( cond )
    return 1
  }

  function f4a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  function f5a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  function f6a_1_3( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  function f6a_1_3a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        break
      }
    } while( cond )
    return 1
  }

  //-------------------------------------------------

  function f1_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        return 2
      }
    } while( true )
    return 1
  }

  function f4_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_4a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        return 2
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        return 2
      }
    } while( cond )
    return 1
  }

  function f2a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        return 2
      }
    } while( cond )
    return 1
  }

  function f3a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        return 2
      }
    } while( cond )
    return 1
  }

  function f4a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        return 2
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        return 2
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6a_1_4( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        return 2
      }
    } while( cond )
    return 1
  }

  function f6a_1_4a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        return 2
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  //-------------------------------------------------

  function f1_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1
  }

  function f4_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_5a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        throw new RuntimeException()
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1
  }

  function f2a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1
  }

  function f3a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1
  }

  function f4a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6a_1_5( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1
  }

  function f6a_1_5a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        throw new RuntimeException()
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  //-------------------------------------------------

  function f1_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        assert cond
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        assert cond
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f2_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f3_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        assert cond
      }
    } while( true )
    return 1
  }

  function f3_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1
  }

  function f4_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        assert cond
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f4_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        assert cond
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        assert cond
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        assert true
      }
      else {
        while( true ) {
        }
      }
    } while( true )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f1a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f2a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f3a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f4a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f5a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f6a_1_6( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  function f6a_1_6a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        assert cond
      }
    } while( cond )
    return 1
  }

  //------------------------------------------------------------

  function f1a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1
  }

  function f2a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
        continue
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1
  }

  function f3a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
        break
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1
  }

  function f4a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
        return 1
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f5a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
        throw new RuntimeException()
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f6a_1_7( cond: boolean ) : int {
    do {
      if( cond ) {
        assert cond
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1
  }

  function f6a_1_7a( cond: boolean ) : int {
    do {
      if( cond ) {
        while( true ) {
        }
      }
      else {
        while( true ) {
        }
      }
    } while( cond )
    return 1  //## issuekeys: MSG_UNREACHABLE_STMT
  }
}