package gw.internal.gosu.parser.composition

class FooImpl implements IFoo
{
  var _foo : String
  
  construct( foo: String )
  {
    _foo = foo
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
