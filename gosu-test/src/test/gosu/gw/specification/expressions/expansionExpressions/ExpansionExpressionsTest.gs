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

    var x6 : C[]  = {new C(){ :PropInt = 3}, new C(){ :PropInt = 5}}
    var f6 = x6*.PropInt
    assertTrue(Arrays.equals({3, 5}, f6))
    var f61 = x6*._propInt
    assertTrue(Arrays.equals({3, 5}, f61))

    var x7 : C[]  = {new C(){ :PropDoubleArray = {3.1, 5.3}}, new C(){ :PropDoubleArray = {1.2, 5.6, 7.9}}}
    var f7 = x7*.PropDoubleArray
    assertTrue(Arrays.equals({3.1, 5.3, 1.2, 5.6, 7.9}, f7))
    var f71 = x7*._propDoubleArray
    assertTrue(Arrays.equals({3.1, 5.3, 1.2, 5.6, 7.9}, f71))
    var f72 = x7*.PropDoubleArray*.toString()
    assertTrue(Arrays.equals({"3.1", "5.3", "1.2", "5.6", "7.9"}, f72))
  }

  function testExpansionOn2DArray(){
    var int2DArray : int[][] = {{1, 2, 3}, {4, 5, 6}}
    var ret = int2DArray*.length                       // test against property
    assertTrue(Arrays.equals({3,3}, ret))

    ret = int2DArray*.sum()                            // test against method
    assertTrue(Arrays.equals({6,15}, ret))

    var str2DArray : String[][] = {{"test1", "test2"}, {"test11", "test12"}}
    ret = str2DArray*.join("-")*.length()
    assertTrue(Arrays.equals({11,13}, ret))

    var retFromStrArray = str2DArray*.join("-")
    assertTrue(Arrays.equals({"test1-test2","test11-test12"}, retFromStrArray))
  }

  function testExpansionOn3DArray(){
    var str3DArray : String[][][] =  {{{"a","b"}, {"c","d"}},
                                      {{"e"},{"g"},{"i"}},
                                      {{"j","k","m"}}}
    var ret = str3DArray*.length
    assertTrue(Arrays.equals({2, 3, 1}, ret))

    var retFrom3DArray1 = str3DArray*.concat({{"1"}})*.length
    assertTrue(Arrays.equals({2,2,1,1,1,1,1,3,1},retFrom3DArray1))

    var retFrom3DArray2 = str3DArray*.concat({{"1"}})*.concat({"2"})*.toUpperCase()
    assertTrue(Arrays.equals({"A","B","2","C","D","2","1","2",
                              "E","2","G","2","I","2","1","2",
                              "J","K","M","2","1","2"},retFrom3DArray2))
  }
}