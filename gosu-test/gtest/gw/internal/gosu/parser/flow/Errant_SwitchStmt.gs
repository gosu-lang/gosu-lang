package gw.internal.gosu.parser.flow
uses java.lang.RuntimeException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_SwitchStmt {
  enum MyEnum { A, B, C }

  function f1( value: MyEnum ) : int {
    switch( value ) {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2( value: MyEnum ) : int {
    switch( value ) {
      case A:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f1_0( value: MyEnum ) : int {
    switch( value ) {
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_0( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f1_0_1( value: MyEnum ) : int {
    switch( value ) {
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_0_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f1_0_2( value: MyEnum ) : int {
    switch( value ) {
      default:
        throw new RuntimeException()
    }
  }

  function f2_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
        throw new RuntimeException()
    }
  }

  function f3_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
        throw new RuntimeException()
    }
  }

  function f5_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
        throw new RuntimeException()
    }
  }

  function f6_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        throw new RuntimeException()
    }
  }

  function f7_0_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
        throw new RuntimeException()
    }
  }

  //-------

  function f1_0_3( value: MyEnum ) : int {
    switch( value ) {
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_0_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f1_0_4( value: MyEnum ) : int {
    switch( value ) {
      default:
        while( true ) {}
    }
  }

  function f2_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
        while( true ) {}
    }
  }

  function f3_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
        while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
        while( true ) {}
    }
  }

  function f5_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
        while( true ) {}
    }
  }

  function f6_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        while( true ) {}
    }
  }

  function f7_0_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
        while( true ) {}
    }
  }

  //-------

  function f1_0_3a( value: MyEnum ) : int {
    switch( value ) {
      default:
        return 1
    }
  }

  function f2_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
      default:
        return 1
    }
  }

  function f3_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      default:
        return 1
    }
  }

  function f5_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      default:
        return 1
    }
  }

  function f6_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        return 1
    }
  }

  function f7_0_3a( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      default:
        return 1
    }
  }

  //-------

  function f2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
       case B:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
       case B:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
       case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      case B:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      case B:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      case B:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      case B:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      case B:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      case B:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
        throw new RuntimeException()
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      case B:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      case B:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
        assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        break
      case B:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        return 1
      case B:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        throw new RuntimeException()
      case B:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6( value: MyEnum ) : int {
    switch( value ) {
      case A:
        while( true ) {
        }
      case B:
        while( true ) {
        }
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  ///////////////////////////////////////////////////

  //-------

  function f2_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1a( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
      case C:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          break
      case C:
        break

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          break
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          break
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          break
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          break
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_3_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          return 1
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_4_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          while( true ) {
          }
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f3_7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7_1( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  ///////////////////////////////////////////////////

  //-------

  function f2_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          break
      case C:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          break
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          break
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          break
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          break
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_3_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          return 1
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          throw new RuntimeException()
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          throw new RuntimeException()
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          throw new RuntimeException()
      case C:
          break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          throw new RuntimeException()
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          throw new RuntimeException()
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_4_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          throw new RuntimeException()
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          while( true ) {
          }
      case C:
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f3_7_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7_2( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      case C:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  ///////////////////////////////////////////////////

  //-------

  function f2_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          break
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_3_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          return 1
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_4_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          throw new RuntimeException()
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          while( true ) {
          }
      default:
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f3_7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7_3( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  ///////////////////////////////////////////////////

  //-------

  function f2_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          break
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_3_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          return 1
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_4_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          throw new RuntimeException()
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f3_7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7_4( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  ///////////////////////////////////////////////////

  //-------

  function f2_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_2_1_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          break
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          return 1
      default:
        return 1
    }
  }

  function f3_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          return 1
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          return 1
      default:
        return 1
    }
  }

  function f5_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          return 1
      default:
        return 1
    }
  }

  function f6_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          return 1
      default:
        return 1
    }
  }

  function f7_3_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          return 1
      default:
        return 1
    }
  }

  //-------

  function f2_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          throw new RuntimeException()
      default:
        return 1
    }
  }

  function f3_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          throw new RuntimeException()
      default:
        return 1
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          throw new RuntimeException()
      default:
        return 1
    }
  }

  function f5_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          throw new RuntimeException()
      default:
        return 1
    }
  }

  function f6_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          throw new RuntimeException()
      default:
        return 1
    }
  }

  function f7_4_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          throw new RuntimeException()
      default:
        return 1
    }
  }

  //-------

  function f2_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          if( value != null ) {
            return 1
          }
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        return 1
    }
  }

  function f3_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_5_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f2_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_6_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          while( true ) {
          }
      default:
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //-------

  function f3_7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          break
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          return 1
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          throw new RuntimeException()
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f6_7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          assert value === A
      case B:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f7_7_5( value: MyEnum ) : int {
    switch( value ) {
      case A:
          while( true ) {
          }
      case B:
          assert value === A
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        break
    }
  }  //## issuekeys: MSG_MISSING_RETURN

}