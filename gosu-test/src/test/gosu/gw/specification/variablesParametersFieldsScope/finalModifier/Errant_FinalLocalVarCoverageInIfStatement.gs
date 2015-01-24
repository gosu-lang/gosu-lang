package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalLocalVarCoverageInIfStatement {
  class C1 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      v1 = 1
    }
  }

  class C2 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
      }
      else {
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      else {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      v1 = 2
    }
  }

  class C3 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        v1 = 0
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
        v1 = 0
        print( v1 )
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C4 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        v1 = 0
      }
      else {
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        v1 = 0
      }
      else {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C5 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        v1 = 0
      }
      else {
        v1 = 1
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        v1 = 0
      }
      else {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
        v1 = 1
        print( v1 )
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C6 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
      }
      else {
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
        if( cond ) {
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        }
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      else {
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C7 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
      else {
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
        if( cond ) {
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        }
        else {
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 1
          print( v1 )
        }
        print( v1 )
      }
      else {
        print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C8 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
      else {
        if( cond ) {
          v1 = 0
        }
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
      else {
        if( cond ) {
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        }
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C9 {

    construct( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
      else {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
    }
    function foo( cond: boolean ) {
      final var v1 : int
      if( cond ) {
        if( cond ) {
          v1 = 0
        }
        else {
          v1 = 1
        }
      }
      else {
        if( cond ) {
          v1 = 0
        }
        else {
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 1
          print( v1 )
        }
      }
      v1 = 2  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C10 {

    construct( cond: boolean ) {
      final var v1 : int = 0
      if( cond ) {
        v1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
    }
  }

  class C11 {

    construct( cond: boolean ) {
      final var v1 : int = 0
      if( cond ) {
        v1 = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
      else {
        v1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      }
    }
  }
}
