package gw.specification.expressions.expansionExpressions

uses java.lang.Iterable
uses java.util.LinkedList
uses java.util.Iterator
uses java.lang.Integer

class Errant_ExpansionExpressionsTest {
 function testbasicExpansionNonStaticMethod() {
    var f0 : String[] = {"a", "b", "c"}
    var ret : String[]  = f0*.toUpperCase()

    var f1 : String[][] = { {"a", "b", "c"},{"d"}}
    ret = f1*.concat("h")*.toUpperCase()

    var f2 : String[][][] = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f2*.concat("h")*.toUpperCase()
  }

  function testbasicExpansionProperty() {
    var f0 : String[] = {"a", "b", "c"}
    var ret : boolean[]  = f0*.Alpha

    var f1 : String[][][] = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f1*.Alpha
  }

  function testExpansionArrayIteratorIterableProperty() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : boolean[]  = f4*.Alpha

    var f8 : Iterable<Iterable<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f8*.Alpha


    var f7: Iterator<String>  = {"a", "b", "c"}.iterator()
    ret = f7*.Alpha

    var f9 : Iterator<Iterator<Iterator<String>>> = { {{"a"}.iterator(),{ "b", "c"}.iterator()}.iterator(),{{"d"}.iterator()}.iterator()}.iterator()
    ret = f9*.Alpha
  }


  function testExpansionArrayIteratorIterable() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : String[] = f4*.toUpperCase()

    var f8 : Iterable<Iterable<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f8*.toUpperCase()

    var f5 : Iterable<Object> =  {"a", "b", "c"}
    ret = f5*.toString()

    var f7: Iterator<String>  = {"a", "b", "c"}.iterator()
    ret = f7*.toUpperCase()

    var f9 : Iterator<Iterator<Iterator<String>>> = { {{"a"}.iterator(),{ "b", "c"}.iterator()}.iterator(),{{"d"}.iterator()}.iterator()}.iterator()
    ret = f9*.toUpperCase()

    var f10 : Iterable<List<Iterable<String>>> = { {{"a"},{ "b", "c"}},{{"d"}}}
    ret = f10*.toUpperCase()
  }

  function testExpansionDynamic() {
    var f0 : dynamic.Dynamic = {"a", "b", "c"}
    var ret : dynamic.Dynamic[] = f0*.toUpperCase()
  }

  function testExpansionInterval() {
    var f1 = 1..3
    var ret = f1*.toString()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
  }

  function testExpansionNullSafety() {
    var f0 : String[] = null
    var ret : String[] = f0*.toUpperCase()
    f0 = {"a", null, "c"}
    ret =  f0*.toUpperCase()

  }

  function testExpansionReturnTypes() {
    var x0 : A[] = {new A(), new A()}
    A.i = 0
    var nil = x0*.noReturn()  //## issuekeys: MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE, MSG_VOID_EXPRESSION_NOT_ALLOWED
    x0*.noReturn()

    var x1 : List<A>  = {new A(), new A()}
    A.i = 0
    var ints : int[] = x1*.getNum()

    A.i = 0
    var x2 : A[][][]  = {{{new A()}}, {{new A()}}}
    ints = x2*.getNum()


    var x3 : C[]  = {new C()}
    var f3 : int[] = x3*.arr1

    var x4 : C[]  = {new C()}
    var f4 : int[] = x4*.retArr1()

    var x5 : C[]  = {new C()}
    var f5 : int[] = x5*.retLst1()    //## issuekeys: MSG_TYPE_MISMATCH

    var x6 : C[]  = {new C()}
    var f6 : int[][] = x6*.arr2


    var x7 : C[]  = {new C()}
    var f7 : int[][] = x7*.retArr2()

    var x8 : C[]  = {new C()}
    var f8: int[] = x8*.retLst2()   //## issuekeys: MSG_TYPE_MISMATCH

    var x9 : C[]  = {new C()}
    var f9 : int[][][] = x9*.arr3

    var x10 : C[]  = {new C()}
    var f10 : int[][][] = x10*.retArr3()

    var x11 : C[]  = {new C()}
    var f11: LinkedList<LinkedList<Integer>>[] = x11*.retLst3()  //## issuekeys: MSG_TYPE_MISMATCH, MSG_PARAMETERIZED_ARRAY_COMPONENT
  }
}