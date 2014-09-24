package gw.specContrib.scopes

class Errant_PropertyDeclaration {
  var Property1: int
  var f1: int as Property1     //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var f2: int as Property2
  var Property2: int          //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var Property11 = 45
  property get Property11( ) : String { return null }   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  property get Property12( ) : String { return null }
  var Property12 = 42                                  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var f21: int as Property21  //## issuekeys: MSG_PROPERTY_ALREADY_DEFINED
  var f22: int as Property21             //## issuekeys: MSG_PROPERTY_ALREADY_DEFINED

  function foo(final Param: int) {
    final var Local = 2
    var o = new Object() {
      property get Local(): int { return 0 }

      property get Param(): int { return 0 }
    }
  }

  var f3: int as Property3
  property get Property3(): int { return 0 }      // overriding getter for field property
  property set Property3(v: int) {}               // overriding setter for field property

  class A {
    var a: int as MyParentProp
  }

  class B extends A {
    var b: int as MyParentProp     //## issuekeys: MSG_PROPERTY_ALREADY_DEFINED
  }
}