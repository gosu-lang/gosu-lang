package gw.specContrib.statements

class Errant_SwitchWithSemicolons {
  function test() {
    var x : int
    switch(x){
      case(1):
        print(x);
        ;
      case(2):
          ;
        break
      default:
          ;
    }
  }
}