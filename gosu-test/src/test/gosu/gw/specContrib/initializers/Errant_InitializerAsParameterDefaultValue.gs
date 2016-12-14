package gw.specContrib.initializers

uses java.util.List
uses java.util.Map

class Errant_InitializerAsParameterDefaultValue {
  final static var hm : HashMap<Integer,Integer> = {1 -> 2}
  final static var date: Date = new Date()

  // IDE-2291
  function foo1(i: int[] = {}) {}

  function foo2(i: int[] = {1, 2}) {}

  function foo3(i: Integer[] = {1, 2}) {}

  function foo4(i: List<Integer> = {1, 2}) {}

  function foo5(i: Map<String, String> = { "a" -> "b" }) {} //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED

  function foo6(i: Map<Integer, Integer> = hm) {} //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED

  function foo7( list: Date[] = {date} ) {} //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
}
