package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

class Errant_ParameterLevelSuppressWarnings_all {
  var _jc = new JC()  //## issuekeys: MSG_DEPRECATED_MEMBER
  var _str = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION

  construct( @SuppressWarnings("all") j: JC ) {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function foo( @SuppressWarnings("all") j: JC )  {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function oo( @SuppressWarnings("all") j: JC )  {
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  property get Cha() : JC {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha( @SuppressWarnings("all") j : JC ) {
  }
}