package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal
uses java.lang.Integer
uses java.math.BigInteger

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionWithArith_BigInteger implements IDimension< SampleDimensionWithArith_BigInteger, BigInteger >{

  var _value : BigInteger

  construct(value : BigInteger){
    _value = value
  }

  override function toNumber(): BigInteger {
    return _value
  }

  override function fromNumber(number : BigInteger): SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger (number)
  }

  override function numberType(): Class< BigInteger > {
    return BigInteger
  }

  override function compareTo(o: SampleDimensionWithArith_BigInteger): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function add(o : SampleDimensionWithArith_BigInteger) : SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger (this.toNumber() + o.toNumber() + 20)
  }

  function multiply(o : SampleDimensionWithArith_BigInteger) : SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger ((this.toNumber() * o.toNumber()) + 20)
  }

  function subtract(o : SampleDimensionWithArith_BigInteger) : SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger (this.toNumber() - o.toNumber() + 20)
  }

  function divide(o : SampleDimensionWithArith_BigInteger) : SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger ((this.toNumber() / o.toNumber()) + 20)
  }

  function modulo(o : SampleDimensionWithArith_BigInteger) : SampleDimensionWithArith_BigInteger {
    return new SampleDimensionWithArith_BigInteger ((this.toNumber() % o.toNumber()) + 20)
  }

}