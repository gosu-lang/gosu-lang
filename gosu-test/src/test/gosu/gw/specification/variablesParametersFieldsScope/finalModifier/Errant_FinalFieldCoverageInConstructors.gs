package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldCoverageInConstructors {
  // Not initialized in decl, no constructors
  static class c1 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
  }

  // Not initialized in decl, one constructor empty
  static class c2 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
    }
  }

  // Not initialized in decl, one constructor fully initialized
  static class c3 {
    final var _field1 : int
    construct() {
      _field1 = 1
    }
  }

  // Not initialized in decl, two constructorsm, one fully init, other empty
  static class c4 {
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
      _field1 = 1
    }
    construct( p: int ) {
      // mising initialization
    }
  }

  // Not initialized in decl, two constructorsm, both fully init
  static class c5 {
    final var _field1 : int
    construct() {
      _field1 = 1
    }
    construct( p: int ) {
      _field1 = 1
    }
  }

  // Initialized in decl, no constructors
  static class c6 {
    final var _field1 : int = 1
  }

  // Initialized in decl, one constructor empty
  static class c7 {
    final var _field1 : int = 1
    construct() {
    }
  }

  // Initialized in decl, one constructor fully initialized
  static class c8 {
    final var _field1 : int = 1
    construct() {
      _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }

  // Initialized in decl, two constructorsm, one fully init, other empty
  static class c9 {
    final var _field1 : int = 1
    construct() {
      _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
    construct( p: int ) {
    }
  }

  // Initialized in decl, two constructorsm, both fully init
  static class c10 {
    final var _field1 : int = 1
    construct() {
      _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
    construct( p: int ) {
      _field1 = 1  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
    }
  }
}
