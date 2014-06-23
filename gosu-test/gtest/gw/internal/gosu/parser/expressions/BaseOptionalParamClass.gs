package gw.internal.gosu.parser.expressions

class BaseOptionalParamClass 
{
  var _res : String

  construct( p1: String, p2: int )
  {
    _res = p1 + ", " + p2
  }

  function overrideMe( x: String ) {}

  property get Res() : String
  {
    return _res
  }
}
