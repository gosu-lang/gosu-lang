package gw.internal.gosu.parser.expressions

uses gw.testharness.DoNotVerifyResource
uses java.util.ArrayList
uses java.util.HashMap
uses java.lang.CharSequence

@DoNotVerifyResource
class Errant_TypeIsTest {
  function f() {
    var x: List

    if( x typeis ArrayList ) {}

    if( x typeis HashMap ) {}

    if( x typeis CharSequence ) {}

    // String is final, there it never implements List
    if( x typeis String ) {}  //## issuekeys: MSG_INCONVERTIBLE_TYPES

    var h: ArrayList
    // Neither HashMap nor ArrayList is an interface and they are not in either's hierarchy
    if( h typeis HashMap ) {}    //## issuekeys: MSG_INCONVERTIBLE_TYPES
  }
}
