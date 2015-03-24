package gw.specification.dimensions.p2

uses java.lang.Class

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimensionLength implements IDimension<SampleDimensionLength, SpecialNumberType > {

  var _value : SpecialNumberType

  construct(value : SpecialNumberType){
    _value = value
  }

  override function toNumber(): SpecialNumberType {
    return _value
  }

  override function fromNumber(number: SpecialNumberType): SampleDimensionLength {
    return new SampleDimensionLength(number)
  }

  override function numberType(): Class< SpecialNumberType > {
    return SpecialNumberType
  }

  override function compareTo(o: SampleDimensionLength): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

  function add(o : SampleDimensionLength) : SampleDimensionLength{
    return new SampleDimensionLength(this.toNumber().add(o.toNumber()))
  }

  function subtract(o : SampleDimensionLength) : SampleDimensionLength{
    return new SampleDimensionLength(this.toNumber().subtract(o.toNumber()))
  }

}