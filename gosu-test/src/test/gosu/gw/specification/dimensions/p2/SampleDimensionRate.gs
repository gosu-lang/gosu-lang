package gw.specification.dimensions.p2

uses java.lang.Class

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionRate implements IDimension<SampleDimensionRate, SpecialNumberType >{

  var _value : SpecialNumberType

  construct(value : SpecialNumberType){
    _value = value
  }

  override function toNumber(): SpecialNumberType {
    return _value
  }

  override function fromNumber(number: SpecialNumberType): SampleDimensionRate {
    return new SampleDimensionRate(number)
  }

  override function numberType(): Class< SpecialNumberType > {
    return SpecialNumberType
  }

  override function compareTo(o: SampleDimensionRate): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function multiply(o : SampleDimensionTime) : SampleDimensionLength{
    return new SampleDimensionLength(this.toNumber().multiply(o.toNumber()))
  }

  function add(o : SampleDimensionRate) : SampleDimensionRate{
    return new SampleDimensionRate(this.toNumber().add(o.toNumber()))
  }

  function subtract(o : SampleDimensionRate) : SampleDimensionRate{
    return new SampleDimensionRate(this.toNumber().subtract(o.toNumber()))
  }
}