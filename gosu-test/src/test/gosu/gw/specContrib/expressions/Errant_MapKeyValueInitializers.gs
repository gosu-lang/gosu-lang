package gw.specContrib.expressions

uses java.util.*
uses java.lang.*

class Errant_MapKeyValueInitializers {

  function foo3(map: HashMap<Integer, String>) {
  }

  function foo4(map: HashMap<Integer, String>) {
  }

  function caller() {
    foo3({  //## issuekeys: MSG_TYPE_MISMATCH
        "bar"->
        "foo"
    })

    foo3({  //## issuekeys: MSG_TYPE_MISMATCH
        1->
        2,
        1->
        "2",
        "2"->
        "1"
    })

    foo4({  //## issuekeys: MSG_TYPE_MISMATCH
        {1, 2, 3}->
        "foo"
    })

    foo4({  //## issuekeys: MSG_TYPE_MISMATCH
        {1, 2, 3}->
        {1, 2, 3}
    })
  }

}