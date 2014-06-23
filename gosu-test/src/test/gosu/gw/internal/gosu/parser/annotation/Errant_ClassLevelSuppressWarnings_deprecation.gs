package gw.internal.gosu.parser.annotation

uses gw.internal.gosu.parser.annotation.abc.JC
uses java.lang.SuppressWarnings

@SuppressWarnings("deprecation")
class Errant_ClassLevelSuppressWarnings_deprecation {
  var _jc: JC

  construct( j: JC ) {}

  function foo( j: JC )  {
    var jj: JC
  }

  function oo( j: JC )  {
    var jj: JC
  }

  property get Cha() : JC {
    var s = "" as String  //## issuekeys: MSG_UNNECESSARY_COERCION
    return null
  }
  property set Cha(j : JC) {
  }
}