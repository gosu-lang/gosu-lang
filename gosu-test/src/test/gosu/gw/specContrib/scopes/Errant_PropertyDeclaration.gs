package gw.specContrib.scopes

class Errant_PropertyDeclaration {
  var Property1: int
  var f1: int as Property1     //## issuekeys: MSG_

  var f2: int as Property2
  var Property2: int          //## issuekeys: MSG_

  var Property11 = 45
  property get Property11( ) : String { return null }   //## issuekeys: MSG_

  property get Property12( ) : String { return null }
  var Property12 = 42                                  //## issuekeys: MSG_

  var f21: int as Property21
  var f22: int as Property21             //## issuekeys: MSG_

  function foo(final Param: int) {
    final var Local = 2
    var o = new Object() {
      property get Local(): int { return 0 }      //## issuekeys: MSG_

      property get Param(): int { return 0 }      //## issuekeys: MSG_
    }
  }
}