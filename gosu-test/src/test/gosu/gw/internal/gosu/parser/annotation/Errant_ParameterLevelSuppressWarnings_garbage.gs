package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

class Errant_ParameterLevelSuppressWarnings_garbage {
  var _jc = new JC()  //## issuekeys: MSG_DEPRECATED_MEMBER
  var _str = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION

  construct( @SuppressWarnings("garbage") j: JC ) {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function foo( @SuppressWarnings("garbage") j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function oo( @SuppressWarnings("garbage") j: JC )  {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  property get Cha() : JC {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha( @SuppressWarnings("garbage") j : JC ) {  //## issuekeys: MSG_DEPRECATED_MEMBER
  }
}