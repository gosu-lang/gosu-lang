package gw.specContrib.expressions.cast.generics

class Errant_GenericsAssignCastabilityInterfaces4 {
  class AClass {}
  class BClass {}

  interface IOne<T> {}
  interface ITwo<T> extends IOne<T> {}

  function test() {
    var a1: IOne<AClass>
    var b1 = a1 as IOne<BClass>  //## issuekeys: MSG_TYPE_MISMATCH

    var a2: ITwo<AClass>
    var b2 = a2 as IOne<BClass>  //## issuekeys: MSG_TYPE_MISMATCH
  }
}