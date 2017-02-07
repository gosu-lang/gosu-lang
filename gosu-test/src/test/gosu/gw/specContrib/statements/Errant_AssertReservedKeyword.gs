package gw.specContrib.statements

class Errant_AssertReservedKeyword {

  //IDE-2807
  var assert = 42      //## issuekeys: IDENTIFIER EXPECTED
  function assert() {}      //## issuekeys: IDENTIFIER EXPECTED
}