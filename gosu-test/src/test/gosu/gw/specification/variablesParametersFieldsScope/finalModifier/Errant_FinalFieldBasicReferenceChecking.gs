package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldBasicReferenceChecking {
  class C1 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( cond: boolean ) {
      if( cond )
      {
        _field1 = 9
        if( cond ) {
          print( _field1 )
        }
      }
      print( _field1 )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    }
  }

  class C2 {
    final var _field1 : int

    construct( cond: boolean ) {
      if( cond )
      {
        _field1 = 9
        if( cond ) {
          print( _field1 )
        }
      }
      else {
        _field1 = 9
      }
      print( _field1 )
    }
  }

  class C3 {
    final var size : int

    construct() {
      while( true ) {
        size = 4
        print( size )
        return
      }
    }
  }

  class C4 {
    final var size : int

    construct() {
      while( true ) {
        print( size )  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
        size = 4
        return
      }
    }
  }
}
