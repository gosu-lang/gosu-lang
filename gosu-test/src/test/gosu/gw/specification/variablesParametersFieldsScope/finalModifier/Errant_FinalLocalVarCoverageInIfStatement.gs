package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalLocalVarCoverageInIfStatement {
  class C1 {

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      if( cond ) {
      }
    }
  }

  class C2 {

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      if( cond ) {
      }
      else {
      }
    }
  }

  class C3 {

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      if( cond ) {
        _field1 = 0
      }
    }
  }

  class C4 {

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      if( cond ) {
        _field1 = 0
      }
      else {
      }
    }
  }

  class C5 {

    construct( cond: boolean ) {
      final var _field1 : int
      if( cond ) {
        _field1 = 0
      }
      else {
        _field1 = 1
      }
    }
  }

  class C6 {

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
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

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
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

    construct( cond: boolean ) {
      final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
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

    construct( cond: boolean ) {
      final var _field1 : int
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

    construct( cond: boolean ) {
      final var _field1 : int = 0
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
    }
  }

  class C11 {

    construct( cond: boolean ) {
      final var _field1 : int = 0
      if( cond ) {
        _field1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      else {
        _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
    }
  }
}
