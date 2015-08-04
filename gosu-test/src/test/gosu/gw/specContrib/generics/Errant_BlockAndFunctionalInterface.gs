package gw.specContrib.generics

uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.I
uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.Fun1
uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.Fun2

class Errant_BlockAndFunctionalInterface {
  function acceptBlock1<S extends I>(p: block(a: S)) {}
  function acceptBlock2<S extends I>(p: block(): S) {}

  function test<S extends I, Q>() {
    // IDE-2125
    var b1: block(p: S)
    Errant_BlockAndFunctionalInterface_Java.acceptFun1(b1)
    var b2: block(p: Q)
    Errant_BlockAndFunctionalInterface_Java.acceptFun1(b2)    //## issuekeys: INFERRED TYPE for 'K' is not within its bounds
    var b3: block(): S
    Errant_BlockAndFunctionalInterface_Java.acceptFun2(b3)
    var b4: block(): Q
    Errant_BlockAndFunctionalInterface_Java.acceptFun2(b4)    //## issuekeys: INFERRED TYPE for 'K' is not within its bounds

    var f1: Fun1<S>
    acceptBlock1(f1)
    var f2: Fun2<Q>
    acceptBlock2(f2)      //## issuekeys: INFERRED TYPE for 'S' is not within its bounds

    acceptBlock1(new Fun1<S>() { override function fun(p: S) {} })
    acceptBlock2(new Fun2<Q>() { function fun(): Q { return null } })     //## issuekeys: INFERRED TYPE for 'S' is not within its bounds
  }
}
