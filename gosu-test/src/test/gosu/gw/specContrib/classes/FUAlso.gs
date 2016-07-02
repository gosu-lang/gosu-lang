package gw.specContrib.classes

class FUAlso extends FUMaybe<ArrayList> {
  construct() {
    super( {"S", "E"} )
  }
  function prop() : ArrayList {
    return Owner
  }
  function field() : ArrayList {
    return _t
  }
  function fieldPropn() : ArrayList {
    return this._t
  }
}