package gw.specContrib.operators.checkedArithmetic

class Errant_CheckedArithmeticDefaultParams {

  function foo1(i : int = !-42, j : int = !-5, k : int = !-100) : int {
    return !-i !* !-j
  }

  function test() {
    print(foo1(:i = !-5, :j = !-5!*2!+!-42))
  }
}