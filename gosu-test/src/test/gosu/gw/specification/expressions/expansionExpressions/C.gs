package gw.specification.expressions.expansionExpressions

uses java.lang.Integer
uses java.util.Map
uses java.util.LinkedList
uses java.lang.Double

class C {
  public var arr : int[] = {1, 2, 3}
  public var lst : LinkedList<Integer> = {1, 2, 3}
  public var _propInt : int as PropInt
  public var _propDoubleArray : Double[] as PropDoubleArray  //Class type

  property get num() : int { return 8 }

}