package gw.specification.dimensions.p1

uses java.lang.Number
uses java.math.BigDecimal

class SpecialNumWithArith extends Number{

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

  function add(o : SpecialNumWithArith) : SpecialNumWithArith {
    return new SpecialNumWithArith (o.doubleValue() + this.doubleValue())
  }

  function multiply(o : SpecialNumWithArith) : SpecialNumWithArith {
    return new SpecialNumWithArith (o.doubleValue() * this.doubleValue())
  }

  function subtract(o : SpecialNumWithArith) : SpecialNumWithArith {
    return new SpecialNumWithArith (this.doubleValue() - o.doubleValue())
  }

  function divide(o : SpecialNumWithArith) : SpecialNumWithArith {
    return new SpecialNumWithArith (this.doubleValue() / o.doubleValue())
  }

  function modulo(o : SpecialNumWithArith) : SpecialNumWithArith {
    return new SpecialNumWithArith (this.doubleValue() % o.doubleValue())
  }



}