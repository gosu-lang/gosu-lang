package gw.internal.gosu.parser.composition

class FooBarMixinWithOverride implements IFoo, IBar
{
  delegate _foo represents IFoo
  delegate _bar represents IBar
  
  construct( foo: String, bar: String )
  {
    _foo = new FooImpl( foo )
    _bar = new BarImpl( bar )
  }
  
  override function foo() : String
  {
    return _foo.foo() + "overridden"
  }
}
