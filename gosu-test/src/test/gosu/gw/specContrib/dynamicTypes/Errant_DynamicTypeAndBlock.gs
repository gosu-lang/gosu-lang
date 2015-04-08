package gw.specContrib.dynamicTypes

uses dynamic.Dynamic

class Errant_DynamicTypeAndBlock {
  function foo(p: Dynamic) {}

  function test() {
    // IDE-2011
    var d1: Dynamic = \x -> x
    foo(\x -> x.Type)

    var d2: Dynamic
    d2.func(\a -> a.field)
  }
}