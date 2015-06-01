package gw.specContrib.operators

class Errant_BinaryOperatorsWithNull {
  function test(i: int, o: Object) {
    // IDE-1769
    var b1 = i < null    //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var b2 = i == null   //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var b3 = i >= null   //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var b4 = i != null   //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
    var b5 = o === null
    var b6 = o !== null
  }

  function testNull(int1: int) {
    //Relational operators
    var a111 = int1 < null      //## issuekeys: ERROR
    var a112 = int1 > null      //## issuekeys: ERROR
    var a113 = int1 <= null     //## issuekeys: ERROR
    var a114 = int1 >= null     //## issuekeys: ERROR
    var a115 = int1 == null     //## issuekeys: ERROR
    var a116 = int1 != null     //## issuekeys: ERROR

    //Bitwise and Bit Shift operators
    var b111 = int1 << null     //## issuekeys: ERROR
    var b112 = int1 >> null     //## issuekeys: ERROR
    var b113 = int1 >>> null    //## issuekeys: ERROR
    var b114 = int1 ~ null      //## issuekeys: ERROR
    var b115 = int1 & null      //## issuekeys: ERROR
    var b116 = int1 | null      //## issuekeys: ERROR
    var b117 = int1 ^ null      //## issuekeys: ERROR

    //Conditional
    var c311 = int1 && null     //## issuekeys: ERROR
    var c312 = int1 || null     //## issuekeys: ERROR

    //Unary
    var d111 = -null          //## issuekeys: ERROR
    var d112 = !null          //## issuekeys: ERROR

    //identity
    var e111 = int1 === null          //## issuekeys: ERROR
    var e112 = int1 !== null          //## issuekeys: ERROR
  }
}