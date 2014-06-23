package gw.lang.parser

uses gw.testharness.DoNotVerifyResource
uses java.lang.CharSequence


@DoNotVerifyResource
abstract class Errant_AbstractInstantiation {

  function func1() {
    var x = new Errant_AbstractInstantiation()
  }

  abstract class Inner{}

  function func2() {
    var x = new Inner()
  }

}