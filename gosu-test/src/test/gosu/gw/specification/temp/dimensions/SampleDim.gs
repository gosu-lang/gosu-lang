package gw.specification.temp.dimensions
uses java.lang.Integer
uses java.lang.Class
uses gw.lang.IDimension

class SampleDim implements IDimension<SampleDim, Integer> {
  var _value: Integer;

  construct( value: Integer ) {
    _value = value
  }

  override function toNumber(): Integer {
    return _value
  }

  override function fromNumber( value: Integer ): SampleDim {
    return new SampleDim( value )
  }

  override function numberType(): Class<Integer> {
    return Integer
  }

  override function compareTo( o: SampleDim ): int {
    return _value.compareTo( o._value )
  }
}