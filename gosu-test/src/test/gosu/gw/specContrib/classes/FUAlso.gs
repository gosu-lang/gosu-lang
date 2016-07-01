package gw.specContrib.classes

uses gw.specContrib.classes.abc.FUToo

class FUAlso extends FUToo<ArrayList> {
  construct() {
    super( {"S", "E"} )
  }
  function foo() : ArrayList {
    return Owner
  }
  function bar() : ArrayList {
    return _t
  }
}