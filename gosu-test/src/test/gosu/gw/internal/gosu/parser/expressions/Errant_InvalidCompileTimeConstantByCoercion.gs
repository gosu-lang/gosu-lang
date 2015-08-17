package gw.internal.gosu.parser.expressions
uses java.math.BigDecimal
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_InvalidCompileTimeConstantByCoercion
{
  function foo( x: BigDecimal = "1.1" ) // error, we do not support implicit coercion from String to BigDecimal
  {
    print( x )
  }

  function foo2( x: BigDecimal = 1.1 ) // OK, we support primitives as compile-time constant (as new BigDecimal( "1.1" ))
  {
    print( x )
  }

  function foo3( x: Boolean = true ) // OK 'true' is a compile-time constant, the compiler boxes it for us at the call site
  {
    print( x )
  }

  function foo4( x: Boolean = Boolean.TRUE ) // error Boolean.TRUE is not a compile-time constant
  {
    print( x )
  }
}
