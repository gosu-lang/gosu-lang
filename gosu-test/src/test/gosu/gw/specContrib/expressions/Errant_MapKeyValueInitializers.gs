package gw.specContrib.expressions

uses java.util.*
uses java.lang.*

class Errant_MapKeyValueInitializers {

  function foo3(map: HashMap<Integer, String>) {
  }

  function foo4(map: HashMap<Integer, String>) {
  }

  function caller() {
    foo3({ "bar"-> "foo"} )  //## issuekeys: MSG_TYPE_MISMATCH

    foo3({ 1 -> 2, 1 -> "2", "2" -> "1" })  //## issuekeys: MSG_TYPE_MISMATCH

    foo4({ {1, 2, 3} -> "foo" })  //## issuekeys: MSG_TYPE_MISMATCH

    foo4({ {1, 2, 3} -> {1, 2, 3} })  //## issuekeys: MSG_TYPE_MISMATCH
  }

}