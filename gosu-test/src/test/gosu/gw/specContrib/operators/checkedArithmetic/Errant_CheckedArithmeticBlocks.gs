package gw.specContrib.operators.checkedArithmetic

class Errant_CheckedArithmeticBlocks {

  var square = \x : int -> x !* x
  var sum = \x : int -> x !+ x
  var subt = \x : int -> x !- x
  var myResult1 = square(!-10)
  var myResult2 = sum(!-10)
  var myResult3 = subt(!-10)


  var adder1 = \ x : int, y : int -> { return !-x !- !-y }
  var adder2 = \ x : int, y : int -> !+x !- y            //## issuekeys: UNEXPECTED TOKEN: !+

  var mysum = adder1(!+1, !-2)                           //## issuekeys: ')' EXPECTED
  var mysub = adder1(!-1, !-2)

  var captured = 10
  var addTo10 = \x : int -> !-(!-captured) !+ !-x
  var myresult = addTo10(!-20)

  var xx1 = new ArrayList<Integer>() {+1, -2, !+3, !-4}           //## issuekeys: UNEXPECTED TOKEN: !+
  var yy1 = xx1.map(\elt : Integer -> elt !- 2)

}