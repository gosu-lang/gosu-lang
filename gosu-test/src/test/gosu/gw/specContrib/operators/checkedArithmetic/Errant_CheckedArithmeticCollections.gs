package gw.specContrib.operators.checkedArithmetic

class Errant_CheckedArithmeticCollections {

  function test() {
    var array1 : int[] = {!-1,!-2,3}
    var arrayList1 : ArrayList<Integer> = {!-1,-2,!-3, 2!+5, !-2!*!-5}
    var arrayList2 : ArrayList<Integer> = {!-1,-2,!-3, 2!+5, !-2!*!+5}         //## issuekeys: UNEXPECTED TOKEN: !+
    var hashMap1 : HashMap = { !-1 -> !-42.5, 3->4}

    var x1 = array1[1!-1] !+ 5
    var x2 = arrayList1.get(1!+1)
    var x3 = hashMap1.get(!-1)

  }
}