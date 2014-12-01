package gw.specContrib.typeinference

uses java.util.Date

class Errant_IfTypeNarrowing {

  function test() {
    var x: Object = "neat"

    if (x typeis String) {
      print(x.charAt(0))
    } else if (x typeis Date) {
      print(x.Time)
    }
  }

}