package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

class Errant_ConstructorLevelSuppressWarnings_all {
  var _jc: JC  //## issuekeys: MSG_DEPRECATED_MEMBER

  @SuppressWarnings("all")
  construct( j: JC ) {
    var jj: JC
  }

  function foo( j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function oo( j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  property get Cha() : JC {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha(j : JC) {  //## issuekeys: MSG_DEPRECATED_MEMBER
  }
}