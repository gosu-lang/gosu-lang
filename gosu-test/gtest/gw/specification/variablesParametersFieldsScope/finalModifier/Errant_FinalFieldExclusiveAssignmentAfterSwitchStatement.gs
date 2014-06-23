package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldExclusiveAssignmentAfterSwitchStatement {
  static enum MyEnum { En1, En2, En3 }

  class C1 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
      }
      _field1 = 0
    }
  }

  class C2 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
          default:
      }
      _field1 = 0
    }
  }

  class C3 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
          default:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C4 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
          default:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        default:
          _field1 = 0
          return
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C5_2 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          break
        default:
          _field1 = 0
          return
      }
      _field1 = 0
    }
  }

  class C5_3 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 1
          break
        default:
          _field1 = 0
          return
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C6 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
      }
      _field1 = 0
    }
  }

  class C7 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
      }
      _field1 = 0
    }
  }

  class C10 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C11 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C12 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C13 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C14 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C15 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C16 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C17 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C18 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          default:
      }
      _field1 = 0
    }
  }

  class C19 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          default:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C20 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
        default:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C21 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        default:
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C22 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
            _field1 = 0
            break
        case 2:
        default:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C23 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          break
        default:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C24 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          _field1 = 0
        default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C25 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          _field1 = 0
          break
        default:
          _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C26 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C27 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
        case En2:
          _field1 = 0
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C28 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
        case En2:
        case En3:
          _field1 = 0
          break
          // no default necessary
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C29 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          _field1 = 0
          break
        case En2:
        case En3:
          _field1 = 0
          break
          // no default necessary
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C30 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          _field1 = 0
          break
        case En2:
          break
        case En3:
          _field1 = 0
          break
          // no default necessary
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C31 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          _field1 = 0
          break
        case En2:
          _field1 = 0
          break
        case En3:
          _field1 = 0
          break
          // no default necessary
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C32 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          return
      }
      _field1 = 0
    }
  }

  class C33 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          if( value > 0 ) {
            return
          }
      }
      _field1 = 0
    }
  }

  class C34 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        default:
          return
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C35 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          break
        default:
          return
      }
      _field1 = 0
    }
  }

  class C36 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        default:
          return
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C37 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          break
      }
      _field1 = 0
    }
  }

  class C38 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          _field1 = 1
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C39 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          if( value > 0 ) {
            _field1 = 0
            return
          }
          break
      }
      _field1 = 0
    }
  }

  class C40 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          switch( value )
          {
            case 1:
              _field1 = 0
              return
            default:
              if( value > 0 ) {
                _field1 = 0
                return
              }
              break
          }
          break
      }
      _field1 = 0
    }
  }

  class C41 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          switch( value )
          {
            case 1:
              _field1 = 0
              break
            default:
              if( value > 0 ) {
                _field1 = 0
                return
              }
              break
          }
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C42 {
    final var _field1 : int

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          return
        default:
          switch( value )
          {
            case 1:
              _field1 = 0
              return
            default:
              if( value > 0 ) {
                _field1 = 0
                break
              }
              break
          }
          break
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }
}
