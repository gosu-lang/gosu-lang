package gw.specification.dimensions.p0

uses java.lang.Number
uses java.math.BigDecimal
uses java.lang.Comparable

class SpecialNumberWithArith extends Number implements Comparable< SpecialNumberWithArith >{

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
    return (_value.toString() + " SSN")
  }

  override function compareTo(o: SpecialNumberWithArith): int {
    return _value.doubleValue() < o.doubleValue()  ? -1 : _value.doubleValue() > o.doubleValue() ? 1 : 0
  }

  function add(o : SpecialNumberWithArith) : SpecialNumberWithArith {
    return new SpecialNumberWithArith (o.doubleValue() + this.doubleValue())
  }

  function multiply(o : SpecialNumberWithArith) : SpecialNumberWithArith {
    return new SpecialNumberWithArith (o.doubleValue() * this.doubleValue())
  }

  function modulo(o : SpecialNumberWithArith) : SpecialNumberWithArith {
    return new SpecialNumberWithArith (this.doubleValue() % o.doubleValue())
  }

  function subtract(o : SpecialNumberWithArith) : SpecialNumberWithArith {
    return new SpecialNumberWithArith (this.doubleValue() - o.doubleValue())
  }

  function divide(o : SpecialNumberWithArith) : SpecialNumberWithArith {
    return new SpecialNumberWithArith (this.doubleValue() / o.doubleValue())
  }
}