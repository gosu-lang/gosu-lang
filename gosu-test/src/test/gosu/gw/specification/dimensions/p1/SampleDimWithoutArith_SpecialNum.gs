package gw.specification.dimensions.p1

/**
 * Created by sliu on 2/18/2015.
 */
final class SampleDimWithoutArith_SpecialNum extends baseSampleDim <SpecialNum>{
  construct(o: SpecialNum) {
    super(o)
  }

  override function fromNumber(number: SpecialNum): SampleDimWithoutArith_SpecialNum {
    return new SampleDimWithoutArith_SpecialNum (number)
  }

}