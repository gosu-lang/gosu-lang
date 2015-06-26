package gw.specContrib.classes.method_Scoring.collections_And_Generics

uses java.lang.Integer

class Errant_ClassTypeParam<T extends Integer> {

  class Type1 {
  }

  class Type2 {
  }

  function m(p: T): Type1 {
    return null
  }

  function m(p: Object): Type2 {
    return null
  }

  //IDE-1729
  function test() {
    var a111: Type1 = m(1)          //## issuekeys: INCOMPATIBLE TYPES
    var a112: Type1 = new Errant_ClassTypeParam().m(1)

    var b111: Type2 = m(1)
    var b112: Type2 = new Errant_ClassTypeParam().m(1)  //## issuekeys: INCOMPATIBLE TYPES
  }
}