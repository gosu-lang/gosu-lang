package gw.specification.types.typeConversion.conversionInvolvingReferenceTypes

uses java.lang.Integer

class TestDim implements IDimension<TestDim, Integer> {
  var _value: Integer

  construct(value: Integer) {
    _value = value
  }

  override function toNumber(): Integer {
    return _value
  }

  override function fromNumber(p0: Integer): TestDim {
    return new TestDim(p0)
  }

  override function numberType(): java.lang.Class<Integer> {
    return Integer
  }

  override function toString() : String {
    return _value.toString()
  }

  override function compareTo( o: TestDim ) : int {
    return _value.compareTo( o._value )
  }
}