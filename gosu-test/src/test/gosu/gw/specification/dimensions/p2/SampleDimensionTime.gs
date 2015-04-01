package gw.specification.dimensions.p2

uses java.lang.Class
uses java.math.BigDecimal

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionTime implements IDimension<SampleDimensionTime, SpecialNumberType >{

  var _value : SpecialNumberType

  construct(value : SpecialNumberType){
    _value = value
  }

  override function toNumber(): SpecialNumberType {
    return _value
  }

  override function fromNumber(number : SpecialNumberType): SampleDimensionTime {
    return new SampleDimensionTime(number)
  }

  override function numberType(): Class< SpecialNumberType > {
    return SpecialNumberType
  }

  override function compareTo(o: SampleDimensionTime): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function multiply(o : SampleDimensionRate) : SampleDimensionLength{
      return new SampleDimensionLength(this.toNumber().multiply(o.toNumber()))
  }

  function add(o : SampleDimensionTime) : SampleDimensionTime{
    return new SampleDimensionTime(this.toNumber().add(o.toNumber()))
  }

  function subtract(o : SampleDimensionTime) : SampleDimensionTime{
    return new SampleDimensionTime(this.toNumber().subtract(o.toNumber()))
  }
}