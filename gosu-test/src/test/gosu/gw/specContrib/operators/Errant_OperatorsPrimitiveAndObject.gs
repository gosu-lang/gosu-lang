package gw.specContrib.operators

class Errant_OperatorsPrimitiveAndObject {
  function testObjectPrimitive() {
    var o : Object
    var i : int = 8
    var x0 : int = 8 << o  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var x1 : int = o << 8  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var x2 : int = 8 >> o  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var x3 : int = o >> 8  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var x4 : int = 8 >>> o  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
    var x5 : int = o >>> 8  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    i <<= o  //## issuekeys: MSG_TYPE_MISMATCH
    o <<= i  //## issuekeys: MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG, MSG_TYPE_MISMATCH
    i >>= o  //## issuekeys: MSG_TYPE_MISMATCH
    o >>= i  //## issuekeys: MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG, MSG_TYPE_MISMATCH
    i >>>= o  //## issuekeys: MSG_TYPE_MISMATCH
    o >>>= i  //## issuekeys: MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG, MSG_TYPE_MISMATCH

    var y0 : boolean = !o   //## issuekeys: MSG_TYPE_MISMATCH
    var y1 : int = ~o   //## issuekeys: MSG_TYPE_MISMATCH
  }
}