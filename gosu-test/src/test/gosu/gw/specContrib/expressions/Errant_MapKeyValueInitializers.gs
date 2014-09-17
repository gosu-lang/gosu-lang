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
        "bar"->                 //## issuekeys: MSG_
            "foo"
    })

    foo3({
        1->
            2,                  //## issuekeys: MSG_
        1->
            "2",
        "2"->                   //## issuekeys: MSG_
            "1"
    })

    foo4({
        {1, 2, 3}->             //## issuekeys: MSG_
            "foo"
    })

    foo4({
        {1, 2, 3}->             //## issuekeys: MSG_
        {1, 2, 3}               //## issuekeys: MSG_
    })
  }

}