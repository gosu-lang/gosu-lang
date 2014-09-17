package gw.specContrib.expressions

uses java.lang.Integer
uses java.lang.System

class Errant_CollectionElementInitializer {

  function foo() {
    var a = new Integer[]{
        1,
        ""                              //## issuekeys: MSG_
    }

    var b = new List<Integer>(){
        "a",                            //## issuekeys: MSG_
        23,
        1.1                             //## issuekeys: MSG_
    }

    var xx = new A(){
        System.out.println("")          //## issuekeys: MSG_
    }
  }

  class A {
  }

}