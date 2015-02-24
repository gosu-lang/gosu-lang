package gw.specification.dimensions.p1
/**
 * Created by Sky on 2015/02/19 with IntelliJ IDEA.
 */
uses java.lang.Class

abstract class baseSampleDim<T extends java.lang.Number> implements IDimension< baseSampleDim <T>, T>{
  var _value : T

  construct(){}

  construct(value : T){
     _value = value
  }

  override function toNumber(): T {
    return _value
  }

  abstract override function fromNumber(number : T): baseSampleDim <T> ;

  override function numberType(): Class< T > {
    return T as Object as Class<T>
  }

  override function compareTo(o: baseSampleDim <T>): int {
      return _value.doubleValue() < o.toNumber().doubleValue()  ? -1 : _value.doubleValue() > o.toNumber().doubleValue() ? 1 : 0
  }

}