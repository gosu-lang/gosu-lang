package gw.specification.dimensions.p1

uses java.math.BigInteger

/**
 * Created by Sky on 2015/02/20 with IntelliJ IDEA.
 */
final class SampleDimWithoutArith_BigInteger extends baseSampleDim <BigInteger>{
  construct (o : BigInteger){
    super(o)
  }

  override function fromNumber(number: BigInteger): baseSampleDim <BigInteger> {
    return new SampleDimWithoutArith_BigInteger (number)
  }
}