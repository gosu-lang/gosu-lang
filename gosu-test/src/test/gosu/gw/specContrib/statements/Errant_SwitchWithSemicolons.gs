package gw.specContrib.statements

class Errant_SwitchWithSemicolons {
  function test() {
    var x : int
    switch(x){
      case(1):
        print(x);
        ;      // this falls through, so there should be a warning
      case(2): //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
          ;
        break
      default:
          ;
    }
  }
}
