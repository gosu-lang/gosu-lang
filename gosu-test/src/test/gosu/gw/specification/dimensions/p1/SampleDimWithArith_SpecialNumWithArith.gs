package gw.specification.dimensions.p1
/**
 * Created by Sky on 2015/02/24 with IntelliJ IDEA.
 */
final class SampleDimWithArith_SpecialNumWithArith extends baseSampleDim < SpecialNumWithArith >{
  construct(o: SpecialNumWithArith) {
    super(o)
  }

  override function fromNumber(number: SpecialNumWithArith): baseSampleDim < SpecialNumWithArith > {
    return new SampleDimWithArith_SpecialNumWithArith (number)
  }

  function add(o : SampleDimWithArith_SpecialNumWithArith) : SampleDimWithArith_SpecialNumWithArith {
    return new SampleDimWithArith_SpecialNumWithArith (this.toNumber().add(o.toNumber()).add(new SpecialNumWithArith(20)))
  }

  function multiply(o : SampleDimWithArith_SpecialNumWithArith) : SampleDimWithArith_SpecialNumWithArith {
    return new SampleDimWithArith_SpecialNumWithArith (this.toNumber().multiply(o.toNumber()).add(new SpecialNumWithArith(20)))
  }

  function subtract(o : SampleDimWithArith_SpecialNumWithArith) : SampleDimWithArith_SpecialNumWithArith {
    return new SampleDimWithArith_SpecialNumWithArith (this.toNumber().subtract(o.toNumber()).add(new SpecialNumWithArith(20)))
  }

  function divide(o : SampleDimWithArith_SpecialNumWithArith) : SampleDimWithArith_SpecialNumWithArith {
    return new SampleDimWithArith_SpecialNumWithArith (this.toNumber().divide(o.toNumber()).add(new SpecialNumWithArith(20)))
  }

  function modulo(o : SampleDimWithArith_SpecialNumWithArith) : SampleDimWithArith_SpecialNumWithArith {
    return new SampleDimWithArith_SpecialNumWithArith (this.toNumber().modulo(o.toNumber()).add(new SpecialNumWithArith(20)))
  }
}