package gw.specContrib.generics

class Errant_RawTypes {
  function test() {
    // IDE-1693
    var a1: B1<A> = C.getB1()
    var a2: B2<Object> = C.getB2()
    var a3: B2<A> = C.getB2()  //## issuekeys: INCOMPATIBLE TYPES
    var a4: B3<A & java.lang.Cloneable> = C.getB3()
  }
}