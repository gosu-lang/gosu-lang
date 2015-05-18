package gw.specContrib.generics

// IDE-2231
class Errant_RawReturnType extends Errant_RawReturnTypeJava.JavaClass2<String> {
  function test() {
    var p1 = Prop
    var p2 = getProp()
  }
}