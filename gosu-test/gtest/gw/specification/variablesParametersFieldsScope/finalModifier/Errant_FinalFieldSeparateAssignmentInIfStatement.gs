package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldSeparateAssignmentInIfStatement {
  class C1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
      }
      _field1 = 0
    }
  }

  class C1_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        return
      }
      _field1 = 0
    }
  }

  class C2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
      }
      else {
      }
      _field1 = 0
    }
  }

  class C2_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        return
      }
      else {
      }
      _field1 = 0
    }
  }

  class C3 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C3_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
        return
      }
      _field1 = 0
    }
  }

  class C4 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
      }
      else {
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C4_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
        return
      }
      else {
      }
      _field1 = 0
    }
  }

  class C5 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
      }
      else {
        _field1 = 1
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
        return
      }
      else {
        _field1 = 1
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
        return
      }
      else {
        _field1 = 1
        return
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C6 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
      }
      else {
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C6_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
      }
      else {
      }
      _field1 = 0
    }
  }

  class C6_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        return
      }
      else {
      }
      _field1 = 0
    }
  }

  class C7 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      else {
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
        }
      }
      else {
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_3 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
      }
      _field1 = 0
    }
  }

  class C8 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
          return
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
        }
        return
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
        return
      }
      else {
        if( cond ) {
          _field1 = 0
        }
        return
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C8_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
          return
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_3 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
        if( cond ) {
          _field1 = 0
          return
        }
      }
      _field1 = 0
    }
  }

  class C9 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_1 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
        }
      }
      else {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_2 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
        if( cond ) {
          _field1 = 0
        }
        else {
          _field1 = 1
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_3 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
        }
      }
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_4 {

    final var _field1 : int
    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      else {
        if( cond ) {
          _field1 = 0
          return
        }
        else {
          _field1 = 1
          return
        }
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C10 {

    final var _field1 : int = 0
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C10_1 {

    final var _field1 : int = 0
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11 {

    final var _field1 : int = 0
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      else {
        _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11_1 {

    final var _field1 : int = 0
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      else {
        _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11_2 {

    final var _field1 : int = 0
    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      else {
        _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      _field1 = 0  //## issuekeys: MSG_UNREACHABLE_STMT, MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }
}
