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
    ret = f1*.concat("h")*.toUpperCase()
    assertArrayEquals({"AH", "BH", "CH", "DH"}, ret)

    var f2 : String[][][] = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f2*.concat("h")*.toUpperCase()
    assertArrayEquals({"AH", "BH", "CH", "DH"}, ret)
  }

  function testbasicExpansionProperty() {
    var f0 : String[] = {"a", "b", "c"}
    var ret : boolean[]  = f0*.Alpha
    assertArrayEquals({true, true, true}, ret)

    var f1 : String[][][] = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f1*.Alpha
    assertArrayEquals({true, true, true, true}, ret)
  }

  function testExpansionArrayIteratorIterableProperty() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : boolean[]  = f4*.Alpha
    assertArrayEquals({true, true, true}, ret)

    var f8 : Iterable<Iterable<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f8*.Alpha
    assertArrayEquals({true, true, true, true}, ret)


    var f7: Iterator<String>  = {"a", "b", "c"}.iterator()
    ret = f7*.Alpha
    assertArrayEquals({true, true, true}, ret)

    var f9 : Iterator<Iterator<Iterator<String>>> = { {{"a"}.iterator(),{ "b", "c"}.iterator()}.iterator(),{{"d"}.iterator()}.iterator()}.iterator()
    ret = f9*.Alpha
    assertArrayEquals({true, true, true, true}, ret)
  }


  function testExpansionArrayIteratorIterable() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : String[] = f4*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)

    var f8 : Iterable<Iterable<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f8*.toUpperCase()
    assertArrayEquals({"A", "B", "C", "D"}, ret)

    var f5 : Iterable<Object> =  {"a", "b", "c"}
    ret = f5*.toString()
    assertArrayEquals({"a", "b", "c"}, ret)

    var f7: Iterator<String>  = {"a", "b", "c"}.iterator()
    ret = f7*.toUpperCase()
    assertArrayEquals({"A", "B", "C"}, ret)

    var f9 : Iterator<Iterator<Iterator<String>>> = { {{"a"}.iterator(),{ "b", "c"}.iterator()}.iterator(),{{"d"}.iterator()}.iterator()}.iterator()
    ret = f9*.toUpperCase()
    assertArrayEquals({"A", "B", "C", "D"}, ret)

    var f10 : Iterable<List<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f10*.toUpperCase()
    assertArrayEquals({"A", "B", "C", "D"}, ret)
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
    assertTrue(Arrays.equals({1, 1}, ints))
    assertEquals(2, A.i)

    var x18 : List<A>  = {new A(), new A()}
    A.i = 0
    ints = x18*.Bar
    assertTrue(Arrays.equals({2, 2}, ints))
    assertEquals(2, A.i)

    A.i = 0
    var x2 : A[][][]  = {{{new A()}}, {{new A()}}}
    ints = x2*.getNum()
    assertTrue(Arrays.equals({1, 1}, ints))
    assertEquals(2, A.i)


    var x3 : C[]  = {new C()}
    var f3 : int[] = x3*.arr1
    assertTrue(Arrays.equals({1, 2, 3}, f3))

    var x4 : C[]  = {new C()}
    var f4 : int[] = x4*.retArr1()
    assertTrue(Arrays.equals({1, 2, 3}, f4))

    var x6 : C[]  = {new C()}
    var f6 : int[][] = x6*.arr2

    assert2ArrayEquals({{1, 2}, {3}}, f6)

    var x7 : C[]  = {new C()}
    var f7 : int[][] = x7*.retArr2()
    assert2ArrayEquals({{1, 2}, {3}}, f7)

    var x9 : C[]  = {new C()}
    var f9 : int[][][] = x9*.arr3
    assert3ArrayEquals({{{1, 2}}, {{3}}}, f9)

    var x10 : C[]  = {new C()}
    var f10 : int[][][] = x10*.retArr3()
    assert3ArrayEquals({{{1, 2}}, {{3}}}, f10)
  }

  function assertArrayEquals(a : boolean[], b : boolean[])  {
    assertTrue(Arrays.equals(a, b))
  }

  function assert2ArrayEquals(a : int[][], b : int[][])  {
    var i = 0
    while(i < a.length) {
     assertTrue(Arrays.equals(a[i], b[i]))
     i++
    }
  }

  function assert3ArrayEquals(a : int[][][], b : int[][][])  {
    var i = 0
    while(i < a.length) {
     assert2ArrayEquals(a[i], b[i])
     i++
    }
  }
}