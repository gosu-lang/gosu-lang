package gw.specContrib.scopes

class Errant_CyclicVariableDeclaraion {
  var f = f                   //## issuekeys: MSG_

  function foo(p: int = p) {  //## issuekeys: MSG_
    var a = a                 //## issuekeys: MSG_
  }
}