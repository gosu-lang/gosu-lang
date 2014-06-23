package gw.internal.gosu.parser.expressions
uses java.math.BigDecimal
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_InvalidCompileTimeConstantByCoercion
{
  function foo( x: BigDecimal = "1.1" ) // error "1.1" coerces as new BigDecimal( "1.1" ), which is not compile-time const
  {
    print( x )
  }

  function foo2( x: BigDecimal = 1.1 ) // error 1.1 coerces as new BigDecimal( "1.1" ), which is not compile-time const
  {
    print( x )
  }

  function foo3( x: Boolean = true ) // error true (with implicit cast) is not a compile-time constant
  {
    print( x )
  }

  function foo4( x: Boolean = Boolean.TRUE ) // error Boolean.TRUE is not a compile-time constant
  {
    print( x )
  }
}
