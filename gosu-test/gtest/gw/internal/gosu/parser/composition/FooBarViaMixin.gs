package gw.internal.gosu.parser.composition

class FooBarViaMixin implements IFoo, IBar
{
  delegate _fooBar represents IFoo, IBar
  
  construct( foo: String, bar: String )
  {
    _fooBar = new FooBarMixin( foo, bar )
  }
}
