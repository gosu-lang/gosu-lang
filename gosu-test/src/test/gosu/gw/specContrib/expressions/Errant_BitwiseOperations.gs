package gw.specContrib.expressions

class Errant_BitwiseOperations {

  function func1() {
    var bitAnd1 = 8.45 & 42 //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var bitAnd2 = 42 & 8.45 //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }

}