package gw.specification.classes.Field_Declarations_in_Classes

class Errant_FinalFieldTest {
  final var f : int = 1
  static final var b : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
  final var g : int[] = new int[2]
  final var h : int[]  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
  final var j : int[]

  construct() {
    h = new int[2]
    j = new int[2]
    b = 0  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }

  construct(x : float) {
    //h = new int[2]
    j = new int[2]
    b = 0  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }

  construct(x : int[]) {
    h = x
    j = x
    b = 0  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }

  function m0() {
    f = 2  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    g = new int[2]  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    g[0] = 1
    b = 0  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }
}