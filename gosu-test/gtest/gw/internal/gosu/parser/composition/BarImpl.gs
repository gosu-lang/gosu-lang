package gw.internal.gosu.parser.composition

class BarImpl implements IBar
{
  var _bar: String
  
  construct( bar: String )
  {
    _bar = bar
  }

  override function bar() : String
  {
    return _bar
  }

  override function bar2( p1: boolean ) : boolean
  {
    return p1
  }
}
