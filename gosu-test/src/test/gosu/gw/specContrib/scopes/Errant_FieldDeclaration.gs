package gw.specContrib.scopes

class Errant_FieldDeclaration {
  public var enclosingVar: String = "non-static"

  function encloseAnonymousClass() {
    var pInstance = new Errant_FieldDeclaration() {
      var enclosingVar = "outer most string"   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }
  }

  class MyInnerClass {
    var enclosingVar = "sfd"   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  }
}