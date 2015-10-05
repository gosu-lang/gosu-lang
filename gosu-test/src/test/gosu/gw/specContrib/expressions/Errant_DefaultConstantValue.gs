package gw.specContrib.expressions

class Errant_DefaultConstantValue {

  //ide-2329
  function f1(o : Object = null) {}
  function f2(o : Object[] = {null}) {}
  function f3(o: Object = 1 > 2 ? "t" : "f") {}
  function f4(o: Object = 1 > 2 ? null : null) {}
  function f5(o: Object =  1 > 2 ? null as Object : null) {}

  //ide-2975
  final var int1: Integer = 43
  function foo(xs: int = int1) {}       //## issuekeys: PARAMETER DEFAULT VALUE MUST BE A CONSTANT EXPRESSION

  // ide-2974
  final static var tex3 = 42 > 44 ? 42 : null
  function fooContidionalTernary3(c : int = tex3) {}       //## issuekeys: PARAMETER DEFAULT VALUE MUST BE A CONSTANT EXPRESSION
  final static var tex4 = 42 > 44 ? null : 42
  function fooContidionalTernary4(c : int = tex4) {}        //## issuekeys: PARAMETER DEFAULT VALUE MUST BE A CONSTANT EXPRESSION
  function test() {
    var i: Integer = tex3
  }

  //ide-2328, ide-1283
  function m1(a: java.lang.Class = java.lang.Runnable) {}
  function m2(a: java.lang.Class = java.util.List<Object>) {}
  function m3(a: java.lang.Class = java.util.List<String>) {}       //## issuekeys: PARAMETER DEFAULT VALUE MUST BE A CONSTANT EXPRESSION

  //ide-1904
  static class MyAnno implements IAnnotation {
    construct(s: List<Integer>) {}
  }

  @MyAnno({1, 2, 3})
  class AAA {}

}