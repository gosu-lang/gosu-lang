package gw.specification.expressions.intervalExpressions

uses java.lang.Class

final class MyDim implements IDimension< MyDim, Integer> {
  var _value: Integer;

  construct( value: Integer ) {
    _value = value
  }

  override function toNumber(): Integer {
    return _value
  }

  override function fromNumber( value: Integer ): MyDim {
    return new MyDim ( value )
  }

  override function numberType(): Class<Integer> {
    return Integer
  }

  override function compareTo( o: MyDim): int {
    return _value.compareTo( o._value )
  }
}