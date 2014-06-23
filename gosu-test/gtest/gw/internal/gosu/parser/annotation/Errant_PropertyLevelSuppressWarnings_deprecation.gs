package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

class Errant_PropertyLevelSuppressWarnings_deprecation {
  var _jc: JC  //## issuekeys: MSG_DEPRECATED_MEMBER

  construct( j: JC ) {}  //## issuekeys: MSG_DEPRECATED_MEMBER

  function foo( j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function oo( j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  @SuppressWarnings("deprecation")
  property get Cha() : JC {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha(j : JC) {  //## issuekeys: MSG_DEPRECATED_MEMBER
  }
}