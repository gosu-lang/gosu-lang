package gw.specContrib.classes.method_Scoring.collections_And_Generics

uses java.util.ArrayList

uses gw.specContrib.classes.method_Scoring.collections_And_Generics.Errant_Wildcards_MethodScoring_Java.A

class Errant_Wildcards_MethodScoring {
  function test(l: ArrayList<Object>) {
    // IDE-2123
    var a1: A = Errant_Wildcards_MethodScoring_Java.foo(l)
  }
}