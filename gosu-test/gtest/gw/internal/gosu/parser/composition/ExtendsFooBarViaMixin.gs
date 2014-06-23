package gw.internal.gosu.parser.composition

class ExtendsFooBarViaMixin extends FooBarViaMixin
{
  construct( foo: String, bar: String )
  {
    super( foo, bar )
  }
  
  override function foo() : String
  {
    return super.foo() + "mine"
  }
}
