package gw.specContrib.typeinference

uses java.lang.StringBuilder

class Errant_NamedArgs {
  function foo<T>( p1: T, p2(s: T): int ) : T {
    return p1
  }

  //IDE-1943
  function test() {
    foo( "", \s -> s.length() )

    foo( :p1 = "", :p2 = \s -> s.length() )
    foo( "", :p2 = \s -> s.length() )
    foo( :p1 = "", \s -> s.length() )  //## issuekeys: MSG_EXPECTING_NAMED_ARG

    var s = foo( :p2=\s : StringBuilder-> s.length(), :p1=null )
    s.append( "" ) // assert infers StringBuilder
  }
}