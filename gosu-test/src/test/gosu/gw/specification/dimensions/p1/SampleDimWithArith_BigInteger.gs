package gw.specification.dimensions.p1

uses java.math.BigInteger

/**
 * Created by Sky on 2015/02/20 with IntelliJ IDEA.
 */
final class SampleDimWithArith_BigInteger extends baseSampleDim <BigInteger>{
  construct(o: BigInteger) {
    super(o)
  }

  override function fromNumber(number: BigInteger): baseSampleDim <BigInteger> {
    return null
  }

  function add(o: SampleDimWithArith_BigInteger) : SampleDimWithArith_BigInteger {
    return new SampleDimWithArith_BigInteger (o.toNumber() + this.toNumber() + 20)
  }

  function multiply(o: SampleDimWithArith_BigInteger) : SampleDimWithArith_BigInteger {
    return new SampleDimWithArith_BigInteger ((o.toNumber() * this.toNumber()) + 20)
  }

  function subtract(o: SampleDimWithArith_BigInteger) : SampleDimWithArith_BigInteger {
    return new SampleDimWithArith_BigInteger ((this.toNumber() - o.toNumber()) + 20)
  }

  function divide(o: SampleDimWithArith_BigInteger) : SampleDimWithArith_BigInteger {
    return new SampleDimWithArith_BigInteger ((this.toNumber() / o.toNumber()) + 20)
  }

  function modulo(o: SampleDimWithArith_BigInteger) : SampleDimWithArith_BigInteger {
    return new SampleDimWithArith_BigInteger ((this.toNumber() % o.toNumber()) + 20)
  }
}