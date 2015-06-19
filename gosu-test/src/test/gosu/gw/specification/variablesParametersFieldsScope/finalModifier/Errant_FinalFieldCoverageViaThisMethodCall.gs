package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldCoverageViaThisMethodCall {
  class C1 {
    final var _field1 : int

    construct( s: int ) {
      this()
    }

    construct() {
      _field1 = 0
    }
  }

  class C2 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( s: int ) {
      this()
    }

    construct() {
    }
  }

  class C3 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( s: int ) {
      this()
      _field1 = 0
    }

    construct() {
    }
  }

  class C4 {
    final var _field1 : int

    construct( s: int ) {
      this()
      _field1 = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }

    construct() {
      _field1 = 0
    }
  }

  class C5 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( s: int ) {
      this()
    }

    construct() {
      _field1 = 0
    }

    construct( s: String ) {
      //  empty
    }
  }

  class C6 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct( s: String ) {
      this( _field1 )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION, MSG_BAD_IDENTIFIER_NAME
    }

    construct() {
      _field1 = 0
    }

    construct( s: int ) {
      _field1 = s
    }
  }
}
