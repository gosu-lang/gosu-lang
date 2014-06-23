package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

class Errant_ParameterLevelSuppressWarnings_deprecation {
  var _jc = new JC()  //## issuekeys: MSG_DEPRECATED_MEMBER
  var _str = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION

  construct( @SuppressWarnings("deprecation") j: JC ) {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function foo( @SuppressWarnings("deprecation") j: JC )  {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  function oo( @SuppressWarnings("deprecation") j: JC )  {
    var jj: JC  //## issuekeys: MSG_DEPRECATED_MEMBER
  }

  property get Cha() : JC {  //## issuekeys: MSG_DEPRECATED_MEMBER
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha( @SuppressWarnings("deprecation") j : JC ) {
  }
}