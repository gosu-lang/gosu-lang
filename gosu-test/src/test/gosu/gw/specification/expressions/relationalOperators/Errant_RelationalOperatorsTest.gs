package gw.specification.expressions.relationalOperators

class Errant_RelationalOperatorsTest {

  function testPrimitive_Object() {
    var o : Object
    var x = 45 > o  //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
  }
}