package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalLocalVarCoverageInSwitchStatement {
  enum MyEnum { En1, En2, En3 }

  class C1 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
      }
      print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    }
  }

  class C2 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        default:
      }
      print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    }
  }

  class C3 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        default:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C4 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        default:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
      }
    }
  }

  class C5 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        default:
          v1 = 0
          return
      }
    }
  }

  class C5_2 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          break
        default:
          v1 = 0
          return
      }
    }
  }

  class C5_2_1 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          break
        default:
          v1 = 0
          return
      }
    }
  }

  class C5_2_2 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
        default:
          v1 = 0
          return
      }
    }
  }

  class C5_3 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 1
          print( v1 )
          break
        default:
          v1 = 0
          return
      }
    }
  }

  class C6 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
      }
    }
  }

  class C7 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C8 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
      }
    }
  }

  class C9 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
      }
    }
  }

  class C10 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      }
    }
  }

  class C11 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
        case 2:
      }
    }
  }

  class C12 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C13 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
      }
    }
  }

  class C14 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
          print( v1 )
      }
    }
  }

  class C15 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
        case 2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C16 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
          print( v1 )
          break
      }
    }
  }

  class C17 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
        case 2:
          v1 = 0
          break
      }
    }
  }

  class C18 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
        default:
      }
    }
  }

  class C19 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
        default:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C20 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
        case 2:
        default:
          v1 = 0
          break
      }
    }
  }

  class C21 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        case 2:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
        default:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
          print( v1 )
      }
    }
  }

  class C22 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
        case 2:
        default:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
      }
    }
  }

  class C23 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
        case 2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          break
        default:
          v1 = 0
      }
    }
  }

  class C24 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
        case 2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
        default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          v1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
      }
    }
  }

  class C25 {

    function foo( value: int ) {
      final var v1 : int
      switch( value )
      {
        case 1:
          v1 = 0
          break
        case 2:
          v1 = 0
          break
        default:
          v1 = 0
      }
    }
  }

  class C26 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
      }
    }
  }

  class C27 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
        case En2:
          v1 = 0
          break
      }
    }
  }

  class C28 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
        case En2:
        case En3:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
          // no default necessary
      }
    }
  }

  class C29 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
          v1 = 0
          break
        case En2:
        case En3:
          v1 = 0
          break
          // no default necessary
      }
    }
  }

  class C30 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
          v1 = 0
          break
        case En2:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          break
        case En3:
          v1 = 0
          break
          // no default necessary
      }
    }
  }

  class C31 {

    function foo( value: MyEnum ) {
      final var v1 : int
      switch( value )
      {
        case En1:
          v1 = 0
          break
        case En2:
          v1 = 0
          break
        case En3:
          print( v1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
          v1 = 0
          print( v1 )
          break
          // no default necessary
      }
    }
  }
}
