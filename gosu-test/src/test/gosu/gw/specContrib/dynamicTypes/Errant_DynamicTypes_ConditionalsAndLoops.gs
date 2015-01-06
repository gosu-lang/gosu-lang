package gw.specContrib.dynamicTypes

uses dynamic.Dynamic

class Errant_DynamicTypes_ConditionalsAndLoops {
  var dd: Dynamic

  function testConditionalsAndLoops() {
    if (dd > 42) {
      print("if comparison")
    }
    //IDE-1550
    if (dd) {
      print("if boolean")
    }
    while (dd > 10) {
      print("comparison comparison")
      dd++
    }
    //IDE-1550
    while (dd) {
      print("while boolean")
    }
    do {
      print("do while comparison")
    } while (dd == "string")

    //IDE-1550
    do {
      print("do while boolean")
    } while (dd)

    //IDE-1546
    for (ele in dd) {
      print("foreach dyanamic")
    }

    switch (dd) {
      case "one":
        print("string")
        break
      //IDE-426
      case 2:
        print("int")
        break
      case dd:
        print("dynamic")
        break
      default:
        break
    }
  }
}