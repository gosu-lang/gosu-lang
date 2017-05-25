package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInMethodCallParameter {

  function foo() {
    var x: Object[] = {} as Integer[]

    bar(:a = {1, 2, 3})
    bar(:a = {1, 2, 3} as Integer[])
    called({1, 2, 3} as Float[])
    bar2(:a = {1, 2, 3} as ArrayList<Integer>)
    bar3(:a = {1, 2, 3} as ArrayList<Integer>)
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