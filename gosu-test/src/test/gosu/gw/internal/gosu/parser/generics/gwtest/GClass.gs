package gw.internal.gosu.parser.generics.gwtest

class GClass<T>
{
  var _s: String
  var _t: T

  construct( s: String, x: T )
  {
    _s = s
    _t = x
  }
}
