package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal
uses java.lang.Integer

/**
 * Created by Sky on 2015/02/20 with IntelliJ IDEA.
 */
final class SampleDimensionWithArith_Integer implements IDimension< SampleDimensionWithArith_Integer, Integer >{

  var _value : Integer

  construct(value : Integer){
    _value = value
  }

  override function toNumber(): Integer {
    return _value
  }

  override function fromNumber(number : Integer): SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer (number)
  }

  override function numberType(): Class< Integer > {
    return Integer
  }

  override function compareTo(o: SampleDimensionWithArith_Integer): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function add(o : SampleDimensionWithArith_Integer) : SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer (this.toNumber() + o.toNumber() + 20)
  }

  function multiply(o : SampleDimensionWithArith_Integer) : SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer ((this.toNumber() * o.toNumber()) + 20)
  }

  function subtract(o : SampleDimensionWithArith_Integer) : SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer (this.toNumber() - o.toNumber() + 20)
  }

  function divide(o : SampleDimensionWithArith_Integer) : SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer ((this.toNumber() / o.toNumber()) + 20)
  }

  function modulo(o : SampleDimensionWithArith_Integer) : SampleDimensionWithArith_Integer {
    return new SampleDimensionWithArith_Integer ((this.toNumber() % o.toNumber()) + 20)
  }
}