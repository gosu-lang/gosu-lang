package gw.specification.dimensions.p1

uses java.lang.Integer
/**
 * Created by Sky on 2015/02/20 with IntelliJ IDEA.
 */
final class SampleDimWithoutArith_Integer extends baseSampleDim <Integer> {
  construct(o: Integer) {
    super(o)
  }

  override function fromNumber(number: Integer): baseSampleDim <Integer> {
    return new SampleDimWithoutArith_Integer (number)
  }

}