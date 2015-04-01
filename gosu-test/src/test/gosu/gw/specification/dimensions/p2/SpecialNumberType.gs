package gw.specification.dimensions.p2
/**
 * Created by Sky on 2015/02/23 with IntelliJ IDEA.
 */
uses java.lang.Number
uses java.math.BigDecimal
uses java.lang.Comparable

  class SpecialNumberType extends Number implements Comparable< SpecialNumberType >{

  var _value : BigDecimal

  construct(value : BigDecimal){
    _value = value
  }

  override function intValue(): int {
    return _value.intValue()
  }

  override function longValue(): long {
    return _value.longValue()
  }

  override function floatValue(): float {
    return _value.floatValue()
  }

  override function doubleValue(): double {
    return _value.doubleValue()
  }

  override function toString() : String{
    return (_value.toString() + " SN")
  }

  override function compareTo(o: SpecialNumberType): int {
    return _value.doubleValue() < o.doubleValue()  ? -1 : _value.doubleValue() > o.doubleValue() ? 1 : 0
  }

  function multiply(o: SpecialNumberType): SpecialNumberType {
    return new SpecialNumberType (o.doubleValue() * this.doubleValue())
  }

  function add(o : SpecialNumberType) : SpecialNumberType {
    return new SpecialNumberType (o.doubleValue() + this.doubleValue())
  }

  function subtract(o : SpecialNumberType) : SpecialNumberType {
    return new SpecialNumberType (this.doubleValue() - o.doubleValue())
  }
}