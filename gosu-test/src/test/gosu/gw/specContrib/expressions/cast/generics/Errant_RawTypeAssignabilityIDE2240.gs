package gw.specContrib.expressions.cast.generics

/**
 * Test for IDE-2240
 */
class Errant_RawTypeAssignabilityIDE2240 {

    class A<T> {}

    function test(a: A, t: Type) {
      var a1: A<String> = a        //## issuekeys: INCOMPATIBLE TYPES.

      var t1: Type<String> = t
    }
}