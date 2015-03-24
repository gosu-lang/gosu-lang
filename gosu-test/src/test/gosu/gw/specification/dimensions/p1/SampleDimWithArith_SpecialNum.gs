package gw.specification.dimensions.p1

uses java.math.BigDecimal

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimWithArith_SpecialNum extends baseSampleDim <SpecialNum>{

  construct(o: SpecialNum) {
    super(o)
  }

  override function fromNumber(SpecialNum: SpecialNum): SampleDimWithArith_SpecialNum {
    return null
  }

  function add(o : SampleDimWithArith_SpecialNum) : SampleDimWithArith_SpecialNum {
    return new SampleDimWithArith_SpecialNum (new SpecialNum(this.toNumber().doubleValue() + o.toNumber().doubleValue()))
  }
  
  function multiply(o : SampleDimWithArith_SpecialNum) : SampleDimWithArith_SpecialNum {
    return new SampleDimWithArith_SpecialNum (new SpecialNum(this.toNumber().doubleValue() * o.toNumber().doubleValue()))
  }
  
  function subtract(o : SampleDimWithArith_SpecialNum) : SampleDimWithArith_SpecialNum {
    return new SampleDimWithArith_SpecialNum (new SpecialNum(this.toNumber().doubleValue() - o.toNumber().doubleValue()))
  }
  
  function divide(o : SampleDimWithArith_SpecialNum) : SampleDimWithArith_SpecialNum {
    return new SampleDimWithArith_SpecialNum (new SpecialNum(this.toNumber().doubleValue() / o.toNumber().doubleValue()))
  }
  
  function modulo(o : SampleDimWithArith_SpecialNum) : SampleDimWithArith_SpecialNum {
    return new SampleDimWithArith_SpecialNum (new SpecialNum(this.toNumber().doubleValue() % o.toNumber().doubleValue()))
  }
}