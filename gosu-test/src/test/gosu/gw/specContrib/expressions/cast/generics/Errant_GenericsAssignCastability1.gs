package gw.specContrib.expressions.cast.generics

uses java.lang.*
uses java.io.Serializable

class Errant_GenericsAssignCastability1 {
  class A{}
  class B extends A {}

  class Box<T> {}

  var boxA: Box<A>
  var boxB: Box<B>

  function testAssignability() {
    var a111: Box<A> = boxB
    //IDE-1719 - Parser issue - Should show error
    var b111: Box<B> = boxA      //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testCastability() {
    var x111 = boxA as Box<B>
    var x112 = boxB as Box<A>  //## issuekeys: MSG_UNNECESSARY_COERCION
  }

  function testAssignCastabilityInBuiltTypes() {
    var box1 : Box<java.lang.Number>
    var box2 : Box<Integer>
    var box3 : Box<Serializable>

    //Assignability
    var x111 : Box<java.lang.Number> = box2
    var x112 : Box<Integer> = box1      //## issuekeys: MSG_TYPE_MISMATCH

    var y111 : Box<Serializable> = box2
    var y112 : Box<Integer> = box3      //## issuekeys: MSG_TYPE_MISMATCH

    var z111 : Box<Serializable> = box1
    var z112 : Box<java.lang.Number> = box3      //## issuekeys: MSG_TYPE_MISMATCH

    //Castability
    var p111 = box1 as Box<Integer>
    var p112 = box1 as Box<Serializable>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var q111 = box2 as Box<java.lang.Number>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var q112 = box2 as Box<Serializable>  //## issuekeys: MSG_UNNECESSARY_COERCION
    var r111 = box3 as Box<java.lang.Number>
    var r112 = box3 as Box<Integer>
  }
}