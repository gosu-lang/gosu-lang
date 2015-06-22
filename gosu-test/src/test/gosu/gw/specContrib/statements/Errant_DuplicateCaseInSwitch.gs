package gw.specContrib.statements

class Errant_DuplicateCaseInSwitch  {
  class Type1 {}
  enum E { ONE, TWO }

  function test() {
    var x: Object = "neat"

    switch (typeof(x)) {
      case String:
      case String:           //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break
      case Type1:
        break;
    }

    switch (23) {
      case 2:
        break;
      case 1 + 1:       //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break;
    }

    switch (x) {
      case "one":
        break
      case "one":       //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break
    }

    switch (x) {
      case null:
        break
      case null:       //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break
    }

    switch (x) {
      case 42:
        break
      case 42:       //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break
    }

    var e: E
    switch (e) {
      case ONE:
        break;
      case ONE:      //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break;
    }
  }
}