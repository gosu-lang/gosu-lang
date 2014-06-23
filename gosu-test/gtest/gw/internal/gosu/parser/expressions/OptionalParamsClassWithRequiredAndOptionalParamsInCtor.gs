package gw.internal.gosu.parser.expressions

class OptionalParamsClassWithRequiredAndOptionalParamsInCtor 
{
  var _p1: int
  var _p2: String
  var _p3: int
  
  construct( p1: int, p2: String, p3: int = 1 )
  {
    _p1 = p1
    _p2 = p2
    _p3 = p3
  }
  
  override function toString() : String
  {
    return _p1 + ", " + _p2 + ", " + _p3
  }
}
