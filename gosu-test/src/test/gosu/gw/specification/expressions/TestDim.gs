package gw.specification.expressions

uses java.lang.Integer
uses java.lang.Comparable

final class TestDim implements IDimension<TestDim, Integer> {
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

  function add(p0 : TestDim) : TestDim {
    return new TestDim(_value + p0.toNumber())
  }

  function subtract(p0 : TestDim) : TestDim {
    return new TestDim(_value - p0.toNumber())
  }

  function multiply(p0 : TestDim) : TestDim {
    return new TestDim(_value * p0.toNumber())
  }

  function divide(p0 : TestDim) : TestDim {
    return new TestDim(_value / p0.toNumber())
  }

  override function toString(): String {
    return _value.toString()
  }

  override function compareTo(o: TestDim): int {
    return _value.compareTo(o.toNumber());
  }
}
