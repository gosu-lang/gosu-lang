package gw.internal.gosu.compiler.featureliteral

class Errant_BadChainingInFeatureLiteral {

  class A {
    var prop: A
    function fun(): A { return null }
  }

  function test() {
    var fl1 = A#prop#fun()  // correct error 'Only property literals can be chained'
    var fl2 = A#fun()#prop  // no error - bug
  }

}