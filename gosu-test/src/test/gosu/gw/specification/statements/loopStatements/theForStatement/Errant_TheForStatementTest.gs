package gw.specification.statements.loopStatements.theForStatement

class Errant_TheForStatementTest  {
  function testForBasic() {
    var j = 0
    for(0..2) {
      j++
    }
    j = 0
    for(var x in 0..2) {
      j++
    }
    j = 0
    for(x in 0..2) {
      j++
    }
    for(x : int in 0..2) { }  //## issuekeys: MSG_EXPECTING_IN_FOREACH, MSG_EXPECTING_RIGHTPAREN_FE, MSG_SYNTAX_ERROR, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
  }

  function testForExpressionTypes() {
    var j = 0
    var ints : int[] = {0, 1, 2}
    for(x in ints) {
      j++
    }

    var intList : ArrayList<Integer> = {0, 1, 2}
    var iterable : Iterable<Integer> =  intList
    j = 0
    for(x in iterable) {
      j++
    }

    j = 0
    var iter : Iterator<Integer> = intList.iterator()
    for(x in iter) {
      j++
    }

    j = 0
    for(x in "012") {
      j++
    }
//    for("012") {}  //## issuekeys: MSG_EXPECTING_IDENTIFIER_FOREACH, MSG_EXPECTING_IN_FOREACH
    for(3) {}  //## issuekeys: MSG_EXPECTING_IDENTIFIER_FOREACH, MSG_EXPECTING_IN_FOREACH, MSG_EXPECTING_ARRAYTYPE_FOREACH
  }

  function testLoopVariable() {
    var k = 1
    for(k in ints) { }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_BAD_IDENTIFIER_NAME

    var ints : int[] = {0, 1, 2}
    for(x in ints) { x = 8 }

    var intList : ArrayList<Integer> = {0, 1, 2}
    for(x in intList) { x = 8 }
    var flag = false
    try {
      for(x in intList) { intList.remove(0) }
    } catch(ex : ConcurrentModificationException) { flag = true}
  }

  function testIteratorAndIndexKeyword() {
    var ints : int[] = {0, 1, 2}
    for(x in ints iterator it) {  }  //## issuekeys: MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS
    var intList : ArrayList<Integer> = {0, 1, 2}
    for(x in intList iterator it) {
      if(x > 0) {
        it.remove()
        it = null;  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
      }
    }

    intList = {0, 1, 2}
    var j = 0
    for(x in intList index i) {
      j++
      i = 0  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }

    for(x in intList index i iterator it) { }
    for(x in intList iterator it index i ) { }
    for(x in intList iterator i index i ) { }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    for(x in intList iterator it iterator i ) { }  //## issuekeys: MSG_EXPECTING_RIGHTPAREN_FE, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN
    for(x in intList index it index i ) { }  //## issuekeys: MSG_EXPECTING_RIGHTPAREN_FE, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN
  }

  function testLoneInterval() {
    for(0..2) {  }
    var j = 0
    for(0..2 index i) {
      j++
    }
    for(0..2 iterator it) {  }  //## issuekeys: MSG_FOREACH_ITERATOR_NOT_ALLOWED
  }

}