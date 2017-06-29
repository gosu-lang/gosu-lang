package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInMethodCallParameter {

  function foo() {
    var x: Object[] = {} as Integer[]

    bar(:a = {1, 2, 3})
    bar(:a = {1, 2, 3} as Integer[]) //## issuekeys: MSG_UNNECESSARY_COERCION
    called({1, 2, 3} as Float[]) //## issuekeys: MSG_UNNECESSARY_COERCION
    bar2(:a = {1, 2, 3} as ArrayList<Integer>) //## issuekeys: MSG_UNNECESSARY_COERCION
    bar3(:a = {1, 2, 3} as ArrayList<Integer>) //## issuekeys: MSG_UNNECESSARY_COERCION
  }

  function bar(a: Object[]) {
  }

  function called(obj: Float[]) {
  }

  function bar2(a: ArrayList<Integer>) {

  }

  function bar3(a: ArrayList<Object>) {

  }
}
