package gw.internal.gosu.compiler.sample.expression

class PropertyIdentifier
{
  var _m1 : int

  // Implicit instance
  var _m2 : String as M2

  var _m5 : int
  static var _m6 : int

  // Implicit static
  static var _m3 : String as M3

  // Readonly instance
  property get M4() : int
  {
    return 4
  }

  // Explicit instance
  property get M5() : int
  {
    return _m5
  }
  property set M5( value : int )
  {
    _m5 = value
  }

  // Explicit static
  static property get M6() : int
  {
    return _m6
  }
  static property set M6( value : int )
  {
    _m6 = value
  }

  function accessM5() : int
  {
    return M5
  }
  function changeM5( value : int )
  {
    M5 = value
  }

  static function accessM6() : int
  {
    return M6
  }
  static function changeM6( value : int )
  {
    M6 = value
  }
}