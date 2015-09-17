package gw.specContrib.generics

class Errant_RecursiveGenericMethod {
  function recursive<P>(o: Object): P {
    // r is Object, not P
    var r = o != null ? recursive(null) : null
    // s is Object, not P
    var s = recursive(null)

    var p: P = r  //## issuekeys: MSG_TYPE_MISMATCH

    // r is inferred from LHS type P
    var rr: P = o != null ? recursive(null) : null
    // s is inferred from LHS type P
    var ss: P = recursive(null)

    return rr
  }
}