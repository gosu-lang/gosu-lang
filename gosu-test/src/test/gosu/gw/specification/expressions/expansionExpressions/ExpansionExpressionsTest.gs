package gw.specification.expressions.expansionExpressions

uses gw.BaseVerifyErrantTest
uses java.lang.Iterable
uses java.util.LinkedList
uses java.util.Iterator
uses java.util.Arrays
uses java.lang.Integer

class ExpansionExpressionsTest extends BaseVerifyErrantTest {
  function testErrant_ExpansionExpressionsTest() {
    processErrantType(Errant_ExpansionExpressionsTest)
  }

  function testbasicExpansionNonStaticMethod() {
    var f0 : String[] = {"a", "b", "c"}
    var ret : String[]  = f0*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)

    var f1 : String[][] = { {"a", "b", "c"},{"d"}}
    ret = f1*.concat({"h"})*.toUpperCase()
    assertArrayEquals({"A", "B", "C", "H", "D", "H"}, ret)
  }

  function testExpansionArrayIteratorIterable() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : String[] = f4*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)

    var f5 : Iterable<Object> =  {"a", "b", "c"}
    var f6: Iterable =  {"a", "b", "c"}
    ret = f6*.toString()
    assertArrayEquals({"a", "b", "c"}, ret)

    var f7: Iterator<String>  = {"a", "b", "c"} .listIterator()
    ret = f7*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)
  }

  function testExpansionDynamic() {
    var f0 : dynamic.Dynamic = {"a", "b", "c"}
    var ret : dynamic.Dynamic[] = f0*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)
  }

  function testExpansionInterval() {
    var f1 = 1..3
  }

  function testExpansionNullSafety() {
    var f0 : String[] = null
    var ret : String[] = f0*.toUpperCase()
    assertArrayEquals(ret, {})
    f0 = {"a", null, "c"}
    ret =  f0*.toUpperCase()
    assertArrayEquals({"A", null, "C"}, ret)

  }

  function testExpansionReturnTypes() {
    var x0 : A[] = {new A(), new A()}
    A.i = 0
    x0*.noReturn()
    assertEquals(2, A.i)

    var x1 : List<A>  = {new A(), new A()}
    A.i = 0
    var ints : int[] = x1*.getNum()
    assertEquals(2, A.i)


    var x2 : A[][]  = {{new A()}, {new A()}}
    ints = x2*.length
    assertTrue(Arrays.equals({1, 1}, ints))


    var x3 : C[]  = {new C()}
    var f : int[] = x3*.arr
    assertTrue(Arrays.equals({1, 2, 3}, f))

    var x4 : C[]  = {new C()}
    var f1 : LinkedList<Integer>[] = x4*.lst
    assertTrue(Arrays.equals({{1, 2, 3}}, f1))

    var x5 : C[]  = {new C(), new C()}
    var f2 =  x5*.num
    assertTrue(Arrays.equals({8, 8}, f2))
  }

}