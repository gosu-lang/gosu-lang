package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal
uses java.lang.Integer
uses java.math.BigInteger

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimension_BigInteger implements IDimension< SampleDimension_BigInteger, BigInteger >{

  var _value : BigInteger

  construct(value : BigInteger){
    _value = value
  }

  override function toNumber(): BigInteger {
    return _value
  }

  override function fromNumber(number : BigInteger): SampleDimension_BigInteger {
    return new SampleDimension_BigInteger (number)
  }

  override function numberType(): Class< BigInteger > {
    return BigInteger
  }

  override function compareTo(o: SampleDimension_BigInteger): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

}