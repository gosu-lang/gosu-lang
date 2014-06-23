package gw.internal.gosu.parser.generics.gwtest

class ComplexGenericType<A,B> extends SimpleGenericType<B>
{
  var _a : A as Prop1

  construct( a : A, b : B )
  {
    super( b )
    _a = a
  }
  override function foo( p : B ) : B
  {
    return p
  }

  function genFoo2<E>( p: Foo<E, B> ) {
    genFoo( p )
  }
}