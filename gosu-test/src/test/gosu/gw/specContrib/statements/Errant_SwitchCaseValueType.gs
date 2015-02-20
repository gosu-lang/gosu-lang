package gw.specContrib.statements

class Errant_SwitchCaseValueType  {
  enum E { ONE, TWO }

  function test() {
    var x: Object

    switch (x) {
      case "one":
        break
      case 42:
        break
      case String:
        break
    }

    switch (typeof(x)) {
      case String:
        break
      case 1:          //## issuekeys: INCOMPATIBLE CASE VALUE TYPE
        break
    }

    var i: int
    switch (i) {
      case 1:
        break
      case "one":      //## issuekeys: INCOMPATIBLE CASE VALUE TYPE
        break
    }


    var s: String
    switch (s) {
      case "one":
        break;
      case 1 + 1:      //## issuekeys: INCOMPATIBLE CASE VALUE TYPE
        break;
    }

    var e: E
    switch (e) {
      case ONE:
        break
      case 1:          //## issuekeys: INCOMPATIBLE CASE VALUE TYPE
        break
    }
  }
}