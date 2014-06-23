package gw.internal.gosu.compiler.sample.statement.classes

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
