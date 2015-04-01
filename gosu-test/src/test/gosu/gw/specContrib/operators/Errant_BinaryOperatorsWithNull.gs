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
}