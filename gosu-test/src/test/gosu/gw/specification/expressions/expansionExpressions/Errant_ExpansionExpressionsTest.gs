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
    ret = f1*.concat({"h"})*.toUpperCase()
  }

  function testExpansionArrayIteratorIterable() {
    var f4 : Iterable<String> =  {"a", "b", "c"}
    var ret : String[] = f4*.toUpperCase()

    var f5 : Iterable<Object> =  {"a", "b", "c"}
    ret = f5*.toUpperCase()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    var f6: Iterable =  {"a", "b", "c"}
    ret = f6*.toString()

    var f7: Iterator<String>  = {"a", "b", "c"} .listIterator()
    ret = f7*.toUpperCase()
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

    var x2 : A[][]  = {{new A()}, {new A()}}
    ints = x2*.length

    var x3 : C[]  = {new C()}
    var f : int[] = x3*.arr

    var x4 : C[]  = {new C()}
    var f1 : LinkedList<Integer>[] = x4*.lst    //## issuekeys: MSG_PARAMETERIZED_ARRAY_COMPONENT

    var x5 : C[]  = {new C(), new C()}
    var f2 =  x5*.num
  }

}