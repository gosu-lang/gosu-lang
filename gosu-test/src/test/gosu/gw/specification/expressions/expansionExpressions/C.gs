package gw.specification.expressions.expansionExpressions

uses java.lang.Integer
uses java.util.LinkedList

class C {
  public var arr1 : int[] = {1, 2, 3}
  public var arr2 : int[][] = {{1, 2}, {3}}
  public var arr3 : int[][][] = {{{1, 2}}, {{3}}}
  public var lst1 : LinkedList<Integer> = {1, 2, 3}
  public var lst2 : LinkedList<LinkedList<Integer>> = {{1, 2}, {3}}
  public var lst3 : LinkedList<LinkedList<LinkedList<Integer>>> = {{{1, 2}}, {{3}}}


  function retArr1() : int[] {
    return arr1
  }

  function retArr2() : int[][] {

    return arr2
  }

  function retArr3() : int[][][] {
    return arr3
  }

  function retLst1() : LinkedList<Integer> {
    return lst1
  }

  function retLst2() : LinkedList<LinkedList<Integer>> {
    return lst2
  }

  function retLst3() : LinkedList<LinkedList<LinkedList<Integer>>> {
    return lst3
  }
}