package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldCoverageFromMemberAssignment {
  class C1 {
    final var _x : int

    construct() {
      this._x = 0
    }
  }

  class C2 {
    final var _x : int = 0

    construct() {
      this._x = 0  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  class C3 {
    final var _x : int

    construct() {
      _x = 0
      this._x = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }

  class C4 {
    final var _x : int

    construct() {
      this._x = 0
      _x = 0  //## issuekeys: MSG_VAR_MIGHT_ALREADY_HAVE_BEEN_INIT
    }
  }
}
