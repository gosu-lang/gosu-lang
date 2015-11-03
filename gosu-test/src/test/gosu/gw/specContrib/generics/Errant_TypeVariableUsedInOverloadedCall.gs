package gw.specContrib.generics

class Errant_TypeVariableUsedInOverloadedCall {

  function foo<A>( l: A ): List<A> { return null }

  function bar<B>( l: B ) {
    // addAll() is overloaded, test that <B> is resolved correctly in the overloads
    foo( l ).addAll( {l} )
  }
}