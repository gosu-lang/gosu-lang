package gw.specContrib.initializers

uses java.util.List
uses java.util.Map

class Errant_InitializerAsParameterDefaultValue {
  // IDE-2291
  function foo1(i: int[] = {}) {}

  function foo2(i: int[] = {1, 2}) {}

  function foo3(i: Integer[] = {1, 2}) {}

  function foo4(i: List<Integer> = {1, 2}) {}

  function foo5(i: Map<String, String> = { "a" -> "b" }) {} //## issuekeys: NOT CONSTANT EXPRESSION
}