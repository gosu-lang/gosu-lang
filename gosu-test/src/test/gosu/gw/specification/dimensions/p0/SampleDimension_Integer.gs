package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal
uses java.lang.Integer

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimension_Integer implements IDimension< SampleDimension_Integer, Integer >{

  var _value : Integer

  construct(value : Integer){
    _value = value
  }

  override function toNumber(): Integer {
    return _value
  }

  override function fromNumber(number : Integer): SampleDimension_Integer {
    return new SampleDimension_Integer (number)
  }

  override function numberType(): Class< Integer > {
    return Integer
  }

  override function compareTo(o: SampleDimension_Integer): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

}