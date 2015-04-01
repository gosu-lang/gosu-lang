package gw.specContrib.classes.method_Scoring.objects_And_Primitive_Types

uses java.lang.Integer

class Errant_ObjectsPrimitives_DifferentHierarchyCase {
  interface A {}
  interface B extends A {}
  interface C extends B {}
  interface D extends C {}

  var aa : A
  var bb : B
  var cc : C
  var dd : D

  var int1 : int

  var intObj1 : Integer

  class AA {}
  class KK {}

  //Case #1
  function fun(i : A, j : Integer) : AA {return null}
  function fun(i : B, j : int) : KK {return null}
  function caller() {
    //IDE-1494
    var lvar1 : AA = fun(cc, intObj1)
    var lvar2 : KK = fun(cc, intObj1)      //## issuekeys: INCOMPATIBLE TYPES. FOUND:
  }

  //Case #2
  function fun1(j : Integer, i : A) : AA {return null}
  function fun1(j : int, i : C) : KK {return null}
  function caller1() {
    //IDE-1494
    var lvar1 : AA = fun1(intObj1, dd)
    var lvar2 : KK = fun1(intObj1, dd)      //## issuekeys: INCOMPATIBLE TYPES. FOUND:
  }
}