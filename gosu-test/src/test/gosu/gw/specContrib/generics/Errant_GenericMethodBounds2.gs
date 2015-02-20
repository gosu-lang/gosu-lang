package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Float
uses java.lang.Integer
uses java.lang.Short
uses java.util.ArrayList
uses java.util.HashMap

class Errant_GenericMethodBounds2 {
  class BoundedParamsTest {

    //IDE-572 - Parser fine. Problem in Compiler
    function upperBoundJavaLangNumber<T extends java.lang.Number>(t: T, t2: T) : T { return null }

    function callerUpperBoundJavaaLangNumber() {
      var ubjln111 = upperBoundJavaLangNumber(42.5, 1000)
      var ubjln112 = upperBoundJavaLangNumber(4000, 5000)
      var ubjln113 = upperBoundJavaLangNumber(4000.5, 5000)
      var ubjln114 = upperBoundJavaLangNumber(4000, 5000.5)
      var ubjln115 = upperBoundJavaLangNumber(1 as Short, 1 as Float)
    }

    function upperBoundNumber<T extends Number>(t: T, t2: T) : T { return null }

    function callerUpperBoundNumber() {
      var ubn111 = upperBoundNumber(42.5, 1000)
      var ubn112 = upperBoundNumber(42, 1000)
      var ubn114 = upperBoundNumber(1000, 42.5)
      var ubn115 = upperBoundNumber(1 as Short, 1 as Float)
    }

    //IDE-572 - Parser fine. Problem in Compiler
    function upperBoundSerializable<T extends Serializable>(t12: T, t22: T) : T  { return null }

    function callerUpperBoundSerializable() {
      var hash222 = new HashMap<Integer, Integer>();
      var list222 = new ArrayList<Integer>();

      var ubs111 = upperBoundSerializable({1->2}, {1, 2, 3})
      var ubs112 = upperBoundSerializable(hash222, list222);
    }
  }

  class UnboundedParamsTest {

    //IDE-538 - Parser fine. Problem in Compiler
    function testUnboundedParams<T>(t1: T, t2: T) : T { return null}
    function callerTestUnboundedParams() {
      var ub111 = testUnboundedParams(1.5f, "MyString")

      var myStringArray: String[] = {"foo", "bar"}
      var ub112 = testUnboundedParams(new ArrayList<Integer>(), myStringArray)
    }

    //IDE-538 - Parser fine. Problem in Compiler
    function testUnboundedParams2<T>(tarray: T[], t: T) : T { return null }
    function caller() {
      var ub211 = testUnboundedParams2({1, 2, 3}, 1)
      var ub212 = testUnboundedParams2({1, 2, 3}, "hello")
    }

  }

  class JavaMethodCalls<A> {
    function testJavaMethodWithTypeVarHavingSameNameAsEnclosingClass( p: A ) {
      JavaClass<A>.foo(p)   //## issuekeys: MSG_TYPE_MISMATCH
    }
  }

}
