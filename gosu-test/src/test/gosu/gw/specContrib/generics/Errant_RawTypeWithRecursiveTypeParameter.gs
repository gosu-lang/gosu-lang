package gw.specContrib.generics

class Errant_RawTypeWithRecursiveTypeParameter {
  // IDE-1961
  function test(b: Errant_RawTypeWithRecursiveTypeParameterJava.B, a: Errant_RawTypeWithRecursiveTypeParameterJava.A) {
    b.Prop = a
    b.foo(a)
  }
}