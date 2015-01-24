package gw.specContrib.classes.method_Scoring.collections_And_Generics

uses java.io.Serializable
uses java.lang.Integer
uses java.util.*

class Errant_Generics_MethodScoring_1 {
  class A{}
  class B{}

  class P{}
  interface Q extends Serializable {}
  abstract class R extends java.lang.Number {}

  var pp : P
  var qq : Q
  var rr : R
  var aList: ArrayList
  var aListInt : ArrayList<Integer>
  var intArray : int[]

  function foo<T>(a: T) : A {return null}
  function foo<T extends Serializable>(a: T) : B {return null}

  function caller() {

    //IDE-1509
    var a1111 : A =  foo('c')  //## issuekeys: MSG_TYPE_MISMATCH
    var a1112 : A =  foo(1b)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1113 : A =  foo(1s)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1114 : A =  foo(42)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1115 : A =  foo(42.5f)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1116 : A =  foo(100L)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1117 : A =  foo(42.5)  //## issuekeys: MSG_TYPE_MISMATCH
    var a1118 : A =  foo(pp)
    var a1119 : A =  foo(qq)            //## issuekeys: MSG_TYPE_MISMATCH
    var a1120 : A =  foo(rr)            //## issuekeys: MSG_TYPE_MISMATCH
    var a1121 : A =  foo(aList)            //## issuekeys: MSG_TYPE_MISMATCH
    var a1122 : A =  foo(aListInt)            //## issuekeys: MSG_TYPE_MISMATCH
    var a1123 : A =  foo(intArray)
    var a1124 : A =  foo({1,2,3})            //## issuekeys: MSG_TYPE_MISMATCH
    var a1125 : A =  foo({qq, qq, qq})            //## issuekeys: MSG_TYPE_MISMATCH

    //IDE-1509
    var b1111 : B =  foo('c')
    var b1112 : B =  foo(1b)
    var b1113 : B =  foo(1s)
    var b1114 : B =  foo(42)
    var b1115 : B =  foo(42.5f)
    var b1116 : B =  foo(100L)
    var b1117 : B =  foo(42.5)
    var b1118 : B =  foo(pp)            //## issuekeys: MSG_TYPE_MISMATCH
    var b1119 : B =  foo(qq)
    var b1120 : B =  foo(rr)
    var b1121 : B =  foo(aList)
    var b1122 : B =  foo(aListInt)
    var b1123 : B =  foo(intArray)            //## issuekeys: MSG_TYPE_MISMATCH
    var b1124 : B =  foo({1,2,3})
    var b1125 : B =  foo({qq, qq, qq})

    //IDE-1509
    var c1111 =  foo('c')
    var c1112 =  foo(1b)
    var c1113 =  foo(1s)
    var c1114 =  foo(42)
    var c1115 =  foo(42.5f)
    var c1116 =  foo(100L)
    var c1117 =  foo(42.5)
    var c1118 =  foo(pp)
    var c1119 =  foo(qq)
    var c1120 =  foo(rr)
    var c1121 =  foo(aList)
    var c1122 =  foo(aListInt)
    var c1123 =  foo(intArray)
    var c1124 =  foo({1,2,3})
    var c1125 =  foo({qq, qq, qq})
  }
}