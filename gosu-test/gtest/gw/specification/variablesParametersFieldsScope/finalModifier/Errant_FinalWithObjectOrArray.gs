package gw.specification.variablesParametersFieldsScope.finalModifier

uses java.lang.Integer

class Errant_FinalWithObjectOrArray {

  class C1 {
    final var _x : int[] = {1, 2, 3}
    final var _l : List<Integer> = {1, 2, 3}

    function foo () {
      final var x : int[] = {1, 2, 3}
      var y : int[] = {1, 2, 3}
      x[1] = -1
      _x[1] = -1
      x = y  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      _x = y  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE

      final var l : List<Integer> = {1, 2, 3}
      var k : List<Integer> = {1, 2, 3}
      l[1] = -1
      _l[1] = -1
      l = k  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      _l = k  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    }
  }

}
