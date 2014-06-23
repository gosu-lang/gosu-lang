package gw.internal.gosu.parser.blocks

class ConstructorSuperTest {

  var _func : block(String):String
  var _val : String

  public construct(sParentId: String, newEntryFunc(String) : String) {
    _func = newEntryFunc
    _val = sParentId
  }

  function getVal() : String {
    return _func(_val)
  }

}
