package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_StaticFinalFieldCoverageInConstructors {
  // Not initialized in decl, no constructors
  static class c1 {
    static final var STATIC_FIELD1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
  }

  // Not initialized in decl, one constructor empty
  static class c2 {
    static final var STATIC_FIELD1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
    }
  }

  // Not initialized in decl, one constructor fully initialized
  static class c3 {
    static final var STATIC_FIELD1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
  }

  // Not initialized in decl, two constructorsm, one fully init, other empty
  static class c4 {
    static final var STATIC_FIELD1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
    construct( p: int ) {
      // mising initialization
    }
  }

  // Not initialized in decl, two constructorsm, both fully init
  static class c5 {
    static final var STATIC_FIELD1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
    construct( p: int ) {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
  }

  // Initialized in decl, no constructors
  static class c6 {
    static final var STATIC_FIELD1 : int = 1
  }

  // Initialized in decl, one constructor empty
  static class c7 {
    static final var STATIC_FIELD1 : int = 1
    construct() {
    }
  }

  // Initialized in decl, one constructor fully initialized
  static class c8 {
    static final var STATIC_FIELD1 : int = 1
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
  }

  // Initialized in decl, two constructorsm, one fully init, other empty
  static class c9 {
    static final var STATIC_FIELD1 : int = 1
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
    construct( p: int ) {
    }
  }

  // Initialized in decl, two constructorsm, both fully init
  static class c10 {
    static final var STATIC_FIELD1 : int = 1
    construct() {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
    construct( p: int ) {
      STATIC_FIELD1 = 1  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
  }
}
