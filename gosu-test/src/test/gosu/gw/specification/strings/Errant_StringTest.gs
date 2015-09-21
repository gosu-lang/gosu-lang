package gw.specification.strings

uses java.util.Date

class Errant_StringTest {
  function basic() {
    var b : char = 'B'
    var x0 = "Don't worry"
    var x1 = 'Don't worry'  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN
    var x2 = 'Don\'t worry'
    var x3 = 'he said "OK"'
    var x4 = "he said "OK""  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN
    x0[0] = 'W'  //## issuekeys: MSG_STR_IMMUTABLE
    var x5 = "\b\t\n\f\r\v\a\$\<\"\'\\"
    var x6 = "A\101\u0041"
    var x7 = "\J"  //## issuekeys: MSG_INVALID_CHAR_AT
    var x8 = "\uGGGG"  //## issuekeys: MSG_INVALID_CHAR_AT
    var x9 = "\377"
    var x10 = "\378"
    var x11 = "\388"
    var x12 = "\888"  //## issuekeys: MSG_INVALID_CHAR_AT
    var x13 = x0+x2
    var x14 = 10 + new Date() + "A"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var i = 0
    var x15 = "hello" + i - 1 + "!"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var x16 = "A\101\u0041"
    var x17 = "A" + null
    var x18 = "A" % null  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var x19 = "A" + b
    var x20 = 8 + "A"
    var x21 = "A" + 8
    var x22 = "A" + true
    var x23 = "A"/"B"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var x24 = "A"*"B"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var x25 = "A"%"B"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var x26 = "A"+"B"
    var x27 = "A"-"B"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
  }

}
