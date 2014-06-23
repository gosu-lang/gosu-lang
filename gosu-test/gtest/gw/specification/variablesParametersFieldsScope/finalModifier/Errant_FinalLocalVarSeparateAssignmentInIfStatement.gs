package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalLocalVarSeparateAssignmentInIfStatement {
  class C1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
      }
      localVar = 0
    }
  }

  class C1_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        return
      }
      localVar = 0
    }
  }

  class C2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
      }
      else {
      }
      localVar = 0
    }
  }

  class C2_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        return
      }
      else {
      }
      localVar = 0
    }
  }

  class C3 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C3_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
        return
      }
      localVar = 0
    }
  }

  class C4 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
      }
      else {
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C4_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
        return
      }
      else {
      }
      localVar = 0
    }
  }

  class C5 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
      }
      else {
        localVar = 1
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
        return
      }
      else {
        localVar = 1
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        localVar = 0
        return
      }
      else {
        localVar = 1
        return
      }
      localVar = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C6 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
      }
      else {
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C6_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
      }
      else {
      }
      localVar = 0
    }
  }

  class C6_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        return
      }
      else {
      }
      localVar = 0
    }
  }

  class C7 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      else {
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
        }
      }
      else {
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
          return
        }
      }
      else {
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7_3 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      else {
      }
      localVar = 0
    }
  }

  class C8 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
          return
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
        }
        return
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_1_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
        return
      }
      else {
        if( cond ) {
          localVar = 0
        }
        return
      }
      localVar = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C8_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
          return
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8_3 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      else {
        if( cond ) {
          localVar = 0
          return
        }
      }
      localVar = 0
    }
  }

  class C9 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_1 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
        }
      }
      else {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_2 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      else {
        if( cond ) {
          localVar = 0
        }
        else {
          localVar = 1
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_3 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      else {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
        }
      }
      localVar = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9_4 {

    construct( cond: boolean ) {
      final var localVar : int
      if( cond ) {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      else {
        if( cond ) {
          localVar = 0
          return
        }
        else {
          localVar = 1
          return
        }
      }
      localVar = 0  //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }

  class C10 {

    construct( cond: boolean ) {
      final var localVar : int = 0
      if( cond ) {
        localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C10_1 {

    construct( cond: boolean ) {
      final var localVar : int = 0
      if( cond ) {
        localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11 {

    construct( cond: boolean ) {
      final var localVar : int = 0
      if( cond ) {
        localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      else {
        localVar = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11_1 {

    construct( cond: boolean ) {
      final var localVar : int = 0
      if( cond ) {
        localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      else {
        localVar = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C11_2 {

    construct( cond: boolean ) {
      final var localVar : int = 0
      if( cond ) {
        localVar = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      else {
        localVar = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
        return
      }
      localVar = 0  //## issuekeys: MSG_UNREACHABLE_STMT, MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }
}
