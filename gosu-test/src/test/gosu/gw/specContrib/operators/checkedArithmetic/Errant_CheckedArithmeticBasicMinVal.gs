package gw.specContrib.operators.checkedArithmetic

class Errant_CheckedArithmeticBasicMinVal {

  function foo() {
    var x = 5
    var y = 50
    var z001 = x !+ y
    var z002 = x !- y
    var z003 = x !* y
    var z004 = x !/ y            //## issuekeys: UNEXPECTED TOKEN: /
    var z005 = x !^ y            //## issuekeys: UNEXPECTED TOKEN: ^
    var z006 = x !& y            //## issuekeys: UNEXPECTED TOKEN: &
    var z007 = x !+!- y
    var z008 = x !-!+ y            //## issuekeys: UNEXPECTED TOKEN: !+
    var z009 = x !-!* y            //## issuekeys: UNEXPECTED TOKEN: !*
    var z010 = x !+!* y            //## issuekeys: UNEXPECTED TOKEN: !*
    var z011 = x !*!- y
    var z012 = x !*!+ y             //## issuekeys: UNEXPECTED TOKEN: !+
    var z013 = x !+!+ y              //## issuekeys: UNEXPECTED TOKEN: !+
    var z014 = x !-!- y
    var z015 = x !*!* y            //## issuekeys: UNEXPECTED TOKEN: !*
    var z016 = x !+!+!+ y            //## issuekeys: UNEXPECTED TOKEN: !+
    var z017 = x !-!-!- y            //## issuekeys: UNEXPECTED TOKEN: !-
    var z018 = x !*!*!* y            //## issuekeys: UNEXPECTED TOKEN: !*

    var z019 = 5!+56!-32!*45/4^2!+(x!-y)!*2
    var z020 = x !+(!+y)                //## issuekeys: UNEXPECTED TOKEN: !+
    var z021 = x !+(x!-y)!*6

    var z026 = !+Integer.MAX_VALUE            //## issuekeys: UNEXPECTED TOKEN: !+
    var z027 = !-Integer.MAX_VALUE
    var z028 = !+Integer.MIN_VALUE            //## issuekeys: UNEXPECTED TOKEN: !+
    var z029 = !-Integer.MIN_VALUE
    var z030 = !-Long.MIN_VALUE

    var z031 = (!-Integer.MIN_VALUE !- !-Integer.MAX_VALUE)/2
    var z032 = (Integer.MAX_VALUE !+ Integer.MAX_VALUE)/2
    var z033 = (!-Integer.MAX_VALUE !- Integer.MAX_VALUE)/2
    var z034 = (Integer.MAX_VALUE !- !-Integer.MAX_VALUE)/2
    var z035 = (!-Integer.MAX_VALUE !- !-Integer.MAX_VALUE)/2
  }
}