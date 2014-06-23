package gw.internal.gosu.parser.annotation

class Errant_DeprecationWarningsMaskedByEnclosingDeprecation {

  class UsesDeprecated1 {
    var _x : SoDeprecated  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  @Deprecated("")
  class UsesDeprecated2 {
    var _x : SoDeprecated
  }

  class UsesDeprecated3 {
    @Deprecated("")
    var _x : SoDeprecated
  }

  @Deprecated("")
  class UsesDeprecated4 {
    @Deprecated("")
    var _x : SoDeprecated
  }


  class UsesDeprecated5 {
    function foo( d: SoDeprecated ) {}  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  @Deprecated("")
  class UsesDeprecated6 {
    function foo( d: SoDeprecated ) {}
  }

  class UsesDeprecated7 {
    @Deprecated("")
    function foo( d: SoDeprecated ) {}
  }

  @Deprecated("")
  class UsesDeprecated8 {
    @Deprecated("")
    function foo( d: SoDeprecated ) {}
  }


  class UsesDeprecated9 {
    property get Foo() : SoDeprecated { return null }  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  @Deprecated("")
  class UsesDeprecated10 {
    property get Foo() : SoDeprecated { return null }
  }

  class UsesDeprecated11 {
    @Deprecated("")
    property get Foo() : SoDeprecated { return null }
  }

  @Deprecated("")
  class UsesDeprecated12 {
    @Deprecated("")
    property get Foo() : SoDeprecated { return null }
  }


  @Deprecated("sooo deprecated")
  class SoDeprecated {}

}