package gw.internal.gosu.parser.composition

class FooBarImpl implements IFoo, IBar
{
  var _foo: String
  delegate _bar represents IBar
  
  construct( foo: String, bar: String )
  {
    _foo = foo
    _bar = new BarImpl( bar )
  }
  
  override function foo() : String
  {
    return _foo
  }

  override function foo2( p1: int ) : int
  {
    return p1
  }
}
