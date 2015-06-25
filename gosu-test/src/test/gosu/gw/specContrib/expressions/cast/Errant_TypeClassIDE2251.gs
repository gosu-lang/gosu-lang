package gw.specContrib.expressions.cast

/**
 * Test for IDE-2251
 */
class Errant_TypeClassIDE2251 {

  function test() {
    var a: java.lang.Class<Boolean>
    var b: Type<Boolean>
    a = b
    var t = a as Type<Boolean>
  }


  class GosuClass {
    function test() {
      var a: java.lang.Class<Boolean>
      var b: java.lang.Class<Object> = a
      var t = b as Type<Boolean>

      var o: Object
      switch (o.Class) {
        case Boolean:
      }
    }
  }

  var a3 : java.lang.Class<ArrayList>
  var t3 = a3 as Type<CharSequence>

  var a5 : java.lang.Class<AbstractList>
  var t5 = a5 as Type<ArrayList>
}