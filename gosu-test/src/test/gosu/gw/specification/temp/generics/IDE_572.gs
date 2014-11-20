package gw.specification.temp.generics
uses java.math.BigDecimal
uses java.util.List
uses java.lang.CharSequence
uses java.util.Set
uses java.util.HashSet
uses java.util.ArrayList
uses java.lang.StringBuilder
uses java.math.BigInteger
uses java.lang.Integer
uses java.lang.Float
uses java.util.HashMap

uses java.io.Serializable
uses java.lang.Number

class IDE_572 {
  function foo<T extends Number>(t1 : T, t2 : T) : T{ return t1 }
  function caller() {
    foo(1.5, 1.5)
    foo(1.5, 1.5f)
    foo(1, 1.5)
    foo(1s, 1.5)
    foo(1, 1s)
    foo(1b, 1s)
  }

  function bar<T extends Serializable>(t1 : T, t2 : T): T  { return t1 }
  function callers() {
    var m = {1 -> 2};
    var l = {1, 2, 3}
    bar(m, l)
    bar({1 -> 2}, {1, 2, 3})
  }
}