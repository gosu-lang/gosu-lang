package gw.internal.gosu.parser.generics

uses java.util.ArrayList
uses java.lang.Integer
uses java.util.AbstractList

class Errant_AssignabilityAndComparability<T extends AbstractList> {

  function testMe( t : T ) {
    t = new ArrayList()  //## issuekeys: MSG_TYPE_MISMATCH
    var x : T
    t = x

    var test = t == new ArrayList()
    test = t == new Integer( 2 )  //## issuekeys: MSG_TYPE_MISMATCH
  }
}