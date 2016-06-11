package gw.specContrib.generics

uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.I
uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.Fun1
uses gw.specContrib.generics.Errant_BlockAndFunctionalInterface_Java.Fun2

class Errant_RecursiveForwardReferences {
  static class Test1 {
    static class TestClass<T extends TestClass<T, U, V>, U extends OtherClass<U, V, T>, V extends ThirdClass<V, T, U>> extends OtherClass<U, V, T> {
      var v: V

      function testMe<TT extends TestClass<TT, UU, VV>, UU extends OtherClass<UU, VV, TT>, VV extends ThirdClass<VV, TT, UU>>( t: TT ) : VV {
        return t.v
      }
    }

    static class OtherClass<T extends OtherClass<T, U, V>, U extends ThirdClass<U, V, T>, V extends TestClass<V, T, U>> extends ThirdClass<U, V, T> {
    }

    static class ThirdClass<T extends ThirdClass<T, U, V>, U extends TestClass<U, V, T>, V extends OtherClass<V, T, U>> {
    }
  }

  static class Test2 {
    static class TestClass<T extends TestClass<T, U, V>, U extends OtherClass<U, V, T>, V extends ThirdClass<V, T, U>> extends OtherClass<U, V, T> {
    }

    static class OtherClass<T extends OtherClass<T, U, V>, U extends ThirdClass<U, V, T>, V extends TestClass<V, T, U>> extends ThirdClass<U, V, T> {
    }

    static class ThirdClass<T extends ThirdClass<T, U, V>, U extends TestClass<U, V, T>, V extends OtherClass<V, T, U>> extends TestClass<U, V, T> {  //## issuekeys: MSG_CYCLIC_INHERITANCE
    }
  }
}
