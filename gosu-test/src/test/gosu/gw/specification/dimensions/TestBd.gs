package gw.specification.dimensions

uses java.math.BigDecimal

public final class TestBd implements IDimension<TestBd, BigDecimal> {
  private var _number : BigDecimal
  public construct(number : BigDecimal) {
    _number = number
  }
  override public function toNumber() : BigDecimal {
    return _number
  }
  override public function fromNumber(units : BigDecimal) : TestBd {
    return new TestBd(units)
  }
  override public function numberType() : Class<BigDecimal> {
    return BigDecimal
  }
  override public function compareTo(o : TestBd) : int {
    return _number.compareTo(o._number)
  }
}
