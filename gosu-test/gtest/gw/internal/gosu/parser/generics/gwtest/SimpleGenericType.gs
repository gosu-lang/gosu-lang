package gw.internal.gosu.parser.generics.gwtest

class SimpleGenericType<T>
{
  var _member : T as HeyImaTypeVar

  construct( member : T )
  {
    _member = member
  }
  function foo( p : T ) : T
  {
    return p
  }

  function genFoo<E>( p: Foo<E, T> ) {
  }
}