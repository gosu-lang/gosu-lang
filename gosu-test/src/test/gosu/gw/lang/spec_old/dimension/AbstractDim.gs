package gw.lang.spec_old.dimension

uses java.lang.Class
uses java.lang.Integer

class AbstractDim<T extends AbstractDim<T>> implements IDimension<T, java.lang.Integer>
{
  var _value : Integer
  
  construct( value : Integer )
  {
    _value = value
  }

  override function numberType() : java.lang.Class<Integer>
  {
    return Integer
  }

  override function toNumber() : Integer
  {
    return _value
  }

  override function fromNumber( p0: Integer ) : T
  {
    return new T( p0 )
  }

  override function toString() : String
  {
    return _value.toString() + " inches"
  }
  
  override function hashCode() : int
  {
    return _value
  }
  
  override function equals( o : Object ) : boolean
  {
    return( o typeis T && o._value == _value )
  }

  override function compareTo( o: T ) : int {
    var n = o.toNumber()
    return _value > n ? 1 : _value < n ? -1 : 0
  }
}
