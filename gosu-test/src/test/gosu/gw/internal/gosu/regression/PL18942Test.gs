package gw.internal.gosu.regression

uses gw.test.TestClass

uses java.util.*

class PL18942Test extends TestClass {

  function testRegression() {
    var b = false
    f( \ l -> { b = true } )
    assertTrue(b)
  }

  function f<T>(b : block(arg : List<T>)) {
    var a : List<T> = new ArrayList<T>()
    b(a) // Syntax error
  }
}
