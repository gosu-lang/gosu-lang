package gw.specContrib.expressions

uses java.util.*
uses java.lang.*

class Errant_MapKeyValueInitializers {

  function foo3(map: HashMap<Integer, String>) {
  }

  function foo4(map: HashMap<Integer, String>) {
  }

  function caller() {
    foo3({
        "bar"->                 //## issuekeys: MSG_TYPE_MISMATCH
            "foo"
    })

    foo3({
        1->
            2,                  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
        1->
            "2",
        "2"->                   //## issuekeys: MSG_TYPE_MISMATCH
            "1"
    })

    foo4({
        {1, 2, 3}->             //## issuekeys: MSG_TYPE_MISMATCH
            "foo"
    })

    foo4({
        {1, 2, 3}->             //## issuekeys: MSG_TYPE_MISMATCH
        {1, 2, 3}               //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    })
  }

}