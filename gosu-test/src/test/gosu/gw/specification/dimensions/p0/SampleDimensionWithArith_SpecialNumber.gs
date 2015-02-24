package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionWithArith_SpecialNumber implements IDimension< SampleDimensionWithArith_SpecialNumber, SpecialNumber >{

  var _value : SpecialNumber

  construct(value : SpecialNumber){
    _value = value
  }

  override function toNumber(): SpecialNumber {
    return _value
  }

  override function fromNumber(specialNumber : SpecialNumber): SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (specialNumber)
  }

  override function numberType(): Class< SpecialNumber > {
    return SpecialNumber
  }

  override function compareTo(o: SampleDimensionWithArith_SpecialNumber): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function add(o : SampleDimensionWithArith_SpecialNumber) : SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (new SpecialNumber(this.toNumber().doubleValue() + o.toNumber().doubleValue()))
  }

  function multiply(o : SampleDimensionWithArith_SpecialNumber) : SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (new SpecialNumber(this.toNumber().doubleValue() * o.toNumber().doubleValue()))
  }

  function subtract(o : SampleDimensionWithArith_SpecialNumber) : SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (new SpecialNumber(this.toNumber().doubleValue() - o.toNumber().doubleValue()))
  }

  function divide(o : SampleDimensionWithArith_SpecialNumber) : SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (new SpecialNumber(this.toNumber().doubleValue() / o.toNumber().doubleValue()))
  }

  function modulo(o : SampleDimensionWithArith_SpecialNumber) : SampleDimensionWithArith_SpecialNumber {
    return new SampleDimensionWithArith_SpecialNumber (new SpecialNumber(this.toNumber().doubleValue() % o.toNumber().doubleValue()))
  }
}