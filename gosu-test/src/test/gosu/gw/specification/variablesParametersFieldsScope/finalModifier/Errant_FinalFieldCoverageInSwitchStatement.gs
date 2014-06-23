package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldCoverageInSwitchStatement {
  static enum MyEnum { En1, En2, En3 }

  class C1 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
      }
    }
  }

  class C2 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        default:
      }
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
    }
  }

  class C5_2 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          break
        default:
          _field1 = 0
          return
      }
    }
  }

  class C5_2_1 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          break
        default:
          _field1 = 0
          return
      }
    }
  }

  class C5_2_2 {
    final var _field1 : int

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
        default:
          _field1 = 0
          return
      }
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
    }
  }

  class C6 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
      }
    }
  }

  class C7 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
      }
    }
  }

  class C8 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
      }
    }
  }

  class C9 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
      }
    }
  }

  class C10 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      }
    }
  }

  class C11 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
      }
    }
  }

  class C12 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          _field1 = 0
      }
    }
  }

  class C13 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
          _field1 = 0
          break
      }
    }
  }

  class C14 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
      }
    }
  }

  class C15 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
          break
        case 2:
          _field1 = 0
      }
    }
  }

  class C16 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
          _field1 = 0
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
          break
      }
    }
  }

  class C17 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

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
    }
  }

  class C18 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: int ) {
      switch( value )
      {
        case 1:
        case 2:
        default:
      }
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
    }
  }

  class C23 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

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
    }
  }

  class C26 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
          _field1 = 0
          break
      }
    }
  }

  class C27 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( value: MyEnum ) {
      switch( value )
      {
        case En1:
        case En2:
          _field1 = 0
          break
      }
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
    }
  }

  class C30 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

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
    }
  }
}
