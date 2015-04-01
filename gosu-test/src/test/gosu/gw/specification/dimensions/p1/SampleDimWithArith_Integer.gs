package gw.specification.dimensions.p1

uses java.lang.Integer

/**
 * Created by Sky on 2015/02/20 with IntelliJ IDEA.
 */
final class SampleDimWithArith_Integer extends baseSampleDim <Integer> {
  construct(o: Integer) {
    super(o)
  }

  override function fromNumber(number: Integer): baseSampleDim <Integer> {
    return new SampleDimWithArith_Integer (number)
  }

  function add(o: SampleDimWithArith_Integer) : SampleDimWithArith_Integer {
    return new SampleDimWithArith_Integer (o.toNumber() + this.toNumber() + 20)
  }

  function multiply(o: SampleDimWithArith_Integer) : SampleDimWithArith_Integer {
    return new SampleDimWithArith_Integer ((o.toNumber() * this.toNumber()) + 20)
  }

  function subtract(o: SampleDimWithArith_Integer) : SampleDimWithArith_Integer {
    return new SampleDimWithArith_Integer (this.toNumber() - o.toNumber() + 20)
  }

  function divide(o: SampleDimWithArith_Integer) : SampleDimWithArith_Integer {
    return new SampleDimWithArith_Integer ((this.toNumber() / o.toNumber()) + 20)
  }

  function modulo(o: SampleDimWithArith_Integer) : SampleDimWithArith_Integer {
    return new SampleDimWithArith_Integer ((this.toNumber() % o.toNumber()) + 20)
  }
}