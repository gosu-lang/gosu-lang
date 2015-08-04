package gw.specContrib.typeinference

uses java.util.ArrayList
uses java.util.List
uses java.lang.CharSequence
uses java.lang.Integer
uses gw.lang.reflect.IType
uses java.lang.CharSequence
uses java.util.Map

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

  // IDE-1943
  static class GosuClass2 {
    function foo<T>(p1: T, p2: block(p: T): int) {}

    function test() {
      foo("", \s -> s.length())
      foo(:p1 = "", :p2 = \s -> s.length())
      foo(:p2 = \s -> s.length(), :p1 = "")
    }
  }

  // IDE-2253
  static class GosuClass3 {
    class A<T> {}

    function foo1<T>(p1: T, p2: T): A<T> { return null }

    function foo2<T>(values: T[]): A<T> { return null }

    function test() {
      var a2: A<Integer> = foo1(Integer.MAX_VALUE, null)
      var a1: A<Integer> = foo2({100})
      var a3: A<Integer> = foo2({100, null}) // infers null as Integer

      var yy : List<CharSequence> = hey( \t->t.charAt(0))
      foo( hey( \t->t.charAt(0) ) ) // infers t
    }

    function hey<T>( doit(t:T) ) : List<T> { return null }
    function foo( l: List<CharSequence> ) {}
  }

  static class GosuClass4 {
    function foo<M extends CharSequence>() : List<M> {
      var map: Map<IType,CharSequence>

      return map
          .keySet()
          .where( \ type -> true )
          .flatMap( \ mapping -> new ArrayList<Object>() ) // should not apply ctx type here, List<M> only applies to entire expression i.e., the *last* flatMap() call
          .flatMap( \ mapping -> null )
    }
  }
}