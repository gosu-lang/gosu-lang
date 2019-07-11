package gw.specContrib.generics

uses java.lang.Integer

class Errant_CompoundTypeWithTypeVar {
  reified function fun<T,S>(a: T, b: T, c: S) {
    if (a typeis Integer) {
      if (a == b) {
        print("equal")
      }
      var r : S  = null
      if( a == c ) {}
    }
  }
}
