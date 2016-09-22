package gw.specContrib.expressions.relationalOperators

class Errant_RelationalOperatorsTest {

  // IDE-2110
  function foo() {
    var sh: Short
    var i: Integer
    var str: String
    var cShort1: Comparable<Short>
    var cShort2: Comparable<Short>
    var cInteger1: Comparable<Integer>
    var cInteger2: Comparable<Integer>
    var cString1: Comparable<String>
    var cString2: Comparable<String>

    if (sh < i) {
    }
    if (sh < String) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'TYPE<JAVA.LANG.STRING>'
    }
    if (sh < cShort1) {
    }
    if (sh < cInteger1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.COMPARABLE<JAVA.LANG.INTEGER>'
    }
    if (sh < cString1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.SHORT', 'JAVA.LANG.COMPARABLE<JAVA.LANG.STRING>'
    }

    if (i < String) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'TYPE<JAVA.LANG.STRING>'
    }
    if (i < cShort1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.COMPARABLE<JAVA.LANG.SHORT>'
    }
    if (i < cInteger1) {
    }
    if (i < cString1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.INTEGER', 'JAVA.LANG.COMPARABLE<JAVA.LANG.STRING>'
    }

    if (str < cShort1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.COMPARABLE<JAVA.LANG.SHORT>'
    }
    if (str < cInteger1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.COMPARABLE<JAVA.LANG.INTEGER>'
    }
    if (str < cString1) {
    }

    if (cShort1 < cShort2) {
    }
    if (cShort1 < cInteger1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.COMPARABLE<JAVA.LANG.SHORT>', 'JAVA.LANG.COMPARABLE<JAVA.LANG.INTEGER>'
    }
    if (cShort1 < cString1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.COMPARABLE<JAVA.LANG.SHORT>', 'JAVA.LANG.COMPARABLE<JAVA.LANG.STRING>'
    }

    if (cInteger1 < cInteger2) {
    }
    if (cInteger1 < cString1) {      //## issuekeys: OPERATOR '<' CANNOT BE APPLIED TO 'JAVA.LANG.COMPARABLE<JAVA.LANG.INTEGER>', 'JAVA.LANG.COMPARABLE<JAVA.LANG.STRING>'
    }

    if (cString1 < cString2) {
    }
  }

}