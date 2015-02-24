package gw.specification.dimensions.p0

uses java.lang.Class

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimension_SpecialNumberWithArith implements IDimension< SampleDimension_SpecialNumberWithArith, SpecialNumberWithArith >{

  var _value : SpecialNumberWithArith

  construct(value : SpecialNumberWithArith){
    _value = value
  }

  override function toNumber(): SpecialNumberWithArith {
    return _value
  }

  override function fromNumber(specialNumber : SpecialNumberWithArith): SampleDimension_SpecialNumberWithArith {
    return new SampleDimension_SpecialNumberWithArith (specialNumber)
  }

  override function numberType(): Class< SpecialNumberWithArith > {
    return SpecialNumberWithArith
  }

  override function compareTo(o: SampleDimension_SpecialNumberWithArith): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

}