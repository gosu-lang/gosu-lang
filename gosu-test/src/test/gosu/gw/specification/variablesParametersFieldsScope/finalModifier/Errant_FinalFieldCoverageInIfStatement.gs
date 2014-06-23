package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldCoverageInIfStatement {
  class C1 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond ) {
      }
    }
  }

  class C2 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond ) {
      }
      else {
      }
    }
  }

  class C3 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
      }
    }
  }

  class C4 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0
      }
      else {
      }
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
    }
  }

  class C6 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond ) {
        if( cond ) {
          _field1 = 0
        }
      }
      else {
      }
    }
  }

  class C7 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

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
    }
  }

  class C8 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

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
    }
  }

  class C10 {
    final var _field1 : int = 0

    construct( cond: boolean ) {
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
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
    }
  }
}
