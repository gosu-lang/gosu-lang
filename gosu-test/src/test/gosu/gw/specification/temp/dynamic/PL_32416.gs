package gw.specification.temp.dynamic

class PL_32416 {
  function ternaryLub() {
    var a = false
    var b = 3
    var c : dynamic.Dynamic = "bye"
    var x = a ? b : c
    x.blah( 0 ) // x has to be dynamic for this to compile
  }

  function listLub() {
    var a : dynamic.Dynamic
    var x = { a, 4}
    x[1].blah() // x's component type has to be dynamic for this to compile
  }
}