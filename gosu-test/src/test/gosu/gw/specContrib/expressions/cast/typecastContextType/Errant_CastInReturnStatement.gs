package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInReturnStatement {


  function returnStmt(): Object[] {
    return {1, 2, 3} as Integer[] //## issuekeys: MSG_UNNECESSARY_COERSION
  }

  function returnStmt2(): Integer[] {
    return {1, 2, 3} as Integer[] //## issuekeys: MSG_UNNECESSARY_COERSION
  }

  function returnStmt3(): Float[] {
    return {1, 2, 3} as Integer[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.FLOAT[]' TO 'JAVA.LANG.INTEGER[]'
  }

  function returnStmt4(): Object[] {
    return {1, 2, 3} as Float[]      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.LANG.INTEGER[]' TO 'JAVA.LANG.FLOAT[]'
  }

  function returnStmt5() : Object[] {
    return {1, 2, 3}
  }

  function returnStmt6() : Object[] {
    return {'a','b','c'}
  }
}
