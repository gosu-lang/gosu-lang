package gw.internal.gosu.parser.flow
uses java.lang.RuntimeException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_WhileStmt {
  function f0() : int {
    while( true ) {
      continue
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f0a() : int {
    while( true ) {
      continue
    }
  }

  function f01() : int {
    while( true ) {
      break
    }
    return 1
  }

  function f01a() : int {
    while( true ) {
      break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f02() : int {
    while( true ) {
      return 1
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f02a() : int {
    while( true ) {
      return 1
    }
  }

  function f03() : int {
    while( true ) {
      throw new RuntimeException()
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f03a() : int {
    while( true ) {
      throw new RuntimeException()
    }
  }

  function f04() : int {
    while( true ) {
      assert true
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f04a() : int {
    while( true ) {
      assert true
    }
  }

  function f05() : int {
    while( true ) {
      while( true ) {
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f05a() : int {
    while( true ) {
      while( true ) {
      }
    }
  }

  function f06() : int {
    while( true ) {
      while( !true ) {
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f06a() : int {
    while( true ) {
      while( !true ) {
      }
    }
  }

  function f07() : int {
    while( !true ) {
      while( true ) {
      }
    }
    return 1
  }

  function f07a() : int {
    while( !true ) {
      while( true ) {
      }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f08() : int {
    while( !true ) {
      while( !true ) {
      }
    }
    return 1
  }

  function f08a() : int {
    while( !true ) {
      while( !true ) {
      }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f09( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f10( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        break
      }
    }
    return 1
  }

  function f10a( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        continue
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f11( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        return 1
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f12( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        throw new RuntimeException()
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f13( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        assert cond
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f14( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        while( true ) {
        }
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f15( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        while( true ) {
          break
        }
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f16( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        break
      }
      else {
        return 1
      }
    }
    return 1
  }

  function f16a( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        return 1
      }
      else {
        break
      }
    }
    return 1
  }

  function f17( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
      }
      else {
        break
      }
    }
    return 1
  }

  function f18( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        if( cond ) {
          break
        }
      }
      else {
      }
    }
    return 1
  }

  function f19( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
      }
      else {
      }
    }
    return 1    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f20( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        continue
      }
      return 1
    }
    return 2    //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function f21( cond: boolean ) : int {
    while( true ) {
      if( cond ) {
        if( cond ) {
          break
        }
        continue
      }
      return 1
    }
    return 2
  }

  function f22( cond: boolean ) : int {
    while( true ) {
      try {
        return 1
      }
      catch( e: RuntimeException ) {
        if( cond ) {
          continue
        }
        throw e
      }
    }
  }
}
