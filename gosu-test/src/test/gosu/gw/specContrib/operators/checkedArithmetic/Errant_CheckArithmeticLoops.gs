package gw.specContrib.operators.checkedArithmetic

class Errant_CheckArithmeticLoops {
  var x1 = 5
  var y1 = 45
  function testLoops() {


    //Cases for !+
    for(int1 in x1..!+y1) {            //## issuekeys: UNEXPECTED TOKEN: !+
      print(!+x1)                       //## issuekeys: ')' EXPECTED
      print(x1!+y1)
      print(!+x1!+y1)                   //## issuekeys: ')' EXPECTED
    }
    for(int1 in !+x1..!+y1) {           //## issuekeys: UNEXPECTED TOKEN: !+

    }
    for(int1 in !+x1..y1) {              //## issuekeys: UNEXPECTED TOKEN: !+

    }

    //Cases for !-
    for(int1 in x1..!-y1) {
      print(!-x1)
      print(x1!-y1)
      print(!-x1!-y1)
    }
    for(int1 in !-x1..!-y1) {

    }
    for(int1 in !-x1..y1) {

    }

    //Cases for !*
    for(int1 in x1..!*y1) {            //## issuekeys: UNEXPECTED TOKEN: !*
      print(!*x1)            //## issuekeys: ')' EXPECTED
      print(x1!*y1)
      print(!*x1!*y1)            //## issuekeys: ')' EXPECTED
    }

    for(int1 in !*x1..!*y1) {            //## issuekeys: UNEXPECTED TOKEN: !*
    }

    for(int1 in !*x1..y1) {            //## issuekeys: UNEXPECTED TOKEN: !*
    }


    for(int1 in !+x1..!-y1 iterator iterator1 ) {           //## issuekeys: UNEXPECTED TOKEN: !+
    }

    for(int1 in !-x1..!-y1 index index1) {
    }
  }


  function testIfStmt () {
    if(!-x1 > !+y1  && !+x1 < !-y1 && !-x1!*y1 > 42) {           //## issuekeys: ')' EXPECTED
      print("mystring")
    }
  }

  function testSwitch() {
    var switch1 : int
    switch(switch1) {
      case !-1:
        print(1 !- !-switch1)
        break
      case !-2:
        print(!-switch1)
        break
    }
  }
}