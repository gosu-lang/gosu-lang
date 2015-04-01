package gw.specification.expressions.tableOfExpressionForms

uses java.math.BigInteger
uses java.lang.Comparable
uses java.util.Timer

class Errant_ArgumentTypesTest {
  function testArithmeticOperators() {
    var k : Timer

    var x0 = 42.5f + null              //## issuekeys: MSG_TYPE_MISMATCH
    var x1 = 42b + k                  //## issuekeys: MSG_TYPE_MISMATCH
    var x2 = 'c' + BigInteger.ONE
    var x3  = true + 5                 //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testBitwiseOperators() {
    var o: Object
    var s: String
    var k : Timer


    var eq1 = 42 & 41
    var eq2 = 42 & true              //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_IMPLICIT_COERCION_ERROR
    var eq3 = 42 & o                  //## issuekeys: MSG_TYPE_MISMATCH, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var eq4 = 42 & s                 //## issuekeys: MSG_TYPE_MISMATCH, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var eq5 = 42 & k                 //## issuekeys: MSG_TYPE_MISMATCH, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_TYPE_MISMATCH
    var eq6 = o & k               //## issuekeys: MSG_TYPE_MISMATCH, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }

}