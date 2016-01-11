package gw.lang.spec_old.expressions

uses java.awt.Rectangle
uses java.lang.Math

class MemberAccessTestClass
{
  var _x : String as X
  var _bounds : Rectangle as Bounds
  
  construct( value : String )
  {
    _x = value
    _bounds = new Rectangle( 1, 1, 1, 1 )
  }
  
  function name() : String
  {
    return X
  }
  
  function voidReturnType() : void
  {
    _x = "z"
  }
}
