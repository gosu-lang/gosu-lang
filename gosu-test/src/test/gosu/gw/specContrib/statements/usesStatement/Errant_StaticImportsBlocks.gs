package gw.specContrib.statements.usesStatement

uses ClassWithBlocks#f1(block(String, Integer): Long)
uses ClassWithBlocks#f2(block(Object, Object): Object)
uses ClassWithBlocks#f3(block(String, Long): Integer)

class Errant_StaticImportsBlocks {
  function foo() {
    f1(\s, i -> 123L)
    f2<Integer, Float, String>(\a: Integer, b -> "hi")
  }
}