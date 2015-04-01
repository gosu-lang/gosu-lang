package gw.specification.dimensions.p1
/**
 * Created by Sky on 2015/02/24 with IntelliJ IDEA.
 */
final class SampleDimWithoutArith_SpecialNumWithArith extends baseSampleDim < SpecialNumWithArith >{
  construct(o: SpecialNumWithArith) {
    super(o)
  }

  override function fromNumber(number: SpecialNumWithArith): baseSampleDim < SpecialNumWithArith > {
    return new SampleDimWithoutArith_SpecialNumWithArith (number)
  }
}