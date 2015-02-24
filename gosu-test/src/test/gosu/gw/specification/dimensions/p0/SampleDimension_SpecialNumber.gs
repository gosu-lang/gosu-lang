package gw.specification.dimensions.p0

uses java.lang.Class
uses java.math.BigDecimal
uses gw.specification.dimensions.p2.SampleDimensionTime

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimension_SpecialNumber implements IDimension< SampleDimension_SpecialNumber, SpecialNumber >{

  var _value : SpecialNumber

  construct(value : SpecialNumber){
    _value = value
  }

  override function toNumber(): SpecialNumber {
    return _value
  }

  override function fromNumber(specialNumber : SpecialNumber): SampleDimension_SpecialNumber {
    return new SampleDimension_SpecialNumber (specialNumber)
  }

  override function numberType(): Class< SpecialNumber > {
    return SpecialNumber
  }

  override function compareTo(o: SampleDimension_SpecialNumber): int {
    return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

}