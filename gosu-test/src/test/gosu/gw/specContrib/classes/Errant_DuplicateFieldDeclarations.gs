package gw.specContrib.classes

class Errant_DuplicateFieldDeclarations {
  var v1: String = "sf"
  var v1: String = "sf"      //## issuekeys: VARIABLE 'V1' IS ALREADY DEFINED IN THE SCOPE

  interface Int {
    var v2: String = "sf"
    var v2: String = "sf"      //## issuekeys: VARIABLE 'V2' IS ALREADY DEFINED IN THE SCOPE
  }

  structure Struct {
    var v3: String = "sf"
    var v3: String = "sf"      //## issuekeys: VARIABLE 'V3' IS ALREADY DEFINED IN THE SCOPE
  }

  enum Enum1 {
    var v3: String = "sf"
    var v3: String = "sf"      //## issuekeys: VARIABLE 'V3' IS ALREADY DEFINED IN THE SCOPE
  }

  enum Enum2 {
    ONE,
    ONE      //## issuekeys: VARIABLE 'ONE' IS ALREADY DEFINED IN THE SCOPE
  }

  annotation Ann {
    var v3: String = "sf"
    var v3: String = "sf"      //## issuekeys: VARIABLE 'V3' IS ALREADY DEFINED IN THE SCOPE
  }
}