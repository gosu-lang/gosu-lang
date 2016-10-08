package gw.specContrib.generics

uses java.util.function.Supplier

class Errant_FunctionalIterfaceInference {
  function test1() {
    var list: List<String>
    foo(  \ -> list )
    foo2( list )
  }
  function foo<U>( mapper(): List<U> ) : List<U> {
    return null
  }
  function foo2<U>( m: List<U> ) : List<U> {
    return null
  }

  function test2() {
    var z: Optional<String>
    flatMap( \ o -> z )
  }
  public function flatMap<U>( mapper(o: Object): Optional<U> ): Optional<U> {
    return null
  }

  function test3() {
    var r: Optional<String>
    var x = r.flatMap( \ m -> r )

    var list2: List<String>
    flatMap( \ -> list2 )
  }
  function flatMap<U>( supp: Supplier<List<U>> ) : List<U> {
    return supp.get()
  }

  function test4() {
    var zz: Optional<String>
    zz.flatMap( \ m -> zz )
  }
}