package gw.specContrib.statements

class Errant_SwitchDefaultCase  {
  function test() {
    var x: Object
    switch (x) {
      case 1:
      default:
        break
    }

    switch (x) {
      default:
        break
      case String:       //## issuekeys: CASE AFTER DEFAULT
    }

    switch(x) {
      default:
      default:          //## issuekeys: DUPLICATE DEFAULT
        break
    }
  }
}