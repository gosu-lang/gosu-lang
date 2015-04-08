package gw.specContrib.dynamicTypes

uses dynamic.Dynamic

class Errant_DynamicTypeAndBlock {
  function foo(p: Dynamic) {}

  function test() {
    // IDE-2011
    var d: Dynamic = \x -> x
    foo(\x -> x.Type)
  }
}