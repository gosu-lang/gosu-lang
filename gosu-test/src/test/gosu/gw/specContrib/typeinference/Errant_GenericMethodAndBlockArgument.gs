package gw.specContrib.typeinference

uses java.util.ArrayList
uses java.util.List

class Errant_GenericMethodAndBlockArgument {
  // IDE-1882
  static class GosuClass<T> {
    function foo<R>(block(p: T): List<R>): List<R> {
      return null
    }

    static function test() {
      var gc = new GosuClass<Object>()
      var a = gc.foo( \p1 -> gc.foo( \p2 -> new ArrayList<String>() ) )
      var b: List<String> = a
    }
  }
}