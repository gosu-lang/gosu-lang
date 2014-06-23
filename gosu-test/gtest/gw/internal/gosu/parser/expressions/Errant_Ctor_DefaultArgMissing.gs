package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_Ctor_DefaultArgMissing
{
  construct( x: int, y: int = 1, z: int ) 
  {
    print( x + y + z )
  }
}
